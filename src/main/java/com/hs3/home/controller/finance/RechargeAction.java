package com.hs3.home.controller.finance;

import com.alibaba.fastjson.JSONObject;
import com.hs3.dao.bank.RechargeWayDao;
import com.hs3.entity.bank.*;
import com.hs3.entity.finance.Recharge;
import com.hs3.entity.sys.SysMailtask;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.exceptions.UnLogException;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.bank.*;
import com.hs3.service.finance.RechargeService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.service.sys.SysMailtaskService;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.FileUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.auth.Auth;
import com.hs3.web.utils.WebUtils;
import com.pays.*;
import com.pays.daddy.DaddyPayApi;
import com.pays.jmoney.JmoneyPayApi;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@Scope("prototype")
@RequestMapping({"/recharge"})
public class RechargeAction
        extends HomeAction {
    private static final Logger logPay = Logger.getLogger("com.hs3.pays");

    @Autowired
    private UserService userService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private BankNameService bankNameService;

    @Autowired
    private BankApiService bankApiService;

    @Autowired
    private BankLevelService bankLevelService;

    @Autowired
    private BankSysService bankSysService;

    @Autowired
    private BankUserService bankUserService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysMailtaskService sysMailtaskService;
    @Autowired
    private RechargeWayDao rechargeWayDao;

    @ResponseBody
    @RequestMapping({"/saveRechargeInfo"})
    public Object saveRechargeInfo(String id, String card, String niceName) {
        this.rechargeService.updateInfo(getLogin().getAccount(), id, card, niceName);
        return Jsoner.success();
    }


    @Auth
    @ResponseBody
    @RequestMapping({"/rechargePayNotice/{bankApiId}"})
    public void rechargePayNotice(@PathVariable("bankApiId") Integer bankApiId) {
        BankApi bankApi = this.bankApiService.find(bankApiId);
        PayApi payApi = PayApiFactory.getInstance(bankApi.getClassKey());


        Map<String, String> params = getParams();

        logRechargePayNoticeParams(params);

        NoticeResult noticeResult = payApi.verify(params, exchangeBankApiToApiParam(bankApi));

        handleResult(payApi, noticeResult, params);
    }


    @Auth
    @ResponseBody
    @RequestMapping({"/{classKey}/rechargePayNotice"})
    public void rechargePayNotice(@PathVariable("classKey") String classKey) {
        Map<String, String> params = getParams();
        logPay.info("--> " + classKey + " pay rechargePayNotice, params :" + JSONObject.toJSONString(params));
        logRechargePayNoticeParams(params);


        PayApi payApi = PayApiFactory.getInstance(classKey);
        for (BankApi bankApi : this.bankApiService.findByClassKey(classKey)) {
            NoticeResult noticeResult = payApi.verify(params, exchangeBankApiToApiParam(bankApi));
            if (noticeResult.isStatus()) {
                handleResult(payApi, noticeResult, params);
                return;
            }
        }
    }

    private void handleResult(PayApi payApi, NoticeResult noticeResult, Map<String, String> params) {
        String r = payApi.getResultString(noticeResult.isStatus(), params);
        if (noticeResult.isStatus()) {
            if (noticeResult.getNotifyType() == 0) {
                Recharge m = new Recharge();
                m.setOperator(payApi.getId());
                m.setId(noticeResult.getOrderId());
                m.setSerialNumber(noticeResult.getApiOrderId());
                try {
                    if (noticeResult.getAmount() == null) {
                        this.rechargeService.updateBySuccess(m, null);
                    } else {
                        this.rechargeService.updateBySuccess(m, null, noticeResult.getAmount(), payApi.getId());
                    }
                } catch (BaseCheckException e) {
                    Recharge data = this.rechargeService.find(m.getId());
                    if (!e.getMessage().equals("该记录已被处理！")) {
                        r = payApi.getResultString((data != null) && ((data.getStatus() == 1) || (data.getStatus() == 2)));
                    }
                    if (e.getClass() != UnLogException.class) {
                        logPay.error(noticeResult.getOrderId() + ":" + noticeResult.getApiOrderId() + e.getMessage());
                    }
                }
            } else {
                noticeResult.getNotifyType();
            }
        }

        logPay.info(noticeResult.getOrderId() + ":" + noticeResult.getApiOrderId() + ":" + noticeResult.isStatus() + ":" + noticeResult.getNotifyType() + ":" + r);
        try {
            getResponse().getWriter().write(r);
        } catch (IOException e) {
            logPay.error(noticeResult.getOrderId() + ":" + r + "写入异常！", e);
        }
    }

    private ApiParam exchangeBankApiToApiParam(BankApi bankApi) {
        ApiParam apiParam = new ApiParam();
        apiParam.setEmail(bankApi.getEmail());
        apiParam.setIsCredit(bankApi.isCredit());
        apiParam.setKey(bankApi.getSign());
        apiParam.setPublicKey(bankApi.getPublicKey());
        apiParam.setMerchantCode(bankApi.getMerchantCode());
        apiParam.setShopUrl(bankApi.getShopUrl());

        return apiParam;
    }


    @RequestMapping(value = {"/rechargePay/{classKey}/{bankApiId}"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargePay(@PathVariable("classKey") String classKey, @PathVariable("bankApiId") Integer bankApiId, String type) {
        PayApi payApi = PayApiFactory.getInstance(classKey);

        if (payApi == null) {
            return getErrorView();
        }

        BankApi bankApi = this.bankApiService.find(bankApiId);

        if (bankApi == null) {
            return getErrorView();
        }

        if (!bankApi.getClassKey().equals(classKey)) {
            return getErrorView();
        }

        User user = this.userService.findByAccount(getLogin().getAccount());

        boolean checkBankApi = false;

        for (BankApi ba : this.rechargeService.listBankApi(user.getAccount(), user.getRegchargeAmount(), user.getRegchargeCount())) {
            if (ba.getId().equals(bankApi.getId())) {
                checkBankApi = true;
                break;
            }
        }

        if (!checkBankApi) {
            return getErrorView();
        }

        ModelAndView modelAndView = getViewWithHeadModel(PayApiFactory.isTwoCode(classKey) ? "/recharge/rechargeTwoCode" : "/recharge/rechargePay");
        modelAndView.addObject("payApi", payApi);
        modelAndView.addObject("bankApi", bankApi);
        List<BankName> bankNameList = new ArrayList<>();
        Map<String, String> thirdPartyAllBanks = payApi.getAllBanks(true);

        for (Map.Entry<String, String> entry : thirdPartyAllBanks.entrySet()) {
            BankName bankName = new BankName();

            try {
                if (null != type) {
                    if (type.equals("qq")) {
                        if (entry.getValue().contains("QQ")) {
                            bankName.setCode(entry.getKey());
                            bankName.setTitle(entry.getValue());
                        }
                    } else if (type.equals("ali")) {
                        if (entry.getValue().contains("支付宝")) {
                            bankName.setCode(entry.getKey());
                            bankName.setTitle(entry.getValue());
                        }
                    }
                } else {
                    if (!entry.getValue().contains("支付宝") && !entry.getValue().contains("QQ")) {
                        bankName.setCode(entry.getKey());
                        bankName.setTitle(entry.getValue());
                    }
                }
            } catch (Exception e) {
                logPay.error(e.getMessage(), e);
            }

            if (null != bankName.getCode())
                bankNameList.add(bankName);
        }

        List<RechargeWay> rechargeWays = this.rechargeWayDao.listRechargeWay();
        BankName bankName = bankNameList.get(0);
        RechargeWay rechargeWayResult = null;

        if (null != bankName) {
            boolean isAli = false;
            boolean isQQ = false;

            if (bankName.getTitle().contains("支付宝")) {
                isAli = true;
            } else if (bankName.getTitle().contains("QQ")) {
                isQQ = true;
            }
            for (RechargeWay rechargeWay : rechargeWays) {
                if (rechargeWay.getAlino().equals("alimobile") && isAli) {
                    rechargeWayResult = rechargeWay;
                    break;
                } else if (rechargeWay.getAlino().equals("qq") && isQQ) {
                    rechargeWayResult = rechargeWay;
                    break;
                }
            }

            if (null == rechargeWayResult) {
                for (RechargeWay rechargeWay : rechargeWays) {
                    if (rechargeWay.getAlino().equals("online") && isAli) {
                        rechargeWayResult = rechargeWay;
                        break;
                    }

                }
            }

            if (null != rechargeWayResult) {
                modelAndView.addObject("txtMinAmount", rechargeWayResult.getMinmoney());
                modelAndView.addObject("txtMaxAmount", rechargeWayResult.getMaxmoney());
            }
        }

        modelAndView.addObject("bankNameList", bankNameList);
        addValue(modelAndView);

        if (modelAndView.getModel().get("needBindCard") != null) {
            if (isMobile()) {
                return redirect("/user/bindCard");
            }

            return redirect("/user/index?tabId=myCard");
        }

        return modelAndView;
    }

    @RequestMapping(value = {"/rechargeOutstanding"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargeOutstanding(String message) {
        ModelAndView mv = getViewWithHeadModel("/recharge/rechargeOutstanding");
        if (!StrUtils.hasEmpty(message)) {
            mv.addObject("message", message);
        }
        User user = this.userService.findByAccount(getLogin().getAccount());
        addBaseValue(mv, user);
        return mv;
    }


    @RequestMapping(value = {"/rechargeOutstanding"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object rechargeOutstanding(int rechargeType, BigDecimal amount, String name, String card, String aliAccount, String aliId, String aliName, MultipartFile img) {
        String account = (getLogin().getAccount());
        if (amount == null) {
            return rechargeOutstanding("请填写充值金额！");
        }
        if (StrUtils.checkHadSpecialChar(name, card, aliAccount, aliId, aliName)) {
            return rechargeOutstanding("填写信息包含特殊字符！");
        }

        StringBuilder content = new StringBuilder("<table border='1'>");
        content.append("<tr><td>游戏账号user id</td><td>").append(account).append("</td></tr>");
        content.append("<tr><td>充值金额dep amount</td><td>").append(amount.toEngineeringString()).append("</td></tr>");

        if (rechargeType == 0) {
            if (StrUtils.hasEmpty(card, name)) {
                return rechargeOutstanding("请填写完整信息");
            }
            if (!StrUtils.isNumber(card, 4, 4)) {
                return rechargeOutstanding("请输入存款卡,后4位号码");
            }
            if (!StrUtils.isName(name, 2, 15)) {
                return rechargeOutstanding("请认真填写存款卡的开户名");
            }
            content.append("<tr><td>充值方式dep type</td><td>银行转账</td></tr>");
            content.append("<tr><td>存款人姓名dep name</td><td>").append(name).append("</td></tr>");
            content.append("<tr><td>存款卡号后四位last 4 digit of dep card</td><td>").append(card).append("</td></tr>");
        } else if (rechargeType == 1) {
            if (StrUtils.hasEmpty(aliAccount)) {
                return rechargeOutstanding("请填写支付宝账号");
            }
            if (StrUtils.hasEmpty(aliId)) {
                return rechargeOutstanding("请填写支付宝订单号");
            }
            content.append("<tr><td>充值方式dep type</td><td>AA支付宝</td></tr>");
            content.append("<tr><td>支付宝账号alipay account</td><td>").append(aliAccount).append("</td></tr>");
            content.append("<tr><td>支付宝订单alipay or#</td><td>").append(aliId).append("</td></tr>");
        } else if (rechargeType == 3) {
            if (!StrUtils.isName(aliName, 2, 15)) {
                return rechargeOutstanding("请填写存款支付宝认证姓名");
            }
            content.append("<tr><td>充值方式dep type</td><td>支付宝转银行卡</td></tr>");
            content.append("<tr><td>存款支付宝认证姓名Alipay Chinese name</td><td>").append(aliName).append("</td></tr>");
        } else if (rechargeType == 2) {
            content.append("<tr><td>充值方式dep type</td><td>以上都不是</td></tr>");
        }

        String imgPath = null;
        if (!img.isEmpty()) {
            try {
                String domain = WebUtils.getDomainName(getRequest());
                imgPath = FileUtils.save(getSession().getServletContext().getRealPath("/"), "/res/upload/", img);
                content.append("<tr><td>上传截图</td><td><a href=\"").append(domain).append(imgPath).append("\" >点击查看</a></td></tr>");
                content.append("<tr><td>截图路径</td><td>").append(domain).append(imgPath).append("</td></tr>");
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        content.append("</table>");

        String title = this.sysConfigService.find("PROJECT_NAME").getVal() + "+dep check NO." + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS") + "+" + account + "+" +
                amount.toEngineeringString();

        Date now = new Date();
        SysMailtask mailtask = new SysMailtask();
        mailtask.setAccount(account);
        mailtask.setCreateTime(now);
        mailtask.setChangeTime(now);
        mailtask.setStatus(0);
        mailtask.setType(2);
        mailtask.setTitle(title);
        mailtask.setContent(content.toString());
        mailtask.setImgPath(imgPath);

        this.sysMailtaskService.save(mailtask);

        return rechargeOutstanding("您的提交已成功，请耐心等待客服处理！");
    }


    @RequestMapping(value = {"/rechargeLower"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargeLower() {
        ModelAndView modelAndView = getViewWithHeadModel("/recharge/rechargeLower");

        addValue(modelAndView);

        if (modelAndView.getModel().get("needBindCard") != null) {
            if (isMobile()) {
                return redirect("/user/bindCard");
            }
            return redirect("/user/index?tabId=myCard");
        }

        return modelAndView;
    }


    @ResponseBody
    @RequestMapping(value = {"/rechargeLower"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object rechargeLower(String targetUser, BigDecimal chargeamount, String remark, String sourceUserSafePassword, Integer rechargeStyle) {
        User source = this.userService.findByAccount(getLogin().getAccount());
        User target = this.userService.findByAccount(targetUser);

        if (!source.getSafePassword().equals(StrUtils.MD5(sourceUserSafePassword))) {
            return Jsoner.error("安全密码不正确！");
        }
        if (target == null) {
            return Jsoner.error("目标用户不存在！");
        }
        if (chargeamount.compareTo(BigDecimal.ZERO) <= 0) {
            return Jsoner.error("充值金额不正确！");
        }
        if (StrUtils.hasEmpty(remark)) {
            return Jsoner.error("用途备注不能为空！");
        }
        if (remark.length() > 5) {
            return Jsoner.error("用途备注不得超过5个字！");
        }
        if (StrUtils.checkHadSpecialChar(remark)) {
            return Jsoner.error("备注用途不能包含特殊字符！");
        }
        if ((rechargeStyle == null) || ((rechargeStyle != 1) && (rechargeStyle != 2) && (rechargeStyle != 3) && (rechargeStyle != 4))) {
            return Jsoner.error("充值形式错误！");
        }
        try {
            this.rechargeService.saveByLower(source, target, chargeamount, rechargeStyle, remark);
        } catch (BaseCheckException e) {
            return Jsoner.error(e.getMessage());
        }

        return Jsoner.success();
    }

    @Auth
    @RequestMapping(value = {"/rechargePay"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargePayGet(String type) {
        User user = getLogin();

        if (user == null) {
            return redirect("/login");
        }

        ModelAndView modelAndView = getViewWithHeadModel("/recharge/rechargePay");

        List<BankApi> bankApiAllList = this.rechargeService.listBankApi(user.getAccount(), user.getRegchargeAmount(), user.getRegchargeCount());

        addValue(modelAndView);

        if (modelAndView.getModel().get("needBindCard") != null) {
            if (isMobile()) {
                return redirect("/user/bindCard");
            }
            return redirect("/user/index?tabId=myCard");
        }

        try {
            if (!bankApiAllList.isEmpty()) {
                boolean isMobile = isMobile();
                for (BankApi bankApi : bankApiAllList) {
                    boolean b1 = (isMobile) && ((bankApi.getIsSupportMobile() == 1) || (bankApi.getIsSupportMobile() == 2));
                    boolean b2 = (!isMobile) && ((bankApi.getIsSupportMobile() == 1) || (bankApi.getIsSupportMobile() == 0));
                    if ((b1) || (b2)) {
                        return rechargePay(bankApi.getClassKey(), bankApi.getId(), type);
                    }
                }
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }

        return modelAndView;
    }

    @Auth
    @RequestMapping(value = {"/rechargeMoney"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargeMoneyGet(String account, String sign, BigDecimal chargeamount, String card, String niceName, Integer bankNameId, String classKey, Integer bankApiId, Integer rechargeType) {
        if (!StrUtils.hasEmpty(account, sign, chargeamount, bankNameId, rechargeType)) {
            return rechargeMoney(account, sign, chargeamount, card, niceName, bankNameId, classKey, bankApiId, rechargeType);
        }
        if (getLogin() == null) {
            return redirect("/login");
        }
        ModelAndView modelAndView = getViewWithHeadModel("/recharge/recharge");
        modelAndView.addObject("bankNameList", this.bankNameService.findByStatus(null, 0));

        addValue(modelAndView);

        if (modelAndView.getModel().get("needBindCard") != null) {
            if (isMobile()) {
                return redirect("/user/bindCard");
            }
            return redirect("/user/index?tabId=myCard");
        }

        try {
            boolean hadBankLevel = (Boolean) modelAndView.getModel().get("hadBankLevel");
            if (!hadBankLevel) {
                List bankApiList = (List) modelAndView.getModel().get("bankApiList");

                if (!bankApiList.isEmpty()) {
                    boolean isMobile = isMobile();

                    for (Object bankApi1 : bankApiList) {
                        BankApi bankApi = (BankApi) bankApi1;
                        boolean b1 = (isMobile) && ((bankApi.getIsSupportMobile() == 1) || (bankApi.getIsSupportMobile() == 2));
                        boolean b2 = (!isMobile) && ((bankApi.getIsSupportMobile() == 1) || (bankApi.getIsSupportMobile() == 0));
                        if ((b1) || (b2)) {
                            return rechargePay(bankApi.getClassKey(), bankApi.getId(), null);
                        }
                    }
                }
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }

        return modelAndView;
    }

    private void addValue(ModelAndView modelAndView) {
        User user = this.userService.findByAccount(getLogin().getAccount());
        addBaseValue(modelAndView, user);


        modelAndView.addObject("sign", StrUtils.MD5(user.getAccount() + "yqwehniasdiuh.,.!#$^"));
        try {
            this.rechargeService.checkUserCanRecharge(user);
        } catch (BaseCheckException e) {
            modelAndView.setViewName(getViewName("/recharge/rechargeMessage"));
            modelAndView.addObject("message", e.getMessage());
            return;
        }

        List<BankUser> bu = this.bankUserService.findByAccount(getLogin().getAccount());
        if ((bu == null) || (bu.size() < 1)) {
            modelAndView.addObject("needBindCard", true);
            return;
        }

        if (user.getSafePassword() == null) {
            modelAndView.addObject("needSetSafePassword", true);
            return;
        }


        this.rechargeService.updateByUserExpire(user.getAccount(), 15);
        List<Recharge> rechargeList = this.rechargeService.listUserStatus(user.getAccount(), 0);
        if (!rechargeList.isEmpty()) {
            Recharge recharge = rechargeList.get(0);
            if (recharge.getRechargeType() == 1) {
                BankApi bankApi = this.bankApiService.find(recharge.getReceiveBankId());

                modelAndView.addObject("bankApi", bankApi);
                modelAndView.addObject("isQrCode", PayApiFactory.isTwoCode(bankApi.getClassKey()));
            }
        }
        modelAndView.addObject("rechargeList", rechargeList);
    }

    private void addBaseValue(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.addObject("hadBankLevel", this.bankLevelService.count(user.getRegchargeCount(), user.getRegchargeAmount()) > 0);
        modelAndView.addObject("bankApiList", this.rechargeService.listBankApi(user.getAccount(), user.getRegchargeAmount(), user.getRegchargeCount()));
        modelAndView.addObject("bankArticleId", this.sysConfigService.find("RECHARGE_BANK_ARTICLE_ID").getVal());
        modelAndView.addObject("bankRemark", this.sysConfigService.find("RECHARGE_BANK_REMARK").getVal());
    }


    @RequestMapping(value = {"/rechargeMoney"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object rechargeMoney(String account, String sign, BigDecimal chargeamount, String card, String niceName, Integer bankNameId, String classKey, Integer bankApiId, Integer rechargeType) {
        ModelAndView modelAndView;

        if (!StrUtils.MD5(account + "yqwehniasdiuh.,.!#$^").equals(sign)) {
            account = getLogin().getAccount();
        }
        modelAndView = getViewWithHeadModel("/recharge/rechargeResult");


        User user = this.userService.findByAccount(account);
        addBaseValue(modelAndView, user);
        if (user.getSafePassword() == null) {
            modelAndView.addObject("error", "请先设置安全密码！");
            return modelAndView;
        }

        List<BankUser> bu = this.bankUserService.findByAccount(account);
        if ((bu == null) || (bu.size() < 1)) {
            modelAndView.addObject("error", "请先绑定银行卡！");
            return modelAndView;
        }

        if (chargeamount.compareTo(new BigDecimal(0)) <= 0) {
            modelAndView.addObject("error", "充值金额不正确！");
            return modelAndView;
        }

        Recharge recharge;
        try {
            if (rechargeType == 0) {
                recharge = this.rechargeService.saveByBank(user, chargeamount, bankNameId);

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date startTime = recharge.getCreateTime();
                Date endTime = DateUtils.addMinute(startTime, 15);
                modelAndView.addObject("expireTime", df.format(endTime));
                modelAndView.addObject("nowTime", df.format(startTime));
                modelAndView.addObject("sysExpireTime", 15);
                modelAndView.addObject("recharge", recharge);

                return modelAndView;
            }
            if (rechargeType == 1) {
                PayApi payApi = PayApiFactory.getInstance(classKey);
                if (payApi == null) {
                    return getErrorView();
                }

                recharge = this.rechargeService.saveByPay(user, chargeamount, card, niceName, bankNameId, bankApiId, payApi, isMobile(), classKey);

                if (PayApiFactory.isTwoCode(classKey)) {
                    modelAndView.addObject("classKey", classKey);
                    modelAndView.addObject("bankApi", this.bankApiService.find(bankApiId));
                    modelAndView.addObject("recharge", recharge);
                    return modelAndView;
                }

                return rechargePayView(recharge, classKey);
            }

            throw new BaseCheckException("操作不支持！");
        } catch (BaseCheckException e) {
            modelAndView = getViewWithHeadModel("/recharge/rechargePay");
            addValue(modelAndView);
//            modelAndView = (ModelAndView) rechargeMoneyGet(null, null, null, null, null, null, null, null, null);
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }


    @RequestMapping({"/rechargePayForward"})
    public Object rechargePayForward(String rechargeId) {
        Recharge recharge = this.rechargeService.updateByExpire(rechargeId, 15);
        if (recharge == null) {
            return getErrorView();
        }
        if (!recharge.getAccount().equals(getLogin().getAccount())) {
            return getErrorView();
        }

        return rechargePayView(recharge, null);
    }


    @RequestMapping({"/rechargePayQrcodeResult"})
    public Object rechargePayQrcodeResult(String rechargeId) {
        ModelAndView modelAndView = getViewWithHeadModel("/recharge/rechargeResult");

        User user = this.userService.findByAccount(getLogin().getAccount());
        addBaseValue(modelAndView, user);

        Recharge recharge = this.rechargeService.updateByExpire(rechargeId, 15);
        BankApi bankApi = this.bankApiService.find(recharge.getReceiveBankId());

        modelAndView.addObject("classKey", bankApi.getClassKey());
        modelAndView.addObject("bankApi", bankApi);
        modelAndView.addObject("recharge", recharge);

        return modelAndView;
    }


    private Object rechargePayView(Recharge recharge, String classKey) {
        BankApi bankApi = this.bankApiService.find(recharge.getReceiveBankId());
        PayApi payApi = PayApiFactory.getInstance(bankApi.getClassKey());

        String domainName = WebUtils.getDomainName(getRequest());
        String baseUrl = bankApi.getShopUrl() + "/" + this.sysConfigService.find("PROJECT_NAME").getVal() + "/" + bankApi.getClassKey();
        // String shopUrl = baseUrl + "/regcharge";
        String payNoticeUrl = baseUrl + "/notify";
        String payReturnUrl = baseUrl + "/callBack";
        String shopNoticeUrl = domainName + "/recharge/rechargePayNotice/" + bankApi.getId();
        if ("jubao".equals(payApi.getId())) {
            shopNoticeUrl = domainName + "/recharge/rechargePayJuBaoNotice";
        }
        String shopReturnUrl;

        if (isMobile()) {
            domainName = domainName.replace("https://www.", "https://m.");
            domainName = domainName.replace("http://www.", "http://m.");
        }

        shopReturnUrl = domainName;

        ModelAndView mv = getView("/recharge/rechargePayForward");
        //daddy
        if (PayApiFactory.getInstance(classKey) instanceof DaddyPayApi) {
            PayApiParam param = new PayApiParam(recharge.getAmount().setScale(2, 4).toEngineeringString(), recharge.getBankNameCode().toUpperCase(), bankApi.getShopUrl(), payNoticeUrl,
                    shopNoticeUrl, payReturnUrl, shopReturnUrl, bankApi.getShopUrl(), recharge.getId(), bankApi.getMerchantCode(), bankApi.getSign(), bankApi.getPublicKey(), getIP(),
                    bankApi.getEmail(), bankApi.isCredit(), isMobile(), recharge.getTraceId(), bankApi.getDepositMode() + "");
            DaddyPayApi daddyPayApi = (DaddyPayApi) payApi;
            String reqResult = daddyPayApi.sendPayReqHtml(param);
            if (StringUtils.isNotBlank(reqResult)) {
                mv.addObject("xml", reqResult);
            }
        }
        //jmoney
        if (PayApiFactory.getInstance(classKey) instanceof JmoneyPayApi) {
            PayApiParam param = new PayApiParam(recharge.getAmount().setScale(2, 4).toEngineeringString(), recharge.getBankName(), bankApi.getShopUrl(), payNoticeUrl,
                    shopNoticeUrl, payReturnUrl, shopReturnUrl, bankApi.getShopUrl(), recharge.getId(), bankApi.getMerchantCode(), bankApi.getSign(), bankApi.getPublicKey(), getIP(),
                    bankApi.getEmail(), bankApi.isCredit(), isMobile(), recharge.getTraceId(), bankApi.getDepositMode() + "");
            JmoneyPayApi jmoneyPayApi = (JmoneyPayApi) payApi;
            String reqResult = jmoneyPayApi.sendPayReqHtml(param);
            if (StringUtils.isNotBlank(reqResult)) {
                mv.addObject("xml", reqResult);
            }
        }

        return mv;
    }


    @RequestMapping(value = {"/rechargeFinish"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargeFinish(String id, Integer bankApiId) {
        Recharge recharge = this.rechargeService.updateByExpire(id, 15);

        ModelAndView modelAndView = getViewWithHeadModel("/recharge/rechargeFinish");

        User user = this.userService.findByAccount(getLogin().getAccount());
        if (!recharge.getAccount().equals(user.getAccount())) {
            return getErrorView();
        }
        modelAndView.addObject("recharge", recharge);
        modelAndView.addObject("bankApi", this.bankApiService.find(bankApiId));
        addBaseValue(modelAndView, user);

        return modelAndView;
    }


    @Auth
    @RequestMapping({"/rechargePayFinish/{id}"})
    public Object rechargePayFinish(@PathVariable("id") String id) {
        System.out.println(id);
        return getViewWithHeadModel("/recharge/rechargePayFinish");
    }


    @ResponseBody
    @RequestMapping({"/rechargeCancel"})
    public Object rechargeCancel(String id) {
        Recharge recharge = this.rechargeService.find(id);
        if (!recharge.getAccount().equals(getLogin().getAccount())) {
            return Jsoner.error("撤销失败！");
        }
        int i = 0;
        if (recharge.getStatus() == 0) {
            i = this.rechargeService.updateStatus(id, 4);
        }

        return Jsoner.success(i);
    }

    @Auth
    @ResponseBody
    @RequestMapping({"/rechargeCheck"})
    public void rechargeCheck(String id) throws IOException {
        Recharge r = this.rechargeService.find(id);
        String result = (r != null) && (r.getStatus() == 2) ? "0" : "1";
        getResponse().getWriter().write(result);
    }

    @Auth
    @ResponseBody
    @RequestMapping({"/aliPayAutoNotify"})
    public void aliPayAutoNotify() throws IOException {
        String result = "Faild";

        Map<String, String> params = getParams();
        logRechargePayNoticeParams(params);


        String numID = params.get("NumID");
        String money = params.get("Money");
        String keys = params.get("Keys");
        String payUser = params.get("PayUser");
        try {
            if (!StrUtils.hasEmpty(payUser)) {
                Recharge recharge;


                Date today = DateUtils.toDate(DateUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
                Recharge rechargeNew = this.rechargeService.findByTodayTraceId(payUser, today);
                Recharge rechargeOld = this.rechargeService.findByTodayTraceId(payUser, DateUtils.addDay(today, -1));
                if (((rechargeNew != null) && (rechargeOld == null)) || ((rechargeNew == null) && (rechargeOld != null))) {
                    recharge = rechargeNew != null ? rechargeNew : rechargeOld;
                    String sign = "";
                    if (recharge.getRechargeType() == 0) {
                        BankSys bankSys = this.bankSysService.find(recharge.getReceiveBankId());
                        sign = bankSys.getSign();
                    } else if (recharge.getRechargeType() == 1) {
                        BankApi bankApi = this.bankApiService.find(recharge.getReceiveBankId());
                        sign = bankApi.getSign();
                    }
                    if ((sign.equals(keys)) && (recharge.getAmount().compareTo(new BigDecimal(money)) == 0)) {
                        recharge.setOperator("");
                        recharge.setSerialNumber(numID);

                        this.rechargeService.updateBySuccess(recharge, null);
                        result = "Success";
                    }
                }
            }
        } catch (Exception e) {
            logPay.error(payUser + " aliPayAutoNotify exception:" + e.getMessage(), e);
        }

        getResponse().getWriter().write(result);
    }

    private void logRechargePayNoticeParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder("\n-------------------start-------------------\n");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        sb.append("-------------------end-------------------");
        logPay.info(sb.toString());
    }

    /*
     * daddy pay 支付完成回传接口
     *
     * @param pay_time           DP系统从银行抓取到的客户转账时间，格式：年月日时分秒
     * @param bank_id            客户充值的银行编码
     * @param amount             数字，需保留两位小数点，无千位符，单位为CNY
     * @param company_order_num  字符串，订单号是唯一的
     * @param mownecum_order_num 字符串，DP系统订单号
     * @param pay_card_num       数字串，客户充值时使用的付款卡号
     * @param pay_card_name      客户充值使用的付款卡姓名
     * @param channel            网银或ATM转账
     * @param area               交易地址
     * @param fee                数字，客户付款交易手续费，直接来自银行，目前只有大部分工行卡可以获取到该手续费，抓取不到的转款手续费默认为0.00
     * @param transaction_charge 数字，DP系统平台收取单笔订单的服务费
     * @param key                MD5(MD5(config)+pay_time+bank_id+amount+company_order_num+mownecum_order_num+pay_card_num+pay_card_name+channel+area+fee+transaction_charge+deposit_mode)
     *                           备注：config即为DP系统提供的key(固定字符串)
     * @param deposit_mode       1银行卡
     *                           2第三方、QQ钱包、企业版微信与支付宝
     *                           3移动电子钱包（个人版支付宝二维码）
     *                           4 HTML5（WAP）模式
     *                           5 BitCoin模式
     *                           6收银台模式（目前只开放支付宝）
     * @param base_info          渠道原始关键信息
     * @param operating_time     DP系统的订单完成时间
     */

//    public void daddyPayTransVerify(
//            @RequestParam(value = "pay_time", required = false) String pay_time,
//            @RequestParam(value = "bank_id", required = false) String bank_id,
//            @RequestParam(value = "amount", required = false) String amount,
//            @RequestParam(value = "company_order_num", required = false) String company_order_num,
//            @RequestParam(value = "mownecum_order_num", required = false) String mownecum_order_num,
//            @RequestParam(value = "pay_card_num", required = false) String pay_card_num,
//            @RequestParam(value = "pay_card_name", required = false) String pay_card_name,
//            @RequestParam(value = "channel", required = false) String channel,
//            @RequestParam(value = "area", required = false) String area,
//            @RequestParam(value = "fee", required = false) String fee,
//            @RequestParam(value = "transaction_charge", required = false) String transaction_charge,
//            @RequestParam(value = "key", required = false) String key,
//            @RequestParam(value = "deposit_mode", required = false) String deposit_mode,
//            @RequestParam(value = "base_info", required = false) String base_info,
//            @RequestParam(value = "operating_time", required = false) String operating_time
//    ) {
//        PayApiParam apiParam = new PayApiParam();
//
//        Map<String, String> param = new HashMap<>();
//        param.put("pay_time", pay_time);
//        param.put("bank_id", bank_id);
//        param.put("amount", amount);
//        param.put("company_order_num", company_order_num);
//        param.put("mownecum_order_num", mownecum_order_num);
//        param.put("pay_card_num", pay_card_num);
//        param.put("pay_card_name", pay_card_name);
//        param.put("channel", channel);
//        param.put("area", area);
//        param.put("fee", fee);
//        param.put("transaction_charge", transaction_charge);
//        param.put("deposit_mode", deposit_mode);
//        param.put("key", key);
//        param.put("base_info", base_info);
//        param.put("operating_time", operating_time);
//
//        PayApi payApi = new DaddyPayApi();
//
//        NoticeResult noticeResult = payApi.verify(param, apiParam);
//
//    }

}

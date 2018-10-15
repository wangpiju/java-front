package com.hs3.home.controller.finance;

import com.hs3.entity.bank.BankName;
import com.hs3.entity.bank.BankUser;
import com.hs3.entity.finance.Deposit;
import com.hs3.entity.finance.FinanceSetting;
import com.hs3.entity.finance.FinanceWithdraw;
import com.hs3.entity.sys.SysMailtask;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.lotts.LotteryFactory;
import com.hs3.models.Jsoner;
import com.hs3.service.bank.BankLevelService;
import com.hs3.service.bank.BankNameService;
import com.hs3.service.bank.BankUserService;
import com.hs3.service.finance.DepositService;
import com.hs3.service.finance.FinanceSettingService;
import com.hs3.service.finance.FinanceWithdrawService;
import com.hs3.service.finance.RechargeService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.service.sys.SysMailtaskService;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.FileUtils;
import com.hs3.utils.NumUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.auth.Auth;
import com.hs3.web.utils.WebUtils;
import com.pays.WithdrawApi;
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
import java.util.*;


@Controller
@Scope("prototype")
@RequestMapping({"/deposit"})
public class DepositAction
        extends HomeAction {
    private static final Logger log = Logger.getLogger(DepositAction.class);

    private static final String PRE_CARD = "*** **** **** **** ";

    private static final String PRE_NAME = "**";

    private static final String PAGE_BIND_BANKCARD_HOME = "/user/index?needBindCard=0";

    private static final String PAGE_BIND_BANKCARD_MOBILE = "/user/bindCard";

    private static final String KEY_SAFEPASSWORD = "keySafePassword";

    @Autowired
    private UserService userService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private BankUserService bankUserService;
    @Autowired
    private BankNameService bankNameService;
    @Autowired
    private BankLevelService bankLevelService;
    @Autowired
    private FinanceSettingService financeSettingService;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private FinanceWithdrawService financeWithdrawService;
    @Autowired
    private SysMailtaskService sysMailtaskService;
    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping(value = {"/safePassword"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object safePassword() {
        ModelAndView modelAndView = getViewWithHeadModel("/deposit/safePassword");

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        addBaseValue(modelAndView, user);
        try {
            this.depositService.checkUserCanDeposit(user, null);
        } catch (BaseCheckException e) {
            modelAndView.setViewName(getViewName("/deposit/depositMessage"));
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }
        if (user.getSafePassword() == null) {
            modelAndView.addObject("needSetSafePassword", Boolean.valueOf(true));
            return modelAndView;
        }
        List<BankUser> bankUserList = this.bankUserService.listByAccount(user.getAccount(), Integer.valueOf(0));

        if (bankUserList.isEmpty()) {
            return redirect(getBindCardPage());
        }

        return modelAndView;
    }


    @RequestMapping(value = {"/safePassword"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object safePassword(String safePassword) {
        getSession().removeAttribute("keySafePassword");

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());

        if (safePassword == null) {
            return getDepositResult("请输入安全密码", user);
        }
        if (!StrUtils.MD5(safePassword).equals(user.getSafePassword())) {
            return getDepositResult("安全密码错误，请确认后重新输入！", user);
        }

        List<BankUser> bankUserList = this.bankUserService.listByAccount(user.getAccount(), Integer.valueOf(0));

        if (bankUserList.isEmpty()) {
            return redirect(getBindCardPage());
        }


        int random = NumUtils.getRandom(100, 999);
        getSession().setAttribute("keySafePassword", Integer.valueOf(random));

        return redirect("/deposit/depositMoney?keySafePassword=" + random);
    }


    @RequestMapping(value = {"/depositMoney"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object depositMoney(String keySafePassword) {
        ModelAndView mv = getViewWithHeadModel("/deposit/deposit");

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        addBaseValue(mv, user);


        if (!checkKeySafePassword(keySafePassword)) {
            mv.setViewName("/deposit/safePassword");
            return mv;
        }

        List<BankUser> bankUserListCheck = new ArrayList();
        List<BankUser> bankUserList = this.bankUserService.listByAccount(user.getAccount(), Integer.valueOf(0));
        Map<Integer, BankName> bankNameMap = new HashMap();
        for (BankUser bankUser : bankUserList) {
            BankName bankName = this.bankNameService.find(bankUser.getBankNameId());
            if (bankName.getDepositStatus().intValue() == 0) {
                bankUser.setCard("*** **** **** **** " + bankUser.getCard().substring(bankUser.getCard().length() - 4));
                bankUser.setNiceName("**" + bankUser.getNiceName().substring(bankUser.getNiceName().length() - 1));
                bankNameMap.put(bankUser.getBankNameId(), bankName);

                bankUserListCheck.add(bankUser);
            }
        }
        mv.addObject("bankUserList", bankUserListCheck);
        mv.addObject("user", user);
        mv.addObject("bankNameMap", bankNameMap);
        FinanceSetting fs = (FinanceSetting) this.financeSettingService.list().get(0);
        mv.addObject("moneyDepositMin", fs.getDepositMinMoney());
        mv.addObject("moneyDepositMax", fs.getDepositMaxMoney());
        mv.addObject("countDepositMax", fs.getDepositMaxCount());
        HashMap<String, BigDecimal> userDepositAmount = depositService.getUserDepositAmount(user.getAccount());
        mv.addObject("actualBalanceZ", userDepositAmount.get("actualBalanceZ"));

        return mv;
    }


    @RequestMapping(value = {"/depositMoney"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object depositMoney(BigDecimal amount, String keySafePassword, Integer bankCardId) {
        amount = amount.setScale(2, 1);

        if (!checkKeySafePassword(keySafePassword)) {
            return safePassword();
        }
        getSession().removeAttribute("keySafePassword");

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());

        FinanceSetting fs = (FinanceSetting) this.financeSettingService.list().get(0);
        if (amount.compareTo(fs.getDepositMinMoney()) < 0) {
            return getDepositResult("系统限制最低提现额度为：" + fs.getDepositMinMoney(), user);
        }
        if (amount.compareTo(fs.getDepositMaxMoney()) > 0) {
            return getDepositResult("系统限制最高提现额度为：" + fs.getDepositMaxMoney(), user);
        }

        Integer depositCount = user.getDepositCount();
        if (depositCount.intValue() >= fs.getDepositMaxCount().intValue()) {
            return getDepositResult("系统限制最多提现次数为：" + fs.getDepositMaxCount(), user);
        }

        ModelAndView modelAndView = getDepositResult(null, user);
        try {
            this.depositService.saveSplitAmount(user.getAccount(), amount, fs.getDepositSplitMaxMoney(), bankCardId);
        } catch (BaseCheckException e) {
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }


    @ResponseBody
    @RequestMapping({"/depositCancel"})
    public Object depositCancel(String id) {
        Deposit deposit = this.depositService.find(id);
        if (!deposit.getAccount().equals(((User) getLogin()).getAccount())) {
            return Jsoner.error("撤销失败！");
        }
        int i = 0;
        if ((deposit != null) && (deposit.getStatus().intValue() == 0)) {
            i = this.depositService.updateStatus(id, Integer.valueOf(4));
        }

        return Jsoner.getByResult(i == 1);
    }

    @RequestMapping(value = {"/depositOutstanding"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object depositOutstanding(String message) {
        ModelAndView mv = getViewWithHeadModel("/deposit/depositOutstanding");
        if (!StrUtils.hasEmpty(new Object[]{message})) {
            mv.addObject("message", message);
        }
        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        addBaseValue(mv, user);
        return mv;
    }

    @RequestMapping(value = {"/depositOutstanding"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object depositOutstanding(BigDecimal amount, String card, Date depositTime, MultipartFile img) {
        String account = ((User) getLogin()).getAccount();
        if (amount == null) {
            return depositOutstanding("请填写提现金额！");
        }
        if (depositTime == null) {
            return depositOutstanding("请填写提款时间！");
        }
        if (StrUtils.checkHadSpecialChar(card)) {
            return depositOutstanding("填写信息包含特殊字符！");
        }

        StringBuilder content = new StringBuilder("<table border='1'>");
        content.append("<tr><td>游戏账号user id</td><td>").append(account).append("</td></tr>");
        content.append("<tr><td>提现金额with amount</td><td>").append(amount.toEngineeringString()).append("</td></tr>");

        if (StrUtils.hasEmpty(new Object[]{card})) {
            return depositOutstanding("请填写完整信息");
        }
        if (!StrUtils.isNumber(card, 4, 4)) {
            return depositOutstanding("请输入提款卡,后4位号码");
        }
        content.append("<tr><td>提款卡号后四位last 4 digit of dep card</td><td>").append(card).append("</td></tr>");
        content.append("<tr><td>提款时间with time</td><td>").append(DateUtils.format(depositTime, "yyyy-MM-dd HH:mm")).append("</td></tr>");

        String imgPath = null;
        if (!img.isEmpty()) {
            try {
                String domain = WebUtils.getDomainName(getRequest());
                imgPath = FileUtils.save(getSession().getServletContext().getRealPath("/"), "/res/upload/", img);
                content.append("<tr><td>上传截图</td><td><a href=\"" + domain + imgPath + "\" >点击查看</a></td></tr>");
                content.append("<tr><td>截图路径</td><td>" + domain + imgPath + "</td></tr>");
            } catch (Exception localException) {
            }
        }
        content.append("</table>");

        String title = this.sysConfigService.find("PROJECT_NAME").getVal() + "+with check NO." + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS") + "+" + account + "+" +
                amount.toEngineeringString();

        Date now = new Date();
        SysMailtask mailtask = new SysMailtask();
        mailtask.setAccount(account);
        mailtask.setCreateTime(now);
        mailtask.setChangeTime(now);
        mailtask.setStatus(Integer.valueOf(0));
        mailtask.setType(Integer.valueOf(3));
        mailtask.setTitle(title);
        mailtask.setContent(content.toString());
        mailtask.setImgPath(imgPath);

        this.sysMailtaskService.save(mailtask);

        return depositOutstanding("您的提交已成功，请耐心等待客服处理！");
    }


    @Auth
    @ResponseBody
    @RequestMapping({"/{simpleName}/withdrawalNotify"})
    public void withdrawalNotify(String type, @PathVariable("simpleName") String simpleName) {
        Map<String, String> params = getParams();
        params.remove("type");
        try {
            String result = null;
            if ("requestWithdrawApproveInformation".equals(type)) {


                result = doApprove(simpleName, params);
            } else if ("withdrawalResult".equals(type)) {


                result = doResult(simpleName, params);
            } else {
                result = type;
            }

            log.info(simpleName + type + ":" + result);
            getResponse().getWriter().write(result);
        } catch (IOException e) {
            log.error(simpleName + type + " exception:" + e.getMessage(), e);
        }
    }

    private String doApprove(String simpleName, Map<String, String> params) {
        WithdrawApi withdrawApi = WithdrawApi.getInstance(simpleName);
        String orderId = withdrawApi.getOrderId(params);
        String tradeNo = withdrawApi.getTradeNo(params);

        Deposit deposit = this.depositService.find(orderId);
        if (deposit == null) {
            return withdrawApi.getReturnMessage(orderId, tradeNo, "5", "order can not found!");
        }

        FinanceWithdraw financeWithdraw = this.financeWithdrawService.findByAmount(deposit.getAmount());
        String key = financeWithdraw.getSign();
        try {
            if (withdrawApi.checkApprove(params, key)) {


                deposit.setOperator(financeWithdraw.getAutoOperator());
                deposit.setRemark("AUTO DOING");
                this.depositService.updateByDoing(deposit);
                return withdrawApi.getReturnMessage(orderId, tradeNo, "4", "");
            }
            return withdrawApi.getReturnMessage(orderId, tradeNo, "0", "");
        } catch (BaseCheckException e) {
            return withdrawApi.getReturnMessage(orderId, tradeNo, "9", e.getMessage());
        } catch (Exception e) {
            log.error(orderId + ":" + tradeNo, e);
        }
        return withdrawApi.getReturnMessage(orderId, tradeNo, "9", "exception");
    }

    private String doResult(String simpleName, Map<String, String> params) {
        WithdrawApi withdrawApi = WithdrawApi.getInstance(simpleName);
        String orderId = withdrawApi.getOrderId(params);
        String tradeNo = withdrawApi.getTradeNo(params);

        Deposit deposit = this.depositService.find(orderId);

        String key = this.financeWithdrawService.findByAmount(deposit.getAmount()).getSign();
        try {
            if (withdrawApi.checkResult(params, key)) {
                if (withdrawApi.isSuccessAll(params)) {


                    deposit.setRemark("AUTO SUCCESS");
                    deposit.setSerialNumber(tradeNo);
                    this.depositService.updateBySuccess(deposit);
                    return withdrawApi.getReturnMessage(orderId, tradeNo, "1", "AUTO SUCCESS");
                }
                if (withdrawApi.isSuccessParts(params)) {


                    deposit.setRemark("AUTO SUCCESS PARTS");
                    deposit.setSerialNumber(tradeNo);
                    this.depositService.updateBySuccess(deposit, withdrawApi.getAmount(params));
                    return withdrawApi.getReturnMessage(orderId, tradeNo, "1", "AUTO SUCCESS PARTS");
                }


                deposit.setRemark("AUTO REJECT");
                this.depositService.updateByReject(deposit);
                return withdrawApi.getReturnMessage(orderId, tradeNo, "1", "AUTO REJECT");
            }

            return withdrawApi.getReturnMessage(orderId, tradeNo, "0", "key check faild");
        } catch (BaseCheckException e) {
            return withdrawApi.getReturnMessage(orderId, tradeNo, "0", e.getMessage());
        } catch (Exception e) {
            log.error(orderId + ":" + tradeNo, e);
        }
        return withdrawApi.getReturnMessage(orderId, tradeNo, "0", "exception");
    }


    private boolean checkKeySafePassword(String keySafePassword) {
        Object sessionValue = getSession().getAttribute("keySafePassword");
        if ((sessionValue == null) || (!sessionValue.toString().equals(keySafePassword))) {
            return false;
        }
        return true;
    }


    private ModelAndView getDepositResult(String error, User user) {
        ModelAndView modelAndView = getViewWithHeadModel("/deposit/depositResult");
        addBaseValue(modelAndView, user);
        if (error != null) {
            modelAndView.addObject("error", error);
        }

        return modelAndView;
    }

    private void addBaseValue(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.addObject("hadBankLevel", Boolean.valueOf(this.bankLevelService.count(user.getRegchargeCount().intValue(), user.getRegchargeAmount()) > 0));
        modelAndView.addObject("bankApiList", this.rechargeService.listBankApi(user.getAccount(), user.getRegchargeAmount(), user.getRegchargeCount().intValue()));
        modelAndView.addObject("groups", LotteryFactory.getGroups());
        modelAndView.addObject("lotts", this.lotteryService.list(null));
    }


    private String getBindCardPage() {
        return isMobile() ? "/user/bindCard" : "/user/index?needBindCard=0";
    }
}

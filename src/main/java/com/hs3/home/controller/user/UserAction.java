package com.hs3.home.controller.user;

import com.hs3.db.Page;
import com.hs3.entity.bank.BankUser;
import com.hs3.entity.contract.ContractConfig;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.users.DailyAcc;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.ContractUtils;
import com.hs3.models.Jsoner;
import com.hs3.models.PageData;
import com.hs3.models.user.UserTeamInfo;
import com.hs3.service.bank.BankUserService;
import com.hs3.service.contract.ContractConfigService;
import com.hs3.service.lotts.BonusGroupService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.report.TeamReportService;
import com.hs3.service.user.*;
import com.hs3.utils.DateUtils;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.auth.Auth;
import com.hs3.web.utils.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@Scope("prototype")
@RequestMapping({"/user"})
public class UserAction
        extends HomeAction {
    private static final int DEFAULT_LIMIT = 20;
    @Autowired
    private UserService userService;
    @Autowired
    private UserQuotaService userQuotaService;
    @Autowired
    private BankUserService bankUserService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private BonusGroupService bonusGroupService;
    @Autowired
    private ExtCodeService extcodeservice;
    @Autowired
    private TeamReportService teamReportService;
    @Autowired
    private ContractConfigService contractConfigService;
    @Autowired
    private DailyRuleService dailyRuleService;
    @Autowired
    private DailyAccService dailyAccService;

    @RequestMapping({"/index"})
    public Object index(String needBindCard, String tabId) throws Exception {
        ModelAndView mv = getViewWithHeadModel("/user/index");


        Map<String, String> lotMap = new LinkedHashMap();
        lotMap.put("ssc", "时时彩");
        lotMap.put("11x5", "11选5");
        lotMap.put("pk10", "北京赛车");
        lotMap.put("k3", "安徽快三");
        lotMap.put("3d", "福彩3D");
        lotMap.put("pl3", "排列三");
        mv.addObject("showLotts", lotMap);
        mv.addObject("startTime", DateUtils.formatDate(new Date()));
        mv.addObject("endTime", DateUtils.formatDate(new Date()));

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());

        BonusGroup bonusGroup = this.bonusGroupService.find(user.getBonusGroupId());


        BigDecimal stepRatio = bonusGroup.getUserMinRatio().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal("0.1") : bonusGroup.getUserMinRatio();

        BigDecimal maxRatio = user.getRebateRatio().subtract(bonusGroup.getUserMinRatio());


        mv.addObject("needBindCard", needBindCard);
        mv.addObject("specicalStr", StrUtils.getSpecialChars());

        List<String> dividendDateList = new ArrayList();
        ContractConfig config = this.contractConfigService.findEntity();
        if (config.getBonusCycle().intValue() == 0) {
            dividendDateList = ContractUtils.getOneDateList(new Date());
        } else {
            dividendDateList = ContractUtils.getSecondDateList(new Date());
        }
        mv.addObject("dividendDateList", dividendDateList);

        mv.addObject("tabId", tabId);
        mv.addObject("maxRatio", maxRatio.setScale(1));
        mv.addObject("stepRatio", stepRatio);
        mv.addObject("bonusGroup", bonusGroup);

        DailyAcc dailyAcc = this.dailyAccService.findByAccount(user.getAccount());
        if (dailyAcc != null) {
            mv.addObject("dailyAcc", dailyAcc);
            if (dailyAcc.getRuleId() != null) {
                mv.addObject("dailyRule", this.dailyRuleService.find(dailyAcc.getRuleId()));
            }
        }
        return mv;
    }


    @RequestMapping({"/list"})
    public Object list(Integer start, Integer limit)
            throws Exception {
        ModelAndView modelAndView = getViewWithHeadModel("/user/list");

        if (start == null) {
            start = Integer.valueOf(0);
        }
        if (limit == null) {
            limit = Integer.valueOf(20);
        }

        queryListRecordList(modelAndView, start, limit, null, 2);

        return modelAndView;
    }

    @RequestMapping({"/listRecordBody"})
    public Object listRecordBody(Integer start, String account, int type) throws Exception {
        ModelAndView modelAndView = getView("/user/listRecordBody");
        queryListRecordList(modelAndView, start, Integer.valueOf(20), account, type);

        return modelAndView;
    }

    private void queryListRecordList(ModelAndView modelAndView, Integer start, Integer limit, String account, int type) throws Exception {
        User user = (User) getLogin();
        String curAccount = null;
        if (StrUtils.hasEmpty(new Object[]{account})) {
            curAccount = user.getAccount();
        } else {
            curAccount = account;
        }
        List<User> listRecordList = new ArrayList();
        if (type > 1) {
            listRecordList = this.userService.listByCond(curAccount, start, limit);
        } else if (!user.getAccount().equals(account)) {
            listRecordList = this.userService.listBySelf(curAccount, user.getAccount(), start, limit);
        }

        modelAndView.addObject("listRecordList", listRecordList);
        modelAndView.addObject("start", start);
        modelAndView.addObject("limit", limit);
        BonusGroup bonusGroup = this.bonusGroupService.find(((User) getLogin()).getBonusGroupId());
        modelAndView.addObject("userBonusGroup", bonusGroup);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userRebateRatio", ((User) getLogin()).getRebateRatio());
    }

    @RequestMapping(value = {"/bindCard"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object bindCardMobile() {
        ModelAndView mv = getViewWithHeadModel("");
        mv.setViewName("/mobile/user/bindCard");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = {"/bindCard"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object bindCard(BankUser m, Integer id, String oldCard, String oldNiceName, String safeWord) {
        if (m.getBankNameId() == null)
            return Jsoner.error("请选择银行类型");
        if (!StrUtils.isNumber(m.getCard(), 16, 19))
            return Jsoner.error("卡号格式错误");
        if (!StrUtils.isAddress(m.getAddress(), 1, 255))
            return Jsoner.error("地址只有包含中文、数字、字母、横杠");
        if (!StrUtils.isName(m.getNiceName(), 1, 50)) {
            return Jsoner.error("姓名格式错误");
        }

        m.setAccount(((User) getLogin()).getAccount());
        m.setParentAccount(((User) getLogin()).getParentAccount());
        m.setUserMark(((User) getLogin()).getUserMark());
        int i = this.bankUserService.saveByUser(m, id, oldCard, oldNiceName, ((User) getLogin()).getAccount(), safeWord);
        if (i == 0)
            return Jsoner.success();
        if (i == 1)
            return Jsoner.error("已经绑定5张卡了!");
        if (i == 2)
            return Jsoner.error("信息校验错误!");
        if (i == 3)
            return Jsoner.error("您绑定的银行不受支持!");
        if (i == 5) {
            return Jsoner.error("一个账号只能绑定一个名字!");
        }
        return Jsoner.error("为了您的资金安全，银行卡不能绑定在多个平台的账户中，此卡已经被绑定。请换另外一张同姓名的银行卡进行绑定!");
    }

    @ResponseBody
    @RequestMapping(value = {"/checkOldCard"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object checkOldCard(int id, String oldNiceName, String oldCard, String safeWord) {
        if ((!Pattern.matches(".{8,16}", safeWord)) || (oldNiceName.length() > 50) || (oldCard.length() > 50)) {
            return Jsoner.error("参数非法！");
        }
        Map<String, Object> map = new HashMap(1);
        int i = this.bankUserService.findRecordByOld(id, oldNiceName, oldCard, safeWord, ((User) getLogin()).getAccount());
        if (i > 0) {
            map.put("valid", Integer.valueOf(1));
        } else {
            map.put("valid", Integer.valueOf(0));
        }
        return Jsoner.success(map);
    }


    @ResponseBody
    @RequestMapping(value = {"/showCard"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object showCard() {
        return Jsoner.success(this.bankUserService.listByAccount(((User) getLogin()).getAccount()));
    }


    @ResponseBody
    @RequestMapping(value = {"/stopCard"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object stopCard() {
        int i = this.userService.updateBankStatus(((User) getLogin()).getAccount());
        if (i <= 0) {
            return Jsoner.error("已经锁定!");
        }
        ((User) getLogin()).setBankStatus("1");
        return Jsoner.success();
    }


    @ResponseBody
    @RequestMapping(value = {"/getQuota"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getQuota() {
        return Jsoner.success(this.userQuotaService.listByAccount(((User) getLogin()).getAccount()));
    }


    @ResponseBody
    @RequestMapping(value = {"/loadQuota"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object loadQuota(String account) {
        List<Map<String, Object>> list = this.userQuotaService.loadQuota(account);
        return Jsoner.success(list);
    }

    @ResponseBody
    @RequestMapping(value = {"/getLottery"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getLottery() {
        List<Lottery> lotterys = this.lotteryService.listByStatus(0);
        return Jsoner.success(lotterys);
    }


    @Auth
    @ResponseBody
    @RequestMapping(value = {"/getMessage"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getMessage(String account) {
        return Jsoner.success(this.userService.findMessage(account));
    }


    @ResponseBody
    @RequestMapping(value = {"/listUserExtCode"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object listUserExtCode()
            throws ParseException {
        Page p = getPageWithParams();
        String url = ShortUrl.getUrl(getRequest(), "/registByCode?code=");
        List<Map<String, Object>> list = this.userService.listUserExtCode(((User) getLogin()).getAccount(), p);

        for (Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext(); ) {
            Map<String, Object> map = (Map) iterator.next();
            map.put("registAddress", url + map.get("code"));
            long createTime = DateUtils.toDate((String) map.get("createTime")).getTime();
            int validtime = ((Integer) map.get("validTime")).intValue();
            if (validtime != 0) {
                long validtimes = validtime * 86400000L;
                long now = new Date().getTime();
                if (createTime + validtimes < now) {
                    map.put("flag", Integer.valueOf(0));
                } else {
                    map.put("flag", Integer.valueOf(1));
                }
            } else {
                map.put("flag", Integer.valueOf(1));
            }
            String mobileRrl =  ShortUrl.getUrl(getRequest(), "/registered?code=");
            mobileRrl = mobileRrl.replace("http://", "http://m.");
            mobileRrl = mobileRrl.replace("www.", "");
            map.put("mobileRegistAddress", mobileRrl + map.get("code"));
        }
        return Jsoner.success(list);
    }


    @ResponseBody
    @RequestMapping(value = {"/amount"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getAmount() {
        Map<String, Object> map = new HashMap();
        map.put("amount", this.userService.getAmount(((User) getLogin()).getAccount()));

        return Jsoner.success(map);
    }


    @ResponseBody
    @RequestMapping(value = {"/getTeamInfo"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object getTeamInfo(String account, String childAccount, Integer status, Date begin, Date end) {
        if ((begin == null) || (end == null)) {
            return Jsoner.error("请选择查询的时间范围");
        }

        if (begin.getTime() > end.getTime()) {
            return Jsoner.error("开始时间不能大于结束时间");
        }

        begin = DateUtils.getDate(begin);

        end = DateUtils.AddSecond(DateUtils.getDate(end), 86399);


        if ((status == null) || (status.intValue() != 1)) {
            status = Integer.valueOf(0);
        }

        Page p = getPageWithParams();
        String parentAccount = ((User) getLogin()).getAccount();
        User u = null;
        if (status.intValue() == 1) {
            if (StrUtils.hasEmpty(new Object[]{childAccount})) {
                account = parentAccount;
            } else {
                account = childAccount;
            }

        } else if (StrUtils.hasEmpty(new Object[]{account})) {
            account = parentAccount;
        }

        u = this.userService.findIsDown(parentAccount, account);
        if (u == null) {
            return Jsoner.error("用户不存在");
        }
        List<UserTeamInfo> c = this.teamReportService.findTeamInfo(account, status.intValue(), begin, end, p);

        String parentList = u.getParentList();
        String subparentList = parentList.substring(parentList.lastIndexOf(((User) getLogin()).getAccount() + ","), parentList.length());
        List<String> userTree = ListUtils.toList(subparentList);


        PageData rel = new PageData(p.getRowCount(), c);
        rel.setObj(userTree);
        return Jsoner.success(rel);
    }


    @RequestMapping(value = {"/delExtCode"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Object delExtCode(int id) {
        int i = this.extcodeservice.setStatusByAccount(((User) getLogin()).getAccount(), Integer.valueOf(id), Integer.valueOf(1)).intValue();
        if (i <= 0) {
            return Jsoner.error("删除失败！id不存在！");
        }
        return Jsoner.success();
    }


    @RequestMapping(value = {"/updateHremark"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Object updateHremark(String account, String homeRemark) {
        if (StrUtils.isAddress(homeRemark, 1, 20)) {
            return Jsoner.getByResult(this.userService.updateHremark(account, homeRemark) > 0);
        }
        return Jsoner.error("输入备注内容格式不符合！");
    }

    @RequestMapping(value = {"/updateDailyWagesStatus"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Object updateDailyWagesStatus(String account, Integer dailyWagesStatus) {
        int count = 0;
        try {
            count = this.userService.updateDailyWagesStatus(account, ((User) getLogin()).getAccount(), dailyWagesStatus);
        } catch (BaseCheckException e) {
            return Jsoner.error(e.getMessage());
        }
        return Jsoner.getByResult(count == 1);
    }

    @ResponseBody
    @RequestMapping(value = {"/verifySafePassword"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object verifySafePassword(String safePassword) {
        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        if (safePassword == null) {
            return Jsoner.error("请输入安全密码");
        }
        if (!StrUtils.MD5(safePassword).equals(user.getSafePassword())) {
            return Jsoner.error("安全密码错误，请确认后重新输入！");
        }
        return Jsoner.success();
    }

    @ResponseBody
    @RequestMapping(value = {"/checkBank"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object checkBank() {
        List<BankUser> bu = this.bankUserService.findByAccount(((User) getLogin()).getAccount());
        if ((bu == null) || (bu.size() < 1)) {
            if (isMobile()) {
                return Jsoner.success("/user/bindCard");
            }
            return Jsoner.success("/user/index?tabId=myCard");
        }
        return Jsoner.success(null);
    }
}

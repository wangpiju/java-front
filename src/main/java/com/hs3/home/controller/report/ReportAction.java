package com.hs3.home.controller.report;

import com.hs3.db.Page;
import com.hs3.entity.finance.Deposit;
import com.hs3.entity.finance.Recharge;
import com.hs3.entity.lotts.AccountChangeType;
import com.hs3.entity.report.TeamInReport;
import com.hs3.entity.report.TeamReport;
import com.hs3.entity.report.UserReport;
import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.models.PageData;
import com.hs3.models.report.AllChange;
import com.hs3.service.finance.DepositService;
import com.hs3.service.finance.RechargeService;
import com.hs3.service.lotts.AccountChangeTypeService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.lotts.SettlementService;
import com.hs3.service.report.TeamInReportService;
import com.hs3.service.report.TeamReportService;
import com.hs3.service.report.UserReportService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
@Scope("prototype")
@RequestMapping({"/report"})
public class ReportAction
        extends HomeAction {
    private static final String TEAM_REPORT_TIME = "TEAM_REPORT_TIME";
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private UserReportService userReportService;
    @Autowired
    private TeamReportService teamReportService;
    @Autowired
    private TeamInReportService teamInReportService;
    @Autowired
    private AccountChangeTypeService accountChangeTypeService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private UserService userService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private SysConfigService sysConfigService;

    private Date getTeamStartTime() {
        SysConfig Teamsys = this.sysConfigService.find("TEAM_REPORT_TIME");
        int teamDay = 45;
        if (Teamsys != null) {
            teamDay = Integer.parseInt(Teamsys.getVal());
        }
        return DateUtils.getDate(DateUtils.addDay(new Date(), -teamDay));
    }


    @RequestMapping(value = {"/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object index(String tabId, String account) {
        ModelAndView mv = getViewWithHeadModel("/report/index");
        addWebDefaultTime(mv);
        List<AccountChangeType> accountChangeType = this.accountChangeTypeService.listByType(Integer.valueOf(0));

        if (((User) getLogin()).getUserType().intValue() == 1) {
            mv.addObject("userPower", Integer.valueOf(1));
        } else {
            mv.addObject("userPower", Integer.valueOf(0));
        }

        mv.addObject("accountChangeTypeJson", accountChangeType);
        mv.addObject("accountChangeTypeList", this.accountChangeTypeService.listByType(Integer.valueOf(1)));


        mv.addObject("teamStartTime", DateUtils.formatDate(getTeamStartTime()));

        mv.addObject("tabId", tabId);
        if ("settlement".equals(tabId)) {
            mv.addObject("account", account);
        }
        return mv;
    }


    @ResponseBody
    @RequestMapping(value = {"/teamList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object teamList(Integer teamStatus, Date startTime, Date endTime, String account) {
        if (teamStatus == null) {
            return Jsoner.error("参数不合法！");
        }
        Date initStartTime = getTeamStartTime();
        if (StrUtils.hasEmpty(new Object[]{startTime})) {
            startTime = initStartTime;
        } else {
            if ((!StrUtils.hasEmpty(new Object[]{startTime})) && (initStartTime.compareTo(startTime) > 0))
                return Jsoner.error("开始时间不能小于系统设置的开始时间！");
            if (startTime.compareTo(endTime) > 0) {
                return Jsoner.error("开始时间不能大于结束时间！");
            }
        }
        String currAccountString = null;

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            User u1 = this.userService.findIsDown(((User) getLogin()).getAccount(), account);
            if (u1 == null) {
                return Jsoner.error("用户不存在！");
            }
            currAccountString = account;
        } else {
            currAccountString = ((User) getLogin()).getAccount();
        }
        Page p = getPageWithParams();
        Map<String, Object> data = new HashMap();
        User user = this.userService.findByAccount(currAccountString);

        String parentList = user.getParentList();
        String subparentList = parentList.substring(parentList.lastIndexOf(((User) getLogin()).getAccount() + ","), parentList.length());
        List<String> userTree = ListUtils.toList(subparentList);

        List<TeamReport> list = new ArrayList();

        if (teamStatus.intValue() == 1) {
            list = this.teamReportService.newHistoryStatistics(p, currAccountString, DateUtils.getDate(startTime), DateUtils.getDate(endTime));
            data.put("list", new PageData(p.getRowCount(), list));
        } else if (teamStatus.intValue() == 2) {
            UserReport userReport = this.userReportService.selfReport(currAccountString, startTime, endTime, user.getTest());
            Map<String, Object> resultMap = this.teamReportService.getTeamList(p, currAccountString, startTime, endTime, user.getTest());
            list = (List) resultMap.get("resultList");
            data.put("userReport", userReport);
            data.put("teamReport", (TeamReport) resultMap.get("curTeamData"));
            data.put("list", new PageData(p.getRowCount(), list));
        }
        data.put("userTree", userTree);
        return Jsoner.success(data);
    }

    @ResponseBody
    @RequestMapping({"/ajaxGetUser"})
    public Object ajaxGetUser(String account) {
        User user = this.userService.findByAccount(account);
        Map<String, Object> data = new HashMap();
        data.put("account", ((User) getLogin()).getAccount());
        data.put("user", user);
        return Jsoner.success(data);
    }


    @ResponseBody
    @RequestMapping(value = {"/teamInList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object teamInList(Integer teamHistoryAndNow, String startTime, String endTime) {
        if (teamHistoryAndNow == null) {
            return Jsoner.error("参数不合法！");
        }
        Page p = getPageWithParams();
        TeamInReport m = new TeamInReport();
        m.setTest(Integer.valueOf(3));
        m.setAccount(((User) getLogin()).getAccount());
        List<TeamInReport> list = new ArrayList();
        if (teamHistoryAndNow.intValue() == 1) {
            list = this.teamInReportService.newHistoryStatistics(p, m, startTime, endTime);
        } else if (teamHistoryAndNow.intValue() == 2) {
            list = this.teamInReportService.newHistoryDetails(p, m, startTime, endTime);
        } else {
            m.setCreateDate(new Date());
            User user = this.userService.findByAccount(m.getAccount());
            m.setTest(user.getTest());
            list = this.teamInReportService.todayDataList(p, m);
        }
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }


    @ResponseBody
    @RequestMapping(value = {"/settlementList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object settlementList(String account, Integer status, Date begin, Date end) {
        if (StrUtils.hasEmpty(new Object[]{account})) {
            account = ((User) getLogin()).getAccount();
        } else {
            User u = this.userService.findIsDown(((User) getLogin()).getAccount(), account);
            if (u == null) {
                return Jsoner.error("用户不存在！");
            }
        }
        if ((status == null) || (1 != status.intValue())) {
            status = Integer.valueOf(0);
        }
        Page p = getPageWithParams();
        List<AllChange> list = this.settlementService.listAll(account, status.intValue(), begin, end, p);
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }


    @ResponseBody
    @RequestMapping(value = {"/rechargeList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargeList(String account, Integer isIncludeChildFlag, String startTime, String endTime) {
        String currAccountString = null;

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            User u1 = this.userService.findIsDown(((User) getLogin()).getAccount(), account);
            if (u1 == null) {
                return Jsoner.error("用户不存在！");
            }
            currAccountString = account;
        } else {
            currAccountString = ((User) getLogin()).getAccount();
        }
        Page p = getPageWithParams();

        boolean isIncludeChild = (isIncludeChildFlag != null) && (isIncludeChildFlag.intValue() != 0);
        List<Recharge> list = this.rechargeService.listByHome(currAccountString, isIncludeChild, startTime, endTime, p);
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }


    @ResponseBody
    @RequestMapping(value = {"/depositList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object depositList(String account, Integer isIncludeChildFlag, String startTime, String endTime) {
        String currAccountString = null;

        if (!StrUtils.hasEmpty(new Object[]{account})) {
            User u1 = this.userService.findIsDown(((User) getLogin()).getAccount(), account);
            if (u1 == null) {
                return Jsoner.error("用户不存在！");
            }
            currAccountString = account;
        } else {
            currAccountString = ((User) getLogin()).getAccount();
        }
        Page p = getPageWithParams();

        boolean isIncludeChild = (isIncludeChildFlag != null) && (isIncludeChildFlag.intValue() != 0);
        List<Deposit> list = this.depositService.listByHome(currAccountString, isIncludeChild, startTime, endTime, p);
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }
}

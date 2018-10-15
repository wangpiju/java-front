package com.hs3.home.controller.gh.proxy;

import com.hs3.entity.report.AgentReport;
import com.alibaba.fastjson.JSONObject;
import com.hs3.dao.article.NoticeDao;
import com.hs3.dao.bank.BankNameDao;
import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.bank.RechargeWayDao;
import com.hs3.dao.finance.RechargeDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.lotts.SettlementDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserNoticeDao;
import com.hs3.db.Page;
import com.hs3.entity.article.NoticeRead;
import com.hs3.entity.bank.BankName;
import com.hs3.entity.bank.BankSys;
import com.hs3.entity.bank.BankUser;
import com.hs3.entity.bank.RechargeWay;
import com.hs3.entity.finance.FinanceSetting;
import com.hs3.entity.finance.Recharge;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.report.AgentReport;
import com.hs3.entity.report.UserReport;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.models.report.AllChange;
import com.hs3.service.article.NoticeReadService;
import com.hs3.service.bank.BankNameService;
import com.hs3.service.bank.BankUserService;
import com.hs3.service.finance.DepositService;
import com.hs3.service.finance.FinanceSettingService;
import com.hs3.service.finance.RechargeService;
import com.hs3.service.newReport.CpsReportService;
import com.hs3.service.report.TeamReportService;
import com.hs3.service.report.UserReportService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.MathUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.entity.UnderUser;
import org.apache.commons.beanutils.ConvertUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Stephen Zhou
 * 代理中心
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/proxy"})
public class ProxyAction extends HomeAction {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserDao userDao;

    @Autowired
    private BetDao betDao;

    @Autowired
    private BankNameDao bankNameDao;

    @Autowired
    private BankUserDao bankUserDao;

    @Autowired
    private RechargeWayDao rechargeWayDao;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private RechargeDao rechargeDao;

    @Autowired
    private UserReportService userReportService;

    @Autowired
    private BankUserService bankUserService;
    @Autowired
    private BankNameService bankNameService;
    @Autowired
    private FinanceSettingService financeSettingService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private UserNoticeDao userNoticeDao;

    @Autowired
    private SettlementDao settlementDao;

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private CpsReportService cpsReportService;

    @Autowired
    private NoticeReadService noticeReadService;


    /***
     <<<<<<< HEAD
     * 特别说明：
     * 1、UserDao会员管理列表函数underUserList(String account)的增加
     * 2、BetDao新增获取会员投注数据的函数betOrderList(String account, Integer include, Integer status, String startTime, String endTime, Integer start, Integer limit)
     * 3、FinanceChangeDao新增获取会员交易记录的函数tradeList(String account, Integer include, Integer accountChangeTypeId, String startTime, String endTime, Integer start, Integer limit)
     *
     * 4、UserReportDao新增获取会员个人盈亏的函数gainLostList(String account, String startDate, String endDate)
     * 5、BankNameDao新增获取银行卡列表函数mapListBankAll()
     * 6、BankUserDao新增通过ID获取BankUser对象的函数findById(String id)
     * 7、新增RechargeWay和RechargeWayDao
     * 8、RechargeService新增保存充值申请的函数saveByPayZ(...)
     * 9、UserDao会员管理列表函数underUserList(String account)加取赔率字段
     *
     * 10、RechargeWay类加入bankNameIds字段
     * 11、RechargeService新增保存充值申请的函数saveByBank_Z(...)
     * 12、RechargeService新增保存充值申请的函数save_Z(...)和RechargeDao新增查询用户充值记录的函数listRecharge(String account, Integer start, Integer limit)
     * 13、UserDao新增根据用户获取团队信息的函数getTeamInfoByAccount(String account)
     * 14、UserDao修改会员管理列表函数underUserList(String account)取的是整个下线团队的会员信息（并备份原接口），新增获取整个团队会员基础信息的接口userTeamList(String account)
     * 15、UserNoticeDao新增listUserNotice(...)函数，DepositService的updateBySuccess(Deposit deposit)函数加入插入私信内容
     *
     */


    //我的账户-序21
    private static String[] getday(int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -num);
        String dayStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String startTime = dayStr + " 00:00:00";
        String endTime = dayStr + " 23:59:59";
        String[] dayStrArr = new String[2];
        dayStrArr[0] = startTime;
        dayStrArr[1] = endTime;
        return dayStrArr;
    }


    //我的账户-序22

    //取N个月前的时间区域
    private static String[] getTerMonth(int monthNum) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -monthNum);
        int dateStr = cal.get(Calendar.DATE);
        cal.add(Calendar.DATE, -(dateStr - 1));
        String dayStr = format.format(cal.getTime());
        String startTime = dayStr + " 00:00:00";

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(cal.getTime());

        String endTime = last + " 23:59:59";
        String[] dayStrArr = new String[2];
        dayStrArr[0] = startTime;
        dayStrArr[1] = endTime;
        return dayStrArr;
    }


    //我的账户-序23

    /***获取会员管理列表**
     *
     * String account			账户
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/getUnderUserList"}, method = {RequestMethod.GET})
    public JsonNode getUnderUserList(String account) {

        HashMap<String, String> returnC = new HashMap<String, String>();

        if (account == null || account.equals("")) {
            returnC.put("message", "账户不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        User user_s = getLogin();
        String account_s = user_s.getAccount();

        List<User> userTeamList = userDao.userTeamList(account_s);
        boolean u_flag = false;
        for (User user : userTeamList) {
            if (account.equals(user.getAccount())) {
                u_flag = true;
                break;
            }
        }
        if (!u_flag) {
            returnC.put("message", "该用户不在您所在的团队内！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        List<Map<String, Object>> userMapL = userDao.underUserList_z(account);
        List<Map<String, Object>> userMapR = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> uo : userMapL) {
            Integer userType = Integer.valueOf(String.valueOf(uo.get("userType")));
            if (userType == 1) {
                uo.put("userTypeName", "代理");
            } else if (userType == 0) {
                uo.put("userTypeName", "会员");
            } else {
                uo.put("userTypeName", "其他");
            }

            userMapR.add(uo);
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(userMapR));
    }


    //我的账户-序27

    /***获取会员投注列表
     *
     * String account			账户
     * Integer include		是否包含下级	0：只查本账户，1：本账户加下级，2：只查下级
     * Integer status			投注状态		100：全部，1：已中奖，2：未中奖，6：未开奖
     * Integer betweenType	时间区域标记	1：今天，2：昨天，3：七天内
     * Integer start			从第几条开始取
     * Integer limit			共取出多少条
     *
     * **/
    @ResponseBody
    @RequestMapping(value = {"/getbetOrderList"}, method = {RequestMethod.GET})
    public JsonNode getbetOrderList(String account, Integer include, Integer status, Integer betweenType, Integer start, Integer limit) {
        HashMap<String, String> returnC = new HashMap<String, String>();

        if (include == null) {
            returnC.put("message", "[是否包含下级]标记不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (include != 0 && include != 1 && include != 2) {
            returnC.put("message", "[是否包含下级]标记传参不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (status != 100 && status != 1 && status != 2 && status != 6 && status != 0) {
            returnC.put("message", "[投注状态]标记传参不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (betweenType != 1 && betweenType != 2 && betweenType != 3) {
            returnC.put("message", "[时间区域标记]标记传参不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (start == null || limit == null || limit == 0) {
            returnC.put("message", "[数据条数]标记不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (account == null || account.equals("")) {
            returnC.put("message", "账户不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        User user_s = getLogin();
        String account_s = user_s.getAccount();

        List<User> userTeamList = userDao.userTeamList(account_s);
        boolean u_flag = false;
        for (User user : userTeamList) {
            if (account.equals(user.getAccount())) {
                u_flag = true;
                break;
            }
        }
        if (!u_flag) {
            returnC.put("message", "该用户不在您所在的团队内！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        String startTime = "";
        String endTime = "";

        if (betweenType != null && betweenType != 0) {
            if (betweenType == 1) {//今天
                String[] dayStrArr = getday(0);
                startTime = dayStrArr[0];
                endTime = dayStrArr[1];
            } else if (betweenType == 2) {//昨天
                String[] dayStrArr = getday(1);
                startTime = dayStrArr[0];
                endTime = dayStrArr[1];
            } else if (betweenType == 3) {//7天内
                String[] dayStrArr = getday(6);
                startTime = dayStrArr[0];
                String[] dayStrArrZ = getday(0);
                endTime = dayStrArrZ[1];
            }
        }

        int betOrderAllCount = betDao.betOrderListCount(account, include, status, startTime, endTime);

        List<Bet> betOrderList = betDao.betOrderList(account, include, status, startTime, endTime, start, limit);
        List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> bethm = null;
        for (Bet betO : betOrderList) {
            bethm = new HashMap<String, Object>();
            bethm.put("id", betO.getId());//投注ID
            bethm.put("lotteryId", betO.getLotteryId());//彩种ID
            bethm.put("lotteryName", betO.getLotteryName());//彩种名称
            bethm.put("amount", betO.getAmount());//投注金额
            bethm.put("createTime", DateUtils.format(betO.getCreateTime()));//投注时间

            Integer status_o = betO.getStatus();
            bethm.put("status", status_o);//状态
            String statusName = "";
            if (status_o == 0) {
                statusName = "等待开奖";
            } else if (status_o == 1) {
                statusName = "已中奖";
            }else if (status_o == 6) {
                statusName = "未开奖";
            }
            else if (status_o == 2) {
                statusName = "未中奖";
            } else if (status_o == 3) {
                statusName = "未开始";
            } else if (status_o == 4) {
                statusName = "个人撤单";
            } else if (status_o == 5) {
                statusName = "系统撤单";
            } else if (status_o == 7) {
                statusName = "恶意注单";
            } else if (status_o == 8) {
                statusName = "暂停";
            } else if (status_o == 9) {
                statusName = "追中撤单";
            }
            bethm.put("statusName", statusName);//状态名称


            bethm.put("win", betO.getWin());//奖金
            bethm.put("openNum", betO.getOpenNum());//开奖号码
            bethm.put("playName", betO.getPlayName());//玩法名称
            bethm.put("content", betO.getContent());//投注内容
            bethm.put("seasonId", betO.getSeasonId());//奖期
            bethm.put("account", betO.getAccount());//账户
            bethm.put("betCount", betO.getBetCount());//注数
            bethm.put("price", betO.getPrice());//倍数
            bethm.put("playerId", betO.getPlayerId());//玩法ID
            resultList.add(bethm);
        }

        HashMap<String, Object> dataO = new HashMap<String, Object>();
        dataO.put("betOrderAllCount", betOrderAllCount);
        dataO.put("list", resultList);

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(dataO));
    }


    //我的账户-序14-0

    /***获取会员交易列表
     *
     * String account						账户
     * Integer include					是否包含下级	0：只查本账户，1：本账户加下级，2：只查下级
     * Integer accountChangeType			交易类型		100：全部，1：提现，2：充值
     * Integer betweenType				时间区域标记	1：今天，2：昨天，3：七天内
     * Integer start						从第几条开始取
     * Integer limit						共取出多少条
     * @throws ParseException
     *
     * **/
    @ResponseBody
    @RequestMapping(value = {"/getTradeList"}, method = {RequestMethod.GET})
    public JsonNode getTradeList(String account, Integer include, Integer accountChangeType, Integer betweenType, Integer start, Integer limit) throws ParseException {
        HashMap<String, String> returnC = new HashMap<String, String>();

        if (account == null || account.trim().equals("")) {
            returnC.put("message", "[账户]不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (include == null) {
            returnC.put("message", "[是否包含下级]标记不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (include != 0 && include != 1 && include != 2) {
            returnC.put("message", "[是否包含下级]标记传参不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (accountChangeType != 100 && accountChangeType != 1 && accountChangeType != 2) {
            returnC.put("message", "[交易类型]标记传参不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (betweenType != 1 && betweenType != 2 && betweenType != 3) {
            returnC.put("message", "[时间区域标记]标记传参不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        User user_s = getLogin();
        String account_s = user_s.getAccount();

        List<User> userTeamList = userDao.userTeamList(account_s);
        boolean u_flag = false;
        for (User user : userTeamList) {
            if (account.equals(user.getAccount())) {
                u_flag = true;
                break;
            }
        }
        if (!u_flag) {
            returnC.put("message", "该用户不在您所在的团队内！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }


        String startTime = "";
        String endTime = "";

        if (betweenType != null && betweenType != 0) {
            if (betweenType == 1) {//今天
                String[] dayStrArr = getday(0);
                startTime = dayStrArr[0];
                endTime = dayStrArr[1];
            } else if (betweenType == 2) {//昨天
                String[] dayStrArr = getday(1);
                startTime = dayStrArr[0];
                endTime = dayStrArr[1];
            } else if (betweenType == 3) {//7天内
                String[] dayStrArr = getday(6);
                startTime = dayStrArr[0];
                String[] dayStrArrZ = getday(0);
                endTime = dayStrArrZ[1];
            }
        }

        Date begin = DateUtils.toDate(startTime);
        Date end = DateUtils.toDate(endTime);
        List<AllChange> allChangeList = settlementDao.listAll_Z(account, include, accountChangeType, begin, end, start, limit);
        int allCount = settlementDao.listAll_ZNum(account, include, accountChangeType, begin, end);


        List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> allChangehm = null;
        for (AllChange allChangeO : allChangeList) {
            allChangehm = new HashMap<String, Object>();
            allChangehm.put("accountChangeTypeName", allChangeO.getChangeType());//交易类型名称
            allChangehm.put("changeAmount", allChangeO.getAmount());//变更金额
            allChangehm.put("changeTime", DateUtils.format(allChangeO.getCreateTime()));//变更时间
            allChangehm.put("account", allChangeO.getAccount());//账户
            allChangehm.put("balance", allChangeO.getBalance());//余额
            allChangehm.put("lotteryName", allChangeO.getLotteryName());//彩种
            allChangehm.put("seasonId", allChangeO.getSeasonId());//期号
            allChangehm.put("playName", allChangeO.getPlayName());//玩法
            resultList.add(allChangehm);
        }

        HashMap<String, Object> dataO = new HashMap<String, Object>();
        dataO.put("list", resultList);
        dataO.put("allCount", allCount);


        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(dataO));
    }


    //我的账户-序14

    /***今日盈虧
     * @throws ParseException ***/
    @ResponseBody
    @RequestMapping(value = {"/getGainLost"}, method = {RequestMethod.GET})
    public JsonNode getGainLost() throws ParseException {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);

        Date begin = DateUtils.toDate(getday(0)[0]);
        Date end = DateUtils.toDate(getday(0)[0]);

        UserReport userReport = userReportService.selfReport(account, begin, end, user.getTest());

        Calendar cal = Calendar.getInstance();
        String dayStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        HashMap<String, Object> obData = new HashMap<String, Object>();
        if (userReport != null) {
            obData.put("account", account);//账户
            obData.put("createDate", dayStr);//日期
            obData.put("betAmount", userReport.getBetAmount());//投注金额
            obData.put("winAmount", userReport.getWinAmount());//中奖金额
            obData.put("activityAndSend", userReport.getActivityAndSend());//活动礼金
            obData.put("rechargeAmount", userReport.getRechargeAmount());//充值金额
            obData.put("drawingAmount", userReport.getDrawingAmount());//提现金额
            obData.put("juniorRebateAmount", userReport.getJuniorRebateAmount());//下级返点金额
        } else {
            obData.put("account", account);//账户
            obData.put("createDate", dayStr);//日期
            obData.put("betAmount", 0.00);//投注金额
            obData.put("winAmount", 0.00);//中奖金额
            obData.put("activityAndSend", 0.00);//活动礼金
            obData.put("rechargeAmount", 0.00);//充值金额
            obData.put("drawingAmount", 0.00);//提现金额
            obData.put("juniorRebateAmount", 0.00);//下级返点金额
        }


        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(obData));
    }


    //我的账户-序15

    /***获取银行卡列表***/
    @ResponseBody
    @RequestMapping(value = {"/getBankNameList"}, method = {RequestMethod.GET})
    public JsonNode getBankNameList() {
        List<Map<String, Object>> mapListBankAll = bankNameDao.mapListBankAll();
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(mapListBankAll));
    }


    //我的账户-序28

    /***获取用户银行卡列表***/
    @ResponseBody
    @RequestMapping(value = {"/getBankUserList"}, method = {RequestMethod.GET})
    public JsonNode getBankUserList() {
        User user_s = getLogin();
        String account = user_s.getAccount();

        List<BankUser> bankUserList = bankUserDao.listByAccount(account, 0);
        List<Map<String, Object>> mapListBankAll = this.bankNameDao.mapListBankAll();

        List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> obDatahm = null;
        for (BankUser obData : bankUserList) {
            obDatahm = new HashMap<String, Object>();
            obDatahm.put("id", obData.getId());//数据ID
            obDatahm.put("bankNameId", obData.getBankNameId());//系统银行ID

            String code = "";
            String title = "";
            for (Map<String, Object> obm : mapListBankAll) {
                if (Integer.valueOf(String.valueOf(obm.get("id"))) == obData.getBankNameId()) {
                    code = String.valueOf(obm.get("code"));
                    title = String.valueOf(obm.get("title"));
                    break;
                }
            }
            obDatahm.put("code", code);
            obDatahm.put("title", title);

            obDatahm.put("card", obData.getCard());//卡号
            obDatahm.put("niceName", obData.getNiceName());//姓名
            obDatahm.put("address", obData.getAddress());//地址
            resultList.add(obDatahm);
        }
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(resultList));
    }


    //我的账户-序28-0

    /***设置用户银行卡*
     *
     * @param id			用户银行卡信息ID
     * @param bankNameId	银行卡ID
     * @param card			卡号
     * @param card2			卡号2
     * @param address		地址
     * @param niceName		开户人姓名
     * @param securityCode	账户安全码
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/setBankUser"}, method = {RequestMethod.POST})
    public JsonNode setBankUser(Integer id, Integer bankNameId, String card, String card2, String address, String niceName, String securityCode) {
        User user_s = getLogin();
        String account = user_s.getAccount();

        User user = userDao.findByAccount(account);
        String u_securityCode = user.getSafePassword();

        HashMap<String, String> returnC = new HashMap<String, String>();

        if (u_securityCode == null || u_securityCode.trim().equals("")) {
            returnC.put("message", "安全码为空，请先设置安全码！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (!securityCode.trim().equals(u_securityCode)) {
            returnC.put("message", "账户安全码输入错误！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (bankNameId == null || bankNameId == 0) {
            returnC.put("message", "请先选择银行！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (!card.equals(card2)) {
            returnC.put("message", "两次银行卡号输入不相同！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (card == null || card.trim().equals("") || card2 == null || card2.trim().equals("")) {
            returnC.put("message", "银行卡号不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (address == null || address.trim().equals("")) {
            returnC.put("message", "银行卡开户地址不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (niceName == null || niceName.trim().equals("")) {
            returnC.put("message", "开户人姓名不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        BankUser bankUser = null;
        if (id == null || id == 0) {

            int num = bankUserDao.findBankCount(account);
            if (num >= 5) {
                returnC.put("message", "每个用户最多只能绑定5张银行卡！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            bankUser = new BankUser();
            bankUser.setAccount(account);
            bankUser.setCreateTime(new Date());
            bankUser.setParentAccount(user.getParentAccount());
        } else {
            bankUser = bankUserDao.findById(String.valueOf(id));
        }

        bankUser.setBankNameId(bankNameId);
        bankUser.setCard(card);
        bankUser.setAddress(address);
        bankUser.setNiceName(niceName);

        if (id == null || id == 0) {
            bankUserDao.save(bankUser);
        } else {
            bankUserDao.update(bankUser);
        }

        returnC.put("message", "设置成功！");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序

    /***获取支付方式列表***/
    @ResponseBody
    @RequestMapping(value = {"/getRechargeWayList"}, method = {RequestMethod.GET})
    public JsonNode getRechargeWayList() {
        List<RechargeWay> obList = rechargeWayDao.listRechargeWay();
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(obList));
    }

    /***获取充值基础信息***/
    @ResponseBody
    @RequestMapping(value = {"/rechargeEntrance"}, method = {RequestMethod.GET})
    public JsonNode rechargeEntrance(Integer rechargeWay) {
        HashMap<String, String> returnC = new HashMap<String, String>();
        try {
            if (rechargeWay == null || rechargeWay == 0) {
                returnC.put("message", "充值方式参数异常！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            HashMap<String, Object> returnO = new HashMap<String, Object>();
            Integer receiveBankId = 0;
            String receiveBankName = "";
            String receiveCard = "";
            String receiveAddress = "";
            String receiveNiceName = "";
            String QRCodeUrl = "";

            User user_s = getLogin();
            String account = user_s.getAccount();
            User user = userDao.findByAccount(account);

            if(user.getTest() == 1){
                returnC.put("message", "测试账户不能充值！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            RechargeWay rechargeWayObj = rechargeWayDao.findByID(rechargeWay);

            if(rechargeWayObj == null){
                returnC.put("message", "充值方式数据异常！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            Integer waytype = rechargeWayObj.getWaytype();

            if(waytype != null && ( waytype == 1 || waytype == 2 )){
                //根据用户获取对应等级的收款银行信息
                BankSys bankSys = rechargeService.findBankSys_Z(rechargeWay, user);
                Integer bankNameId = bankSys.getNameId();
                BankName bankName = bankNameDao.getBankNameById(bankNameId);

                receiveBankId = bankSys.getId();
                receiveBankName = bankName.getTitle();
                receiveCard = bankSys.getCard();
                receiveAddress = bankSys.getAddress();
                receiveNiceName = bankSys.getNiceName();

                if(setPayQRCode(rechargeWayObj)){
                    QRCodeUrl = "/res/payQRCode/" + rechargeWayObj.getAlino() + rechargeWayObj.getRandomNow() + ".jpg";
                }
            }
            returnO.put("receiveBankId", receiveBankId); //收款银行数据ID
            returnO.put("receiveBankName", receiveBankName); //收款银行名称
            returnO.put("receiveCard", receiveCard); //收款银行卡号
            returnO.put("receiveAddress", receiveAddress); //收款银行地址
            returnO.put("receiveNiceName", receiveNiceName); //收款银行开户人姓名
            returnO.put("QRCodeUrl", QRCodeUrl); //二维码路径
            returnO.put("waytype", waytype); //渠道类型 0：其他、1：扫码、2：看卡、3：线上
            returnO.put("attfirst", rechargeWayObj.getAttfirst()); //属性1
            returnO.put("attsecond", rechargeWayObj.getAttsecond()); //属性2
            returnO.put("attthird", rechargeWayObj.getAttthird()); //属性3
            returnO.put("attempty", rechargeWayObj.getAttempty());

            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnO));
        } catch (Exception e) {
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }

    private boolean setPayQRCode(RechargeWay rechargeWayObj) throws IOException {

        String alino = rechargeWayObj.getAlino();
        String realPath = getSession().getServletContext().getRealPath("/");

        if(!StrUtils.hasEmpty(new Object[]{rechargeWayObj.getRandomOld()})) {
            String oldPath = realPath + "/res/payQRCode/" + alino + rechargeWayObj.getRandomOld() + ".jpg";

            File oldFile = new File(oldPath);
            //if(oldFile.isFile() ){
            if(oldFile.exists()){
                oldFile.delete();
            }

        }

        if(!StrUtils.hasEmpty(new Object[]{rechargeWayObj.getRandomNow()})) {

            String path = realPath + "/res/payQRCode/" + alino + rechargeWayObj.getRandomNow() + ".jpg";

            File nowFile = new File(path);
            if (!nowFile.exists()) {
                RechargeWay rechargeWay = rechargeWayDao.find(rechargeWayObj.getId());
                String imgData = rechargeWay.getImgData();
                if (!StrUtils.hasEmpty(new Object[]{imgData})) {

                    BASE64Decoder decoder = new BASE64Decoder();

                    // 解密
                    byte[] b = decoder.decodeBuffer(imgData);

                    // 处理数据
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {
                            b[i] += 256;
                        }
                    }

                    OutputStream out = new FileOutputStream(path);
                    out.write(b);
                    out.flush();
                    out.close();

                    return true;
                } else {
                    return false;
                }
            }else{
                return true;
            }
        }

        return false;
    }


    /***新的提交支付申请*
     *
     * @param rechargeWay       充值方式 1、微信，5、银行转账
     * @param receiveBankId	     收款银行卡ID
     * @param chargeamount	     申请金额
     * @param niceName		     姓名
     * @param checkCode		     注言
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/setPayRequest"}, method = {RequestMethod.POST})
    public JsonNode setPayRequest(Integer rechargeWay, BigDecimal chargeamount, Integer receiveBankId, String niceName, String checkCode) {
        HashMap<String, String> returnC = new HashMap<String, String>();
        try {
            if (rechargeWay == null || rechargeWay == 0) {
                returnC.put("message", "充值方式参数异常！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            if (chargeamount.compareTo(new BigDecimal(0)) <= 0) {
                returnC.put("message", "充值金额不正确！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            RechargeWay rechargeWayObj = rechargeWayDao.findByID(rechargeWay);

            Integer attempty = rechargeWayObj.getAttempty();
            if(attempty != null && attempty == 1 && StrUtils.hasEmpty(checkCode)){
                returnC.put("message", "凭证信息不能为空！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            if (chargeamount.compareTo(BigDecimal.valueOf(rechargeWayObj.getMinmoney())) < 0) {
                returnC.put("message", "充值金额[" + chargeamount + "]小于" + rechargeWayObj.getAlias() + "渠道最低限额[" + rechargeWayObj.getMinmoney() + "]");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            if (chargeamount.compareTo(BigDecimal.valueOf(rechargeWayObj.getMaxmoney())) > 0) {
                returnC.put("message", "充值金额[" + chargeamount + "]大于" + rechargeWayObj.getAlias() + "渠道最高限额[" + rechargeWayObj.getMaxmoney() + "]");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            if (receiveBankId == null || receiveBankId == 0) {
                returnC.put("message", "收款银行数据ID不能为空！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            User user_s = getLogin();
            String account = user_s.getAccount();

            User user = userDao.findByAccount(account);

            rechargeService.saveByBank_ZZ(user, chargeamount, receiveBankId, "", niceName, checkCode);

            HashMap<String, Object> returnO = new HashMap<String, Object>();
            returnO.put("message", "提交成功！");
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnO));
        } catch (BaseCheckException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        } catch (IllegalArgumentException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }


    /***获取账户充值列表**
     *
     * Integer start	从第几条开始取
     * Integer limit	共取出多少条
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/getRechargeList"}, method = {RequestMethod.GET})
    public JsonNode getRechargeList(Integer start, Integer limit) {

        User user_s = getLogin();
        String account = user_s.getAccount();
        List<Recharge> obList = rechargeDao.listRecharge(account, start, limit);

        HashMap<String, Object> returnO = null;
        List<HashMap<String, Object>> returnOL = new ArrayList<HashMap<String, Object>>();
        for (Recharge recharge : obList) {
            returnO = new HashMap<String, Object>();

            returnO.put("id", recharge.getId()); //订单号

            int status = recharge.getStatus();
            returnO.put("status", status); //状态

            String statusName = "";
            if (status == 0) statusName = "未处理";
            else if (status == 1) statusName = "队列等待中";//本意是拒绝
            else if (status == 2) statusName = "完成";
            else if (status == 3) statusName = "已过期";
            else if (status == 4) statusName = "已撤销";
            else if (status == 5) statusName = "正在处理";
            else if (status == 6) statusName = "审核中";
            else if (status == 20) statusName = "待办中";//本意是挂起
            else if (status == 99) statusName = "无信息";

            returnO.put("statusName", statusName); //状态名称

            returnO.put("amount", recharge.getAmount()); //充值金额
            returnO.put("card", recharge.getCard()); //卡号
            returnO.put("niceName", recharge.getNiceName()); //姓名
            returnO.put("bankName", recharge.getBankName()); //充值银行
            returnO.put("createTime", DateUtils.format(recharge.getCreateTime())); //提交申请时间
            returnO.put("receiveBankName", recharge.getReceiveBankName()); //收款银行
            returnO.put("receiveAddress", recharge.getReceiveAddress()); //收款银行支行地址
            returnO.put("checkCode", recharge.getCheckCode()); //注言

            String receiveCard = recharge.getReceiveCard();
            String receiveNiceName = recharge.getReceiveNiceName();
            if (status == 0 || status == 5 || status == 6) {
                returnO.put("receiveCard", receiveCard); //收款银行卡号
                returnO.put("receiveNickName", receiveNiceName); //收款银行开户名
            } else {
                if(receiveCard!=null && receiveCard.length()>4) {
                    returnO.put("receiveCard", "*** **** **** **** " + receiveCard.substring(receiveCard.length() - 4)); //收款银行卡号
                }else{
                    returnO.put("receiveCard", receiveCard); //收款银行卡号
                }
                if(receiveNiceName == null) receiveNiceName = "";
                if(recharge.getRechargeType() == 0 && receiveNiceName.length()>1) {
                    receiveNiceName = "**" + receiveNiceName.substring(receiveNiceName.length() - 1);
                }
                returnO.put("receiveNickName", receiveNiceName); //收款银行开户名
            }

            String bankNameCode = recharge.getBankNameCode();
            returnO.put("bankNameCode", bankNameCode); //银行代码
            String QRCodeUrl = "";
            if(bankNameCode != null && bankNameCode.equals("weixin")){
                QRCodeUrl = "/res/payQRCode/alipayQRCode.jpg";
            }
            returnO.put("QRCodeUrl", QRCodeUrl); //二维码图片路径

            returnOL.add(returnO);
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnOL));
    }

    //我的账户-序18

    /***获取代理报表
     *
     * String 	account			账户
     * Integer 	dateFlag		时间区域标记  0：今天，1：昨天，2：本月，3：上月
     * @throws ParseException
     *
     * **/
    @ResponseBody
    @RequestMapping(value = {"/getUserTeam"}, method = {RequestMethod.GET})
    public JsonNode getUserTeam(String account, Integer dateFlag) throws ParseException {
        HashMap<String, String> returnC = new HashMap<String, String>();

        if (account == null || account.trim().equals("")) {
            returnC.put("message", "账户不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (dateFlag == null || (dateFlag != 0 && dateFlag != 1 && dateFlag != 2 && dateFlag != 3)) {
            returnC.put("message", "[时间区域]标记不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        User u = this.userDao.findByAccount(account);
        if (u == null) {
            returnC.put("message", "找不到使用者帐户！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        User user_s = getLogin();
        String account_s = user_s.getAccount();

        List<User> userTeamList = userDao.userTeamList(account_s);
        boolean u_flag = false;
        for (User user : userTeamList) {
            if (account.equals(user.getAccount())) {
                u_flag = true;
                break;
            }
        }
        if (!u_flag) {
            returnC.put("message", "该用户不在您所在的团队内！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        /*if(u.getUserType() != 1){
            returnC.put("message", "用户类型不是代理商！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }*/

        Date begin = null;
        Date end = null;
        String startTime = "";
        String endTime = "";

        if (dateFlag == 0) {//今天
            String[] dayStrArr = getday(0);
            startTime = dayStrArr[0];
            endTime = dayStrArr[1];
        } else if (dateFlag == 1) {//昨天
            String[] dayStrArr = getday(1);
            startTime = dayStrArr[0];
            endTime = dayStrArr[1];
        } else if (dateFlag == 2) {//本月

            Calendar cal = Calendar.getInstance();
            int dateStr = cal.get(Calendar.DATE);
            String[] dayStrArr = getday(dateStr - 1);
            startTime = dayStrArr[0];
            String[] dayStrArrZ = getday(0);
            endTime = dayStrArrZ[1];

        } else if (dateFlag == 3) {//上月
            String[] dayStrArr = getTerMonth(1);
            startTime = dayStrArr[0];
            endTime = dayStrArr[1];
        }

        begin = DateUtils.toDate(startTime);
        end = DateUtils.toDate(endTime);

        String beginStr_Z = DateUtils.format(begin, "yyyy-MM-dd");
        String endStr_Z = DateUtils.format(end, "yyyy-MM-dd");

        Page p = new Page();
        List<AgentReport> list = cpsReportService.agentsReport(account, null, beginStr_Z, endStr_Z, u.getUserType(), p);

        AgentReport returnOBj = new AgentReport();
        Map<String, Object> returnO = new HashMap<String, Object>();
        if(list.size() > 0){
            returnOBj = list.get(0);
            returnO.put("account", returnOBj.getAccount());
            returnO.put("parentAccount", returnOBj.getParentAccount());
            returnO.put("rootAccount", "");
            returnO.put("regTime", "");
            returnO.put("teamAmount", returnOBj.getTeamAmount());
            returnO.put("teamCount", returnOBj.getTeamCount());
            returnO.put("lowerCount", returnOBj.getLowerCount());
            returnO.put("regCount", returnOBj.getRegCount());
            returnO.put("betPerCount", returnOBj.getBetPerCount());
            returnO.put("firstChargeCount", returnOBj.getFirstChargeCount());
            returnO.put("betAmount", returnOBj.getBetAmount());
            returnO.put("winsAmount", returnOBj.getWinningAmount());
            returnO.put("activityAmount", returnOBj.getActivityAmount().add(returnOBj.getDailyAmount()).add(returnOBj.getDividendAmount()));
            returnO.put("teamRebateAmount", returnOBj.getRebateAmountL());
            returnO.put("rechargeAmount", returnOBj.getRechargeAmount());
            returnO.put("withdrawAmount", returnOBj.getWithdrawAmount());
            returnO.put("profit", returnOBj.getProfit());
            returnO.put("agentRebateAmount", returnOBj.getRebateAmount());
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnO));
    }


    //我的账户-序29

    /***获取下级报表
     *
     * String 	account			账户
     * Integer 	dateFlag		时间区域标记  0：今天，1：昨天，2：本月，3：上月
     * Integer 	start		    从第几条开始取
     * Integer 	limit		    取多少条
     * @throws ParseException
     *
     * **/
    @ResponseBody
    @RequestMapping(value = {"/getUnderLevelReport"}, method = {RequestMethod.GET})
    public JsonNode getUnderLevelReport(String account, Integer dateFlag, Integer start, Integer limit) throws ParseException {
        HashMap<String, String> returnC = new HashMap<String, String>();

        User user_s = getLogin();
        String account_s = user_s.getAccount();

        List<User> userTeamList = userDao.userTeamList(account_s);
        boolean u_flag = false;
        for (User user : userTeamList) {
            if (account.equals(user.getAccount())) {
                u_flag = true;
                break;
            }
        }
        if (!u_flag) {
            returnC.put("message", "该用户不在您所在的团队内！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (dateFlag == null || (dateFlag != 0 && dateFlag != 1 && dateFlag != 2 && dateFlag != 3)) {
            returnC.put("message", "[时间区域]标记不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (start == null || limit == null || limit == 0) {
            returnC.put("message", "[数据条数]标记不合法！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        Date begin = null;
        Date end = null;
        String startTime = "";
        String endTime = "";

        if (dateFlag == 0) {//今天
            String[] dayStrArr = getday(0);
            startTime = dayStrArr[0];
            endTime = dayStrArr[1];
        } else if (dateFlag == 1) {//昨天
            String[] dayStrArr = getday(1);
            startTime = dayStrArr[0];
            endTime = dayStrArr[1];
        } else if (dateFlag == 2) {//本月

            Calendar cal = Calendar.getInstance();
            int dateStr = cal.get(Calendar.DATE);
            String[] dayStrArr = getday(dateStr - 1);
            startTime = dayStrArr[0];
            String[] dayStrArrZ = getday(0);
            endTime = dayStrArrZ[1];

        } else if (dateFlag == 3) {//上月
            String[] dayStrArr = getTerMonth(1);
            startTime = dayStrArr[0];
            endTime = dayStrArr[1];
        }

        begin = DateUtils.toDate(startTime);
        end = DateUtils.toDate(endTime);

        Map<String, Object> underAgentsReport = cpsReportService.underAgentsReport(account, begin, end, start, limit);
        Integer listCount = Integer.valueOf(String.valueOf(underAgentsReport.get("listCount")));
        List<AgentReport> agentTeamReportList = (List<AgentReport>)underAgentsReport.get("agentReportList");

        List<Map<String, Object>> agentTeamReportMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mo = null;
        for(AgentReport agentReport: agentTeamReportList){
            mo = new HashMap<String, Object>();
            mo.put("account", agentReport.getAccount());//账户
            Integer userType = agentReport.getUserType();
            mo.put("userType", userType);//类型   0：会员，1：代理

            if (userType == 1) {
                mo.put("userTypeName", "代理");
            } else if (userType == 0) {
                mo.put("userTypeName", "会员");
            } else {
                mo.put("userTypeName", "其他");
            }

            mo.put("teamCount", agentReport.getTeamCount());//下级团队人数
            mo.put("childCount", agentReport.getLowerCount());//直属下级人数
            mo.put("count", agentReport.getProfit());//盈利
            agentTeamReportMapList.add(mo);
        }

        HashMap<String, Object> dataO = new HashMap<String, Object>();
        dataO.put("underUserListCount", listCount);
        dataO.put("list", agentTeamReportMapList);

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(dataO));
    }


    //我的账户-序30
    /***判断是否允许当前用户进行提款**
     *
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/getWithdrawFlag"}, method = {RequestMethod.GET})
    public JsonNode getWithdrawFlag() {
        HashMap<String, String> returnC = new HashMap<String, String>();
        try {
            User user_s = getLogin();
            String account = user_s.getAccount();

            User user = userDao.findByAccount(account);
            BigDecimal amount = user.getAmount();
            int flagAmountInt = amount.compareTo(BigDecimal.ZERO);
            if (flagAmountInt <= 0) {
                returnC.put("message", "亲 ，您的账户已无余额！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            int getUserDepositNSCount = depositService.getUserDepositNSCount(account);
            if(getUserDepositNSCount > 0){
                returnC.put("message", "您有未完结的提现申请单，暂时不能提交新的请求！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            HashMap<String, Object> returnO = new HashMap<String, Object>();
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnO));

        } catch (Exception e) {
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }

    //我的账户-序
    /***获取提款信息**
     *
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/getWithdrawInformation"}, method = {RequestMethod.GET})
    public JsonNode getWithdrawInformation() {
        HashMap<String, String> returnC = new HashMap<String, String>();
        try {

            User user_s = getLogin();
            String account = user_s.getAccount();

            User user = userDao.findByAccount(account);

            List<HashMap<String, Object>> bankUserListCheck = new ArrayList<HashMap<String, Object>>();
            List<BankUser> bankUserList = this.bankUserService.listByAccount(user.getAccount(), Integer.valueOf(0));
            //Map<Integer, BankName> bankNameMap = new HashMap<Integer, BankName>();
            HashMap<String, Object> hmo = null;
            for (BankUser bankUser : bankUserList) {
                BankName bankName = this.bankNameService.find(bankUser.getBankNameId());
                if (bankName.getDepositStatus().intValue() == 0) {
                    //bankUser.setCard("*** **** **** **** " + bankUser.getCard().substring(bankUser.getCard().length() - 4));
                    //bankUser.setNiceName("**" + bankUser.getNiceName().substring(bankUser.getNiceName().length() - 1));
                    //bankNameMap.put(bankUser.getBankNameId(), bankName);
                    hmo = new HashMap<String, Object>();
                    hmo.put("id", Integer.valueOf(bankUser.getId()));
                    if(bankUser.getCard()!=null && bankUser.getCard().length()>4) {
                        hmo.put("card", "*** **** **** **** " + bankUser.getCard().substring(bankUser.getCard().length() - 4));
                    }else{
                        hmo.put("card", bankUser.getCard());
                    }
                    if(bankUser.getNiceName()!=null && bankUser.getNiceName().length()>1) {
                        hmo.put("niceName", "**" + bankUser.getNiceName().substring(bankUser.getNiceName().length() - 1));
                    }else{
                        hmo.put("niceName", bankUser.getNiceName());
                    }
                    hmo.put("address", bankUser.getAddress());
                    hmo.put("code", bankName.getCode());
                    hmo.put("bankTitle", bankName.getTitle());
                    bankUserListCheck.add(hmo);
                }
            }

            HashMap<String, Object> returnO = new HashMap<String, Object>();

            HashMap<String, BigDecimal> userDepositAmount = depositService.getUserDepositAmount(account);

            returnO.put("amount", userDepositAmount.get("actualBalanceZ")); //可用余额
            returnO.put("bankUserList", bankUserListCheck);//用户所绑定的银行卡列表
            FinanceSetting fs = (FinanceSetting) financeSettingService.list().get(0);
            returnO.put("moneyDepositMin", fs.getDepositMinMoney());//最低提现额度
            returnO.put("moneyDepositMax", fs.getDepositMaxMoney());//最高提现额度
            returnO.put("countMax", (fs.getDepositMaxCount() - user.getDepositCount()));//今天还可以提现次数

            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnO));
        } catch (IllegalArgumentException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }


    //我的账户-序

    /***提交提款申请*
     *
     * @param amount			申请金额
     * @param bankUserId		用户银行卡ID
     * @param securityCode	    账户安全码
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/setWithdraw"}, method = {RequestMethod.POST})
    public JsonNode setWithdraw(BigDecimal amount, Integer bankUserId, String securityCode) {
        HashMap<String, String> returnC = new HashMap<String, String>();
        try {

            amount = amount.setScale(2, 1);

            User user_s = getLogin();
            String account = user_s.getAccount();

            User user = userDao.findByAccount(account);

            String u_securityCode = user.getSafePassword();


            if (u_securityCode == null || u_securityCode.trim().equals("")) {
                returnC.put("message", "安全码为空，请现设置安全码！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            if (!securityCode.trim().equals(u_securityCode)) {
                returnC.put("message", "账户安全码输入错误！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            FinanceSetting fs = (FinanceSetting) this.financeSettingService.list().get(0);
            if (amount.compareTo(fs.getDepositMinMoney()) < 0) {
                returnC.put("message", "系统限制最低提现额度为：" + fs.getDepositMinMoney());
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }
            if (amount.compareTo(fs.getDepositMaxMoney()) > 0) {
                returnC.put("message", "系统限制最高提现额度为：" + fs.getDepositMaxMoney());
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }
            if (user.getDepositCount().intValue() >= fs.getDepositMaxCount().intValue()) {
                returnC.put("message", "系统限制最多提现次数为：" + fs.getDepositMaxCount());
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            depositService.saveSplitAmount(user.getAccount(), amount, fs.getDepositSplitMaxMoney(), bankUserId);

            returnC.put("message", "提交成功！");

            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
        } catch (BaseCheckException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        } catch (IllegalArgumentException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }

    /***获取账户公告与私信列表**
     *
     * Integer type	类型 1：公告 2：私信
     * Integer start	从第几条开始取
     * Integer limit	共取出多少条
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/getUserNoticeList"}, method = {RequestMethod.GET})
    public JsonNode getUserNoticeList(Integer type, Integer start, Integer limit) {
        HashMap<String, String> returnC = new HashMap<String, String>();

        if (StrUtils.hasEmpty(new Object[]{type})) {
            returnC.put("message", "参数不合法");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        try {
            List<Map<String, Object>> obList = new ArrayList<Map<String, Object>>();
            Integer allCount = 0;
            if (type == 1) {
                obList = noticeDao.listNotice_Z(start, limit);
                allCount = noticeDao.listNoticeCount_Z();
                if (obList.size() > 0) {
                    User user_s = getLogin();
                    Object [] objs=obList.stream().map(p -> p.get("id")).toArray();

                    Integer []  ids= (Integer[]) ConvertUtils.convert(objs,Integer[].class);
                    List<Integer> result = noticeReadService.queryReadIds(user_s.getAccount(), 1, ids);
                    if (result != null && result.size()>0) {
                        obList.forEach(ob -> {
                            if (result.contains(ob.get("id"))) {
                                ob.put("isRead", true);
                            } else {
                                ob.put("isRead", false);
                            }
                        });
                    } else {
                        obList.forEach(ob -> {
                            ob.put("isRead", false);
                        });
                    }
                }
            } else if (type == 2) {
                User user_s = getLogin();
                String account = user_s.getAccount();
                obList = userNoticeDao.listUserNotice(account, start, limit);
                allCount = userNoticeDao.listUserNoticeCount(account);
            } else {
                returnC.put("message", "参数不合法");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }
            HashMap<String, Object> dataO = new HashMap<String, Object>();
            dataO.put("list", obList);
            dataO.put("allCount", allCount);
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(dataO));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }

    /***获取公告内容**
     *
     * Integer id   公告id
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/getNoticeInfor"}, method = {RequestMethod.GET})
    public JsonNode getNoticeInfor(Integer id) {
        HashMap<String, String> returnC = new HashMap<String, String>();
        try {
            Map<String, Object> noticeOb = noticeDao.getNotice_Z(id);
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(noticeOb));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }



    /***获取用户是否有未读消息**
     *
     * Integer id   公告id
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/getNoReadNoticeStatus"}, method = {RequestMethod.GET})
    public JsonNode getNoReadNoticeStatus(@RequestParam(value = "type" ,required = false) Integer type) {
        List<Map<String,Object>> obList = noticeDao.listNotice_Z(null, null);
        boolean flag=false;
        int count =0;
        if (obList.size() > 0) {
            User user_s = getLogin();
            Object [] objs=obList.stream().map(p -> p.get("id")).toArray();
            Integer []  ids= (Integer[]) ConvertUtils.convert(objs,Integer[].class);
            List<Integer> result = noticeReadService.queryReadIds(user_s.getAccount(), 1, ids);
            if (result != null && result.size()>0) {
                for (Map<String, Object> ob : obList) {
                    if (!result.contains(ob.get("id"))) {
                        count++;
                        flag = true;
                    }
                }
            } else {
                flag = true;
                count=ids.length;
            }
        }
        JSONObject jb = new JSONObject();
        jb.put("flag", flag);
        jb.put("count", count);
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(jb));
    }

    /**
     * 添加已读取的消息状态
     * @param noticeId
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/addReadNotice"}, method = {RequestMethod.POST,RequestMethod.GET})
    public JsonNode addReadNotice(
            @RequestParam(value = "noticeId") Integer noticeId,
            @RequestParam(value = "type",required = false, defaultValue = "1") Integer type
    ) {
        User user = getLogin();
        NoticeRead nr = new NoticeRead();
        nr.setNoticeId(noticeId);
        nr.setCreateTime(new Date());
        nr.setAccount(user.getAccount());
        nr.setType(type);
        nr.setStatus(0);
        if (null != noticeReadService.getByNoticeId(noticeId, user.getAccount())) {
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(false));
        }
        boolean flag = noticeReadService.save(nr);
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(flag));
    }







}

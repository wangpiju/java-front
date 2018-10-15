package com.hs3.home.controller.gh.lottery;

import com.hs3.entity.lotts.*;
import com.hs3.entity.users.User;
import com.hs3.exceptions.UnLogException;
import com.hs3.home.bean.LotteryPojo;
import com.hs3.home.bean.LotterySaleTimePojo;
import com.hs3.home.constant.BaseConstant;
import com.hs3.home.constant.RedisConstant;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.controller.lotts.LotteryAction;
import com.hs3.home.entity.PlayerBonusVO;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.home.utils.FilterUtil;
import com.hs3.home.utils.RedisUtils;
import com.hs3.lotts.*;
import com.hs3.models.lotts.Order;
import com.hs3.models.lotts.OrderContent;
import com.hs3.models.lotts.PlayerBonus;
import com.hs3.models.lotts.TraceOrder;
import com.hs3.service.lotts.*;
import com.hs3.service.report.SeasonReportService;
import com.hs3.service.user.UserService;
import com.hs3.utils.*;
import com.hs3.web.auth.Auth;
import com.hs3.web.auth.ThreadLog;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author jason.wang
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/lottery"})
public class LotteryResource extends HomeAction {
    private static final Logger logger = Logger.getLogger(LotteryAction.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    //玩法樹隱藏大小單雙
    private static final String bannedPlayer = "k3_star3_big_odd";
    //投注項顯示賠率
    private static final String[] showDetailBet = {"k3_star3_big_odd", "k3_star3_and"};
    private static final List<Integer> seasonLen =
            Arrays.asList(30, 50, 100);
    @Autowired
    LotteryService lotteryService;
    @Autowired
    UserService userService;
    @Autowired
    LotterySaleTimeService lotterySaleTimeService;
    @Autowired
    BetService betService;
    @Autowired
    TraceService traceService;
    @Autowired
    BonusGroupService bonusGroupService;
    @Autowired
    PlayerService playerService;
    @Autowired
    SeasonReportService seasonReportService;

    public static void main(String[] args) {
        BigDecimal a = BigDecimal.valueOf(1.959465464654664);
        System.out.println(a.setScale(3, 1));
    }

    // 熱門彩票列表
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/getHotLotteryList"}, method = {RequestMethod.GET})
    public JsonNode getHotLotteryList() {
        // 取得正常狀態的彩票組列表
        List<Lottery> lotterys = this.lotteryService.listAndOrder(null);

        if (isMobile()) { // mobile
            lotterys = lotterys.stream().filter(x -> x.getStatus() == 0 && x.getMobileStatus() == 0)
                    .collect(Collectors.toList());
        } else {// pc
            lotterys = lotterys.stream().filter(x -> x.getStatus() == 0).collect(Collectors.toList());
        }
        // hot
        List<LotteryPojo> hotLotteryPojos = FilterUtil.filterHotLotter(lotterys);
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(hotLotteryPojos));
    }


    // 取得全部彩票列表
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/getLotteryList"}, method = {RequestMethod.GET})
    public JsonNode getLotteryList(String type) {
        //預設全部
        if (null == type || type.equals(""))
            type = "all";

        if (null != RedisUtils.get(RedisConstant.ALL_LOTTERY_LIST_KEY + "_" + type)) {
            try {
                return mapper.readTree(RedisUtils.get(RedisConstant.ALL_LOTTERY_LIST_KEY + "_" + type));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // 取得正常狀態的彩票組列表
        List<Lottery> lotterys = this.lotteryService.listByStatus(0);
        List<LotteryPojo> allLotteryPojos = new ArrayList<>();
        Map<String, List<LotteryPojo>> orderPojoMap = new HashMap<>();
        List<LotteryPojo> hotLotteryPojos = new ArrayList<>();

        if (isMobile()) { // mobile
            lotterys = lotterys.stream().filter(x -> x.getMobileStatus() == 0)
                    .collect(Collectors.toList());
        } else {// pc
            lotterys = new ArrayList<>(lotterys);
        }

        // all & 分類
        for (Lottery x : lotterys) {
            LotteryPojo lotteryPojo = new LotteryPojo();
            lotteryPojo.setId(x.getId());
            lotteryPojo.setName(x.getTitle());
            lotteryPojo.setImage(x.getId());
            lotteryPojo.setGroupName(x.getGroupName());
            lotteryPojo.setGroupId(x.getGroupId());
            allLotteryPojos.add(lotteryPojo);

            String groupName = x.getGroupId();
            if ("11x5".equals(groupName)) {
                groupName = "x" + groupName;
                lotteryPojo.setGroupId(groupName);
            }
            List<LotteryPojo> resultList = new ArrayList<>();

            if (!orderPojoMap.containsKey(groupName)) {
                resultList.add(lotteryPojo);
                orderPojoMap.put(groupName, resultList);
            } else {
                List<LotteryPojo> list = orderPojoMap.get(groupName);
                resultList.add(lotteryPojo);
                resultList.addAll(list);
                orderPojoMap.remove(groupName);
                orderPojoMap.put(groupName, resultList);
            }
            // 熱門彩種
            if (x.getIsHot() == 1) {
                hotLotteryPojos.add(lotteryPojo);
            }
        }

        orderPojoMap.put("all", allLotteryPojos);
        orderPojoMap.put("hot", hotLotteryPojos);

        RedisUtils.set(RedisConstant.ALL_LOTTERY_LIST_KEY + "_" + type, mapper.valueToTree(BaseBeanUtils.getSuccessReturn(orderPojoMap.get(type))).toString(), 500);
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(orderPojoMap.get(type)));
    }

    //取最新開獎號碼(給外站使用)
    @Auth
    @ResponseBody
    @RequestMapping({"/getNewOpen"})
    public JsonNode getNewOpen(String lotteryId) {
        if (null == lotteryId || lotteryId.equals("dfk3"))
            lotteryId = "dfk3";
        Lottery lott = this.lotteryService.find(lotteryId);

        if (null == lott) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("参数错误"));
        }

        List<LotterySeason> seasonList = this.lotteryService.getLast(lotteryId, 1);

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(seasonList));
    }

    // 過去開獎紀錄
    @ResponseBody
    @RequestMapping({"/getPastOpen"})
    public JsonNode getPastOpen(String lotteryId, String count) {
        //全部彩種
        if (null == lotteryId || "-1".equals(lotteryId)) {
            List<Lottery> lotteryList = this.lotteryService.listByStatus(0);
            List<LotterySeason> allSeasonList = new ArrayList<>();
            lotteryList.forEach(x -> {
                List<LotterySeason> seasonList = this.lotteryService.getLast(x.getId(), 1);
                LotterySaleTime saleTime = this.lotterySaleTimeService.getCurrentByLotteryId(x.getId());
                if (null != saleTime) {
                    int restSeconds = (int) DateUtils.getSecondBetween(new Date(), saleTime.getEndTime()) + 1;
                    if (null != seasonList && seasonList.size() > 0) {
                        LotterySeason lotterySeason = seasonList.get(0);
                        lotterySeason.setRestSeconds(restSeconds);
                        lotterySeason.setName(x.getTitle());
                        lotterySeason.setGroupName(x.getGroupId());
                        allSeasonList.add(lotterySeason);
                    }
                }
            });
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(allSeasonList));
        }

        Lottery lott = this.lotteryService.find(lotteryId);
        if (null == lott) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("参数错误"));
        }

        if (null == count)
            count = "10";

        boolean isToday = false;

        if ("-1".equals(count)) {
            count = "2000";
            isToday = true;
        }

        List<LotterySeason> seasonList = this.lotteryService.getLast(lotteryId, Integer.valueOf(count));
        //如果只請求一筆時要加上倒數計時秒數
        if (count.equals("1") && seasonList.size() > 0) {
            LotterySeason season = seasonList.get(0);
            LotterySaleTime saleTime = this.lotterySaleTimeService.getCurrentByLotteryId(season.getLotteryId());
            if (null != saleTime) {
                int restSeconds = (int) DateUtils.getSecondBetween(new Date(), saleTime.getEndTime()) + 1;
                season.setRestSeconds(restSeconds);
                season.setName(lott.getTitle());
                season.setGroupName(lott.getGroupId());
                seasonList.clear();
                seasonList.add(season);
            }
        }

        //只留下今天的資料
        if (isToday) {
            List<LotterySeason> finalSeasonList = new ArrayList<>();

            for (LotterySeason lotterySeason : seasonList) {
                Calendar cal = Calendar.getInstance();
                lotterySeason.getOpenTime();
                cal.setTime(lotterySeason.getOpenTime());
                if (DateUtils.isSameDay(cal, Calendar.getInstance())) {
                    finalSeasonList.add(lotterySeason);
                }
            }

            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(finalSeasonList));
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(seasonList));
    }

    // 取得當前彩種銷售時間
    @ResponseBody
    @RequestMapping({"/getCurrentSaleTime"})
    public JsonNode getCurrentSaleTime(String lotteryId) {
        Lottery lott = this.lotteryService.find(lotteryId);
        if (null == lott) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("参数错误"));
        }
        LotterySaleTime saleTime;
        LotterySaleTime lastSaleTime = new LotterySaleTime();
        LotterySaleTimePojo lotterySaleTimePojo = new LotterySaleTimePojo();
        int restSeconds = 0;

        try {
            saleTime = this.lotterySaleTimeService.getCurrentByLotteryId(lotteryId);
            lastSaleTime = this.lotterySaleTimeService.getPreviousByLotteryId(lotteryId, new Date());
            restSeconds = (int) DateUtils.getSecondBetween(new Date(), saleTime.getEndTime()) + 1;
            BeanUtils.copyProperties(lotterySaleTimePojo, saleTime);
        } catch (Exception e) {
            logger.error(lotteryId + " getCurrentSaleTime error! ", e);
        }

        lotterySaleTimePojo.setRestSeconds(restSeconds);
        lotterySaleTimePojo.setLastSeasonId(lastSaleTime.getSeasonId());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(lotterySaleTimePojo));
    }

    // 撤單
    @ResponseBody
    @RequestMapping(value = {"/cancel"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public JsonNode cancel(String lotteryId, String ids) {
        List<String> betListId = ListUtils.toList(ids);

        for (String betid : betListId) {
            if (betid.equals(""))
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("提交数据有误！"));
        }

        if (null == lotteryId)
            lotteryId = "";
        logger.info("lotteryId: " + lotteryId + ", 撤單: " + ids);
        return mapper.valueToTree(this.betService.saveUserCancelOrderNewFormat(betListId, getLogin().getAccount()));
    }

    //昨日營利榜
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/getLastDayWinList"}, method = {RequestMethod.GET})
    public JsonNode getLastDayWinList(Integer count) {
        try {
            String content = RedisUtils.get(RedisConstant.YESTERDAY_WIN_LIST);
            if (null != content) {
                JsonNode jsonNode = mapper.readTree(content);
                Long timeStamp = jsonNode.get(0).get("date").asLong();
                Calendar now = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(timeStamp));
                if (!DateUtils.isSameDay(now, cal)) {
                    RedisUtils.del(RedisConstant.YESTERDAY_WIN_LIST);
                } else
                    return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(mapper.readTree(content)));
            }
        } catch (Exception e) {
            logger.error("getLastDayWinList get error, count: " + count, e);
        }
        ArrayNode arrayNode;
        ArrayList<ObjectNode> list = new ArrayList<>();


        //產生隨機獎金
        for (int i = 0; i < 10; i++) {
            ObjectNode objectNode = mapper.createObjectNode();
            Random random = new Random();
            //獎金
            Integer bonus = NumUtils.getRandom(random, BaseConstant.YESTERDAY_MIN, BaseConstant.YESTERDAY_MAX);
            //帳號
            Integer accountLen = NumUtils.getRandom(random, BaseConstant.ACCOUNT_MIN, BaseConstant.ACCOUNT_MAX);
            String account = StrUtils.getRandomString(accountLen);
            Integer img = NumUtils.getRandom(random, 0, 9);
            objectNode.put("bonus", bonus);
            objectNode.put("account", account);
            objectNode.put("img", img);
            objectNode.put("date", new Date().getTime());
            list.add(objectNode);

        }

        list.sort((a, b) -> a.get("bonus").getIntValue() > b.get("bonus").getIntValue() ? -1 : 1);

        arrayNode = mapper.valueToTree(list);
        try {
            System.out.println(arrayNode.toString());

            RedisUtils.set(RedisConstant.YESTERDAY_WIN_LIST, arrayNode.toString(), 60 * 60 * 24);
        } catch (Exception e) {
            logger.error("getLastDayWinList set error", e);
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(arrayNode));

    }


    // 投注
    @ResponseBody
    @RequestMapping(value = {"/bet"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public JsonNode bet(Order order) {
        Integer betNum;
        Integer allNum = 0;
        String curSaleSeason = "";
        String traceId;
        Date curDate = new Date();
        List<Bet> betList = new ArrayList<>();

        BigDecimal allPrice = new BigDecimal("0");
        BigDecimal betAllPrice;
        BigDecimal traceAllPrice = new BigDecimal("0");

        ThreadLog.addKey("action vaild start:" + System.currentTimeMillis());
        Lottery lott = this.lotteryService.find(order.getLotteryId());

        if (lott == null)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("彩种错误：" + order.getLotteryId()));
        if (!lott.getStatus().equals(0))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("暂时不接受  [" + lott.getTitle() + "] 的投注.感谢您的支持."));

        if ((order.getIsTrace() == null) || ((!order.getIsTrace().equals(1))
                && (!order.getIsTrace().equals(0))))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("追号状态非法：" + order.getIsTrace()));
        if ((order.getTraceWinStop() == null) || ((!order.getTraceWinStop().equals(1))
                && (!order.getTraceWinStop().equals(0))))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("追中即停非法：" + order.getTraceWinStop()));
        if ((order.getIsTrace() == 0) && (order.getTraceWinStop() != 0))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("不追号不能追中即停：" + order.getTraceWinStop()));
        if (order.getTraceOrders() == null)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("期号不正确."));

        if ((order.getIsTrace().equals(1))
                && (order.getTraceOrders().size() > lott.getMaxPlan()))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("最大追号期数：" + lott.getMaxPlan()));
        // 取彩種基本資訊
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        User user = this.userService.findByAccount(getLogin().getAccount());
        BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());

        if (user.getRebateRatio().compareTo(bDecimal.getPlayerMaxRatio()) > 0)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("直属代理用户，不允许投注。"));
        if ((!order.getBounsType().equals(0)) && (!order.getBounsType().equals(1)))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("数据不合法：" + order.getBounsType()));
        if (!user.getBetStatus().equals(0))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("您已被禁止投注"));

        Bet bet;
        for (OrderContent orderContent : order.getOrder()) {
            if (order.getIsTrace().equals(1)) {
                traceId = IdBuilder.CreateId(order.getLotteryId(), "Z");
            } else {
                traceId = "";
            }
            bet = new Bet();
            PlayerBase play = lottBase.getPlayer(orderContent.getPlayId());

            if (play == null)
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("投注失败,玩法错误,请刷新页面"));
            if (!this.playerService.getStatus(orderContent.getPlayId(), order.getLotteryId())
                    .equals(0))
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("该玩法停止销售"));

            betNum = play.getCount(orderContent.getContent());
            allNum = allNum + betNum;

            if (betNum.equals(0))
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("注数不能为空！"));
            if (!betNum.equals(orderContent.getBetCount()))
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("注数" + orderContent.getBetCount() + "不正确"));
//			if ((orderContent.getUnit().compareTo(new BigDecimal("2.00")) != 0)
//					&& (orderContent.getUnit().compareTo(new BigDecimal("0.20")) != 0)
//					&& (orderContent.getUnit().compareTo(new BigDecimal("0.02")) != 0)
//					&& (orderContent.getUnit().compareTo(new BigDecimal("0.002")) != 0))
//				return mapper.valueToTree(BaseBeanUtils.getFailReturn("单位模式数据不合法：" + orderContent.getUnit()));

            if (orderContent.getPrice() < 1)
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("倍数" + orderContent.getPrice() + "不能小于1."));

            BonusGroupDetails bGroupDetails = ThreadLocalCache.getGroupDetails(orderContent.getPlayId(),
                    order.getLotteryId(), user.getBonusGroupId());

            bet.setAccount(user.getAccount());
            bet.setBetCount(betNum);
            bet.setBonusType(order.getBounsType());
            bet.setContent(orderContent.getContent());
            bet.setCreateTime(curDate);
            bet.setLastTime(curDate);
            bet.setId(IdBuilder.CreateId(order.getLotteryId(), "D"));
            bet.setLotteryId(order.getLotteryId());
            bet.setLotteryName(lott.getTitle());
            bet.setPlayerId(orderContent.getPlayId());
            bet.setPrice(orderContent.getPrice());
            bet.setSeasonId(order.getTraceOrders().get(0).getSeasonId());
            bet.setStatus(0);

            if (order.getIsTrace().equals(1))
                orderContent.setPrice(1);
            bet.setTheoreticalBonus(play.getBonus().multiply(orderContent.getUnit())
                    .multiply(new BigDecimal(orderContent.getPrice())).setScale(4, 1));

            BigDecimal curPirce = new BigDecimal(betNum).multiply(orderContent.getUnit())
                    .multiply(new BigDecimal(orderContent.getPrice()));
            allPrice = allPrice.add(curPirce);
            bet.setAmount(curPirce.setScale(4, 1));
            bet.setPlayName(play.getFullTitle());
            bet.setUnit(orderContent.getUnit().setScale(4, 1));
            bet.setIsTrace(order.getIsTrace());
            bet.setTest(user.getTest());
            bet.setTraceId(traceId);
            bet.setGroupName(lott.getGroupName());

            //強制設定bonusType為0
            bet.setBonusType(0);
            bet.setBonusRate(bGroupDetails.getBonusRatio().add(user.getRebateRatio()
                    .subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio()))));

            bet.setHashCode(LotteryUtils.hashCode(bet));
            bet.setTraceWinStop(order.getTraceWinStop());
            betList.add(bet);
        }

        Date curOpenTime = null;
        // 處理訂單
        if (order.getIsTrace() == 0) {
            if ((order.getTraceOrders().size() != 1)
                    || (order.getTraceOrders().get(0).getSeasonId().equals("")))
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("期号数量不正确."));
            if (order.getCount() < 1)
                return mapper.valueToTree(BaseBeanUtils
                        .getFailReturn("总注数" + order.getTraceOrders().get(0).getPrice() + "不能小于1."));

            LotterySaleTime saleTime = this.lotterySaleTimeService
                    .find(order.getTraceOrders().get(0).getSeasonId(), order.getLotteryId());
            curOpenTime = saleTime.getOpenTime();

            if (saleTime.getEndTime().getTime() <= new Date().getTime())
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(
                        "期号 " + order.getTraceOrders().get(0).getSeasonId() + " 未开盘或已封盘."));

            if ((order.getTraceOrders().get(0).getSeasonId().equals(""))
                    || (order.getTraceOrders().get(0).getSeasonId() == null))
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("期号 不能为空."));
        } else {
            betAllPrice = allPrice;
            for (TraceOrder to : order.getTraceOrders()) {
                traceAllPrice = traceAllPrice.add(betAllPrice.multiply(new BigDecimal(to.getPrice())));
                if (to.getPrice() < 1)
                    return mapper.valueToTree(BaseBeanUtils.getFailReturn("追号倍数" + to.getPrice() + "不能小于1."));
                if ((to.getSeasonId() == null) || (to.getSeasonId().equals("")))
                    return mapper.valueToTree(BaseBeanUtils.getFailReturn("追号期号" + to.getPrice() + "不能为空！"));

                LotterySaleTime saleTime = this.lotterySaleTimeService.find(to.getSeasonId(), order.getLotteryId());

                if (saleTime.getEndTime().getTime() > new Date().getTime()) {
                    if (curSaleSeason.equals("")) {
                        LotterySaleTime lst = this.lotterySaleTimeService.getCurrentByLotteryId(order.getLotteryId());
                        curSaleSeason = lst.getSeasonId();
                        curOpenTime = lst.getOpenTime();
                    }
                } else {
                    return mapper.valueToTree(BaseBeanUtils.getFailReturn("追号期号 " + to.getSeasonId() + " 未开盘或已封盘."));
                }
            }
        }

        if (!allNum.equals(order.getCount()))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("投注总注数不正确！"));

        ThreadLog.addKey("service start:" + System.currentTimeMillis());

        if (order.getIsTrace() == 0) {
            try {
                if (null == curOpenTime)
                    curOpenTime = new Date();
                this.betService.saveBetOrder(betList, allPrice, curOpenTime, true);
            } catch (UnLogException e) {
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(e.getMessage()));
            } catch (Exception e) {
                logger.error("--> error", e);
            }

            ThreadLog.addKey("service end:" + System.currentTimeMillis());
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(betList));
        }
        Object jsoner = null;

        try {
            jsoner = this.traceService.saveBetTraceOrder(betList, order.getTraceOrders(), curSaleSeason,
                    traceAllPrice, curOpenTime);
        } catch (UnLogException e) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        ThreadLog.addKey("service end:" + System.currentTimeMillis());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(jsoner));
    }

    // 列出獎期
    @Auth
    @ResponseBody
    @RequestMapping({"/listTraceSeasonId"})
    public Object listTraceSeasonId(String lotteryId, Integer count) {
        return mapper.valueToTree(
                BaseBeanUtils.getSuccessReturn(this.lotterySaleTimeService.listTraceSeasonId(lotteryId, count)));
    }

    // 玩法樹
    @ResponseBody
    @RequestMapping({"/getPlayTree"})
    public JsonNode getPalyTree(String lotteryId) throws IOException {
        User user = this.userService.findByAccount(getLogin().getAccount());
        //get from redis
        String redisResult = RedisUtils.get(RedisConstant.PLAY_TREE_REDIS_KEY + "_" + lotteryId + "_" + user.getAccount());
        try {
            if (null != redisResult && !redisResult.equals(""))
                return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(mapper.readTree(redisResult)));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        ObjectNode bonusMap = mapper.createObjectNode();
        ObjectNode bonusMap2 = mapper.createObjectNode();
        Lottery lott = this.lotteryService.find(lotteryId);
        Map<String, Object> result = new HashMap<>();
        List<PlayerBonusVO> playerBonusVOs = new ArrayList<>();
        result.put("lotteryId", lotteryId);

        if (lott == null)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("彩种错误：" + lotteryId));
        // 取彩種基本資訊
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        result.put("title", lott.getGroupName());

        if (null == lottBase)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("彩种資訊錯誤：" + lotteryId));

        List<PlayerBonus> playerBonus = this.playerService.listFullByLotteryIdAndGroupId(lotteryId, getLogin().getBonusGroupId());

        for (PlayerBonus pb : playerBonus) {
            PlayerBonusVO vo = new PlayerBonusVO();
            try {
                BeanUtils.copyProperties(vo, pb);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }
            playerBonusVOs.add(vo);
        }

        removeUnavailablePlayer(playerBonus, lottBase);

        for (PlayerBonus s : playerBonus) {
            PlayerBase pbBase = lottBase.getPlayer(s.getId());
            if (null != pbBase) {
                NumberView[] numberViews = pbBase.getNumView();
                //過濾banned player
                if (pbBase.getId().equals(bannedPlayer)) {
                    lottBase.removePlayer(pbBase.getId());
                } else {
                    if (null != numberViews)
                        for (NumberView numberView : numberViews) {
                            //如果title是空的給預設值
                            if (null == numberView.getTitle()) {
                                numberView.setTitle(pbBase.getTitle());
                            }

                            List<?> nums = numberView.getNums();
                            List<ObjectNode> newNums = new ArrayList<>();

                            for (Object num : nums) {
                                ObjectNode objectNode = mapper.createObjectNode();
                                objectNode.put("ball", num.toString());
                                objectNode.put("choose", false);
                                newNums.add(objectNode);
                            }
                            //若是二同號加上全選
                            if (s.getId().equals("k3_star2_same")) {
                                ObjectNode objectNode = mapper.createObjectNode();
                                objectNode.put("ball", "全选");
                                objectNode.put("choose", false);
                                newNums.add(objectNode);
                            }

                            numberView.setNums(newNums);
                        }
                }
                //前台顯示賠率
                PlayerBonus pbBonus = null;
                if (null != pbBase.getBonusStr()) {
                    //尋找對應的playerBonus
                    for (PlayerBonus aPlayerBonusList : playerBonus) {
                        if (aPlayerBonusList.getId().equals(pbBase.getId())) {
                            pbBonus = aPlayerBonusList;
                            break;
                        }
                    }

                    if (null != pbBonus) {
                        BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());
                        BonusGroupDetails bGroupDetails = ThreadLocalCache.getGroupDetails(pbBonus.getId(),
                                lotteryId, user.getBonusGroupId());

                        BigDecimal bonusRatio = pbBonus.getBonusRatio().add(user.getRebateRatio().subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio())));

                        if (pbBase.getBonusStr().contains("-")) {//範圍賠率
                            String[] pbArray = pbBase.getBonusStr().split("-");
                            BigDecimal bonusTemp1 = new BigDecimal(pbArray[0].trim());
                            BigDecimal bonusTemp2 = new BigDecimal(pbArray[1].trim());
                            bonusTemp1 = bonusTemp1.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                            bonusTemp2 = bonusTemp2.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                            pbBase.setDisplayBonus(bonusTemp1.toString() + " - " + bonusTemp2.toString());

                            for (PlayerBonusVO playerBonusVO : playerBonusVOs) {
                                if (playerBonusVO.getId().equals(s.getId()) && !playerBonusVO.getId().equals(showDetailBet[1])) {
                                    playerBonusVO.setDisplayBonus(bonusTemp1.toString() + " - " + bonusTemp2.toString());
                                    break;
                                }
                            }
                        } else {//單一賠率
                            try {
                                BigDecimal bonusTemp = new BigDecimal(pbBase.getBonusStr());
                                BigDecimal finalDisplayBonus = bonusTemp.multiply(bonusRatio.divide(new BigDecimal(100), 4));

                                pbBase.setDisplayBonus(finalDisplayBonus.toString());

                                for (PlayerBonusVO playerBonusVO : playerBonusVOs) {
                                    if (playerBonusVO.getId().equals(s.getId()) && !playerBonusVO.getId().equals(showDetailBet[1])) {
                                        playerBonusVO.setDisplayBonus(finalDisplayBonus.toString());
                                        break;
                                    }
                                }

                                if (Arrays.asList(showDetailBet).contains(pbBase.getId())) {
                                    NumberView[] numberViewsTemp = pbBase.getNumView();
                                    for (NumberView numberView : numberViewsTemp) {
                                        List<?> numbers = numberView.getNums();
                                        for (Object obj : numbers) {
                                            String value = obj.toString();
                                            //大小
                                            if (pbBase.getId().equals(showDetailBet[0])) {
                                                finalDisplayBonus = finalDisplayBonus.setScale(3, RoundingMode.DOWN);
                                                bonusMap.put(value, finalDisplayBonus.toString());
                                            }
                                            //和值
                                            if (pbBase.getId().equals(showDetailBet[1])) {
                                                finalDisplayBonus = finalDisplayBonus.setScale(2, RoundingMode.DOWN);
                                                Integer[] betCountTemp = {1, 3, 6, 9, 15, 21, 24, 27, 27, 24, 21, 15, 9, 6, 3, 1};

                                                try {
                                                    JsonNode jsonNode = mapper.readTree(value);
                                                    int index = jsonNode.get("ball").asInt();
                                                    if (3 <= index && 18 >= index) {
                                                        int finalIndex = index - 3;
                                                        bonusMap2.put(String.valueOf(index), finalDisplayBonus.divide(new BigDecimal(betCountTemp[finalIndex]), 4).toString());
                                                    }
                                                } catch (Exception e) {
                                                    logger.error("--> error", e);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }

        //若後台該玩法群所有玩法關閉，則隱藏該玩法群
        List<PlayerQun> qunList = lottBase.getQun();
        for (int i = 0; i < qunList.size(); i++) {
            List<PlayerGroup> playerGroups = qunList.get(i).getGroups();
            boolean isAllClosed = false;

            if (null != playerGroups) {
                for (PlayerGroup playerGroup : playerGroups) {
                    if (null == playerGroup.getPlayers() || 0 == playerGroup.getPlayers().size())
                        isAllClosed = true;
                }
            }

            if (isAllClosed)
                lottBase.getQun().remove(i);
        }

        result.put("playGroups", lottBase.getQun());

        //快三才需要bonusArray
        if (lott.getId().contains("k3")) {
            for (PlayerBonusVO pbv : playerBonusVOs) {
                if (pbv.getId().equals(showDetailBet[0])) {
                    pbv.setBonusArray(bonusMap);
                }
                if (pbv.getId().equals(showDetailBet[1])) {
                    pbv.setBonusArray(bonusMap2);
                }
            }

            result.put("playBonus", playerBonusVOs);
        }

        //set to redis
        JsonNode jsonNode = mapper.valueToTree(result);
        RedisUtils.set(RedisConstant.PLAY_TREE_REDIS_KEY + "_" + lotteryId + "_" + user.getAccount(), jsonNode.toString(), 300);

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(result));
    }

    //取得該彩種各玩法顯是賠率
    @ResponseBody
    @RequestMapping({"/getLotteryPlayBetRate"})
    public JsonNode getLotteryPlayBetRate(String lotteryId, String playId) {
        if (null == lotteryId)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("請傳lotteryId"));
        if (!lotteryId.contains("k3"))
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("only k3"));
        Lottery lott = this.lotteryService.find(lotteryId);
        Map<String, Object> result = new HashMap<>();
        User user = getLogin();

        if (lott == null)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("彩种错误：" + lotteryId));

        List<PlayerBonus> pbs = this.playerService.listFullByLotteryIdAndGroupId(lotteryId, user.getBonusGroupId());
        // 取彩種基本資訊
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        for (PlayerBonus s : pbs) {
            PlayerBase pBase = lottBase.getPlayer(s.getId());
            if (null != pBase) {
                BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());
                BonusGroupDetails bGroupDetails = ThreadLocalCache.getGroupDetails(s.getId(),
                        lotteryId, user.getBonusGroupId());
                BigDecimal bonusRatio = s.getBonusRatio().add(user.getRebateRatio().subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio())));

                if (pBase.getBonusStr().contains("-")) {
                    //範圍賠率
                    String[] pbArray = pBase.getBonusStr().split("-");
                    BigDecimal bonusTemp1 = new BigDecimal(pbArray[0].trim());
                    BigDecimal bonusTemp2 = new BigDecimal(pbArray[1].trim());
                    bonusTemp1 = bonusTemp1.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                    bonusTemp2 = bonusTemp2.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                    pBase.setDisplayBonus(bonusTemp1.toString() + " - " + bonusTemp2.toString());
                } else {
                    //單一賠率
                    try {
                        BigDecimal bonusTemp = new BigDecimal(pBase.getBonusStr());
                        BigDecimal finalDisplayBonus = bonusTemp.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                        pBase.setDisplayBonus(finalDisplayBonus.toString());
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            //不包含大小單雙
            if (null != pBase && !pBase.getTitle().contains("大小"))
                result.put(pBase.getTitle(), pBase.getDisplayBonus());
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(result));
    }

    // 玩法數賠率
    @ResponseBody
    @RequestMapping({"/getPlayTreeBetRate"})
    public JsonNode getPlayTreeBetRate(String lotteryId, String playId) {
        if (null == lotteryId)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("請傳lotteryId"));
        if (null == playId)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("請傳playId"));

        Lottery lott = this.lotteryService.find(lotteryId);
        ObjectNode bonusMap = mapper.createObjectNode();
        ObjectNode bonusMap2 = mapper.createObjectNode();
        User user = getLogin();
        PlayerBonusVO vo = new PlayerBonusVO();

        if (lott == null)
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("彩种错误：" + lotteryId));
        // 取彩種基本資訊
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        List<PlayerBonus> pbs = this.playerService.listFullByLotteryIdAndGroupId(lotteryId, user.getBonusGroupId());
        lottBase.loadPlayerBonus(pbs);
        removeUnavailablePlayer(pbs, lottBase);
        PlayerBonus pbBonus = lottBase.getPlayerBonus(playId);
        PlayerBase pbBase = lottBase.getPlayer(playId);

        if (null != pbBase) {
            BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());
            BonusGroupDetails bGroupDetails = ThreadLocalCache.getGroupDetails(pbBonus.getId(),
                    lotteryId, user.getBonusGroupId());
            BigDecimal bonusRatio = pbBonus.getBonusRatio().add(user.getRebateRatio().subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio())));

            if (pbBase.getBonusStr().contains("-")) {//範圍賠率
                String[] pbArray = pbBase.getBonusStr().split("-");
                BigDecimal bonusTemp1 = new BigDecimal(pbArray[0].trim());
                BigDecimal bonusTemp2 = new BigDecimal(pbArray[1].trim());
                bonusTemp1 = bonusTemp1.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                bonusTemp2 = bonusTemp2.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                pbBase.setDisplayBonus(bonusTemp1.toString() + " - " + bonusTemp2.toString());
            } else {
                //單一賠率
                try {
                    BigDecimal bonusTemp = new BigDecimal(pbBase.getBonusStr());
                    BigDecimal finalDisplayBonus = bonusTemp.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                    pbBase.setDisplayBonus(finalDisplayBonus.toString());

                    if (Arrays.asList(showDetailBet).contains(pbBase.getId())) {
                        NumberView[] numberViewsTemp = pbBase.getNumView();
                        for (NumberView numberView : numberViewsTemp) {
                            List<?> numbers = numberView.getNums();
                            for (Object obj : numbers) {
                                String value = obj.toString();
                                //大小
                                if (pbBase.getId().equals(showDetailBet[0])) {
                                    finalDisplayBonus = finalDisplayBonus.setScale(3, RoundingMode.DOWN);
                                    bonusMap.put(value, finalDisplayBonus.toString());
                                }
                                //和值
                                if (pbBase.getId().equals(showDetailBet[1])) {
                                    finalDisplayBonus = finalDisplayBonus.setScale(2, RoundingMode.DOWN);
                                    Integer[] betCountTemp = {1, 3, 6, 9, 15, 21, 24, 27, 27, 24, 21, 15, 9, 6, 3, 1};

                                    try {
                                        String index = String.valueOf(value);

                                        if (index.equals("大") || index.equals("小") || index.equals("单") || index.equals("双")) {
                                            PlayerBonus pbBonusBigOdd = lottBase.getPlayerBonus("k3_star3_big_odd");

                                            BonusGroupDetails bGroupDetailsBigOdd = ThreadLocalCache.getGroupDetails(pbBonusBigOdd.getId(),
                                                    lotteryId, user.getBonusGroupId());
                                            BigDecimal bonusRatioBigOdd = pbBonusBigOdd.getBonusRatio().add(user.getRebateRatio().subtract(bDecimal.getRebateRatio().subtract(bGroupDetailsBigOdd.getRebateRatio())));
                                            BigDecimal bonusTempBigOdd = pbBonusBigOdd.getBonus();
                                            finalDisplayBonus = bonusTempBigOdd.multiply(bonusRatioBigOdd.divide(new BigDecimal(100), RoundingMode.DOWN));

                                            finalDisplayBonus = finalDisplayBonus.setScale(3, RoundingMode.DOWN);
                                            bonusMap2.put(value, finalDisplayBonus.toString());
                                        } else if (3 <= Integer.valueOf(index) && 18 >= Integer.valueOf(index)) {
                                            int finalIndex = Integer.valueOf(index) - 3;
                                            bonusMap2.put(index, finalDisplayBonus.divide(new BigDecimal(betCountTemp[finalIndex]), RoundingMode.DOWN).toString());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            try {
                BeanUtils.copyProperties(vo, pbBase);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }

            //快三和值或大小單雙頁面上要顯示賠率
            if (lott.getId().contains("k3") && (pbBase.getId().equals("k3_star3_and") || pbBase.getId().equals("k3_star3_big_odd"))) {
                if (vo.getId().equals(showDetailBet[0])) {
                    vo.setBonusArray(bonusMap);
                }
                if (vo.getId().equals(showDetailBet[1])) {
                    vo.setBonusArray(bonusMap2);
                }
            }
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(vo));
    }

    //移除後台關閉的玩法
    private void removeUnavailablePlayer(List<PlayerBonus> playerBonus, LotteryBase lottBase) {
        if (isMobile()) {
            int size = playerBonus.size();
            for (int i = size - 1; i >= 0; i--) {
                PlayerBonus pb = playerBonus.get(i);
                if ((pb.getMobileStatus() != 0) || (pb.getSaleStatus() != 0)) {
                    lottBase.removePlayer(pb.getId());
                }
            }
        } else {
            int size = playerBonus.size();
            for (int i = size - 1; i >= 0; i--) {
                PlayerBonus pb = playerBonus.get(i);
                if (null != pb && null != pb.getSaleStatus()) {
                    if (pb.getSaleStatus() != 0) {
                        lottBase.removePlayer(pb.getId());
                    }
                }
            }
        }
    }


    /**
     * @param lotteryId
     * @param len
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/trend"}, method = {RequestMethod.GET})
    public JsonNode trend(String lotteryId, Integer len) {
        HashMap<String, String> returnC = new HashMap<>();
        try {

            if (StrUtils.hasEmpty(lotteryId)) {
                returnC.put("message", "彩种ID不能为空！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

            Lottery lott = this.lotteryService.find(lotteryId);

            if ((len == null) || (!seasonLen.contains(len))) {
                len = 30;
            }

            List<LotterySeason> openList = this.lotteryService.getLast(lotteryId, len);

            LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());

            LotteryTrend trend = lottBase.getTrend();
            if (openList.size() > 0) {
                LotterySeason lastSeason = openList.get(openList.size() - 1);

                List<Map<String, Object>> lost = this.seasonReportService.listLost(lotteryId, lastSeason.getSeasonId(),
                        lottBase.getOpenCount());
                trend.setData(lottBase.getSeasonOpen(openList), lost);
            }

            Map<String, Object> trendMap = new HashMap<>();
            trendMap.put("allTrends", trend.getAllTrends());
            trendMap.put("name", trend.getName());
            trendMap.put("numLen", trend.getNumLen());
            Integer[] nums = trend.getNums();
            trendMap.put("nums", nums);
            trendMap.put("openLen", trend.getOpenLen());
            String[] titles = trend.getTitles();
            trendMap.put("titles", titles);
            trendMap.put("trends", trend.getTrends());

            List<Map<String, Object>> titlesAndNums = new ArrayList<>();
            Map<String, Object> titleAndNum;
            for (String strTit : titles) {
                titleAndNum = new HashMap<>();
                titleAndNum.put("title", strTit);
                titleAndNum.put("nums", nums);
                titlesAndNums.add(titleAndNum);
            }

            trendMap.put("titlesAndNums", titlesAndNums);

            HashMap<String, Object> dataO = new HashMap<>();
            dataO.put("trend", trendMap);
            dataO.put("len", len);
            dataO.put("theme", getThemeName());
            dataO.put("lott", lott);
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(dataO));
        } catch (Exception e) {
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }


}

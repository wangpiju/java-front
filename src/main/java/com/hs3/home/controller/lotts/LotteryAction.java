package com.hs3.home.controller.lotts;

import com.hs3.db.Page;
import com.hs3.entity.lotts.*;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.lotts.*;
import com.hs3.lotts.k3.star3.K3Star3AndPlayer;
import com.hs3.models.Jsoner;
import com.hs3.models.lotts.*;
import com.hs3.service.article.WinGoodNewService;
import com.hs3.service.lotts.*;
import com.hs3.service.report.SeasonReportService;
import com.hs3.service.report.UserReportService;
import com.hs3.service.user.UserNoticeService;
import com.hs3.service.user.UserService;
import com.hs3.utils.*;
import com.hs3.web.auth.ThreadLog;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

@Controller
@Scope("prototype")
@RequestMapping({"/lotts"})
public class LotteryAction extends HomeAction {
    private static final Logger logger = Logger.getLogger(LotteryAction.class);
    private static final List<Integer> seasonLen = Arrays.asList(30, 50, 100);
    private static final List<String> bigOddList = Arrays.asList("大", "小", "单", "双");
    private static String k3_star3_big_odd = "k3_star3_big_odd";
    private static String k3_star3_and = "k3_star3_and";
    private static String n11x5_star3_and = "n11x5_star3_and";
    private static String n11x5_star3_big = "n11x5_star3_big";
    private static String n11x5_star3_small = "n11x5_star3_small";
    private static String n11x5_star3_odd = "n11x5_star3_odd";
    private static String n11x5_star3_even = "n11x5_star3_even";

    @Autowired
    LotterySaleTimeService lotterySaleTimeService;
    @Autowired
    LotteryService lotteryService;
    @Autowired
    PlayerService playerService;
    @Autowired
    BetService betService;
    @Autowired
    UserService userService;
    @Autowired
    BonusGroupService bonusGroupService;
    @Autowired
    BonusGroupDetailsService bonusGroupDetailsService;
    @Autowired
    TraceService traceService;
    @Autowired
    SeasonReportService seasonReportService;
    @Autowired
    private BetInService betInService;
    @Autowired
    private BetInTotalService betInTotalService;
    @Autowired
    private UserNoticeService userNoticeService;
    @Autowired
    private BetTigerService betTigerService;
    @Autowired
    private UserReportService userReportService;
    @Autowired
    private WinGoodNewService winGoodNewService;

    @ResponseBody
    @RequestMapping({"/{lotteryId}/vild"})
    public Object vild(@PathVariable("lotteryId") String lotteryId, String content, String openNum, String playerId) {
        Lottery lott = this.lotteryService.find(lotteryId);
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        PlayerBase play = lottBase.getPlayer(playerId);
        int betCount = play.getCount(content);
        List<Integer> opens = ListUtils.toIntList(openNum);
        BigDecimal win = play.getWin(content, opens);
        return Jsoner.success("注数：" + betCount + ",理论奖金:" + win);
    }

    @RequestMapping({"/{lotteryId}/trend"})
    public Object trend(@PathVariable("lotteryId") String lotteryId, Integer len) {
        ModelAndView mv;
        Lottery lott = this.lotteryService.find(lotteryId);
        if (lott == null) {
            return getErrorView();
        }

        if ((len == null) || (!seasonLen.contains(len))) {
            len = 30;
        }
        List<LotterySeason> openList = this.lotteryService.getLast(lotteryId, len);

        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());

        if (isMobile()) {
            mv = getViewWithHeadModel("/lotts/trend");
            mv.addObject("list", lottBase.getSeasonOpen(openList));
        } else {
            mv = getView("/lotts/trend");
            LotteryTrend trend = lottBase.getTrend();
            if (openList.size() > 0) {
                LotterySeason lastSeason = openList.get(0);

                List<Map<String, Object>> lost = this.seasonReportService.listLost(lotteryId, lastSeason.getSeasonId(),
                        lottBase.getOpenCount());
                trend.setData(lottBase.getSeasonOpen(openList), lost);
            }
            mv.addObject("trend", trend);
            mv.addObject("len", len);
            mv.addObject("theme", getThemeName());
        }
        mv.addObject("lott", lott);
        return mv;
    }

    @RequestMapping({"/tiger/trend"})
    public Object trendTiger(Integer len) {
        String lotteryId = "tiger";
        ModelAndView mv;
        Lottery lott = this.lotteryService.find(lotteryId);
        if (lott == null) {
            return getErrorView();
        }

        if ((len == null) || (!seasonLen.contains(len))) {
            len = 30;
        }

        User user = getLogin();
        List<BetTiger> betTigerList = this.betTigerService.listByAccount(user.getAccount(), len);

        List<LotterySeason> openList = new ArrayList<>();

        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());

        mv = getView("/lotts/trend");

        List<Map<String, Object>> lost = this.seasonReportService.listLostTiger(user.getAccount(),
                lottBase.getOpenCount());
        LotteryTrend trend = lottBase.getTrend();
        for (BetTiger betTiger : betTigerList) {
            LotterySeason ls = new LotterySeason();
            ls.setAddTime(betTiger.getCreateTime());
            ls.setOpenTime(betTiger.getCreateTime());
            ls.setLotteryId("tiger");
            ls.setSeasonId(DateUtils.format(betTiger.getCreateTime()));
            ls.setN1(Character.getNumericValue(betTiger.getOpenNum().charAt(0)));
            ls.setN2(Character.getNumericValue(betTiger.getOpenNum().charAt(2)));
            ls.setN3(Character.getNumericValue(betTiger.getOpenNum().charAt(4)));
            ls.setN4(Character.getNumericValue(betTiger.getOpenNum().charAt(6)));
            ls.setN5(Character.getNumericValue(betTiger.getOpenNum().charAt(8)));
            openList.add(ls);
        }
        trend.setData(lottBase.getSeasonOpen(openList), lost);

        mv.addObject("trend", trend);
        mv.addObject("len", len);
        mv.addObject("theme", getThemeName());
        mv.addObject("lott", lott);
        return mv;
    }

    @RequestMapping({"/tiger/index"})
    public Object indexTiger() {
        String lotteryId = "tiger";
        Lottery lott = this.lotteryService.find(lotteryId);
        if (lott == null) {
            return getErrorView();
        }

        User user = getLogin();

        BonusGroup bonusGroup = this.bonusGroupService.find(user.getBonusGroupId());

        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        List<PlayerBonus> playerBonus = this.playerService.listFullByLotteryIdAndGroupId(lotteryId,
                user.getBonusGroupId());
        lottBase.loadPlayerBonus(playerBonus);

        List<Bet> bets = this.betService.listByAccount(user.getAccount(), 10);

        ModelAndView mv = getViewWithHeadModel("/lotts/slotMachine");

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
                if (pb.getSaleStatus() != 0) {
                    lottBase.removePlayer(pb.getId());
                }
            }
        }

        mv.addObject("bonusGroup", bonusGroup);
        mv.addObject("lott", lott);
        mv.addObject("lottBase", lottBase);
        mv.addObject("bets", bets);
        mv.addObject("openList", this.betTigerService.listByAccount(user.getAccount(), 3));
        mv.addObject("winList",
                this.userReportService.listTigerWin(DateUtils.formatDate(DateUtils.addDay(new Date(), -1)), 3));
        mv.addObject("testNum", NumUtils.getRandom(0, 9) + NumUtils.getRandom(0, 9) + NumUtils.getRandom(0, 9)
                + NumUtils.getRandom(0, 9) + NumUtils.getRandom(0, 9));
        return mv;
    }

    @RequestMapping({"/{lotteryId}/index"})
    public Object index(@PathVariable("lotteryId") String lotteryId) {
        Lottery lott = this.lotteryService.find(lotteryId);
        if (lott == null) {
            return getErrorView();
        }

        User user = this.userService.findByAccount(getLogin().getAccount());
        BonusGroup bonusGroup = this.bonusGroupService.find(getLogin().getBonusGroupId());
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        List<PlayerBonus> playerBonus = this.playerService.listFullByLotteryIdAndGroupId(lotteryId,
                getLogin().getBonusGroupId());
        lottBase.loadPlayerBonus(playerBonus);
        LotterySaleTime saleTime = this.lotterySaleTimeService.getCurrentByLotteryId(lotteryId);

        int allSecond = 0;
        if (null != saleTime)
            allSecond = (int) DateUtils.getSecondBetween(new Date(), saleTime.getEndTime()) + 1;
        String timeformat = DateUtils.timeFormat(allSecond);
        List<String> timeList = ListUtils.toList(timeformat, ":");

        ModelAndView mv;

        if ((lott.getGroupId().equals("pk10")) && (!isMobile())) {
            mv = getViewWithHeadModel("/lotts/pk10");
        } else {
            mv = getViewWithHeadModel("/lotts/index");
        }

        SeasonOpen lastOpen = null;

        if (isMobile()) {
            int size = playerBonus.size();
            for (int i = size - 1; i >= 0; i--) {
                PlayerBonus pb = playerBonus.get(i);
                if ((pb.getMobileStatus() != 0) || (pb.getSaleStatus() != 0)) {
                    lottBase.removePlayer(pb.getId());
                }
            }
            List<LotterySeason> openList = this.lotteryService.getLast(lotteryId, 1);
            List<SeasonOpen> list = lottBase.getSeasonOpen(openList);
            lastOpen = list.get(0);
        } else {
            int size = playerBonus.size();
            for (int i = size - 1; i >= 0; i--) {
                PlayerBonus pb = playerBonus.get(i);
                if (pb.getSaleStatus() != 0) {
                    lottBase.removePlayer(pb.getId());
                }
            }
            List<Bet> bets = this.betService.listByAccount(getLogin().getAccount(), 5);
            List<Trace> traces = this.traceService.listByAccount(getLogin().getAccount(), 5);
            List<LotterySeason> openList = this.lotteryService.getLast(lotteryId, 10);
            if (null != openList && openList.size() > 0) {
                List<String> numStatus = lottBase.getOpenStatus(openList.get(0));
                List<SeasonOpen> list = lottBase.getSeasonOpen(openList);
                lastOpen = list.get(0);
                SeasonCount seasonCount = this.lotterySaleTimeService.getSeasonCountByLotteryId(lotteryId,
                        DateUtils.getToDay());

                mv.addObject("bets", bets);
                mv.addObject("traces", traces);
                mv.addObject("numStatus", numStatus);
                mv.addObject("openList", list);
                mv.addObject("seasonCount", seasonCount);
                mv.addObject("winList", this.winGoodNewService.getWinList());
            }
        }

        mv.addObject("bonusGroup", bonusGroup);
        mv.addObject("current", saleTime);
        mv.addObject("lott", lott);
        mv.addObject("allSecond", allSecond);
        mv.addObject("hour", timeList.get(0));
        mv.addObject("minute", timeList.get(1));
        mv.addObject("second", timeList.get(2));
        mv.addObject("lastOpen", lastOpen);
        //组装赔率
        assemBonus(lottBase, user, playerBonus, lotteryId);
        mv.addObject("lottBase", lottBase);
//        System.out.println(JSONObject.toJSONString(lottBase));
        mv.addObject("betInConfig", this.betInService.getBetInConfig());
        return mv;
    }

    private void assemBonus(LotteryBase lottBase, User user, List<PlayerBonus> playerBonusList, String lotteryId) {
        for (PlayerBase pb : lottBase.getPlayers()) {
            PlayerBase pbBase = lottBase.getPlayer(pb.getId());
            if (null != pbBase) {
                PlayerBonus pbBonus = null;
                if (null != pbBase.getBonusStr()) {
                    for (PlayerBonus aPlayerBonusList : playerBonusList) {
                        if (aPlayerBonusList.getId().equals(pbBase.getId())) {
                            pbBonus = aPlayerBonusList;
                            break;
                        }
                    }
                    if (null != pbBonus) {
                        BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());
                        BonusGroupDetails bGroupDetails = ThreadLocalCache.getGroupDetails(pbBonus.getId(),
                                lotteryId, user.getBonusGroupId());
                        //和值的奖金详情
                        BigDecimal bonusRatio = pbBonus.getBonusRatio().add(user.getRebateRatio()
                                .subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio())));
                        //bonusRatio = 85 + (14-(14-14)) = 99
                        //範圍賠率
                        if (pbBase.getBonusStr().contains("-")) {
                            String[] pbArray = pbBase.getBonusStr().split("-");
                            BigDecimal bonusTemp1 = new BigDecimal(pbArray[0].trim());
                            BigDecimal bonusTemp2 = new BigDecimal(pbArray[1].trim());
                            bonusTemp1 = bonusTemp1.multiply(bonusRatio.divide(new BigDecimal(100), 4)).setScale(2, BigDecimal.ROUND_DOWN);
                            bonusTemp2 = bonusTemp2.multiply(bonusRatio.divide(new BigDecimal(100), 4)).setScale(2, BigDecimal.ROUND_DOWN);
                            pbBase.setDisplayBonus(bonusTemp1.toString() + " - " + bonusTemp2.toString());
                        }
                        //單一賠率
                        else {
                            BigDecimal bonusTemp = new BigDecimal(pbBase.getBonusStr());
                            BigDecimal finalDisplayBonus = bonusTemp.multiply(bonusRatio.divide(new BigDecimal(100), 4));
                            //finalDisplayBonus 216.00 * 0.99
                            pbBase.setDisplayBonus(finalDisplayBonus.setScale(2, BigDecimal.ROUND_DOWN).toString());


                            if (pbBase.getId().equals(k3_star3_and)) {
                                String _diaplayBonus;
                                if (lotteryId.equals("dfk3"))
                                    _diaplayBonus = finalDisplayBonus.divide(new BigDecimal(K3Star3AndPlayer.getSingleBetCount("03")), 4).setScale(2, BigDecimal.ROUND_DOWN).toString();
                                else
                                    _diaplayBonus = finalDisplayBonus.divide(new BigDecimal(K3Star3AndPlayer.getSingleBetCount(K3Star3AndPlayer.defaultDisplayBonusNum)), 4).setScale(2, BigDecimal.ROUND_DOWN).toString();

                                pbBase.setDisplayBonus(_diaplayBonus);
                                NumberView[] numberViewsTemp = pbBase.getNumView();
                                //和值
                                PlayerBonus oddPbBonus = null;
                                BonusGroupDetails bddGroupDetails = null;
                                BonusGroup bddDecimal = null;


                                for (PlayerBonus aPlayerBonusList : playerBonusList) {
                                    if (aPlayerBonusList.getId().equals(k3_star3_big_odd)) {
                                        oddPbBonus = aPlayerBonusList;
                                        bddDecimal = this.bonusGroupService.find(user.getBonusGroupId());
                                        bddGroupDetails = ThreadLocalCache.getGroupDetails(k3_star3_big_odd,
                                                lotteryId, user.getBonusGroupId());
                                        break;
                                    }
                                }

                                BigDecimal pddBonusRatio;

                                if (oddPbBonus == null) {
                                    logger.error("--> k3_star3_big_odd oddPbBonus is null");
                                    pddBonusRatio = new BigDecimal(0);
                                } else {
                                    pddBonusRatio = oddPbBonus.getBonusRatio().add(user.getRebateRatio()
                                            .subtract(bddDecimal.getRebateRatio().subtract(bddGroupDetails.getRebateRatio())));
                                }

                                for (NumberView numberView : numberViewsTemp) {
                                    List<String[]> numBonusList = new ArrayList<>();
                                    for (Object obj : numberView.getNums()) {
                                        String value = obj.toString();
                                        if (K3Star3AndPlayer.getSingleGroupId(value).equals(k3_star3_big_odd)) {
                                            PlayerBase bigOddPbBase = lottBase.getPlayer(k3_star3_big_odd);
                                            //2.05 * (0.99)/
                                            String finalBonus = bigOddPbBase.getBonus().multiply(pddBonusRatio.divide(new BigDecimal(100), 4)).setScale(3, BigDecimal.ROUND_DOWN).toString();
                                            String numBonus[] = new String[]{value, finalBonus};
                                            numBonusList.add(numBonus);
                                        } else {
                                            String finalBonus = finalDisplayBonus.divide(new BigDecimal(K3Star3AndPlayer.getSingleBetCount(value)), 4).setScale(2, BigDecimal.ROUND_DOWN).toString();
                                            String numBonus[] = new String[]{value, finalBonus};
                                            numBonusList.add(numBonus);
                                        }
                                    }
                                    numberView.setNumBonus(numBonusList);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping({"/{lotteryId}/info"})
    public Object info(@PathVariable("lotteryId") String lotteryId) {
        Lottery lott = this.lotteryService.find(lotteryId);
        if (lott == null) {
            return Jsoner.error("参数错误");
        }

        int n = 10;
        if (isMobile()) {
            n = 1;
        }
        if (lotteryId.equals("pk10")) {
            n = 5;
        }

        User u = this.userService.findByAccount(getLogin().getAccount());

        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        LotterySaleTime saleTime = this.lotterySaleTimeService.getCurrentByLotteryId(lotteryId);
        int allSecond = (int) DateUtils.getSecondBetween(new Date(), saleTime.getEndTime()) + 1;
        List<LotterySeason> seasonList = this.lotteryService.getLast(lotteryId, n);

        Map<String, Object> rel = new HashMap<>();
        if (!isMobile()) {
            SeasonCount seasonCount = this.lotterySaleTimeService.getSeasonCountByLotteryId(lotteryId,
                    DateUtils.getToDay());

            List<Bet> bets = this.betService.listByAccount(getLogin().getAccount(), 5);

            List<Trace> traces = this.traceService.listByAccount(getLogin().getAccount(), 5);

            rel.put("numStatus", lottBase.getOpenStatus(seasonList.get(0)));
            rel.put("seasonCount", seasonCount);
            rel.put("bets", bets);
            rel.put("traces", traces);
            rel.put("opens", lottBase.getSeasonOpen(seasonList));
        }

        SeasonOpen open = lottBase.getSeasonOpen(seasonList.get(0));

        rel.put("amount", u.getAmount());
        rel.put("seasonId", saleTime.getSeasonId());
        rel.put("allSecond", allSecond);
        rel.put("lastOpen", open);
        rel.put("lott", lott);
        rel.put("userNotice", this.userNoticeService.deleteAndList(getLogin().getAccount(), 5));
        //加上上一期期號
        try {
            LotterySaleTime lastSaleTime = this.lotterySaleTimeService.getPreviousByLotteryId(lotteryId, new Date());
            rel.put("lastSeasonId", lastSaleTime.getSeasonId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Jsoner.success(rel);
    }

    /* Error */
//    @ResponseBody
//    @RequestMapping({"/{lotteryId}/upload"})
//    public Object upload(org.springframework.web.multipart.MultipartFile file, @PathVariable("lotteryId") String lotteryId) {
//        return null;
//    }

    @ResponseBody
    @RequestMapping(value = {"/tiger/bet"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object betTiger(Order order) {
        order.setLotteryId("tiger");
        Integer betNum;
        Integer allNum = 0;
        String traceId;
        Date curDate = new Date();
        List<Bet> betList = new ArrayList<>();
        BigDecimal allPrice = new BigDecimal("0");

        ThreadLog.addKey("action vaild start:" + System.currentTimeMillis());
        Lottery lott = this.lotteryService.find(order.getLotteryId());
        if (lott == null)
            return Jsoner.error("彩种错误：" + order.getLotteryId());
        if (!lott.getStatus().equals(0)) {
            return Jsoner.error("暂时不接受  [" + lott.getTitle() + "] 的投注.感谢您的支持.");
        }
        if ((order.getIsTrace() == null) || ((!order.getIsTrace().equals(1))
                && (!order.getIsTrace().equals(0))))
            return Jsoner.error("追号状态非法：" + order.getIsTrace());
        if ((order.getTraceWinStop() == null) || ((!order.getTraceWinStop().equals(1))
                && (!order.getTraceWinStop().equals(0))))
            return Jsoner.error("追中即停非法：" + order.getTraceWinStop());
        if ((order.getIsTrace() == 0) && (order.getTraceWinStop() != 0)) {
            return Jsoner.error("不追号不能追中即停：" + order.getTraceWinStop());
        }
        if (order.getTraceOrders() == null) {
            return Jsoner.error("期号不正确.");
        }

        if ((order.getIsTrace().equals(1))
                && (order.getTraceOrders().size() > lott.getMaxPlan())) {
            return Jsoner.error("最大追号期数：" + lott.getMaxPlan());
        }

        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());

        User user = this.userService.findByAccount(getLogin().getAccount());
        BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());
        if (user.getRebateRatio().compareTo(bDecimal.getPlayerMaxRatio()) > 0) {
            return Jsoner.error("直属代理用户，不允许投注。");
        }
        if ((!order.getBounsType().equals(0)) && (!order.getBounsType().equals(1)))
            return Jsoner.error("数据不合法：" + order.getBounsType());
        if (!user.getBetStatus().equals(0))
            return Jsoner.error("您已被禁止投注");
        PlayerBase play;
        for (OrderContent orderContent : order.getOrder()) {
            if (order.getIsTrace().equals(1)) {
                traceId = IdBuilder.CreateId(order.getLotteryId(), "Z");
            } else {
                traceId = "";
            }
            Bet bet = new Bet();
            play = lottBase.getPlayer(orderContent.getPlayId());
            if (play == null)
                return Jsoner.error("投注失败,玩法错误,请刷新页面");
            if (!this.playerService.getStatus(orderContent.getPlayId(), order.getLotteryId())
                    .equals(0)) {
                return Jsoner.error("该玩法停止销售");
            }

            betNum = play.getCount(orderContent.getContent());

            allNum = allNum + betNum;
            if (betNum.equals(0)) {
                return Jsoner.error("注数不能为空！");
            }
            if (!betNum.equals(orderContent.getBetCount()))
                return Jsoner.error("注数" + orderContent.getBetCount() + "不正确");
            if ((orderContent.getUnit().compareTo(new BigDecimal("2.00")) != 0)
                    && (orderContent.getUnit().compareTo(new BigDecimal("0.20")) != 0)
                    && (orderContent.getUnit().compareTo(new BigDecimal("0.02")) != 0)
                    && (orderContent.getUnit().compareTo(new BigDecimal("0.002")) != 0))
                return Jsoner.error("单位模式数据不合法：" + orderContent.getUnit());
            if (orderContent.getPrice() < 1)
                return Jsoner.error("倍数" + orderContent.getPrice() + "小于能小于1.");
            BonusGroupDetails bGroupDetails = this.bonusGroupDetailsService.find(orderContent.getPlayId(),
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
            //.divide(new BigDecimal("2"), 4));
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
            if (bet.getBonusType().equals(0)) {
                bet.setBonusRate(bGroupDetails.getBonusRatio().add(user.getRebateRatio()
                        .subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio()))));
            } else
                bet.setBonusRate(bGroupDetails.getBonusRatio());
            bet.setHashCode(LotteryUtils.hashCode(bet));
            bet.setTraceWinStop(order.getTraceWinStop());
            betList.add(bet);
        }

        if (!allNum.equals(order.getCount())) {
            return Jsoner.error("投注总注数不正确！");
        }
        ThreadLog.addKey("service start:" + System.currentTimeMillis());
        try {
            betList = this.betService.saveBetTigerOrder(user.getAccount(), betList, allPrice,
                    DateUtils.addMinute(new Date(), 10));
        } catch (BaseCheckException e) {
            return Jsoner.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Jsoner.error("系统繁忙，请稍后再试！");
        }
        ThreadLog.addKey("service end:" + System.currentTimeMillis());

        Map<String, Object> map = new HashMap<>();
        map.put("betList", betList);

        BigDecimal winAll = new BigDecimal("0");
        for (Bet b : betList) {
            if (b.getWin() != null) {
                winAll = winAll.add(b.getWin());
            }
        }
        map.put("win", winAll);

        map.put("order", order);

        return Jsoner.success(map);
    }

    @ResponseBody
    @RequestMapping(value = {"/{lotteryId}/reBet"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object reBet(@PathVariable("lotteryId") String lotteryId, String betId) {
        if ("tiger".equals(lotteryId)) {
            return Jsoner.error("彩票老虎机不允许再次投注");
        }
        String account = getLogin().getAccount();
        Bet b = this.betService.find(betId);
        if ((b == null) || (!b.getAccount().equals(account))) {
            return Jsoner.error("注单不存在");
        }

        LotterySaleTime saleTime = this.lotterySaleTimeService.getCurrentByLotteryId(lotteryId);
        if (saleTime == null) {
            return Jsoner.error("当前没有可以投注的奖期");
        }
        String seasonId = saleTime.getSeasonId();

        Order o = new Order();
        o.setAmount(b.getAmount());
        o.setBounsType(b.getBonusType());
        o.setCount(b.getBetCount());
        o.setIsTrace(0);
        o.setLotteryId(lotteryId);
        o.setTraceWinStop(0);

        List<TraceOrder> traceOrders = new ArrayList<>();
        TraceOrder traceOrder = new TraceOrder();
        traceOrder.setPrice(b.getPrice());
        traceOrder.setSeasonId(seasonId);
        traceOrders.add(traceOrder);
        o.setTraceOrders(traceOrders);

        List<OrderContent> contents = new ArrayList<>();
        OrderContent content = new OrderContent();
        content.setBetCount(b.getBetCount());
        content.setContent(b.getContent());
        content.setPlayId(b.getPlayerId());
        content.setPrice(b.getPrice());
        content.setUnit(b.getUnit());
        contents.add(content);
        o.setOrder(contents);

        return bet(lotteryId, o);
    }

    @ResponseBody
    @RequestMapping(value = {"/{lotteryId}/bet"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object bet(@PathVariable("lotteryId") String lotteryId, Order order) {

        //現在強制設置為非追號單
        order.setIsTrace(0);
        order.setTraceWinStop(0);

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
            return Jsoner.error("彩种错误：" + order.getLotteryId());
        if (!lott.getStatus().equals(0)) {
            return Jsoner.error("暂时不接受  [" + lott.getTitle() + "] 的投注.感谢您的支持.");
        }
        if ((order.getIsTrace() == null) || ((!order.getIsTrace().equals(1))
                && (!order.getIsTrace().equals(0))))
            return Jsoner.error("追号状态非法：" + order.getIsTrace());
        if ((order.getTraceWinStop() == null) || ((!order.getTraceWinStop().equals(1))
                && (!order.getTraceWinStop().equals(0))))
            return Jsoner.error("追中即停非法：" + order.getTraceWinStop());
        if ((order.getIsTrace() == 0) && (order.getTraceWinStop() != 0)) {
            return Jsoner.error("不追号不能追中即停：" + order.getTraceWinStop());
        }
        if (order.getTraceOrders() == null) {
            return Jsoner.error("期号不正确.");
        }

        if ((order.getIsTrace().equals(1))
                && (order.getTraceOrders().size() > lott.getMaxPlan())) {
            return Jsoner.error("最大追号期数：" + lott.getMaxPlan());
        }

        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        PlayerBase playBigOdd = lottBase.getPlayer(k3_star3_big_odd);
        User user = this.userService.findByAccount(getLogin().getAccount());
        BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());
        if (user.getRebateRatio().compareTo(bDecimal.getPlayerMaxRatio()) > 0) {
            return Jsoner.error("直属代理用户，不允许投注。");
        }
        if ((!order.getBounsType().equals(0)) && (!order.getBounsType().equals(1)))
            return Jsoner.error("数据不合法：" + order.getBounsType());
        if (!user.getBetStatus().equals(0))
            return Jsoner.error("您已被禁止投注");
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
                return Jsoner.error("投注失败,玩法错误,请刷新页面");
            if (!this.playerService.getStatus(orderContent.getPlayId(), order.getLotteryId())
                    .equals(0)) {
                return Jsoner.error("该玩法停止销售");
            }

            betNum = play.getCount(orderContent.getContent());

            allNum = allNum + betNum;
            if (betNum.equals(0)) {
                return Jsoner.error("注数不能为空！");
            }

            if (!play.getId().equals(k3_star3_and) && !play.getId().equals(k3_star3_big_odd) && !betNum.equals(orderContent.getBetCount()))
                return Jsoner.error("注数" + orderContent.getBetCount() + "不正确");

            if (orderContent.getPrice() < 1)
                return Jsoner.error("倍数" + orderContent.getPrice() + "不能小于1.");
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
            bet.setSeasonId((order.getTraceOrders().get(0)).getSeasonId());
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
            if (bet.getBonusType().equals(0)) {
                bet.setBonusRate(bGroupDetails.getBonusRatio().add(user.getRebateRatio()
                        .subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio()))));
            } else
                bet.setBonusRate(bGroupDetails.getBonusRatio());
            bet.setHashCode(LotteryUtils.hashCode(bet));
            bet.setTraceWinStop(order.getTraceWinStop());

            try {
                //判斷是否快三合值中有下大小單雙
                if (orderContent.getPlayId().equals(k3_star3_and)) {
                    if (orderContent.getContent().contains("大") || orderContent.getContent().contains("小") ||
                            orderContent.getContent().contains("单") || orderContent.getContent().contains("双")) {

                        String orderBigOddContent = orderContent.getContent();

                        String sb = handleBigOdd(orderBigOddContent);
                        Integer matchCount = sb.split(",").length;
                        Bet betBigOdd = new Bet();
                        BeanUtils.copyProperties(betBigOdd, bet);
                        //原來那注扣掉大小單雙的注數
                        bet.setBetCount(bet.getBetCount() - matchCount);
                        //處理原來投注內容
                        String[] betContentList = bet.getContent().split(",");
                        List<String> betContentListTemp = new ArrayList<>();
                        //移除大小單雙
                        for (String aBetContentList : betContentList) {
                            if (!bigOddList.contains(aBetContentList)) {
                                betContentListTemp.add(aBetContentList);
                            }
                        }
                        String joinedString = StringUtils.join(betContentListTemp, ",");
                        bet.setContent(joinedString);
                        bet.setAmount(new BigDecimal(bet.getPrice()).multiply(new BigDecimal(bet.getBetCount())));

                        //新產生注單
                        betBigOdd.setId(IdBuilder.CreateId(order.getLotteryId(), "D"));
                        betBigOdd.setAmount(betBigOdd.getAmount().subtract(bet.getAmount()));
                        betBigOdd.setContent(sb);
                        betBigOdd.setBetCount(matchCount);
                        betBigOdd.setPlayerId(k3_star3_big_odd);
                        betBigOdd.setPlayName(playBigOdd.getFullTitle());
                        BonusGroupDetails bGroupDetailsBigOdd = ThreadLocalCache.getGroupDetails(betBigOdd.getPlayerId(),
                                order.getLotteryId(), user.getBonusGroupId());
                        betBigOdd.setBonusRate(bGroupDetailsBigOdd.getBonusRatio().add(user.getRebateRatio()
                                .subtract(bDecimal.getRebateRatio().subtract(bGroupDetailsBigOdd.getRebateRatio()))));
                        betList.add(betBigOdd);
                    }
                }
                //TODO 新增十一選五和值大小單雙判斷
                else if (orderContent.getPlayId().equals(n11x5_star3_and)) {
                    if (orderContent.getContent().contains("大") || orderContent.getContent().contains("小") ||
                            orderContent.getContent().contains("单") || orderContent.getContent().contains("双")) {
                        String orderBigOddContent = orderContent.getContent();
                        String sb = handleBigOdd(orderBigOddContent);
                        Integer matchCount = sb.split(",").length;

                        //原來那注扣掉大小單雙的注數
                        bet.setBetCount(bet.getBetCount() - matchCount);
                        //處理原來投注內容
                        String[] betContentList = bet.getContent().split(",");
                        List<String> betContentListTemp = new ArrayList<>();
                        //移除大小單雙
                        for (String aBetContentList : betContentList) {
                            if (!bigOddList.contains(aBetContentList)) {
                                betContentListTemp.add(aBetContentList);
                            }
                        }
                        String joinedString = StringUtils.join(betContentListTemp, ",");
                        BigDecimal originAmount = bet.getAmount();
                        bet.setAmount(new BigDecimal(bet.getPrice()).multiply(new BigDecimal(bet.getBetCount())));
                        bet.setContent(joinedString);
                        //新產生注單大小單雙
                        String[] content = orderContent.getContent().split(",");

                        for (String s : content) {
                            Bet betBigOdd = new Bet();
                            BeanUtils.copyProperties(betBigOdd, bet);
                            String playerId = null;

                            switch (s){
                                case "大":
                                    playerId = n11x5_star3_big;
                                    break;
                                case "小":
                                    playerId = n11x5_star3_small;
                                    break;
                                case "单":
                                    playerId = n11x5_star3_odd;
                                    break;
                                case "双":
                                    playerId = n11x5_star3_even;
                                    break;
                            }

                            if(null != playerId){
                                betBigOdd.setId(IdBuilder.CreateId(order.getLotteryId(), "D"));
                                betBigOdd.setAmount(originAmount.subtract(bet.getAmount()).divide(new BigDecimal(matchCount)));
                                betBigOdd.setContent(s);
                                betBigOdd.setBetCount(1);
                                betBigOdd.setPlayerId(playerId);
                                PlayerBase playBigOddFinal = lottBase.getPlayer(playerId);
                                betBigOdd.setPlayName(playBigOddFinal.getFullTitle());
                                BonusGroupDetails bGroupDetailsBigOdd = ThreadLocalCache.getGroupDetails(betBigOdd.getPlayerId(),
                                        order.getLotteryId(), user.getBonusGroupId());
                                betBigOdd.setBonusRate(bGroupDetailsBigOdd.getBonusRatio().add(user.getRebateRatio()
                                        .subtract(bDecimal.getRebateRatio().subtract(bGroupDetailsBigOdd.getRebateRatio()))));
                                betList.add(betBigOdd);
                            }
                        }

//                        bet.setContent(joinedString);
//                        bet.setAmount(new BigDecimal(bet.getPrice()).multiply(new BigDecimal(bet.getBetCount())));
                    }
                }
            } catch (Exception e) {
                logger.error(lotteryId + "--> error ", e);
            }
            if (bet.getAmount().compareTo(BigDecimal.ZERO) > 0)
                betList.add(bet);
        }
        Date curOpenTime = null;
        if (order.getIsTrace().equals(0)) {

            if ((order.getTraceOrders().size() != 1)
                    || (order.getTraceOrders().get(0).getSeasonId().equals("")))
                return Jsoner.error("期号数量不正确.");
            if (order.getCount() < 1) {
                return Jsoner.error("总注数" + order.getTraceOrders().get(0).getPrice() + "不能小于1.");
            }
            LotterySaleTime saleTime = this.lotterySaleTimeService
                    .find(order.getTraceOrders().get(0).getSeasonId(), order.getLotteryId());
            curOpenTime = saleTime.getOpenTime();
            if (saleTime.getEndTime().getTime() <= new Date().getTime()) {
                return Jsoner.error("期号 " + order.getTraceOrders().get(0).getSeasonId() + " 未开盘或已封盘.");
            }
            if ((order.getTraceOrders().get(0).getSeasonId().equals(""))
                    || (order.getTraceOrders().get(0).getSeasonId() == null)) {
                return Jsoner.error("期号 不能为空.");
            }
        } else {
            betAllPrice = allPrice;
            for (TraceOrder to : order.getTraceOrders()) {
                traceAllPrice = traceAllPrice.add(betAllPrice.multiply(new BigDecimal(to.getPrice())));
                if (to.getPrice() < 1)
                    return Jsoner.error("追号倍数" + to.getPrice() + "不能小于1.");
                if ((to.getSeasonId() == null) || (to.getSeasonId().equals("")))
                    return Jsoner.error("追号期号" + to.getPrice() + "不能为空！");
                LotterySaleTime saleTime = this.lotterySaleTimeService.find(to.getSeasonId(), order.getLotteryId());
                if (saleTime.getEndTime().getTime() > new Date().getTime()) {
                    if (curSaleSeason.equals("")) {
                        LotterySaleTime lst = this.lotterySaleTimeService.getCurrentByLotteryId(order.getLotteryId());
                        curSaleSeason = lst.getSeasonId();
                        curOpenTime = lst.getOpenTime();
                    }
                } else {
                    return Jsoner.error("追号期号 " + to.getSeasonId() + " 未开盘或已封盘.");
                }
            }
        }

        if (!allNum.equals(order.getCount())) {
            return Jsoner.error("投注总注数不正确！");
        }
        ThreadLog.addKey("service start:" + System.currentTimeMillis());

        if (order.getIsTrace().equals(0)) {
            if (null == curOpenTime)
                curOpenTime = new Date();
            this.betService.saveBetOrder(betList, allPrice, curOpenTime, true);
            ThreadLog.addKey("service end:" + System.currentTimeMillis());
            return Jsoner.success(betList);
        }
        Object jsoner = this.traceService.saveBetTraceOrder(betList, order.getTraceOrders(), curSaleSeason,
                traceAllPrice, curOpenTime);
        ThreadLog.addKey("service end:" + System.currentTimeMillis());
        return Jsoner.success(jsoner);
    }

    @ResponseBody
    @RequestMapping({"/{lotteryId}/cancelOrder"})
    public Object bet(@PathVariable("lotteryId") String lotteryId, String ids) {
        List<String> betListId = ListUtils.toList(ids);
        for (String betid : betListId) {
            if (betid == null || (betid.equals("")))
                return Jsoner.error(lotteryId + "提交数据有误！");
        }
        return this.betService.saveUserCancelOrder(betListId, getLogin().getAccount());
    }

    @ResponseBody
    @RequestMapping({"/{lotteryId}/listTraceSeasonId"})
    public Object listTraceSeasonId(@PathVariable("lotteryId") String lotteryId, Integer count) {
        return Jsoner.success(this.lotterySaleTimeService.listTraceSeasonId(lotteryId, count));
    }

    @ResponseBody
    @RequestMapping({"/betIn/buy"})
    public Object betIn(BetIn betIn) {
        try {
            betIn.setAccount(getLogin().getAccount());
            return Jsoner.success(this.betInService.save(betIn));
        } catch (BaseCheckException e) {
            logger.error(e.getMessage() + "[" + betIn.getAccount() + "]" + "[" + betIn.getAmount() + "]" + "["
                    + betIn.getBetId() + "]" + "[" + betIn.getMultiples() + "]");
            return Jsoner.error(e.getMessage());
        }
    }

    @RequestMapping({"/betIn/betInTotalTable"})
    public Object betInTotalTable(BetInTotal m, Date beginTime, Date endTime, Integer isIncludeChildFlag) {
        ModelAndView modelAndView = getView("/lotts/betInTotalTable");

        BetInTotal cond = new BetInTotal();
        cond.setBetId(m.getBetId());
        cond.setLotteryName(m.getLotteryName());
        cond.setPlayName(m.getPlayName());
        cond.setAccount(m.getAccount());

        boolean isIncludeChild = (isIncludeChildFlag != null) && (isIncludeChildFlag != 0);

        User user = this.userService.findByAccount(getLogin().getAccount());
        if (!StrUtils.hasEmpty(cond.getAccount())) {
            User findUser = this.userService.findByAccount(cond.getAccount());
            if (findUser == null) {
                return modelAndView;
            }

            if (findUser.getParentList().indexOf(user.getParentList()) != 0) {
                return modelAndView;
            }
        } else {
            cond.setAccount(user.getAccount());
        }

        Page p = getPageWithParams();
        List<BetInTotal> list = this.betInTotalService.findByCond(false, cond, beginTime, endTime, isIncludeChild, p);
        modelAndView.addObject("betInTotalList", list);
        modelAndView.addObject("p", p);
        return modelAndView;
    }

    @RequestMapping({"/betIn/betInTable"})
    public Object betInTable(Integer id) {
        ModelAndView modelAndView = getView("/lotts/betInTable");

        BetInTotal betInTotal = this.betInTotalService.find(id);
        modelAndView.addObject("betInTotal", betInTotal);

        BetIn betIn = new BetIn();
        betIn.setBetId(betInTotal.getBetId());
        modelAndView.addObject("betInList", this.betInService.listByCond(betIn, null, null, null));

        return modelAndView;
    }

    @RequestMapping({"/lottery/index"})
    public Object lotteryIndex(Integer id) {
        logger.info("get " + id + " index");
        return getViewWithHeadModel("/lottery/index");
    }

    private String handleBigOdd(String orderBigOddContent) {
        StringBuilder sb = new StringBuilder();
        if (orderBigOddContent.contains("大")) {

            sb.append("大");
        }
        if (orderBigOddContent.contains("小")) {

            if (!sb.toString().isEmpty())
                sb.append(",");
            sb.append("小");
        }
        if (orderBigOddContent.contains("单")) {
            if (!sb.toString().isEmpty())
                sb.append(",");
            sb.append("单");
        }
        if (orderBigOddContent.contains("双")) {
            if (!sb.toString().isEmpty())
                sb.append(",");
            sb.append("双");
        }

        return sb.toString();
    }
}

package com.hs3.home.controller.internal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hs3.cache.BonusRiskRedisService;
import com.hs3.commons.BetStatus;
import com.hs3.commons.BonusType;
import com.hs3.commons.RedisCons;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.*;
import com.hs3.entity.sys.SysClear;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserNotice;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ThreadLocalCache;
import com.hs3.lotts.open.INumberBuilder;
import com.hs3.lotts.open.NumberBuilderFactory;
import com.hs3.service.lotts.BonusGroupService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.lotts.SettlementService;
import com.hs3.service.sys.SysClearService;
import com.hs3.service.user.UserService;
import com.hs3.utils.IdBuilder;
import com.hs3.utils.ListUtils;
import com.hs3.utils.NumUtils;
import com.hs3.utils.RedisUtils;
import com.hs3.web.auth.Auth;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * program: java-front
 * des:
 * author: Terra
 * create: 2018-06-21 14:24
 **/
@Controller
@Scope("prototype")
@RequestMapping({"/api/risk_bonus"})
public class BonusRiskController extends HomeAction {

    private static final Logger logger = LoggerFactory.getLogger(BonusRiskController.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private BonusRiskRedisService bonusRiskRedisService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private UserService userService;
    @Autowired
    private BonusGroupService bonusGroupService;
    @Autowired
    private SysClearService sysClearService;

    private static String[] types = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "1"};

    private static String[] bet = {"大", "小", "单", "双"};

    private static String[] bet2 = new String[]{"04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17"};

    private static Integer maxBetAmount = 20000;

    /**
     * 模拟投注，并使用风控控制
     *
     * @param lotteryId
     * @param title
     * @param seasonId
     * @param account
     * @param count
     */
    @ResponseBody
    @RequestMapping(value = {"/test_bonus"}, method = {RequestMethod.POST})
    public void testBo(
            @RequestParam(value = "lotteryId") String lotteryId,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "seasonId") String seasonId,
            @RequestParam(value = "account") String account,
            @RequestParam(value = "count", required = false, defaultValue = "100000") Integer count) {
        long startTime = System.currentTimeMillis();
        User user = userService.findByAccount(account);
        Lottery lott = this.lotteryService.find(lotteryId);
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        INumberBuilder iNumberBuilder = NumberBuilderFactory.getInstance(title);
        LotterySeason lotterySeason = iNumberBuilder.create(lotteryId, seasonId, null, null);
        String openNum = ListUtils.toString(lottBase.getSeasonOpen(lotterySeason).getNums());
        List<Bet> betList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Random playRan = new Random();
            Integer playerTypeRan = NumUtils.getRandom(playRan, 0, types.length - 1);
            Integer playerType = Integer.parseInt(types[playerTypeRan]);
            if (playerType == 0) {
                Random rand = new Random();
                Integer randIndex = NumUtils.getRandom(rand, 0, bet.length - 1);
                String betContent = bet[randIndex];
                Random random = new Random();
                Integer randPrice = NumUtils.getRandom(random, 2, maxBetAmount);
                Bet bet = assemBet(lott, lottBase, lotteryId, "k3_star3_big_odd", user, betContent, randPrice);
                betList.add(bet);
            } else if (playerType == 1) {
                Random rand = new Random();
                Integer randIndex = NumUtils.getRandom(rand, 0, bet2.length - 1);
                String betContent = bet2[randIndex];
                Random random = new Random();
                Integer randPrice = NumUtils.getRandom(random, 2, 1000);
                Bet bet = assemBet(lott, lottBase, lotteryId, "k3_star3_and", user, betContent, randPrice);
                betList.add(bet);
            }

        }
//        System.out.println("--> 创建投注耗时 : " + (System.currentTimeMillis() - startTime));
        Map<BigDecimal, LotterySeason> mapGainNum = new HashMap<>();
        JSONObject flag = null;
        for (int i = 0; i < 5; i++) {
            flag = riskCheckBonus(lottBase, openNum, lotteryId, seasonId, betList);
            if (!flag.getBoolean("flag")) {
                String beforeNums = openNum;
                lotterySeason = iNumberBuilder.create(lotteryId, seasonId, null, null);
                mapGainNum.put(flag.getBigDecimal("gain"), lotterySeason);
                openNum = ListUtils.toString(lottBase.getSeasonOpen(lotterySeason).getNums());
                logger.warn(String.format("--> change nums , lotteryId : %s, sessionId : %s, beforeNums : %s, afterNums : %s", lotteryId, seasonId, beforeNums, openNum));
            } else {
                break;
            }
        }
        if (!flag.getBoolean("flag")) {
            TreeSet<BigDecimal> treeSet = new TreeSet<>(mapGainNum.keySet());
            lotterySeason = mapGainNum.get(treeSet.last());
        }
        if (mapGainNum.size() > 0) {
            System.out.println(String.format("--> 彩种 : %s, 期号 : %s, 换号 : %s", lotteryId, seasonId, JSON.toJSONString(mapGainNum)));
        }
        logger.info(String.format("--> 最终号码 : %s", openNum));
    }

    /**
     * 风控开奖
     *
     * @param openNum   开奖号码
     * @param lotteryId 彩种id
     * @param seasonId  奖期id
     */
    public JSONObject riskCheckBonus(LotteryBase lb, String openNum, String lotteryId, String seasonId, List<Bet> betList) {
        JSONObject jsonObject = new JSONObject();
        long startTime = System.currentTimeMillis();
        boolean flag = true;
        //1.查询所有的订单
        //2.计算总奖金
        //3.计算总返点
        BigDecimal currTotalBetAmount = new BigDecimal(0);
        BigDecimal currTotalWin = new BigDecimal(0);
        BigDecimal currTotalUserRatio = new BigDecimal(0);
        BigDecimal currTotalAgentRatio = new BigDecimal(0);
        int count = 0;
        for (Bet bet : betList) {
            count++;
            Map<String, BigDecimal> bonusSimple = calcBetAndAmount(lb, bet, openNum);
            currTotalBetAmount = currTotalBetAmount.add(bonusSimple.get("betAmount") == null ? new BigDecimal(0) : bonusSimple.get("betAmount"));
            currTotalWin = currTotalWin.add(bonusSimple.get("win") == null ? new BigDecimal(0) : bonusSimple.get("win"));
            currTotalUserRatio = currTotalUserRatio.add(bonusSimple.get("userRatio") == null ? new BigDecimal(0) : bonusSimple.get("userRatio"));
            currTotalAgentRatio = currTotalAgentRatio.add(bonusSimple.get("agentRatio") == null ? new BigDecimal(0) : bonusSimple.get("agentRatio"));
        }
        BigDecimal gain = currTotalBetAmount.subtract(currTotalWin).subtract(currTotalUserRatio).subtract(currTotalAgentRatio);
        Long bonusPool;
        if (gain.compareTo(BigDecimal.ZERO) > 0) {
            bonusPool = bonusRiskRedisService.incrBonusPool(lotteryId, gain.longValue());
            bonusRiskRedisService.incrBetAmountPool(lotteryId, currTotalBetAmount.longValue());
        } else {
            bonusPool = bonusRiskRedisService.getBonusPool(lotteryId);
            if (gain.add(new BigDecimal(bonusPool)).compareTo(BigDecimal.ZERO) > 0) {
                bonusPool = bonusRiskRedisService.incrBonusPool(lotteryId, gain.longValue());
                bonusRiskRedisService.incrBetAmountPool(lotteryId, currTotalBetAmount.longValue());
            } else {
                logger.warn(String.format("--> bonus pool isn't enough, change nums, lotteryId:%s, seasonId:%s", lotteryId, seasonId));
                flag = false;
            }
        }
//        System.out.println("--> 开奖耗时 : " + (System.currentTimeMillis() - startTime));
        logger.info(String.format("--> riskCheckBonus result, lotteryId : %s, seasonId : %s, openNums: %s, betCount : %s, betAmount : %s, win:%s, userRatio : %s, agentRatio : %s, gain : %s, bonusPool: %s",
                lotteryId, seasonId, openNum, betList.size(), currTotalBetAmount, currTotalWin, currTotalUserRatio, currTotalAgentRatio, gain, bonusPool));
        if (!flag) {
            jsonObject.put("gain", gain);
            jsonObject.put("openNum", openNum);
        }
        jsonObject.put("flag", flag);
        return jsonObject;
    }


    //处理状态为0/6（未开奖）的投注记录
    public Map<String, BigDecimal> calcBetAndAmount(LotteryBase lb, Bet bet, String openNumsByParam) {
        Map<String, BigDecimal> bonusSimple = new HashMap<>();
        bonusSimple.put("betAmount", bet.getAmount());
        //获取所赢取的金额
        BigDecimal win = settlementService.getBetWin(lb, bet, openNumsByParam);
        bonusSimple.put("win", win);
        if (win.compareTo(BigDecimal.ZERO) > 0) {
//            System.out.println(win + " " + JSONObject.toJSONString(bet));
            int i;
        }
        User user = this.userDao.findByAccountMaster(bet.getAccount());
        //取当前用户所在奖金组的回扣比率
        BigDecimal group = ThreadLocalCache.getGroup(user.getBonusGroupId()).getRebateRatio();
        //取当前游戏当前玩法的回扣比率
        BigDecimal details = ThreadLocalCache.getGroupDetails(bet.getPlayerId(), bet.getLotteryId(), user.getBonusGroupId()).getRebateRatio();

        //{游戏回扣比率与奖金组回扣比率差}：用【取当前游戏当前玩法的回扣比率】减去【取当前用户所在奖金组的回扣比率】
        BigDecimal subRatio = details.subtract(group);

        //{用户计算关键回扣比率}：【当前用户的回扣比率】+{游戏回扣比率与奖金组回扣比率差}
        BigDecimal userRatio = user.getRebateRatio().add(subRatio);
        //{用户计算关键回扣比率}与0作比较，如果结果为小于返回-1，如果结果为大于返回1，如果结果为等于返回0，为了计算，所以结果小于0时让其值等于0
        if (userRatio.compareTo(BigDecimal.ZERO) < 0) {
            userRatio = BigDecimal.ZERO;
        }

        //BonusType为0是高奖，为1是高返，且{用户计算关键回扣比率}与0作比较大于0
        //用户返点
        if ((bet.getBonusType() == BonusType.high_rebate.code()) && (userRatio.compareTo(BigDecimal.ZERO) > 0)) {
            BigDecimal ratio = bet.getAmount().multiply(userRatio).divide(SettlementService.HUNDRED);
            bonusSimple.put("userRatio", ratio);
        }
        //用户上级返点
        if (ThreadLocalCache.getHasRatio(user)) {//如果用户充值次数与最低充值次数都小于等于0时返回false，否则返回true

            //取出用户上级账户树
            List<String> accounts = ListUtils.toList(user.getParentList());

            //当前用户的回扣比率
            BigDecimal currentRebateRatio = user.getRebateRatio();

            //该次循环到的上级用户的直属下级的回扣比率
            BigDecimal cRatio = currentRebateRatio;

            //至少二级代理才会执行该循环，坐标每次减一表示不断给上级返点回扣
            BigDecimal agentRatio = new BigDecimal(0);
            for (int i = accounts.size() - 2; i >= 0; i--) {
                String pAccount = accounts.get(i);//该次循环到的上级用户账户
                User p = this.userDao.findByAccountMaster(pAccount);
                //该次循环到的上级用户的回扣比率
                BigDecimal paccRebateRatio = p.getRebateRatio();
                //该次循环到的上级用户的回扣比率 减去 直属下级的回扣比率
                BigDecimal calculationRebateRatio = paccRebateRatio.subtract(cRatio);
                //{返点关键回扣比率}：该次循环到的上级用户的回扣比率+{游戏回扣比率与奖金组回扣比率差}
                //BigDecimal pRatio = p.getRebateRatio().add(subRatio);
                //if (pRatio.compareTo(BigDecimal.ZERO) > 0) {//如果{返点关键回扣比率}比0大
                //{上级是否该获得回扣金额的关键回扣比率}：{返点关键回扣比率}-{用户计算关键回扣比率}
                //BigDecimal nowRatio = pRatio.subtract(userRatio);
                //if (nowRatio.compareTo(BigDecimal.ZERO) > 0) {//如果{上级是否该获得回扣金额的关键回扣比率}大于0则给其应得的回扣金额
                if (calculationRebateRatio.compareTo(BigDecimal.ZERO) > 0) {//如果该次循环到的上级用户的回扣比率 减去 当前用户的回扣比率
                    //该次循环到的上级用户所应获得的回扣金额
                    //BigDecimal ratio = bet.getAmount().multiply(nowRatio).divide(HUNDRED);
                    BigDecimal ratio = bet.getAmount().multiply(calculationRebateRatio).divide(SettlementService.HUNDRED);
                    agentRatio = agentRatio.add(ratio == null ? new BigDecimal(0) : ratio);
                }
            }
            bonusSimple.put("agentRatio", agentRatio);
        }
        return bonusSimple;
    }

    private Bet assemBet(Lottery lott, LotteryBase lb, String lotteryId, String playerId,
                         User user, String betContent, Integer randPriceInput) {
        Bet bet = new Bet();
        bet.setAccount(user.getAccount());
        bet.setBetCount(1);
        bet.setBonusType(0);
        bet.setContent(betContent);
        bet.setCreateTime(new Date());
        bet.setLastTime(new Date());
        bet.setId(IdBuilder.CreateId(lotteryId, "D"));
        bet.setLotteryId(lotteryId);
        bet.setLotteryName(lb.getTitle());
        bet.setPlayerId(playerId);

        bet.setPrice(randPriceInput);
        bet.setStatus(0);
        bet.setUnit(new BigDecimal(1));
        bet.setPlayerId(playerId);
        PlayerBase play = lb.getPlayer(bet.getPlayerId());
        bet.setTheoreticalBonus(play.getBonus().multiply(new BigDecimal(1)).multiply(new BigDecimal(1)).setScale(4, 1));

        BonusGroupDetails bGroupDetails = ThreadLocalCache.getGroupDetails(playerId, lotteryId, user.getBonusGroupId());
        BonusGroup bDecimal = bonusGroupService.find(user.getBonusGroupId());
        bet.setBonusRate(bGroupDetails.getBonusRatio().add(
                user.getRebateRatio().subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio()))));
        bet.setAmount(new BigDecimal(randPriceInput));
        bet.setPlayName(play.getFullTitle());
        bet.setUnit(new BigDecimal(1));
        bet.setIsTrace(0);
        bet.setGroupName(lott.getGroupName());
        return bet;
    }

    /**
     * 修改奖金池
     *
     * @param lotteryId
     * @param subAmount
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/update_bonus_pool"}, method = {RequestMethod.POST})
    public Long updateBonusPool(
            @RequestParam(value = "lotteryId") String lotteryId,
            @RequestParam(value = "subAmount") Long subAmount) {
        return bonusRiskRedisService.incrBonusPool(lotteryId, subAmount);
    }

    /**
     * 获取奖金池
     *
     * @param lotteryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/get_bonus_pool"}, method = {RequestMethod.GET})
    public Long getBonusPool(@RequestParam(value = "lotteryId") String lotteryId) {
        return bonusRiskRedisService.getBonusPool(lotteryId);
    }

    /**
     * 初始化奖金池
     *
     * @param lotteryId
     * @param subAmount
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/init_bonus_pool"}, method = {RequestMethod.POST})
    public Boolean setBonusPool(
            @RequestParam(value = "lotteryId") String lotteryId,
            @RequestParam(value = "subAmount") Long subAmount) {
        boolean result = RedisUtils.hset(RedisCons.BONUS_POOL, lotteryId, subAmount + "");
        return result;
    }

    /**
     * 获取总投注额
     *
     * @param lotteryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/get_bet_amount"}, method = {RequestMethod.GET})
    public String getBetAmount(
            @RequestParam(value = "lotteryId") String lotteryId) {
        String result = RedisUtils.hget(RedisCons.BET_AMOUNT_POOL, lotteryId);
        return result;
    }

    /**
     * 设置总投注额
     *
     * @param lotteryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/set_bet_amount"}, method = {RequestMethod.POST})
    public boolean setBetAmount(
            @RequestParam(value = "lotteryId") String lotteryId,
            @RequestParam(value = "subAmount") Long subAmount) {
        boolean result = RedisUtils.hset(RedisCons.BET_AMOUNT_POOL, lotteryId, subAmount + "");
        return result;
    }

}

package com.hs3.home.controller.gh.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hs3.cache.BonusRiskRedisService;
import com.hs3.commons.*;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.risk.BonusRiskDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.*;
import com.hs3.entity.users.User;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.ThreadLocalCache;
import com.hs3.lotts.open.INumberBuilder;
import com.hs3.service.lotts.BonusRiskService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.lotts.SettlementService;
import com.hs3.service.risk.BonusRiskConfigService;
import com.hs3.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * program: java-front
 * des:
 * author: Terra
 * create: 2018-07-31 15:12
 **/
@Service
public class TestBonusRiskService {
    private static final Logger logger = LoggerFactory.getLogger(TestBonusRiskService.class);

    @Autowired
    private BetDao betDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private BonusRiskRedisService bonusRiskRedisService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private BonusRiskDao bonusRiskDao;
    @Autowired
    private BonusRiskConfigService bonusRiskConfigService;

    public BonusRisk getLotterySeason(LotterySeason lotterySeason, INumberBuilder iNumberBuilder, String lotteryId, String seasonId, List<Bet> betList) {
        BonusRisk br = new BonusRisk();
        try {
            long startTime = System.currentTimeMillis();
            Lottery lottery = this.lotteryService.find(lotteryId);
            LotteryBase lottBase = LotteryFactory.getInstance(lottery.getGroupName());
//        logger.info(String.format("--> lotterySeason:%s, lottBase : %s, lotteryId: %s", JSON.toJSONString(lotterySeason), JSON.toJSONString(lottBase), lotteryId));
            String openNum = ListUtils.toString(lottBase.getSeasonOpen(lotterySeason).getNums());
            Map<BigDecimal, LotterySeason> mapGainNum = new HashMap<>();


            br.setIsChangeNum(Whether.no.getStatus());
            br.setCreateNumCount(0);
            br.setLotteryId(lotteryId);
            br.setSeasonId(seasonId);
            br.setInitNum(openNum);

            JSONObject flag = null;
            for (int i = 0; i < Constants.RISK_BONUS_CHANGE_COUNT; i++) {
                flag = riskCheckBonus(lottBase, openNum, lotteryId, seasonId, br, betList);
                if (!flag.getBoolean("flag")) {
                    String beforeNums = openNum;
                    lotterySeason = iNumberBuilder.create(lotteryId, seasonId, null, null);
                    mapGainNum.put(flag.getBigDecimal("gain"), lotterySeason);
                    openNum = ListUtils.toString(lottBase.getSeasonOpen(lotterySeason).getNums());
                    logger.warn(String.format("--> change nums , lotteryId : %s, sessionId : %s, beforeNums : %s, afterNums : %s", lotteryId, seasonId, beforeNums, openNum));
                    br.setCreateNumCount(i + 1);
                    br.setIsChangeNum(Whether.yes.getStatus());
                } else {
                    break;
                }
            }
            if (!flag.getBoolean("flag")) {
                TreeSet<BigDecimal> treeSet = new TreeSet<>(mapGainNum.keySet());
                lotterySeason = mapGainNum.get(treeSet.last());
                //风控拦截失败，奖池数据为负，奖池清0
                bonusRiskRedisService.setBonusPool(lotteryId, 0L);
            }
            br.setCreateTime(new Date());
            br.setEndNum(ListUtils.toString(lottBase.getSeasonOpen(lotterySeason).getNums()));
            if (mapGainNum.size() > 0) {
                logger.warn(String.format("--> 彩种 : %s, 期号 : %s, 换号 : %s", lotteryId, seasonId, JSON.toJSONString(mapGainNum)));
            }
            logger.info(String.format("--> lotteryId :%s, seasonId:%s, at last openNum : %s, \nlotterySeason: %s, spend time : %s", lotteryId, seasonId, openNum, JSON.toJSONString(lotterySeason), (System.currentTimeMillis() - startTime)));
        } catch (Exception e) {
            logger.error(String.format("--> getLotterySeason error,lotteryId:%s, seasonId:%s", lotteryId, seasonId), e);
        }
        return br;
    }


    /**
     * 风控开奖
     *
     * @param openNum   开奖号码
     * @param lotteryId 彩种id
     * @param seasonId  奖期id
     */
    public JSONObject riskCheckBonus(LotteryBase lb, String openNum, String lotteryId, String seasonId, BonusRisk br, List<Bet> betList) {
        JSONObject jsonObject = new JSONObject();
        boolean flag = true;
        jsonObject.put("flag", true);
        if (betList == null || betList.size() == 0) {
            return jsonObject;
        }
        jsonObject.put("betSize", betList.size());
        BigDecimal currTotalBetAmount = new BigDecimal(0);
        BigDecimal currTotalWin = new BigDecimal(0);
        BigDecimal currTotalUserRatio = new BigDecimal(0);
        BigDecimal currTotalAgentRatio = new BigDecimal(0);
//        while (betList != null && betList.size() > 0) {
        for (Bet bet : betList) {
            Map<String, BigDecimal> bonusSimple = calcBetAndAmount(lb, bet, openNum);
            currTotalBetAmount = currTotalBetAmount.add(bonusSimple.get("betAmount") == null ? new BigDecimal(0) : bonusSimple.get("betAmount"));
            currTotalWin = currTotalWin.add(bonusSimple.get("win") == null ? new BigDecimal(0) : bonusSimple.get("win"));
            currTotalUserRatio = currTotalUserRatio.add(bonusSimple.get("userRatio") == null ? new BigDecimal(0) : bonusSimple.get("userRatio"));
            currTotalAgentRatio = currTotalAgentRatio.add(bonusSimple.get("agentRatio") == null ? new BigDecimal(0) : bonusSimple.get("agentRatio"));
        }
        //计算当期盈利
        BigDecimal gain = currTotalBetAmount.subtract(currTotalWin).subtract(currTotalUserRatio).subtract(currTotalAgentRatio);
        Long bonusPool=0L;
        boolean firstBonusPoolIsFull = bonusRiskRedisService.getFirstBonusPoolFull(lotteryId);
        if (gain.compareTo(BigDecimal.ZERO) > 0) {
            if (!Constants.MATRIX) {
                bonusPool = bonusRiskRedisService.incrBonusPool(lotteryId, gain.multiply(new BigDecimal(10000)).longValue());
                if (!firstBonusPoolIsFull) {
                    bonusPool = setSecondRiskPool(lotteryId, bonusPool);
                }
                bonusRiskRedisService.incrBetAmountPool(lotteryId, currTotalBetAmount.longValue());
//                JSONObject jb = transCollectCount(lotteryId, bonusPool);
//                bonusPool = jb.getLong("bonusPool");
            } else {
                flag = false;
            }
        } else {
            bonusPool = bonusRiskRedisService.getBonusPool(lotteryId);
            if (gain.add(new BigDecimal(bonusPool).divide(new BigDecimal(10000))).compareTo(BigDecimal.ZERO) > 0) {
                if (!Constants.MATRIX) {
                    bonusPool = bonusRiskRedisService.incrBonusPool(lotteryId, gain.multiply(new BigDecimal(10000)).longValue());
                    if (!firstBonusPoolIsFull) {
                        bonusPool = setSecondRiskPool(lotteryId, bonusPool);
                    }
                    bonusRiskRedisService.incrBetAmountPool(lotteryId, currTotalBetAmount.longValue());
//                    JSONObject jb = transCollectCount(lotteryId, bonusPool);
//                    bonusPool = jb.getLong("bonusPool");
                } else {
                    flag = false;
                }

            } else {
                bonusPool = 0L;
                logger.warn(String.format("--> bonus pool isn't enough, change nums, lotteryId:%s, seasonId:%s", lotteryId, seasonId));
                flag = false;
            }
        }
        logger.info(String.format("--> riskCheckBonus result, lotteryId : %s, seasonId : %s, openNums: %s",
                lotteryId, seasonId, openNum));
        if (!flag) {
            jsonObject.put("gain", gain);
            jsonObject.put("openNum", openNum);
        }
        jsonObject.put("flag", flag);

        br.setValidBetAmount(currTotalBetAmount);
        br.setRebate(currTotalUserRatio.add(currTotalAgentRatio));
        if (br.getCreateNumCount() == 0) {
            br.setInitBonus(currTotalWin);
            br.setInitGains(gain);
        }
        br.setEndBonus(currTotalWin);
        br.setEndGains(gain);
        br.setBonusPoolLeft(new BigDecimal(bonusPool).divide(new BigDecimal(10000)));
        if (firstBonusPoolIsFull) {
            br.setBonusPoolLeft(new BigDecimal(0));
            br.setSecondBonusPoolLeft(new BigDecimal(bonusPool).divide(new BigDecimal(10000)));
        } else {
            firstBonusPoolIsFull = bonusRiskRedisService.getFirstBonusPoolFull(lotteryId);
            if (firstBonusPoolIsFull) {
                br.setBonusPoolLeft(new BigDecimal(0));
                br.setSecondBonusPoolLeft(new BigDecimal(bonusPool).divide(new BigDecimal(10000)));
            } else {
                br.setBonusPoolLeft(new BigDecimal(bonusPool).divide(new BigDecimal(10000)));
                br.setSecondBonusPoolLeft(new BigDecimal(0));
            }
        }
        return jsonObject;
    }


    //处理状态为0/6（未开奖）的投注记录
    public Map<String, BigDecimal> calcBetAndAmount(LotteryBase lb, Bet bet, String openNumsByParam) {
        Map<String, BigDecimal> bonusSimple = new HashMap<>();
        bonusSimple.put("betAmount", bet.getAmount());
        //获取所赢取的金额
        BigDecimal win = settlementService.getBetWin(lb, bet, openNumsByParam);
        bonusSimple.put("win", win);
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
                    logger.info(String.format("--> simple agentRatio :%s, betId:%s", agentRatio, bet.getId()));
                }
                cRatio = paccRebateRatio;
            }
            bonusSimple.put("agentRatio", agentRatio);
        }
        return bonusSimple;
    }

    private Long setSecondRiskPool(String lotteryId, Long currentBonusPool) {
        BonusRiskConfig brc = bonusRiskConfigService.getByLotteryId(lotteryId);
        logger.info("--> bonusRiskConfig : " + JSONObject.toJSONString(brc));
        if (brc != null) {
            BigDecimal revenueThreshold = brc.getRevenueRate().multiply(brc.getDayFlowWater());
            if (revenueThreshold.multiply(new BigDecimal(100)).compareTo(new BigDecimal(currentBonusPool)) < 0) {
                BigDecimal newBonusPool = new BigDecimal(currentBonusPool).subtract(revenueThreshold.multiply(new BigDecimal(100)));
                bonusRiskRedisService.setBonusPool(lotteryId, newBonusPool.longValue());
                bonusRiskRedisService.setFirstBonusPoolFull(lotteryId);
                return newBonusPool.longValue();
            }
        }
        return currentBonusPool;
    }
}

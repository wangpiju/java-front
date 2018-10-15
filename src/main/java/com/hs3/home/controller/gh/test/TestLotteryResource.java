package com.hs3.home.controller.gh.test;

import com.alibaba.fastjson.JSONObject;
import com.hs3.cache.BonusRiskRedisService;
import com.hs3.commons.BonusType;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.lotts.*;
import com.hs3.entity.users.User;
import com.hs3.home.constant.RedisConstant;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.ExcelUtils;
import com.hs3.lotts.*;
import com.hs3.lotts.open.INumberBuilder;
import com.hs3.lotts.open.NumberBuilderFactory;
import com.hs3.models.lotts.PlayerBonus;
import com.hs3.service.lotts.BonusGroupService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.lotts.PlayerService;
import com.hs3.service.lotts.SettlementService;
import com.hs3.service.user.UserService;
import com.hs3.utils.*;
import com.hs3.web.auth.Auth;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author jason.wang
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/test"})
public class TestLotteryResource extends HomeAction {

    //@Value("${excel.path}")
    private static String excelPath = "D://";
    @Autowired
    LotteryService lotteryService;
    @Autowired
    UserService userService;
    @Autowired
    BonusGroupService bonusGroupService;
    @Autowired
    PlayerService playerService;
    @Autowired
    private BonusRiskRedisService bonusRiskRedisService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private TestBonusRiskService testBonusRiskService;

    //50%
    private String[] betList = {"大", "小", "大", "小", "大", "小", "大", "小", "大", "小"};
    private String[] betList1 = {"单", "双", "单", "双", "单", "双", "单", "双", "单", "双"};
    //60%
    private String[] betList3 = {"大", "大", "大", "小", "大", "小", "大", "小", "大", "小"};
    private String[] betList4 = {"单", "单", "单", "双", "单", "双", "单", "双", "单", "双"};

    //60%
    private String[] betList5 = {"大", "大", "大", "大", "大", "小", "大", "小", "大", "小"};
    private String[] betList6 = {"单", "单", "单", "单", "单", "双", "单", "双", "单", "双"};

    private String[] bet = {"大", "小", "单", "双"};

    private String[] betAnd = {"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};

    private int allSameCounter = 0;
    private int killSameCounter = 0;

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/simulateRisk"}, method = {RequestMethod.GET})
    public void simulateRisk() {
        String lotteryId = "dfk3";
        // 取彩種基本資訊
        Lottery lott = this.lotteryService.find(lotteryId);
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        PlayerBase playerBase = null;

        User user = this.userService.findByAccount("jasontest");//14
        User user2 = this.userService.findByAccount("hongfa003");//12
        List<Integer> numberList = Arrays.asList(0, 1, 2, 4, 6, 8, 9, 10, 11, 14, 15);

        //一個月
        int days = 30;
        //一天期數
        int seasons = 1439;
        //一期注數
        int bets = 5;

        XSSFWorkbook wb = new XSSFWorkbook();
        List<List<String>> excelContent = new ArrayList<>();
        excelContent.add(Arrays.asList("期號", "整体流水", "整体上下级反点", "玩法id", "中奖金额", "初始号码", "初始损益", "补开号码", "补开后派奖", "补开后损益", "獎池剩馀金额", "第二獎池剩馀金额", "奖号获取次数", "是否停止開獎", "初始损益比例", "补开后损益比例"));

        XSSFSheet sheet = wb.createSheet();

        for (int j = 0; j < days * seasons; j++) {
            if (j % seasons == 0) {
                //清除獎池
                this.bonusRiskRedisService.clearBonusPool(lotteryId);
                //删除奖池满的标志
                this.bonusRiskRedisService.delFirstBonusPoolFull();
            }

            XSSFRow row;
            //開獎號碼
            Random rand = new Random();
            List<Integer> last = autoCreate(rand, String.valueOf(j));
            //每期流水
            Long water = 0L;
            //每期初始派彩
            BigDecimal initailPayoff = BigDecimal.valueOf(0);
            //每期返點差
            BigDecimal rebatePoint = BigDecimal.valueOf(0);

            List<Bet> betList = new ArrayList<>();

            for (int k = 0; k < bets; k++) {
                //大小單雙 90% 和值10%
                int choose = NumUtils.getRandom(rand, 1, 10);
                String betContent;
                //和值
                if (1 == choose) {
                    playerBase = lottBase.getPlayer("k3_star3_and");
                    choose = NumUtils.getRandom(rand, 0, 13);
                    betContent = betAnd[choose];
                }
                //大小單雙
                else {
                    playerBase = lottBase.getPlayer("k3_star3_big_odd");
                    choose = NumUtils.getRandom(rand, 0, 3);
                    betContent = bet[choose];
                }

                Integer randPrice = NumUtils.getRandom(rand, 10, 1600);
                Integer randUser = NumUtils.getRandom(rand, 0, 1);
                BigDecimal win;

                if(randUser == 0)
                     win = getBetWin(lott, lottBase, "dfk3", playerBase.getId(), last, user, betContent, randPrice, rebatePoint);
                else
                     win = getBetWin(lott, lottBase, "dfk3", playerBase.getId(), last, user2, betContent, randPrice, rebatePoint);

                //整體流水
                water += randPrice;
                //整體初始派彩
                initailPayoff = initailPayoff.add(win);
                Bet bet;

                if(randUser == 0)
                    bet = assemBet(lott, lottBase, lotteryId, playerBase.getId(), user, betContent, randPrice);
                else
                    bet = assemBet(lott, lottBase, lotteryId, playerBase.getId(), user2, betContent, randPrice);

                betList.add(bet);
            }

            row = sheet.createRow(j);

            /*---------------------*/
            INumberBuilder iNumberBuilder = NumberBuilderFactory.getInstance("k3");
            LotterySeason lotterySeason = iNumberBuilder.create(lotteryId, j + "", null, null);
            BonusRisk br = testBonusRiskService.getLotterySeason(lotterySeason, iNumberBuilder, lotteryId, j + "", betList);
            /*---------------------*/
            try {
                //流水
                BigDecimal validAmount = br.getValidBetAmount();
                //初始損益
                BigDecimal initailGain = br.getInitGains();
                //補開損益
                BigDecimal afterGain = br.getEndGains();
                //初始損益比例
                BigDecimal originRate = initailGain.divide(validAmount, RoundingMode.DOWN);
                //補開後損益比例
                BigDecimal afterRate=afterGain.divide(validAmount, RoundingMode.DOWN);
                excelContent.add(Arrays.asList(String.valueOf(j), br.getValidBetAmount() + "", String.valueOf(br.getRebate()), playerBase.getId()
                        , String.valueOf(br.getInitBonus()), br.getInitNum(), String.valueOf(br.getInitGains()),
                        br.getEndNum(), br.getEndBonus() + "", String.valueOf(br.getEndGains()), String.valueOf(br.getBonusPoolLeft()), br.getSecondBonusPoolLeft() + "", String.valueOf(br.getCreateNumCount()), String.valueOf(br.getStopOpen())
                ,String.valueOf(originRate), String.valueOf(afterRate)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int z = 0; z < excelContent.get(j).size(); z++) {
                if (numberList.contains(z)){
                    try{
                        Double result = Double.valueOf(excelContent.get(j).get(z));
                        ExcelUtils.createCellX(row, z, result, "number");
                    }
                    catch (Exception e){
                        ExcelUtils.createCellX(row, z, excelContent.get(j).get(z), null);
                    }
                }
                else
                    ExcelUtils.createCellX(row, z, excelContent.get(j).get(z), null);
            }
//                System.out.println("期號: " + j + ", 整體流水: " + water + ", 整體返點差: " + rebatePoint + ", 玩法: " + playerBase.getId()
//                + ", 初始中獎金額: "+ initailPayoff + ", 初始開獎號碼: " + ListUtils.toString(last, ",") +
//                        ", 初始損益: " +(water - initailPayoff.longValue()) + ", 補開號碼: " +ListUtils.toString(finalOpen, ",") + ", 補開後派彩:" + afterRiskWin.longValue()
//                +", 補開後損益: " +finalRiskWin + ", 補開次數: " + reOpenCount + ", 獎池剩餘金額: " +  bonusRiskRedisService.getBonusPool(lotteryId) );
        }
        //加總計算
        //////////////
        Long totalWater = 0L;
        Long totalWin = 0L;
        Long totalGain = 0L;
        Long totalAfterWin = 0L;
        Long totalAfterGain = 0L;

        for(List<String> list: excelContent){
            try {
                totalWater += Long.valueOf(list.get(1));
                totalWin += Long.valueOf(list.get(4));
                totalGain += Long.valueOf(list.get(6));
                totalAfterWin += Long.valueOf(list.get(8));
                totalAfterGain += Long.valueOf(list.get(9));
            }
            catch (Exception e){

            }
        }

        excelContent.add(Arrays.asList("", String.valueOf(totalWater), "", ""
                , String.valueOf(totalWin),"", String.valueOf(totalGain), "",
                String.valueOf(totalAfterWin) , String.valueOf(totalAfterGain), "", "", "", "","", ""));

        XSSFRow row = sheet.createRow(days * seasons);

        Double result = Double.valueOf(excelContent.get(days * seasons).get(1));
        ExcelUtils.createCellX(row, 1, result, "number");
        Double result4 = Double.valueOf(excelContent.get(days * seasons).get(4));
        ExcelUtils.createCellX(row, 4, result4, "number");
        Double result6 = Double.valueOf(excelContent.get(days * seasons).get(6));
        ExcelUtils.createCellX(row, 6, result6, "number");
        Double result8 = Double.valueOf(excelContent.get(days * seasons).get(8));
        ExcelUtils.createCellX(row, 8, result8, "number");
        Double result9 = Double.valueOf(excelContent.get(days * seasons).get(9));
        ExcelUtils.createCellX(row, 9, result9, "number");
        //////////////
        System.out.println("開始產生檔案: " + playerBase.getFullTitle() + ", " + playerBase.getId());

        try {
            ExcelUtils.createXFile(wb, excelPath, lottBase.getTitle() + "_" + playerBase.getFullTitle() + "_3.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("結束: " + playerBase.getFullTitle() + ", " + playerBase.getId());

    }

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/testBetNew"}, method = {RequestMethod.GET})
    public void testBetNew(String lotteryId, String playerId) {
        // 取彩種基本資訊
        Lottery lott = this.lotteryService.find(lotteryId);
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        // 開獎五十萬次
        try {
            PlayerBase playerBase = lottBase.getPlayer(playerId);
            System.out.println("開始: " + playerBase.getFullTitle() + ", " + playerBase.getId());
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFCellStyle style = ExcelUtils.getTitleStyleX(wb);
            List<List<String>> excelContent = new ArrayList<>();
            excelContent.add(Arrays.asList("方案号", "彩种id", "玩法id", "下注内容", "注单金额", "派彩", "开奖号码", "开奖结果"));
            XSSFSheet sheet = wb.createSheet(lottBase.getTitle());

            for (int i = 0; i < 300000; i++) {
                //System.out.println(i);
                XSSFRow row = null;
                Random rand = new Random();
                Integer randIndex = NumUtils.getRandom(rand, 0, 3);
                String betContent = bet[randIndex];
                List<Integer> last = autoCreate(rand, String.valueOf(i));
                // 派彩
                /*
                    隨機投注 2~20000
                */
                Random random = new Random();
                Integer randPrice = NumUtils.getRandom(random, 2, 20000);

                BigDecimal win = getBetWin(lott, lottBase, lotteryId, playerBase.getId(), last, user, betContent, randPrice, BigDecimal.valueOf(0));
                int isWin = 0;
                if (win.compareTo(new BigDecimal(0)) > 0)
                    isWin = 1;
//                excelContent.add(Arrays.asList(String.valueOf(i), lotteryId.toString(),
//                        playerBase.getId().toString(), betContent, String.valueOf(randPrice), String.valueOf(win),
//                        ListUtils.toString(last, ","), String.valueOf(isWin)));
//                row = sheet.createRow(i);
//                for (int j = 0; j < excelContent.get(i).size(); j++) {
//                   // ExcelUtils.createCellX(row, j, excelContent.get(i).get(j), style);
//                }
                if (i % 1000 == 0) {
                    System.out.println("killSameCounter: " + killSameCounter);
                    System.out.println("allSameCounter: " + allSameCounter);
                }
            }
            System.out.println("killSameCounter: " + killSameCounter);
            System.out.println("allSameCounter: " + allSameCounter);
            System.out.println("開始產生檔案: " + playerBase.getFullTitle() + ", " + playerBase.getId());
            //ExcelUtils.createXFile(wb, excelPath, lottBase.getTitle() + "_" + playerBase.getFullTitle() + ".xlsx");
            System.out.println("結束: " + playerBase.getFullTitle() + ", " + playerBase.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/testPK10Bet"}, method = {RequestMethod.GET})
    public void testPK10Bet(String lotteryId, String betContent, String playerId) {
        if (null == lotteryId)
            lotteryId = "pk10";
        // 取彩種基本資訊
        Lottery lott = this.lotteryService.find(lotteryId);
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        User user = this.userService.findByAccount(((User) getLogin()).getAccount());

        // 開獎十萬次
        try {
            PlayerBase playerBase = lottBase.getPlayer(playerId);

            if (null == betContent) {
                betContent = playerBase.getBasicBet();
            }

            String[] betContents = betContent.split(",");
            for (String betStr : betContents) {
                System.out.println("開始: " + playerBase.getFullTitle() + ", " + playerBase.getId() + "," + betStr);
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFCellStyle style = ExcelUtils.getTitleStyle(wb);
                HSSFRow row = null;
                List<List<String>> excelContent = new ArrayList<>();
                excelContent.add(Arrays.asList("方案号", "彩种id", "玩法id", "下注内容", "注单金额", "派彩", "开奖号码", "开奖结果"));
                // sheet name
                HSSFSheet sheet = wb.createSheet(lottBase.getTitle());
                HSSFSheet sheet2 = wb.createSheet(lottBase.getTitle() + "_2");

                try {
                    // 驗證注數是不是一注 不是的話更改投注內容
                    System.out.println("count: " + playerBase.getCount(betContent));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                for (int i = 0; i < 100000; i++) {
                    betStr = getBetContent();
                    List<Integer> last = new ArrayList<>();
                    if (lotteryId.equals("pk10"))
                        last = openNum(10, lotteryId);
                    else if (lotteryId.equals("dfk3"))
                        last = openNum(3, lotteryId);

                    // 派彩
                    BigDecimal win = getBetWin(lott, lottBase, lotteryId, playerBase.getId(), last, user, betStr, null, BigDecimal.valueOf(0));
                    int isWin = 0;
                    if (win.compareTo(new BigDecimal(0)) > 0)
                        isWin = 1;
                    if (i > 60000) {
                        row = sheet2.createRow(i - 60000);
                        excelContent.add(Arrays.asList(String.valueOf(i), lotteryId.toString(),
                                playerBase.getId().toString(), betStr, String.valueOf(100), String.valueOf(win),
                                ListUtils.toString(last, ","), String.valueOf(isWin)));

                        for (int j = 0; j < excelContent.get(i).size(); j++) {
                            ExcelUtils.createCell(row, j, excelContent.get(i).get(j), style);
                        }
                    } else {
                        row = sheet.createRow(i);

                        excelContent.add(Arrays.asList(String.valueOf(i), lotteryId.toString(),
                                playerBase.getId().toString(), betStr, String.valueOf(100), String.valueOf(win),
                                ListUtils.toString(last, ","), String.valueOf(isWin)));

                        for (int j = 0; j < excelContent.get(i).size(); j++) {
                            ExcelUtils.createCell(row, j, excelContent.get(i).get(j), style);
                        }
                    }
                }
                ExcelUtils.createFile(wb, excelPath, lottBase.getTitle() + "_" + playerBase.getFullTitle() + "_" + betStr + ".xls");
                System.out.println("結束: " + playerBase.getFullTitle() + ", " + playerBase.getId() + "," + betStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/testBet"}, method = {RequestMethod.GET})
    public void testBet(String lotteryId, String betContent, String playerId) {
        if (null == lotteryId)
            lotteryId = "dfk3";

        // 取彩種基本資訊
        Lottery lott = this.lotteryService.find(lotteryId);
        LotteryBase lottBase = LotteryFactory.getInstance(lott.getGroupName());
        User user = this.userService.findByAccount(((User) getLogin()).getAccount());

        // 開獎十萬次
        try {
            List<PlayerBonus> playerBonus = this.playerService.listFullByLotteryIdAndGroupId(lotteryId,
                    ((User) getLogin()).getBonusGroupId());
            int size = playerBonus.size();

            for (int i = size - 1; i >= 0; i--) {
                PlayerBonus pb = (PlayerBonus) playerBonus.get(i);
                if ((pb.getMobileStatus().intValue() != 0) || (pb.getSaleStatus().intValue() != 0)) {
                    lottBase.removePlayer(pb.getId());
                }
            }
            //如果有傳玩法id
            if (null != playerId) {
                PlayerBase playerBase = lottBase.getPlayer(playerId);
                System.out.println("開始: " + playerBase.getFullTitle() + ", " + playerBase.getId());
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFCellStyle style = ExcelUtils.getTitleStyle(wb);
                HSSFRow row = null;
                List<List<String>> excelContent = new ArrayList<>();
                excelContent.add(Arrays.asList("方案号", "彩种id", "玩法id", "下注内容", "注单金额", "派彩", "开奖号码", "开奖结果"));
                // sheet name
                HSSFSheet sheet = wb.createSheet(lottBase.getTitle());
                HSSFSheet sheet2 = wb.createSheet(lottBase.getTitle() + "_2");
                for (int i = 0; i < 100000; i++) {
                    List<Integer> last = new ArrayList<>();
                    // 產生開獎號碼
                    if (lotteryId.contains("k3"))
                        last = autoCreate(new Random(), null);
                    else if (lotteryId.contains("ssc"))
                        last = openNum(5, lotteryId);
                    else if (lotteryId.contains("pk10"))
                        last = openNum(10, lotteryId);

                    // 派彩
                    BigDecimal win = getBetWin(lott, lottBase, lotteryId, playerBase.getId(), last, user, betContent, null, BigDecimal.valueOf(0));
                    int isWin = 0;
                    if (win.compareTo(new BigDecimal(0)) > 0)
                        isWin = 1;
                    if (i > 60000) {
                        row = sheet2.createRow(i - 60000);
                        excelContent.add(Arrays.asList(String.valueOf(i), lotteryId.toString(),
                                playerBase.getId().toString(), betContent, String.valueOf(100), String.valueOf(win),
                                ListUtils.toString(last, ","), String.valueOf(isWin)));

                        for (int j = 0; j < excelContent.get(i).size(); j++) {
                            ExcelUtils.createCell(row, j, excelContent.get(i).get(j), style);
                        }
                    } else {
                        row = sheet.createRow(i);

                        excelContent.add(Arrays.asList(String.valueOf(i), lotteryId.toString(),
                                playerBase.getId().toString(), betContent, String.valueOf(100), String.valueOf(win),
                                ListUtils.toString(last, ","), String.valueOf(isWin)));

                        for (int j = 0; j < excelContent.get(i).size(); j++) {
                            ExcelUtils.createCell(row, j, excelContent.get(i).get(j), style);
                        }
                    }

                }
                ExcelUtils.createFile(wb, excelPath, lottBase.getTitle() + "_" + playerBase.getFullTitle() + "_" + betContent + ".xls");
            } else {
                for (PlayerBase playerBase : lottBase.getPlayers()) {
                    System.out.println("開始: " + playerBase.getFullTitle() + ", " + playerBase.getId());
                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFCellStyle style = ExcelUtils.getTitleStyle(wb);
                    HSSFRow row = null;
                    List<List<String>> excelContent = new ArrayList<>();
                    excelContent.add(Arrays.asList("方案号", "彩种id", "玩法id", "下注内容", "注单金额", "派彩", "开奖号码", "开奖结果"));
                    // sheet name
                    HSSFSheet sheet = wb.createSheet(lottBase.getTitle());
                    HSSFSheet sheet2 = wb.createSheet(lottBase.getTitle() + "_2");
                    try {
                        if (lotteryId.contains("k3")) {
                            if (null == betContent || !betContent.equals(""))
                                betContent = playerBase.getNumView()[0].getNums().get(0).toString();
                        } else if (lotteryId.contains("ssc") || lotteryId.contains("pk10")) {
                            if (lotteryId.contains("ssc"))
                                for (int i = 0; i < playerBase.getNumView().length; i++) {
                                    NumberView numberView = playerBase.getNumView()[i];
                                    if (i == 0)
                                        betContent = numberView.getNums().get(0).toString();
                                    else {
                                        for (int j = 0; j < numberView.getNums().size(); j++) {
                                            if (!betContent.contains(numberView.getNums().get(j).toString())) {
                                                betContent += "," + numberView.getNums().get(j).toString();
                                                break;
                                            }
                                        }
                                    }
                                }
                            else if (lotteryId.contains("pk10")) {
                                if (null != playerBase.getNumView()[0].getTitle())
                                    betContent = "[" + playerBase.getNumView()[0].getTitle() + "]"
                                            + playerBase.getNumView()[0].getNums().get(0).toString();
                                else
                                    betContent = playerBase.getNumView()[0].getNums().get(0).toString();
                            }

                            // ssc_star4_front
                            if (playerBase.getId().equals("ssc_star4_front")) {
                                betContent += ",-";
                            }

                            // ssc_star4_last
                            if (playerBase.getId().equals("ssc_star4_last")) {
                                betContent = "-," + betContent;
                            }

                            // ssc_star3_mid
                            if (playerBase.getId().equals("ssc_star3_mid")) {
                                betContent = "-," + betContent + ",-";
                            }

                            // ssc_star3_front
                            if (playerBase.getId().equals("ssc_star3_front")) {
                                betContent = betContent + ",-,-";
                            }

                            // ssc_star3_last
                            if (playerBase.getId().equals("ssc_star3_last")) {
                                betContent = "-,-," + betContent;
                            }

                            // ssc_star2_last
                            if (playerBase.getId().equals("ssc_star2_last")) {
                                betContent = "-,-,-," + betContent;
                            }
                            // ssc_star2_front
                            if (playerBase.getId().equals("ssc_star2_front")) {
                                betContent = betContent + ",-,-,-";
                            }

                            // 驗證注數是不是一注 不是的話更改投注內容
                            System.out.println("count: " + playerBase.getCount(betContent));
                            while (playerBase.getCount(betContent) <= 0) {
                                for (int i = 0; i < playerBase.getNumView().length; i++) {
                                    for (NumberView numberView : playerBase.getNumView()) {
                                        for (int j = 0; j < numberView.getNums().size(); j++) {
                                            List list = numberView.getNums();
                                            if (!betContent.contains(list.get(j).toString())) {
                                                betContent += list.get(j).toString();
                                                if (playerBase.getCount(betContent) <= 0)
                                                    betContent = betContent.split(",")[0] + list.get(j).toString() + ","
                                                            + betContent.split(",")[1];
                                                break;
                                            }
                                        }

                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                    for (int i = 0; i < 200000; i++) {
                        List<Integer> last = new ArrayList<>();
                        // 產生開獎號碼
                        if (lotteryId.contains("k3"))
                            last = autoCreate(new Random(), null);
                        else if (lotteryId.contains("ssc"))
                            last = openNum(5, lotteryId);
                        else if (lotteryId.contains("pk10"))
                            last = openNum(10, lotteryId);

                        // 派彩
                        BigDecimal win = getBetWin(lott, lottBase, lotteryId, playerBase.getId(), last, user,
                                betContent, null, BigDecimal.valueOf(0));
                        int isWin = 0;
                        if (win.compareTo(new BigDecimal(0)) > 0)
                            isWin = 1;
                        if (i > 60000) {
                            row = sheet2.createRow(i - 60000);
                            excelContent.add(Arrays.asList(String.valueOf(i), lotteryId.toString(),
                                    playerBase.getId().toString(), betContent, String.valueOf(100), String.valueOf(win),
                                    ListUtils.toString(last, ","), String.valueOf(isWin)));

                            for (int j = 0; j < excelContent.get(i).size(); j++) {
                                ExcelUtils.createCell(row, j, excelContent.get(i).get(j), style);
                            }
                        } else {
                            row = sheet.createRow(i);

                            excelContent.add(Arrays.asList(String.valueOf(i), lotteryId.toString(),
                                    playerBase.getId().toString(), betContent, String.valueOf(100), String.valueOf(win),
                                    ListUtils.toString(last, ","), String.valueOf(isWin)));

                            for (int j = 0; j < excelContent.get(i).size(); j++) {
                                ExcelUtils.createCell(row, j, excelContent.get(i).get(j), style);
                            }
                        }

                    }
                    ExcelUtils.createFile(wb, excelPath, lottBase.getTitle() + "_" + playerBase.getFullTitle() + ".xls");
                    System.out.println("結束: " + playerBase.getFullTitle() + ", " + playerBase.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getBetContent() {
        Random ran = new Random();
        Integer randomIndex = NumUtils.getRandom(ran, 0, 9);
        return this.betList[randomIndex];
    }

    // 派彩
    private BigDecimal getBetWin(Lottery lott, LotteryBase lb, String lotteryId, String playerId, List<Integer> openNum,
                                 User user, String betContent, Integer randPriceInput, BigDecimal rebatePoint) {
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
        bet.setStatus(Integer.valueOf(0));
        bet.setUnit(new BigDecimal(1));
        bet.setPlayerId(playerId);
        PlayerBase play = lb.getPlayer(bet.getPlayerId());
        bet.setTheoreticalBonus(play.getBonus().multiply(new BigDecimal(1)).multiply(new BigDecimal(1)).setScale(4, 1));

        BonusGroupDetails bGroupDetails = ThreadLocalCache.getGroupDetails(playerId, lotteryId, user.getBonusGroupId());
        BonusGroup bDecimal = this.bonusGroupService.find(user.getBonusGroupId());
        bet.setBonusRate(bGroupDetails.getBonusRatio().add(
                user.getRebateRatio().subtract(bDecimal.getRebateRatio().subtract(bGroupDetails.getRebateRatio()))));
        //返點差累積
        rebatePoint = rebatePoint.add(user.getRebateRatio().subtract(bDecimal.getRebateRatio()));
        bet.setAmount(new BigDecimal(randPriceInput));
        bet.setPlayName(play.getFullTitle());
        bet.setUnit(new BigDecimal(1));
        bet.setIsTrace(0);
        bet.setGroupName(lott.getGroupName());
        BigDecimal win = new BigDecimal(0);
        try {
            win = play.getWin(bet.getContent(), openNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (win.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal price = bet.getUnit().multiply(new BigDecimal(bet.getPrice().intValue()));
            BigDecimal HUNDRED = new BigDecimal("100");
            BigDecimal ratio = bet.getBonusRate().divide(HUNDRED, 4, 1);

            // 派彩改為小數點下兩位 除了快三大小單雙是三位
            if (null != lb.getGroupId() && null != play && lb.getGroupId().trim().equals("k3")
                    && play.getId().trim().equals("k3_star3_big_odd")) {
                win = win.multiply(ratio).setScale(3, RoundingMode.DOWN).multiply(price);
            } else
                win = win.multiply(ratio).setScale(2, RoundingMode.DOWN).multiply(price);
        }
        return win;
    }

    // 開獎
    private List<Integer> openNum(Integer stars, String lotteryId) {
        List<Integer> last = new ArrayList<>();
        Random ran = new Random();
        for (int i = 0; i < stars; i++) {
            if (lotteryId.contains("k3")) {
                last.add(NumUtils.getRandom(ran, 1, 6));
            } else if (lotteryId.contains("ssc"))
                last.add(NumUtils.getRandom(ran, 0, 9));
            else if (lotteryId.contains("pk10")) {
                last = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(10), Integer.valueOf(2),
                        Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(9), Integer.valueOf(1),
                        Integer.valueOf(4), Integer.valueOf(7), Integer.valueOf(3), Integer.valueOf(8)}));
                int times = NumUtils.getRandom(ran, 50, 100);
                for (int j = 0; j < 10 + times; j++) {
                    int n = NumUtils.getRandom(ran, 0, last.size() - 1);
                    last.add(0, (Integer) last.remove(n));
                }
            }
        }

        return last;
    }


    public List<Integer> autoCreate(Random ran, String seasonId) {

        List<Integer> last = new ArrayList();
        last.add(NumUtils.getRandom(ran, 1, 6));
        last.add(NumUtils.getRandom(ran, 1, 6));
        last.add(NumUtils.getRandom(ran, 1, 6));

        try {
            if (CollectionUtils.checkAllSame(last)) {
                allSameCounter++;
                //防連開
                if (null != RedisUtils.get(RedisConstant.K3_LAST_ALLSAME)) {
                    if (null != RedisUtils.get(RedisConstant.K3_ALLSAME_SEASONID) && Integer.valueOf(seasonId) - (Integer.valueOf(RedisUtils.get(RedisConstant.K3_ALLSAME_SEASONID))) == 1) {
                        ran = new Random();
                        last.clear();
                        last.add(NumUtils.getRandom(ran, 1, 6));
                        last.add(NumUtils.getRandom(ran, 1, 6));
                        last.add(NumUtils.getRandom(ran, 1, 6));
                    }
                    RedisUtils.del(RedisConstant.K3_LAST_ALLSAME);
                } else {
                    RedisUtils.set(RedisConstant.K3_LAST_ALLSAME, "true");
                }
                //防一小時內超過三次
                boolean needReAutoCreate = false;
                if (null != RedisUtils.get(RedisConstant.K3_ALLSAME_COUNTER)) {
                    Integer count = Integer.valueOf(RedisUtils.get(RedisConstant.K3_ALLSAME_COUNTER));
                    if (count >= 3 && Integer.valueOf(seasonId) - (Integer.valueOf(RedisUtils.get(RedisConstant.K3_ALLSAME_SEASONID))) <= 60) {
                        needReAutoCreate = true;
                    } else if (count <= 2 && Integer.valueOf(seasonId) - Integer.valueOf(RedisUtils.get(RedisConstant.K3_ALLSAME_SEASONID)) <= 60) {
                        RedisUtils.incr(RedisConstant.K3_ALLSAME_COUNTER);
                    } else if (Integer.valueOf(seasonId) - (Integer.valueOf(RedisUtils.get(RedisConstant.K3_ALLSAME_SEASONID))) > 60) {
                        RedisUtils.del(RedisConstant.K3_ALLSAME_SEASONID);
                        RedisUtils.del(RedisConstant.K3_ALLSAME_COUNTER);
                    }
                } else {
                    RedisUtils.incr(RedisConstant.K3_ALLSAME_COUNTER);
                    RedisUtils.set(RedisConstant.K3_ALLSAME_SEASONID, seasonId);
                }

                if (needReAutoCreate) {
                    ran = new Random();
                    last.clear();
                    last.add(NumUtils.getRandom(ran, 1, 6));
                    last.add(NumUtils.getRandom(ran, 1, 6));
                    last.add(NumUtils.getRandom(ran, 1, 6));
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        if (CollectionUtils.checkAllSame(last)) {
            killSameCounter++;
        }

        return last;
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
        jsonObject.put("flag", true);
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
        } else {
            bonusPool = bonusRiskRedisService.getBonusPool(lotteryId);
            if (gain.add(new BigDecimal(bonusPool)).compareTo(BigDecimal.ZERO) > 0) {
                bonusPool = bonusRiskRedisService.incrBonusPool(lotteryId, gain.longValue());
            } else {
                //logger.warn(String.format("--> bonus pool isn't enough, change nums, lotteryId:%s, seasonId:%s", lotteryId, seasonId));
                flag = false;
            }
        }
        if (!flag) {
            jsonObject.put("gain", gain);
            jsonObject.put("openNum", openNum);
        }
        jsonObject.put("flag", flag);
        // System.out.println("--> 开奖耗时 : " + (System.currentTimeMillis() - startTime));
        //logger.info(String.format("--> riskCheckBonus result, lotteryId : %s, seasonId : %s, openNums: %s, betCount : %s, betAmount : %s, win:%s, userRatio : %s, agentRatio : %s, gain : %s, bonusPool: %s",
        //lotteryId, seasonId, openNum, betList.size(), currTotalBetAmount, currTotalWin, currTotalUserRatio, currTotalAgentRatio, gain, bonusPool));
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


}

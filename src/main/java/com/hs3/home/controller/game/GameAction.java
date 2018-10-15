package com.hs3.home.controller.game;

import com.hs3.db.Page;
import com.hs3.entity.lotts.AccountChangeType;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.Trace;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.PlayerBase;
import com.hs3.models.CommonModel;
import com.hs3.models.Jsoner;
import com.hs3.models.PageData;
import com.hs3.models.lotts.TraceModel;
import com.hs3.service.lotts.*;
import com.hs3.service.user.UserService;
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
@RequestMapping({"/game"})
public class GameAction
        extends HomeAction {
    private static final int DEFAULT_LIMIT = 20;
    @Autowired
    private BetService betService;
    @Autowired
    private TraceService traceService;
    @Autowired
    private AccountChangeTypeService accountChangeTypeService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private UserService userService;
    @Autowired
    private LotterySaleTimeService lotterySaleTimeService;

    @RequestMapping(value = {"/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object index(String tabId) {
        ModelAndView mv = getViewWithHeadModel("/game/index");
        addWebDefaultTime(mv);
        List<AccountChangeType> accountChangeType = this.accountChangeTypeService.listByType(Integer.valueOf(0));
        if (((User) getLogin()).getUserType().intValue() == 1) {
            mv.addObject("userPower", Integer.valueOf(1));
        } else {
            mv.addObject("userPower", Integer.valueOf(0));
        }
        mv.addObject("accountChangeTypeJson", accountChangeType);
        mv.addObject("account", ((User) getLogin()).getAccount());

        if ((StrUtils.hasEmpty(new Object[]{tabId})) || ("betList".equals(tabId)) || ("betInList".equals(tabId)) || ("trace".equals(tabId))) {
            mv.addObject("curaccount", null);
        } else {
            mv.addObject("curaccount", tabId);
        }
        mv.addObject("tabId", tabId);
        return mv;
    }


    @ResponseBody
    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object list(String seasonId, Integer status, String lotteryId, String startTime, String endTime) {
        String account = ((User) getLogin()).getAccount();
        if (isMobile()) {
            List<Bet> list = this.betService.listByLatest(20, account);
            return Jsoner.success(new PageData(20, list));
        }
        CommonModel m = new CommonModel();

        m.setStatus(status);
        m.setAccount(account);
        m.setLowerLevel(false);
        m.setSeasonId(seasonId);
        m.setLotteryId(lotteryId);
        m.setStartTime(startTime);
        m.setEndTime(endTime);
        Page p = getPageWithParams();
        List<Bet> list = this.betService.list(p, m);
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }


    @ResponseBody
    @RequestMapping(value = {"/teamList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object teamList(String lotteryId, Integer status, String account, String startTime, String endTime) {
        String curAccount = null;
        if (StrUtils.hasEmpty(new Object[]{account})) {
            curAccount = ((User) getLogin()).getAccount();
        } else {
            User u = this.userService.findIsDown(((User) getLogin()).getAccount(), account);
            if (u == null) {
                return Jsoner.error("用户不存在！");
            }
            curAccount = account;
        }

        if (isMobile()) {
            List<Bet> list = this.betService.listByLatest(20, account);
            return Jsoner.success(new PageData(20, list));
        }
        CommonModel m = new CommonModel();

        m.setStatus(status);
        m.setAccount(curAccount);
        m.setLowerLevel(true);
        m.setSeasonId(null);
        m.setLotteryId(lotteryId);
        m.setStartTime(startTime);
        m.setEndTime(endTime);
        Page p = getPageWithParams();
        List<Bet> list = this.betService.list(p, m);
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }


    @ResponseBody
    @RequestMapping(value = {"/traceList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object traceList(Integer status, String lotteryId, String playerId, String startTime, String endTime) {
        TraceModel m = new TraceModel();
        String account = ((User) getLogin()).getAccount();

        m.setLowerLevel(false);
        m.setAccount(account);
        m.setStatus(status);
        m.setLotteryId(lotteryId);
        m.setStartTime(startTime);
        m.setEndTime(endTime);
        m.setPlayerId(playerId);
        Page p = getPageWithParams();
        List<Trace> list = this.traceService.list(p, m);
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }


    @ResponseBody
    @RequestMapping(value = {"/summaryList"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object summaryList(String traceId) {
        Page p = getPageWithParams();
        List<Bet> list = this.betService.summaryList(p, traceId);
        return new PageData(p.getRowCount(), list);
    }


    @ResponseBody
    @RequestMapping(value = {"/findTraceById"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object findTraceById(String id) {
        Trace trace = this.traceService.find(id);
        return trace;
    }


    @ResponseBody
    @RequestMapping(value = {"/ajaxSelect"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object ajaxSelect(String lotteryId) {
        List<String> seasonList = this.lotterySaleTimeService.listSeasonId(lotteryId, Integer.valueOf(20));
        Map<String, String> map = new HashMap();
        if ((lotteryId != null) && (!"".equals(lotteryId))) {
            Lottery lottery = this.lotteryService.getGroupNameByLottery(lotteryId);
            LotteryBase base = LotteryFactory.getInstance(lottery.getGroupName());

            Collection<PlayerBase> playerBase = base.getPlayers();
            Iterator<PlayerBase> it = playerBase.iterator();
            while (it.hasNext()) {
                PlayerBase str = (PlayerBase) it.next();
                String id = str.getId();
                String name = str.getFullTitle();
                map.put(id, name);
            }
        }
        Map<String, Object> maps = new HashMap();
        maps.put("playerMap", map);
        maps.put("seasonList", seasonList);
        return Jsoner.success(maps);
    }


    @ResponseBody
    @RequestMapping(value = {"/ajaxGetBet"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object ajaxGetBet(String id) {
        Bet bet = this.betService.find(id);
        return bet;
    }

    @ResponseBody
    @RequestMapping(value = {"/contentDetails"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object contentDetails(String id, Integer n) {
        return Jsoner.success(this.betService.findContentNext(id, n));
    }

    @RequestMapping({"/gameRecordList"})
    public Object gameRecordList(Integer start, Integer limit) {
        ModelAndView modelAndView = getViewWithHeadModel("/game/gameRecordList");

        if (start == null) {
            start = Integer.valueOf(0);
        }
        if (limit == null) {
            limit = Integer.valueOf(20);
        }

        queryGameRecordList(modelAndView, start, limit);

        return modelAndView;
    }

    @RequestMapping({"/gameRecordBody"})
    public Object gameRecordBody(Integer start) {
        ModelAndView modelAndView = getView("/game/gameRecordBody");
        queryGameRecordList(modelAndView, start, Integer.valueOf(20));

        return modelAndView;
    }

    private void queryGameRecordList(ModelAndView modelAndView, Integer start, Integer limit) {
        User user = (User) getLogin();
        List<Bet> gameRecordList = this.betService.listByCond(user.getAccount(), start, limit);
        modelAndView.addObject("gameRecordList", gameRecordList);
        modelAndView.addObject("start", start);
        modelAndView.addObject("limit", limit);
    }
}

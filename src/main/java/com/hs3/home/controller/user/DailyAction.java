package com.hs3.home.controller.user;

import com.hs3.db.Page;
import com.hs3.entity.users.DailyAcc;
import com.hs3.entity.users.DailyData;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.user.DailyAccService;
import com.hs3.service.user.DailyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Date;

@Controller
@Scope("prototype")
@RequestMapping({"/daily"})
public class DailyAction extends HomeAction {
    @Autowired
    private DailyAccService dailyAccService;
    @Autowired
    private DailyDataService dailyDataService;

    @ResponseBody
    @RequestMapping(value = {"/updateDailyAcc"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object updateDailyAcc(DailyAcc dailyAcc) {
        try {
            if (dailyAcc.getLimitAmount() == null) {
                dailyAcc.setLimitAmount(BigDecimal.ZERO);
            }
            this.dailyAccService.update(dailyAcc, ((User) getLogin()).getAccount());
        } catch (BaseCheckException e) {
            return Jsoner.error(e.getMessage());
        }
        return Jsoner.success();
    }

    @ResponseBody
    @RequestMapping(value = {"/saveDailyAcc"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object saveDailyAcc(DailyAcc dailyAcc) {
        try {
            if (dailyAcc.getLimitAmount() == null) {
                dailyAcc.setLimitAmount(BigDecimal.ZERO);
            }
            this.dailyAccService.save(dailyAcc, ((User) getLogin()).getAccount());
        } catch (BaseCheckException e) {
            return Jsoner.error(e.getMessage());
        }
        return Jsoner.success();
    }

    @RequestMapping({"/dailyAccTable"})
    public Object dailyAccTable(String account) {
        ModelAndView modelAndView = getViewWithHeadModel("/user/dailyAccTable");

        User user = (User) getLogin();

        DailyAcc dailyAcc = new DailyAcc();
        dailyAcc.setAccount(account);
        dailyAcc.setParentAccount(user.getAccount());

        Page p = getPageWithParams();

        modelAndView.addObject("dailyAccList", this.dailyAccService.listByCond(dailyAcc, p));
        modelAndView.addObject("p", p);

        return modelAndView;
    }

    @RequestMapping({"/dailyDataTable"})
    public Object dailyDataTable(String account, Date begin, Date end) {
        ModelAndView modelAndView = getViewWithHeadModel("/user/dailyDataTable");

        User user = (User) getLogin();

        DailyData dailyData = new DailyData();
        dailyData.setAccount(account);
        dailyData.setDailyAmount(BigDecimal.ZERO);

        Page p = getPageWithParams();

        modelAndView.addObject("dailyDataList", this.dailyDataService.listByCond(dailyData, user.getParentList(), begin, end, p));
        modelAndView.addObject("p", p);

        return modelAndView;
    }
}

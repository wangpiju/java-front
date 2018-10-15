package com.hs3.home.controller.finance;

import com.hs3.dao.bank.RechargeWayDao;
import com.hs3.entity.bank.RechargeWay;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.service.bank.BankLevelService;
import com.hs3.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("prototype")
@RequestMapping({"/rechargeNew"})
public class RechargeNewAction extends HomeAction {

    //private static final Logger logPay = Logger.getLogger("com.hs3.pays");

    @Autowired
    private UserService userService;

    @Autowired
    private BankLevelService bankLevelService;

    @Autowired
    private RechargeWayDao rechargeWayDao;


    @RequestMapping(value = {"/rechargeMoney"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargeMoney(Integer rechargeWay) {
        ModelAndView modelAndView = setModelAndView ("/recharge/rechargeNew", rechargeWay);
        return modelAndView;
    }

    private ModelAndView setModelAndView (String viewName, Integer rechargeWay){
        ModelAndView modelAndView = getViewWithHeadModel(viewName);
        RechargeWay rechargeWayObj = rechargeWayDao.findByID(rechargeWay);
        Integer waytype = rechargeWayObj.getWaytype();
        modelAndView.addObject("waytype", waytype);
        modelAndView.addObject("rechargeWay", rechargeWay);
        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        modelAndView.addObject("user", user);
        modelAndView.addObject("hadBankLevel", Boolean.valueOf(this.bankLevelService.count(user.getRegchargeCount().intValue(), user.getRegchargeAmount()) > 0));
        return modelAndView;
    }



}

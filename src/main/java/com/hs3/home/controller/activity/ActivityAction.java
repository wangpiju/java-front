package com.hs3.home.controller.activity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hs3.commons.ActivityType;
import com.hs3.models.activity.ActivitySignForIndex;
import com.hs3.web.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hs3.entity.activity.Activity;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.models.activity.AcivityBetconsumerModel;
import com.hs3.models.activity.ActivitySignModel;
import com.hs3.service.activity.ActivityBaseService;
import com.hs3.service.activity.ActivityBetconsumerService;
import com.hs3.service.activity.ActivityService;
import com.hs3.service.activity.ActivitySignService;
import com.hs3.utils.StrUtils;
import com.hs3.web.utils.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping({"/activity"})
public class ActivityAction extends HomeAction {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivitySignService activitySignService;
    @Autowired
    private ActivityBetconsumerService activityBetconsumerService;
    @Auth
    @RequestMapping(value = {"/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object index(Date date) {
        ModelAndView mv = getViewWithHeadModel("/activity/index");
        Integer userType = 0;
        User user = (User)getLogin();
        if(null != user){
            userType = user.getUserType();
        }
        List<Activity> resultList = this.activityService.listByActive(userType);
        List<Activity> list = new ArrayList<>();
        for (Activity a : resultList) {
            Integer id = a.getId();
            String sid = StrUtils.toUpperCaseFirst(ActivityType.parse(id).getAlias());
            if (sid.toLowerCase().equals(ActivityType.custom.name())) {
                list.add(a);
                continue;
            }
            String serviceName = "activity" + sid + "Service";
            ActivityBaseService service = (ActivityBaseService) SpringContext.getBean(serviceName);
            if(null != user)
                service.setStatus(a, getLogin().getAccount(), getLogin().getUserType() == 1);
            list.add(a);

            if (ActivityType.sign.getType().equals(a.getId())) {
                ActivitySignModel signActivity = this.activitySignService.findFull();
                mv.addObject("signActivity", signActivity);
            }
        }
        mv.addObject("list", list);
        Activity consumerActivity = this.activityService.find(ActivityType.betconsumer.getType());
        mv.addObject("consumerActivity", consumerActivity);
        AcivityBetconsumerModel m = this.activityBetconsumerService.findFull();
        mv.addObject("acivityBetconsumerModel", m);
        return mv;
    }


    @ResponseBody
    @RequestMapping(value = {"/add/{id}"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object add(@PathVariable("id") Integer id) {
        String account = getLogin().getAccount();
        if (id == null) {
            return Jsoner.error("404错误");
        }
        String sid = StrUtils.toUpperCaseFirst(ActivityType.parse(id).getAlias());
        String serviceName = "activity" + sid + "Service";
        ActivityBaseService service = (ActivityBaseService) SpringContext.getBean(serviceName);
        if (service == null) {
            return Jsoner.error("未找到活动");
        }
        return service.add(account, getLogin().getUserType() == 1);
    }
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/sign"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object signDay(Date date) {
        User user = (User)getLogin();
        if(null != user)
            return Jsoner.success(this.activitySignService.forIndex(user.getAccount(), date));
        else
            return Jsoner.success(new ActivitySignForIndex());
    }

    @ResponseBody
    @RequestMapping(value = {"/betconsumer"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getConsumerData() {
        return Jsoner.success(this.activityBetconsumerService.forIndex(getLogin().getAccount()));
    }


    @ResponseBody
    @RequestMapping(value = {"/bonus/{id}"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object bonus(@PathVariable("id") Integer id, String item) {
        String account = getLogin().getAccount();
        if (id == null) {
            return Jsoner.error("404错误");
        }
        String sid = StrUtils.toUpperCaseFirst(ActivityType.parse(id).getAlias());
        String serviceName = "activity" + sid + "Service";
        ActivityBaseService service = (ActivityBaseService) SpringContext.getBean(serviceName);
        if (service == null) {
            return Jsoner.error("未找到活动");
        }
        return service.addBonus(account, getLogin().getUserType() == 1, item);
    }

    @ResponseBody
    @RequestMapping({"/activityDetail"})
    public Object activityDetail(Integer id) {
        Activity a = this.activityService.find(id);
        a.setId(id);
        String sid = StrUtils.toUpperCaseFirst(ActivityType.parse(id).getAlias());
        String serviceName = "activity" + sid + "Service";
        ActivityBaseService service = (ActivityBaseService) SpringContext.getBean(serviceName);
        service.setStatus(a, getLogin().getAccount(), getLogin().getUserType() == 1);
        return Jsoner.success(a);
    }

    @RequestMapping(value = {"/detailIndex"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object detailIndex(Integer id) {
        ModelAndView mv = getViewWithHeadModel("/activity/detail");
        Activity a = this.activityService.find(id);
        a.setId(id);
        String sid = StrUtils.toUpperCaseFirst(ActivityType.parse(id).getAlias());
        String serviceName = "activity" + sid + "Service";
        ActivityBaseService service = (ActivityBaseService) SpringContext.getBean(serviceName);
        service.setStatus(a, getLogin().getAccount(), getLogin().getUserType() == 1);
        mv.addObject("activity", a);
        Activity consumerActivity = this.activityService.find(ActivityType.betconsumer.getType());
        mv.addObject("consumerActivity", consumerActivity);
        AcivityBetconsumerModel m = this.activityBetconsumerService.findFull();
        mv.addObject("acivityBetconsumerModel", m);
        return mv;
    }
}

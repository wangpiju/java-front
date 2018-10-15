package com.hs3.home.controller.finance;

import com.hs3.entity.lotts.Bet;
import com.hs3.home.controller.HomeAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@Scope("prototype")
@RequestMapping({"/betRecord"})
public class BetRecordAction
        extends HomeAction {
    @RequestMapping(value = {"/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object index() {
        ModelAndView mv = getView("/betRecord/index");
        return mv;
    }


    @ResponseBody
    @RequestMapping({"/list"})
    public Object list() {
        List<Bet> list = null;

        return list;
    }
}

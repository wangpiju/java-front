package com.hs3.home.controller.contract;

import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;
import com.hs3.service.contract.ContractMessageService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("prototype")
@RequestMapping({"/contractMessage"})
public class ContractMessageAction extends com.hs3.home.controller.HomeAction {
    @org.springframework.beans.factory.annotation.Autowired
    private ContractMessageService contractMessageService;

    @ResponseBody
    @RequestMapping(value = {"/updateMessageStatus"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object updateMessageStatus(Integer id) {
        int i = this.contractMessageService.updateMessageStatus(id);
        if (i > 0) {
            return Jsoner.success();
        }
        return Jsoner.error();
    }

    @ResponseBody
    @RequestMapping(value = {"/updateStatusByAccount"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object updateMessageStatus() {
        int i = this.contractMessageService.updateStatusByAccount(((User) getLogin()).getAccount());
        if (i > 0) {
            return Jsoner.success();
        }
        return Jsoner.error();
    }
}

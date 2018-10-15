package com.hs3.home.controller.contract;

import com.hs3.db.Page;
import com.hs3.entity.contract.ContractBonus;
import com.hs3.entity.contract.ContractConfig;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.ContractUtils;
import com.hs3.models.Jsoner;
import com.hs3.models.PageData;
import com.hs3.models.contract.ContractRuleModel;
import com.hs3.service.contract.ContractBonusService;
import com.hs3.service.contract.ContractConfigService;
import com.hs3.service.contract.ContractRuleService;
import com.hs3.service.user.UserService;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.utils.ContractCheckForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping({"/contract"})
public class ContractAction extends HomeAction {
    @Autowired
    private ContractRuleService contractRuleService;
    @Autowired
    private ContractConfigService contractConfigService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContractBonusService contractBonusService;

    @RequestMapping({"/setContractRule"})
    public Object setContractRule(String account) {
        ModelAndView mv = getViewWithHeadModel("/contract/setContract");
        User contractUuser = this.userService.findByAccount(account);
        ContractRuleModel contractRuleModel = this.contractRuleService.findContractRule(account, contractUuser.getContractStatus());
        ContractConfig contractConfig = this.contractConfigService.findEntity();
        mv.addObject("contractUuser", contractUuser);
        mv.addObject("contractRuleModel", contractRuleModel);
        mv.addObject("contractConfig", contractConfig);
        return mv;
    }

    @RequestMapping({"/findMyContract"})
    public Object findMyContract() {
        String account = ((User) getLogin()).getAccount();
        ModelAndView mv = getViewWithHeadModel("/contract/myContract");
        User user = this.userService.findByAccount(account);
        ContractRuleModel contractRuleModel = this.contractRuleService.findMyContractRule(account, Integer.valueOf(1));
        ContractRuleModel newContractRuleModel = this.contractRuleService.findMyContractRule(account, Integer.valueOf(0));
        ContractConfig contractConfig = this.contractConfigService.findEntity();
        List<ContractBonus> contractBonus = this.contractBonusService.findContractBonus(account);
        mv.addObject("user", user);
        mv.addObject("contractRuleModel", contractRuleModel);
        mv.addObject("newContractRuleModel", newContractRuleModel);
        mv.addObject("contractConfig", contractConfig);
        mv.addObject("contractBonus", contractBonus);
        return mv;
    }

    @RequestMapping({"/dividendIndex"})
    public Object dividendIndex() {
        ModelAndView mv = getViewWithHeadModel("/contract/lowerDividend");
        List<String> dividendDateList = new ArrayList();
        ContractConfig config = this.contractConfigService.findEntity();
        if (config.getBonusCycle().intValue() == 0) {
            dividendDateList = ContractUtils.getOneDateList(new Date());
        } else {
            dividendDateList = ContractUtils.getSecondDateList(new Date());
        }
        mv.addObject("dividendDateList", dividendDateList);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object contractBonusList(String account, String dividendDate, Integer dividendStatus) throws ParseException {
        Page p = getPageWithParams();
        ContractBonus m = new ContractBonus();
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            User u = this.userService.findIsDown(((User) getLogin()).getAccount(), account);
            if (u == null) {
                return Jsoner.error("用户不存在！");
            }
            m.setAccount(account);
        } else {
            m.setParentAccount(((User) getLogin()).getAccount());
        }

        if (!StrUtils.hasEmpty(new Object[]{dividendDate})) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            List<String> list = ListUtils.toList(dividendDate, "-");
            m.setStartDate(sdf.parse((String) list.get(0)));
            m.setEndDate(sdf.parse((String) list.get(1)));
        }
        m.setStatus(dividendStatus);
        List<ContractBonus> list = this.contractBonusService.list(m, p);
        return Jsoner.success(new PageData(p.getRowCount(), list));
    }

    @ResponseBody
    @RequestMapping(value = {"/addOrUpdate"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object addOrUpdate(ContractRuleModel m) {
        String str = ContractCheckForm.checkForm(m);
        if (!StrUtils.hasEmpty(new Object[]{str})) {
            return Jsoner.error(str);
        }
        if ((m.getContractStatus().intValue() == 0) || (m.getContractStatus().intValue() == 2)) {
            int i = this.contractRuleService.delete(m.getAccount(), m.getContractStatus());
            if (i > 0) {
                this.contractRuleService.addAndUpdateContract(m, null);
            }
        } else {
            this.contractRuleService.addAndUpdateContract(m, null);
        }

        return Jsoner.success("恭喜，您的契约发送成功。");
    }


    @ResponseBody
    @RequestMapping(value = {"/agree"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object agree(String account, String parentAccount) {
        int i = this.contractRuleService.updateToAgree(account, parentAccount);
        if (i == 0) {
            return Jsoner.error("同意契约操作失败！");
        }
        if (account.equals(parentAccount)) {
            return Jsoner.success("恭喜，您接收了平台的契约分红！");
        }
        return Jsoner.success("恭喜，您和上级代理的的契约分红已经成功签订！");
    }


    @ResponseBody
    @RequestMapping(value = {"/refuse"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object refuse(String account, String parentAccount) {
        int i = this.contractRuleService.updateTorRefuse(account, parentAccount);
        if (i == 0) {
            return Jsoner.error("拒绝操作失败！");
        }
        return Jsoner.success("您不同意上级发起的分红契约，已经将结果告知上级！");
    }

    @ResponseBody
    @RequestMapping(value = {"/undo"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object undo(String account, String parentAccount, Integer contractStatus) {
        int i = this.contractRuleService.updateToUndo(account, parentAccount, contractStatus);
        if (i == 0) {
            return Jsoner.error("撤销操作失败！");
        }
        return Jsoner.success("已撤销契约申请，若有需要请重新发起。");
    }

    @ResponseBody
    @RequestMapping(value = {"/payout"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object sendDividendAmount(Integer id, String account, String parentAccount, BigDecimal dividendAmount) {
        int i = this.contractBonusService.saveToSend(id, account, parentAccount, dividendAmount);
        if (i == 2)
            return Jsoner.error("余额不足。");
        if (i == 3) {
            return Jsoner.error("发放分红失败。");
        }
        return Jsoner.success("发放分红成功，请重新整理您的钱包确认金额。");
    }
}

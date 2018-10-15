package com.hs3.home.controller.finance;

import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.service.finance.FinanceChangeService;
import com.hs3.service.lotts.AccountChangeTypeService;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;


@Controller
@Scope("prototype")
@RequestMapping({"/finance"})
public class FinanceChangeAction
        extends HomeAction {
    private static final int DEFAULT_LIMIT = 20;
    @Autowired
    private FinanceChangeService financeChangeService;
    @Autowired
    private AccountChangeTypeService accountChangeTypeService;
    @Autowired
    private UserService userService;

    @RequestMapping({"/amountChangeList"})
    public Object amountChangeList(Integer start, Integer limit) {
        ModelAndView modelAndView = getViewWithHeadModel("/finance/amountChangeList");

        if (start == null) {
            start = Integer.valueOf(0);
        }
        if (limit == null) {
            limit = Integer.valueOf(20);
        }

        queryAmountChangeList(modelAndView, start, limit);

        return modelAndView;
    }

    @RequestMapping({"/amountChangeBody"})
    public Object amountChangeBody(Integer start) {
        ModelAndView modelAndView = getView("/finance/amountChangeBody");
        queryAmountChangeList(modelAndView, start, Integer.valueOf(20));

        return modelAndView;
    }

    private void queryAmountChangeList(ModelAndView modelAndView, Integer start, Integer limit) {
        User user = (User) getLogin();
        List<FinanceChange> financeChangeList = this.financeChangeService.listByCond(user.getAccount(), start.intValue(), limit.intValue());
        modelAndView.addObject("financeChangeList", financeChangeList);
        modelAndView.addObject("start", start);
        modelAndView.addObject("limit", limit);
    }

    @RequestMapping({"/financeChangeTable"})
    public Object financeChangeTable(String startTime, String endTime, Integer isIncludeChildFlag, FinanceChange financeChange) {
        ModelAndView modelAndView = getView("/finance/financeChangeTable");
        modelAndView.addObject("accountChangeTypeList", this.accountChangeTypeService.listByType(Integer.valueOf(1)));

        if (financeChange == null) {
            financeChange = new FinanceChange();
        }


        FinanceChange cond = new FinanceChange();
        cond.setChangeUser(financeChange.getChangeUser());
        cond.setAccountChangeTypeId(financeChange.getAccountChangeTypeId());

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        if (!StrUtils.hasEmpty(new Object[]{cond.getChangeUser()})) {
            User findUser = this.userService.findByAccount(cond.getChangeUser());
            if (findUser == null) {
                return modelAndView;
            }


            if (findUser.getParentList().indexOf(user.getParentList()) != 0) {
                return modelAndView;
            }
        } else {
            cond.setChangeUser(user.getAccount());
        }


        Date st = DateUtils.toDateNull(startTime);
        Date et = DateUtils.toDateNull(endTime);

        if ((cond.getAccountChangeTypeId() == null) || (-1 == cond.getAccountChangeTypeId().intValue())) {
            cond.setAccountChangeTypeId(null);
        }


        boolean isIncludeChild = (isIncludeChildFlag != null) && (isIncludeChildFlag.intValue() != 0);

        Page p = getPageWithParams();
        List<FinanceChange> financeChangeList = this.financeChangeService.listByCond(cond, st, et, isIncludeChild, null, p);

        modelAndView.addObject("financeChangeList", financeChangeList);
        modelAndView.addObject("p", p);

        return modelAndView;
    }

    @RequestMapping({"/financeChangeList"})
    public Object financeChangeList(String startTime, String endTime, Integer isIncludeChildFlag, FinanceChange financeChange) {
        ModelAndView modelAndView = getView("/finance/financeChangeList");
        modelAndView.addObject("accountChangeTypeList", this.accountChangeTypeService.listByType(Integer.valueOf(1)));

        if (financeChange == null) {
            financeChange = new FinanceChange();
        }


        FinanceChange cond = new FinanceChange();
        cond.setChangeUser(financeChange.getChangeUser());
        cond.setAccountChangeTypeId(financeChange.getAccountChangeTypeId());

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());
        if (!StrUtils.hasEmpty(new Object[]{cond.getChangeUser()})) {
            User findUser = this.userService.findByAccount(cond.getChangeUser());
            if (findUser == null) {
                return modelAndView;
            }


            if (findUser.getParentList().indexOf(user.getParentList()) != 0) {
                return modelAndView;
            }
        } else {
            cond.setChangeUser(user.getAccount());
        }


        Date st = DateUtils.toDateNull(startTime);
        Date et = DateUtils.toDateNull(endTime);

        if ((cond.getAccountChangeTypeId() == null) || (-1 == cond.getAccountChangeTypeId().intValue())) {
            cond.setAccountChangeTypeId(null);
        }


        boolean isIncludeChild = (isIncludeChildFlag != null) && (isIncludeChildFlag.intValue() != 0);

        Page p = getPageWithParams();
        List<FinanceChange> financeChangeList = this.financeChangeService.listByCond(cond, st, et, isIncludeChild, null, p);

        modelAndView.addObject("financeChangeList", financeChangeList);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("isIncludeChild", Boolean.valueOf(isIncludeChild));
        modelAndView.addObject("cond", financeChange);
        modelAndView.addObject("p", p);

        return modelAndView;
    }
}

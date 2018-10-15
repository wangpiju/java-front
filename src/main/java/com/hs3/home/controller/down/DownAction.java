package com.hs3.home.controller.down;

import com.hs3.db.Page;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserQuota;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.models.PageData;
import com.hs3.service.user.UserService;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Controller
@Scope("prototype")
@RequestMapping({"/down"})
public class DownAction
        extends HomeAction {
    @Autowired
    private UserService userService;

    //123456
    @ResponseBody
    @RequestMapping(value = {"/setQuota"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object setQuota(@RequestBody List<UserQuota> list) {
        int i = this.userService.updateQuotaByUser(((User) getLogin()).getAccount(), list);

        if (i == 2) {
            return Jsoner.error("此账号不能分配这种返点!");
        }
        if (i == 1) {
            return Jsoner.error("配额不足!");
        }
        return Jsoner.success();
    }


    @ResponseBody
    @RequestMapping(value = {"/adjustQuota"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object adjustQuota(String account, BigDecimal rebateRatio) {
        if ((StrUtils.hasEmpty(new Object[]{account})) || ((rebateRatio.compareTo(BigDecimal.valueOf(0.0D)) != 1) && (rebateRatio.compareTo(BigDecimal.valueOf(0.0D)) != 0))) {
            return Jsoner.error("参数不合法!");
        }

        if (!Pattern.matches("[\\w]{6,12}", account)) {
            return Jsoner.error("账号不合法!");
        }

        int i = this.userService.updateByAdjustQuota((User) getLogin(), account, rebateRatio);
        if (i == 1)
            return Jsoner.error("奖金组错误");
        if (i == 2)
            return Jsoner.error("账号不是当前的下级!");
        if (i == 3)
            return Jsoner.error("您此返点的配额名额不足!");
        if (i == 4)
            return Jsoner.error("只能向上调整配额!");
        if (i == 5)
            return Jsoner.error("会员不能开这种返点!");
        if (i == 6) {
            return Jsoner.error("不能开这种返点!");
        }
        return Jsoner.success();
    }


    @ResponseBody
    @RequestMapping({"/list"})
    public Object userList(String account, BigDecimal beginAmount, BigDecimal endAmount, Integer userType, String nextAccount, Integer isOnLine) {
        if (!StrUtils.hasEmpty(new Object[]{account})) {
            User u1 = this.userService.findIsDown(((User) getLogin()).getAccount(), account);
            if (u1 == null) {
                return Jsoner.error("用户不存在！");
            }
        }
        String parent;
        if (StrUtils.hasEmpty(new Object[]{nextAccount})) {
            parent = ((User) getLogin()).getAccount();
        } else {
            parent = nextAccount;
            User u = this.userService.findIsDown(((User) getLogin()).getAccount(), nextAccount);
            if (u == null) {
                return Jsoner.error("由于当前用户长时间未登录，账户已被收回，请联系在线客服处理！");
            }
        }


        Page p = getPageWithParams();

//    String curAccout = !StrUtils.hasEmpty(tmp121_118) ? account : parent;
        String curAccout = !StrUtils.hasEmpty(account) ? account : parent;
        List<?> list = this.userService.getUserList(account, nextAccount, curAccout, p, userType, beginAmount, endAmount, isOnLine);


        User user = this.userService.findByAccount(curAccout);
        String parentList = user.getParentList();
        String subparentList = parentList.substring(parentList.lastIndexOf(((User) getLogin()).getAccount() + ","), parentList.length());
        List<String> userTree = ListUtils.toList(subparentList);


        PageData rel = new PageData(p.getRowCount(), list);
        rel.setObj(userTree);


        return Jsoner.success(rel);
    }

    @ResponseBody
    @RequestMapping({"/ajaxGetUser"})
    public Object ajaxGetUser(String account) {
        User user = this.userService.findByAccount(account);
        Map<String, Object> data = new HashMap<>();
        data.put("account", ((User) getLogin()).getAccount());
        data.put("user", user);
        return Jsoner.success(data);
    }
}

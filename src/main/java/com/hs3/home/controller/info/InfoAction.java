package com.hs3.home.controller.info;

import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.bank.BankNameService;
import com.hs3.service.bank.BankUserService;
import com.hs3.service.user.UserService;
import com.hs3.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Pattern;


@Controller
@Scope("prototype")
@RequestMapping({"/info"})
public class InfoAction
        extends HomeAction {
    @Autowired
    private UserService userService;
    @Autowired
    private BankUserService bankUserService;
    @Autowired
    private BankNameService bankNameService;

    @ResponseBody
    @RequestMapping(value = {"/setInformation"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object setInformation(String niceName, String qq, String email, String phone, String message) {
        if (StrUtils.hasEmpty(new Object[]{niceName})) if (StrUtils.hasEmpty(new Object[]{qq}))
            if (StrUtils.hasEmpty(new Object[]{email}))
                if (StrUtils.hasEmpty(new Object[]{phone})) if (StrUtils.hasEmpty(new Object[]{message})) {
                    return Jsoner.error("参数不合法!");
                }
        if ((!StrUtils.hasEmpty(new Object[]{niceName})) && (
                (niceName.length() > 50) || (niceName.contains("<")) || (niceName.contains(">")))) {
            return Jsoner.error("昵称不合法!");
        }

        if ((!StrUtils.hasEmpty(new Object[]{qq})) && (
                (qq.length() > 100) || (!Pattern.matches("[0-9]+", qq)))) {
            return Jsoner.error("qq不合法!");
        }

        if ((!StrUtils.hasEmpty(new Object[]{email})) && (
                (email.length() > 255) || (!Pattern.matches("\\w+@\\w+\\.[a-zA-Z]+", email)))) {
            return Jsoner.error("email不合法!");
        }

        if ((!StrUtils.hasEmpty(new Object[]{phone})) && (
                (phone.length() > 20) || (!Pattern.matches("[0-9]+", phone)))) {
            return Jsoner.error("手机格式不合法!");
        }

        if ((!StrUtils.hasEmpty(new Object[]{message})) && (
                (message.length() > 50) || (message.contains("<")) || (message.contains(">")))) {
            return Jsoner.error("预留信息不合法!");
        }

        int i = this.userService.updateInformation(((User) getLogin()).getAccount(), niceName, qq, email, phone, message);
        if (i <= 0) {
            return Jsoner.error();
        }
        ((User) getLogin()).setNiceName(niceName);
        ((User) getLogin()).setQq(qq);
        ((User) getLogin()).setEmail(email);
        ((User) getLogin()).setPhone(phone);
        return Jsoner.success();
    }


    @ResponseBody
    @RequestMapping(value = {"/getInformation"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getInformation() {
        return Jsoner.success(this.userService.findUserInformation(((User) getLogin()).getAccount()));
    }


    @ResponseBody
    @RequestMapping(value = {"/getCity"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getCityByProvinceId(int provinceId) {
        return Jsoner.success(this.bankUserService.listCityByProId(provinceId));
    }


    @ResponseBody
    @RequestMapping(value = {"/getBankAll"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getBankAll() {
        return Jsoner.success(this.bankNameService.listBankAll());
    }


    @ResponseBody
    @RequestMapping(value = {"/getProvinceAll"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getProvinceAll() {
        return Jsoner.success(this.bankUserService.listProvince());
    }
}

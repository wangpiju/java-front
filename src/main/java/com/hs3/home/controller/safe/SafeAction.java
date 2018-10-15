package com.hs3.home.controller.safe;

import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.user.UserService;
import com.hs3.utils.StrUtils;
import com.hs3.web.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Pattern;


@Controller
@Scope("prototype")
@RequestMapping({"/safe"})
public class SafeAction
        extends HomeAction {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = {"/changePassWord"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object changePassWord(String oldpass, String newpass, String niceName, String qq, String email, String phone, String message) {
        if (StrUtils.hasEmpty(new Object[]{newpass})) {
            return Jsoner.error("参数非法!");
        }

        if (StrUtils.hasEmpty(new Object[]{oldpass})) {
            oldpass = "aa123456";
        }

        if (!Pattern.matches(".{8,16}", oldpass)) {
            if (!StrUtils.hasEmpty(new Object[]{oldpass})) {
            }
        } else if (!Pattern.matches(".{8,16}", newpass))
            return Jsoner.error("密码不合法!");

        if ((!StrUtils.hasEmpty(new Object[]{niceName, qq, email, phone})) && ((niceName.length() > 50) || (qq.length() > 100) || (message.length() > 50) || (email.length() > 255) || (phone.length() > 20) ||
                (!Pattern.matches("[0-9]+", phone)) || (!Pattern.matches("[0-9]+", qq)) ||
                (!Pattern.matches("\\w+@\\w+\\.[a-zA-Z]+", email)))) {
            return Jsoner.error("参数不合法!");
        }

        int i = this.userService.updatePasswordByUser(((User) getLogin()).getAccount(), oldpass, newpass, niceName, qq, email, phone, message);
        if (i < 0)
            return Jsoner.error("登录密码不能和安全密码一样！");
        if (i == 0) {
            return Jsoner.error("旧密码错误!");
        }
        getSession().invalidate();
        return Jsoner.success("密码修改成功,请重新登录");
    }


    @Auth
    @ResponseBody
    @RequestMapping(value = {"/setPassWordBySafe"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object changePassWordBySafe(String account, String safePass, String newpass, String code) {
        if (!validationCode(code)) {
            return Jsoner.error("验证码错误!");
        }

        if (StrUtils.hasEmpty(new Object[]{safePass, newpass})) {
            return Jsoner.error("参数不合法!");
        }

        if ((!Pattern.matches("^[0-9]{6}$", safePass)) || (!Pattern.matches(".{8,16}", newpass))) {
            return Jsoner.error("密码不合法!");
        }

        if (safePass.equals(newpass)) {
            return Jsoner.error("登录密码不能和安全密码一样！");
        }

        int i = this.userService.updatePassWordBySafe(account, safePass, newpass);
        if (i == 1) {
            return Jsoner.error("安全密码错误!");
        }
        getSession().invalidate();
        return Jsoner.success("密码修改成功,请重新登录");
    }


    @ResponseBody
    @RequestMapping(value = {"/changeSafeWord"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object changeSafeWord(String oldpass, String newpass) {
        if (StrUtils.hasEmpty(new Object[]{newpass})) {
            return Jsoner.error("参数非法!");
        }

        if (!Pattern.matches("^[0-9]{6}$", newpass)) {
            return Jsoner.error("新密码不合法!");
        }

        if ((!StrUtils.hasEmpty(new Object[]{oldpass})) &&
                (!Pattern.matches("^[0-9]{6}$", oldpass))) {
            return Jsoner.error("旧密码不合法!");
        }


        int i = this.userService.updateSafeWordByUser(((User) getLogin()).getAccount(), oldpass, newpass);
        if (i < 0) {
            return Jsoner.error("安全密码不能和登录密码一样！");
        }
        if (i <= 0) {
            return Jsoner.error("旧密码错误!");
        }
        ((User) getLogin()).setSafePassword(StrUtils.MD5(newpass));
        return Jsoner.success();
    }


    @ResponseBody
    @RequestMapping(value = {"/changeSafeByQuestions"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object changeSafeWordByQuestions(String qsType1, String answer1, String qsType2, String answer2, String safePassWord) {
        if ((StrUtils.hasEmpty(new Object[]{qsType1, qsType2, answer1, answer2, safePassWord})) ||
                (qsType1.length() > 1) || (qsType2.length() > 1) || (answer1.length() > 30) || (answer2.length() > 30)) {
            return Jsoner.error("参数非法!");
        }

        if (!Pattern.matches("^[0-9]{6}$", safePassWord)) {
            return Jsoner.error("密码不合法!");
        }

        int i = this.userService.updateSafeWordByQuestions(((User) getLogin()).getAccount(), qsType1, answer1,
                qsType2, answer2, safePassWord);
        if (i < 0) {
            return Jsoner.error("安全密码不能和登录密码一样！");
        }
        if (i == 1) {
            return Jsoner.error("安全问答验证错误!");
        }
        ((User) getLogin()).setSafePassword(StrUtils.MD5(safePassWord));
        return Jsoner.success();
    }


    @ResponseBody
    @RequestMapping(value = {"/setSafeInformation"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object setSafe(String qsType1, String answer1, String qsType2, String answer2, String safePassWord) {
        if ((StrUtils.hasEmpty(new Object[]{qsType1, qsType2, answer1, answer2})) ||
                (qsType1.length() > 1) || (qsType2.length() > 1) || (answer1.length() > 50) || (answer2.length() > 50)) {
            return Jsoner.error("参数不合法!");
        }

        if (qsType1.equals(qsType2)) {
            return Jsoner.error("两次问题不能相同");
        }

        if ((!StrUtils.hasEmpty(new Object[]{safePassWord})) &&
                (!Pattern.matches("^[0-9]{6}$", safePassWord))) {
            return Jsoner.error("密码不合法 !");
        }


        int i = this.userService.saveQsAndSafeWord(((User) getLogin()).getAccount(), qsType1, answer1, qsType2, answer2, safePassWord);
        if (i == 1) {
            return Jsoner.error("您已经设置过安全问答了!");
        }
        ((User) getLogin()).setSafePassword(StrUtils.MD5(safePassWord));
        return Jsoner.success();
    }


    @ResponseBody
    @RequestMapping(value = {"/getSafeInformation"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getSafeInformation() {
        return Jsoner.success(this.userService.findSafeInformation(((User) getLogin()).getAccount()));
    }

    @RequestMapping(value = {"/accountSafe"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object mobileSafe() {
        ModelAndView mv = getViewWithHeadModel("");
        mv.setViewName("/mobile/user/userSafe");
        return mv;
    }
}

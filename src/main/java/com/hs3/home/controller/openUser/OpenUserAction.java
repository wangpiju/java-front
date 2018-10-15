package com.hs3.home.controller.openUser;

import com.hs3.db.Page;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.users.ExtCode;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.lotts.BonusGroupService;
import com.hs3.service.user.ExtCodeService;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.utils.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;


@Controller
@Scope("prototype")
@RequestMapping({"/openUser"})
public class OpenUserAction
        extends HomeAction {
    @Autowired
    private BonusGroupService bonusGroupService;
    @Autowired
    private ExtCodeService extcodeservice;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = {"/createExtCode"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object createExtCode(int usertype, BigDecimal rebateratio, int validtime, String extaddress, String extQQ) {
        if ((StrUtils.hasEmpty(usertype, rebateratio, validtime, extaddress)) ||
                ((usertype != 0) && (usertype != 1)) || (extaddress.length() > 100) || (extaddress.contains("<")) || (extaddress.contains(">"))) {
            return Jsoner.error("参数不合法!");
        }

        if ((!StrUtils.hasEmpty(new Object[]{extQQ})) && (
                (extQQ.length() > 50) || (!Pattern.matches("[0-9]+", extQQ)))) {
            return Jsoner.error("QQ不合法!");
        }


        if (((User) getLogin()).getUserType() == 0) {
            return Jsoner.error("您没权限做此操作!");
        }

        BonusGroup bg = this.bonusGroupService.find(((User) getLogin()).getBonusGroupId());
        if (bg != null) {
            double count = Math.min(bg.getRebateRatio().doubleValue() - bg.getUserMinRatio().doubleValue(),
                    bg.getNoneMinRatio().doubleValue() - bg.getUserMinRatio().doubleValue());
            if ((rebateratio.doubleValue() > count) || (rebateratio.doubleValue() > ((User) getLogin()).getRebateRatio().doubleValue() - bg.getUserMinRatio().doubleValue()) || (rebateratio.doubleValue() < 0.0D)) {
                return Jsoner.error("返点值不合法!");
            }
        } else {
            return Jsoner.error("奖金组错误!");
        }

        rebateratio = rebateratio.setScale(1, 1);

        ExtCode code = new ExtCode();
        code.setUsertype(String.valueOf(usertype));
        code.setQq(extQQ);
        code.setRebateratio(rebateratio);
        code.setValidtime(validtime);
        code.setExtaddress(extaddress);
        code.setBonusgroupid(((User) getLogin()).getBonusGroupId());
        code.setAccount(((User) getLogin()).getAccount());
        code.setParnetaccount(((User) getLogin()).getParentAccount());
        String url = ShortUrl.getUrl(getRequest(), "/registByCode?code=");
        return Jsoner.success(url + this.extcodeservice.saveExtCode(code));
    }


    @ResponseBody
    @RequestMapping(value = {"/regist"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object regist(String account, String passWord, BigDecimal rebateRatio, Integer userType) {
        if (((User) getLogin()).getUserType() == 0) {
            return Jsoner.error("您没权限做此操作!!");
        }

        if ((StrUtils.hasEmpty(account, passWord)) || ((rebateRatio.compareTo(BigDecimal.valueOf(0.0D)) != 1) && (rebateRatio.compareTo(BigDecimal.valueOf(0.0D)) != 0))) {
            return Jsoner.error("参数不合法!");
        }

        if ((userType != 0) && (userType != 1)) {
            return Jsoner.error("参数不合法!");
        }

        if (!Pattern.matches("^[a-zA-Z][\\w]{5,11}", account)) {
            return Jsoner.error("账号不合法!");
        }

        if (!Pattern.matches(".{8,16}", passWord)) {
            return Jsoner.error("密码不合法!");
        }

        rebateRatio = rebateRatio.setScale(1, 1);

        int i = this.userService.saveByRegist(account, passWord, rebateRatio, getIP(), userType, (User) getLogin());
        switch (i) {
            case 0:
                return Jsoner.success("注册成功!");
            case 1:
                return Jsoner.error("奖金组错误!");
            case 2:
                return Jsoner.error("返点错误!");
            case 3:
                return Jsoner.error("此账号已被注册!");
            case 4:
                return Jsoner.error("配额不足!");
        }
        return Jsoner.error("发生错误!");
    }


    @ResponseBody
    @RequestMapping(value = {"/getExtQuota"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getExtQuota() {
        BonusGroup bg = this.bonusGroupService.find(((User) getLogin()).getBonusGroupId());
        if (bg == null) {
            return Jsoner.error("奖金组不存在！");
        }
        double count = Math.min(bg.getRebateRatio().doubleValue() - bg.getUserMinRatio().doubleValue(),
                bg.getNoneMinRatio().doubleValue() - bg.getUserMinRatio().doubleValue());
        Map<String, Object> map = new HashMap<>();
        if (count > ((User) getLogin()).getRebateRatio().doubleValue()) {
            map.put("rebateRatio", getLogin().getRebateRatio().doubleValue() - bg.getUserMinRatio().doubleValue());
        } else {
            map.put("rebateRatio", count);
        }
        return Jsoner.success(map);
    }

    @RequestMapping(value = {"/addPerson"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object addPerson() {
        ModelAndView mv = getViewWithHeadModel("");
        BonusGroup bonusGroup = this.bonusGroupService.find(((User) getLogin()).getBonusGroupId());

        BigDecimal stepRatio = bonusGroup.getUserMinRatio().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal("0.1") : bonusGroup.getUserMinRatio();

        BigDecimal maxRatio = ((User) getLogin()).getRebateRatio().subtract(bonusGroup.getUserMinRatio());
        mv.addObject("maxRatio", maxRatio.setScale(1));
        mv.addObject("stepRatio", stepRatio);
        mv.addObject("bonusGroup", bonusGroup);
        mv.setViewName("/mobile/user/addPerson");
        return mv;
    }

    @RequestMapping(value = {"/addLinkUser"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object addLinkUser() {
        ModelAndView mv = getViewWithHeadModel("/user/linkUser");
        return mv;
    }

    @RequestMapping(value = {"/regLinkManager"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object regLinkManager() throws ParseException {
        ModelAndView mv = getViewWithHeadModel("/user/regLink");
        Page p = getPageWithParams();
        String url = ShortUrl.getUrl(getRequest(), "/registerByMobile?code=");
        List<Map<String, Object>> list = this.userService.listUserExtCode(((User) getLogin()).getAccount(), p);

        for (Map<String, Object> map : list) {
            map.put("registAddress", url + map.get("code"));
            long createTime = DateUtils.toDate((String) map.get("createTime")).getTime();
            int validtime = ((Integer) map.get("validTime"));
            if (validtime != 0) {
                long validtimes = validtime * 86400000L;
                long now = new Date().getTime();
                if (createTime + validtimes < now) {
                    map.put("flag", 0);
                } else {
                    map.put("flag", 1);
                }
            } else {
                map.put("flag", 1);
            }
        }
        mv.addObject("list", list);
        return mv;
    }
}

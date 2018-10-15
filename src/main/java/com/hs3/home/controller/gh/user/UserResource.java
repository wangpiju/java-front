package com.hs3.home.controller.gh.user;

import com.hs3.home.bean.User;
import com.hs3.home.bean.UserPojo;
import com.hs3.home.constant.BaseConstant;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.service.gh.user.LocalUserService;
import com.hs3.service.sys.LoginIpBlackService;
import com.hs3.utils.StrUtils;
import com.hs3.utils.UserAgentUtils;
import com.hs3.utils.ip.IPSeeker;
import com.hs3.web.auth.Auth;
import com.hs3.web.auth.ThreadLog;
import com.hs3.web.utils.WebUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Pattern;

//import com.hs3.service.user.UserService;

/**
 * @author jason.wang
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/user"})
public class UserResource extends HomeAction {

    private static final Logger logger = Logger.getLogger(UserResource.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private LocalUserService localUserService;
    @Autowired
    private LoginIpBlackService loginIpBlackService;

    /**
     * @param account
     * @param password
     * @param code
     * @param sid
     * @return
     */
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    public JsonNode login(String account, String password, String code, String sid) {
        try {
            String token = (String) getSession().getAttribute("token");
            getSession().removeAttribute("token");

            ThreadLog.setAccount(account);
            ThreadLog.addKey("token:" + token);

            if (!this.loginIpBlackService.vaild(WebUtils.getIP(getRequest()))) {
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("非法IP"));
            }

            boolean validCode = validationCode(code);
            String ip = getIP();
            String agent = UserAgentUtils.getUserAgent(getRequest().getHeader("User-Agent")).toString();

            getSession().setAttribute("token", token);
            JsonNode rel = this.localUserService.saveIplogin(account, password, validCode, ip, agent, getSession().getId(), sid,
                    token, null);
            User user;
            try {
                if (rel.get("code").asInt() == BaseConstant.SUCCESS)
                    user = mapper.convertValue(rel.get("data"), User.class);
                else
                    return mapper.valueToTree(BaseBeanUtils.getFailReturn(rel.get("data")));
            } catch (Exception e) {
                logger.error("--> error ,",e);
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(rel.get("data")));
            }

            com.hs3.entity.users.User user2 = new com.hs3.entity.users.User();
            BeanUtils.copyProperties(user2, user);
            setLogin(user2);
            UserPojo userPojo = new UserPojo();
            userPojo.setAccount(user.getAccount());
            userPojo.setBalance((null == user.getAmount()) ? "0.00" : user.getAmount().toString());
            userPojo.setErrorCount(0);
            userPojo.setMessage("login success");
            userPojo.setNickName(user.getNiceName());
            userPojo.setSessionId(user.getSessionId());
            userPojo.setUserType(user.getUserType().toString());
            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(userPojo));

        } catch (Exception e) {
            logger.error("--> error,",e);
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("登入失敗"));
        }
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/logout"}, method = {RequestMethod.GET})
    public JsonNode logout() {
        getSession().invalidate();
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn("已登出"));
    }


    @Auth
    @ResponseBody
    @RequestMapping(value = {"/register"}, method = {RequestMethod.POST})
    public JsonNode register(String account, String password, String inviteCode, String code) {
        if (inviteCode.length() != 8) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("不是有效的邀请码"));
        }

        if ((StrUtils.hasEmpty(account, inviteCode))) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("帐号或邀请码不得为空"));
        }

        if (!Pattern.matches("^[a-zA-Z][\\w]{5,20}", account)) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("账号不合法!"));
        }

        if (!validationCode(code)) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("验证码错误！"));
        }

        String ip = getIP();
        String ipAddress = IPSeeker.getInstance().getAddress(ip);
        String userAgent = UserAgentUtils.getUserAgent(getRequest().getHeader("User-Agent")).toString();
        int status = this.localUserService.saveByCode(account, password, inviteCode, ip, ipAddress, userAgent);

        switch (status) {
            case 0:
                setLogin(this.localUserService.findByAccount(account));
                return mapper.valueToTree(BaseBeanUtils.getSuccessReturn("注册成功!"));
            case 1:
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("此链接已过有效期!"));
            case 2:
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("这个ip已经注册过了!"));
            case 3:
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("不是有效的邀请码 !"));
            case 4:
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("用户名已被注册!"));
            case 5:
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("此链接返点值不合法!"));
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn("发送错误!"));
    }

}

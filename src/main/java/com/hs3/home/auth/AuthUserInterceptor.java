package com.hs3.home.auth;

import com.hs3.entity.users.User;
import com.hs3.models.Jsoner;
import com.hs3.service.user.UserDoubleLoginService;
import com.hs3.service.user.UserService;
import com.hs3.web.auth.Auth;
import com.hs3.web.auth.AuthInterceptor;
import com.hs3.web.utils.RequestResponseContext;
import com.hs3.web.utils.SpringContext;
import com.hs3.web.utils.WebUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class AuthUserInterceptor
        extends AuthInterceptor {
    private static final Logger logger = Logger.getLogger(AuthUserInterceptor.class);
    private static UserService userService = (UserService) SpringContext.getBean(UserService.class);
    private static UserDoubleLoginService userDoubleLoginService = (UserDoubleLoginService) SpringContext.getBean(UserDoubleLoginService.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("VIEW_VERSION", "20170520");

        RequestResponseContext.set(request, response);

        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return true;
        }

        Auth auth = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
        ResponseBody body =((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class);


        if ((auth != null) && (!auth.validate())) {
            return true;
        }
        String context = request.getContextPath();


        Object user = request.getSession().getAttribute("USER_SESSION");

        boolean isLogin = false;
        boolean isDoubleLogin = false;
        if ((user != null) && (user.getClass().equals(User.class))) {
            isLogin = true;

            User sessionUser = (User) user;


            if (sessionUser.getPasswordStatus() == 0) {
                String url = WebUtils.getUrl(request);

//        if ((!url.equals("/trans")) && (!url.equals("/modifyPwd")) && (!url.equals("/safe/changePassWord"))) {
//          response.sendRedirect(context + "/modifyPwd");
//          return false;
//        }
            }

            if (!userDoubleLoginService.canDouble(sessionUser.getAccount(), sessionUser.getRootAccount())) {
                String sess = null;
                try {
                    sess = userService.getSessionId(sessionUser.getAccount());
                } catch (Exception e) {
                    logger.error("检查重复登录时数据库异常：" + e.getMessage(), e);
                }


                if (sess != null) {
                    String sessionId = request.getSession().getId();


                    isDoubleLogin = !sess.equals(sessionId);
                }
            }
        }
        if ((!isLogin) || (isDoubleLogin)) {
            request.getSession().invalidate();
            if (body != null) {
                if (!isLogin) {
                    return WriteJson(response, Jsoner.noLogin());
                }
                return WriteJson(response,
                        Jsoner.noLogin("您已在别处登录,请勿重复登录,如果不是您本人操作.请尽快修改密码"));
            }
            if (!isLogin) {
                response.sendRedirect(context + "/login");
            } else {
                response.sendRedirect(context + "/out");
            }
            return false;
        }

        User sessionUser = (User) user;
        userService.updateOnlineTime(sessionUser.getAccount(), new Date());
        return true;
    }

    public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj, Exception exception)
            throws Exception {
    }
}

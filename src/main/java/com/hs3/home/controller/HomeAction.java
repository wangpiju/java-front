package com.hs3.home.controller;

import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.LotteryGroup;
import com.hs3.entity.users.User;
import com.hs3.home.controller.lotts.LotteryAction;
import com.hs3.home.utils.CheckMobileUtils;
import com.hs3.models.lotts.LotteryTree;
import com.hs3.service.lotts.LotteryGroupService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.sys.SysServiceService;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.sys.WebDateUtils;
import com.hs3.web.controller.BaseAction;
import com.hs3.web.utils.SpringContext;
import com.hs3.web.utils.WebUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeAction
        extends BaseAction<User> {
    public static final String KEY_SESSION = "USER_SESSION";
    protected static final String DEFAULT_TIME_BEGIN = "webDefaultBeginTime";
    protected static final String DEFAULT_TIME_END = "webDefaultEndTime";
    protected static final String DEFAULT_PROFIT_TIME_BEGIN = "webProfitDefaultBiginTime";
    protected static final String DEFAULT_PROFIT_TIME_END = "webProfitDefaultEndTime";
    private static final String THEME_HOME = "/home";
    private static final String THEME_MOBILE = "/mobile";
    private static final String THEME_ERROR = "/error/404";
    private static final Logger logger = Logger.getLogger(HomeAction.class);

    protected boolean isMobile() {
        //first 
        String domain = WebUtils.getDomainName(getRequest());

        if (domain.contains("//m."))
            return true;

        //second
        boolean isFromMobile = false;

        try{
            //获取ua，用来判断是否为移动端访问
            String userAgent = getRequest().getHeader( "USER-AGENT" );

            if(null == userAgent){
                userAgent = "";
            }

            isFromMobile = CheckMobileUtils.check(userAgent.toLowerCase());

        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }

        if(isFromMobile)
            return true;


//        Cookie[] cookies = getRequest().getCookies();
//
//        if (cookies != null) {
//            Cookie[] arrayOfCookie1;
//            int j = (arrayOfCookie1 = cookies).length;
//            for (int i = 0; i < j; i++) {
//                Cookie c = arrayOfCookie1[i];
//                if (("mobile".equals(c.getName())) && ("1".equals(c.getValue()))) {
//                    return true;
//                }
//            }
//        }
        
        return false;
    }

    protected void setToMobile() {
        Cookie c = new Cookie("mobile", "1");
        getResponse().addCookie(c);
    }

    protected void setToPC() {
        Cookie c = new Cookie("mobile", "0");
        getResponse().addCookie(c);
    }


    protected String getThemeName() {
        String theme = null;
        Cookie[] arrayOfCookie = getRequest().getCookies();
        int j = 0;
        if(null != arrayOfCookie){
            j = arrayOfCookie.length;

            for (int i = 0; i < j; i++) {
                Cookie cookie = arrayOfCookie[i];
                String key = cookie.getName();
                if ((key != null) && (key.equals("theme"))) {
                    theme = cookie.getValue();
                    break;
                }
            }
            if (StrUtils.hasEmpty(new Object[]{theme})) {
                theme = "theme-black";
            } else {
                theme = theme.trim();
            }
        }
        else
            theme = "theme-black";

        return theme;
    }


    protected ModelAndView getViewWithHeadModel(String viewName) {
        ModelAndView mv = getView(viewName);
        LotteryService lotteryService = (LotteryService) SpringContext.getBean("lotteryService");
        UserService userService = (UserService) SpringContext.getBean("userService");
        SysServiceService sysServiceService = (SysServiceService) SpringContext.getBean(SysServiceService.class);
        LotteryGroupService lotteryGroupService = (LotteryGroupService) SpringContext.getBean(LotteryGroupService.class);
        User user = (User) getLogin();

        if(null != user){
            user = userService.findByAccount(user.getAccount());
            mv.addObject("user", user);
            mv.addObject("sysService", sysServiceService.findOpen(user.getUserMark()));
        }
        else{
            mv.addObject("user", null);
            mv.addObject("sysService", null);
        }

        if (!isMobile()) {
            mv.addObject("theme", getThemeName());
            List<Lottery> lotterys = lotteryService.listAndOrder(null);
            List<Lottery> lotterysSaled = new ArrayList<>();
            for (Lottery lottery : lotterys) {
                if (lottery.getStatus() == 0) {
                    lotterysSaled.add(lottery);
                }
            }
            mv.addObject("lotts", lotterysSaled);


            List<LotteryGroup> groups = lotteryGroupService.list(null, 0);
            List<LotteryTree> lotts = new ArrayList<>();
            for (LotteryGroup cs : groups) {
                int showGroup = -1;
                LotteryTree tree = new LotteryTree();
                List<List<Lottery>> list = new ArrayList<>();

                boolean hasLottery = false;
                tree.setKey(cs.getName());
                tree.setIcon(cs.getIcon());
                tree.setValue(list);

                List<Lottery> lott = null;
                for (Lottery l : lotterys)
                    if (l.getStatus() == 0) {

                        if ((!isMobile()) || (l.getMobileStatus() == 0)) {


                            if (cs.getId().equals(l.getLotteryGroupId())) {


                                if (showGroup != l.getShowGroup()) {
                                    lott = new ArrayList<>();
                                    list.add(lott);
                                    showGroup = l.getShowGroup();
                                }
                                lott.add(l);
                                hasLottery = true;
                            }
                        }
                    }
                if (hasLottery) {
                    lotts.add(tree);
                }
            }
            mv.addObject("headLotts", lotts);
        } else {
            //如果是手機導到小網
            String domain = WebUtils.getDomainName(getRequest());
            domain = domain.replace("https://www.", "https://m.");
            domain = domain.replace("http://www.", "http://m.");
            
            return new ModelAndView("redirect:" + domain);
        }
        return mv;
    }

    protected ModelAndView getErrorView() {
        return getView("/error/404");
    }


    protected String getTheme() {
        if (isMobile()) {
            return "/mobile";
        }
        return "/home";
    }

    protected String getSessionKey() {
        return "USER_SESSION";
    }

    protected void addWebDefaultTime(ModelAndView modelAndView) {
        Date now = new Date();
        modelAndView.addObject("webDefaultBeginTime", DateUtils.format(WebDateUtils.getBeginTime(now)));
        modelAndView.addObject("webDefaultEndTime", DateUtils.format(WebDateUtils.getEndTime(now)));
        modelAndView.addObject("webProfitDefaultEndTime", DateUtils.formatDate(WebDateUtils.getEndTime(now)));
        modelAndView.addObject("webProfitDefaultBiginTime", DateUtils.formatDate(WebDateUtils.getBeginTime(now)));
    }
}

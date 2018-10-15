package com.hs3.home.controller.web;

import com.hs3.entity.article.Notice;
import com.hs3.entity.lotts.LotterySaleTime;
import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.ExtCode;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.article.NoticeService;
import com.hs3.service.article.WinGoodNewService;
import com.hs3.service.helpCenter.CenterService;
import com.hs3.service.lotts.LotterySaleTimeService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.sys.LoginIpBlackService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.service.sys.SysServiceService;
import com.hs3.service.user.ExtCodeService;
import com.hs3.service.user.UserLoginIpService;
import com.hs3.service.user.UserService;
import com.hs3.service.webs.ImgRegistService;
import com.hs3.service.webs.ImgService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.NumUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.UserAgentUtils;
import com.hs3.utils.ip.IPSeeker;
import com.hs3.web.auth.Auth;
import com.hs3.web.auth.ThreadLog;
import com.hs3.web.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@Scope("prototype")
@RequestMapping({""})
public class
IndexAction extends HomeAction {
    private static final String[] showTimes = {"cqssc", "gd11x5", "pk10", "3d"};
    @Autowired
    LotteryService lotteryService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private WinGoodNewService winGoodNewService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private ImgRegistService imgRegistService;
    @Autowired
    private UserLoginIpService userLoginIpService;
    @Autowired
    private ExtCodeService extCodeService;
    @Autowired
    private LoginIpBlackService loginIpBlackService;
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private LotterySaleTimeService lotterySaleTimeService;

    @Auth
    @RequestMapping(value = {"/"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object home() {
        return index();
    }

    @ResponseBody
    @RequestMapping(value = {"/info"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object info() {
        return Jsoner.success(lottInfo());
    }

    private List<Object> lottInfo() {
        List<Object> timeShows = new ArrayList<>();
        String[] arrayOfString;
        int j = (arrayOfString = showTimes).length;
        for (int i = 0; i < j; i++) {
            String lo = arrayOfString[i];
            Map<String, Object> timeMap = new HashMap<>();

            LotterySaleTime s = this.lotterySaleTimeService.getCurrentByLotteryId(lo);
            if (null != s) {
                int sec = (int) DateUtils.getSecondBetween(new Date(), s.getEndTime()) + 1;
                timeMap.put("id", lo);
                timeMap.put("time", Integer.valueOf(sec));
            } else {
                timeMap.put("id", lo);
                timeMap.put("time", Integer.valueOf(0));
            }
            timeShows.add(timeMap);
        }
        return timeShows;
    }
    @Auth
    @RequestMapping(value = {"/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object index() {
        ModelAndView mv = getViewWithHeadModel("/index");

        List<Notice> noticeList = this.noticeService.getNoticeList(Integer.valueOf(5));
        mv.addObject("noticeList", noticeList);

        int imgStatus = 0;

        if (isMobile()) {
            imgStatus = 2;
            List<com.hs3.entity.lotts.Lottery> lotterys = this.lotteryService.listAndOrder(null);
            mv.addObject("lotts", lotterys);
        } else {
            mv.addObject("times", lottInfo());
            imgStatus = 1;
            mv.addObject("winList", this.winGoodNewService.getWinList());
            User user = (User) getLogin();

            if(null != user){
                if (this.userLoginIpService.countByFirstLogin(user.getAccount())) {
                    mv.addObject("isFirstLogin", Integer.valueOf(1));
                } else {
                    mv.addObject("isFirstLogin", Integer.valueOf(2));
                }
            }

            Notice noticeBySwitching = this.noticeService.getNotice(1);
            if (noticeBySwitching != null) {
                mv.addObject("notice", noticeBySwitching);
            } else if (noticeList.size() > 0) {
                mv.addObject("notice", noticeList.get(0));
            }

            Cookie[] cookies = getRequest().getCookies();

            if (noticeList.size() > 0) {
                if (cookies != null) {
                    if (noticeBySwitching != null) {
                        for (int i = 0; i < cookies.length; i++) {
                            Cookie cookie = cookies[i];
                            if (cookie.getName().equals("openSwitching")) {
                                mv.addObject("flag", Integer.valueOf(0));
                                break;
                            }
                            mv.addObject("flag", Integer.valueOf(1));
                        }

                    } else {
                        for (int i = 0; i < cookies.length; i++) {
                            Cookie cookie = cookies[i];
                            if (cookie.getName().equals("closeSwitching")) {
                                mv.addObject("flag", Integer.valueOf(0));
                                break;
                            }
                            mv.addObject("flag", Integer.valueOf(1));
                        }

                    }
                } else {
                    mv.addObject("flag", Integer.valueOf(1));
                }
            } else {
                mv.addObject("flag", Integer.valueOf(0));
            }
            if (noticeBySwitching != null) {
//        Cookie oneCookie = new Cookie("openSwitching", System.currentTimeMillis());
                Cookie oneCookie = new Cookie("openSwitching", String.valueOf(System.currentTimeMillis()));

                oneCookie.setMaxAge(86400);

                getResponse().addCookie(oneCookie);
            }

            Cookie cookie = new Cookie("closeSwitching", String.valueOf(System.currentTimeMillis()));

            cookie.setMaxAge(86400);

            getResponse().addCookie(cookie);
        }

        mv.addObject("imgs", this.imgService.listByShow(imgStatus));


        return mv;
    }

    @RequestMapping(value = {"/trans"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object trans() {
        return redirect("/index");
    }

    @RequestMapping(value = {"/modifyPwd"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object modifyPwd() {
        ModelAndView mv = getView("/modifyPwd");
        return mv;
    }

    @RequestMapping(value = {"/logout"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object logout() {
        getSession().invalidate();
        return redirect("/login");
    }

    @Auth
    @RequestMapping(value = {"/mobile"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object mobile() {
        String domain = WebUtils.getDomainName(getRequest());
        domain = domain.replace("https://www.", "https://m.");
        domain = domain.replace("http://www.", "http://m.");
        String url = domain;
        return redirect(url);
    }

    @Auth
    @RequestMapping(value = {"/domain"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object domain() {
        return getViewName("/error_domain");
    }

    @Auth
    @RequestMapping(value = {"/pc"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object pc() {
        setToPC();
        return redirect("/");
    }

    @Auth
    @RequestMapping(value = {"/out"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object out() {
        ModelAndView mv = getView("/out");
        return mv;
    }

    @Auth
    @RequestMapping(value = {"/login"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object login(String sid) {
        String token = StrUtils.getRandomString(8);
        ModelAndView mv = getView("/login");
        mv.addObject("sid", sid);
        mv.addObject("token", token);
        getSession().setAttribute("token", token);


        if (getSession().getAttribute("showCode") != null) {
            mv.addObject("code", "y");
        }

        if (!this.loginIpBlackService.vaild(WebUtils.getIP(getRequest()))) {
            return redirect("https://www.baidu.com");
        }

        //如果是手機導轉到小網
        if (isMobile()) {
            String domain = WebUtils.getDomainName(getRequest());
            domain = domain.replace("https://www.", "https://m.");
            domain = domain.replace("http://www.", "http://m.");

            return new ModelAndView("redirect:" + domain);
        }

        mv.addObject("sysService", this.sysServiceService.findOpen(0));
        return mv;
    }

    @Auth
    @RequestMapping(value = {"/operatorReport"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object operatorReport() {
        ModelAndView mv = getView("/operatorReport");
        return mv;
    }

    @Auth
    @RequestMapping(value = {"/login"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object login(String account, String password, String code, String sid) throws Exception {
        String token = (String) getSession().getAttribute("token");
        getSession().removeAttribute("token");

        ThreadLog.setAccount(account);
        ThreadLog.addKey("token:" + token);
        Jsoner rel = null;

        if (!this.loginIpBlackService.vaild(WebUtils.getIP(getRequest()))) {
            return redirect("https://www.baidu.com");
        }

        ModelAndView mv = getView("/login");

        boolean validCode = validationCode(code);
        String ip = getIP();
        String agent = UserAgentUtils.getUserAgent(getRequest().getHeader("User-Agent")).toString();

        rel = this.userService.saveIplogin(account, password, validCode, ip, agent, getSession().getId(), sid, token, null);
        if (rel.getStatus() == 200) {
            getSession().removeAttribute("showCode");
            setLogin((User) rel.getContent());
            if (isMobile()) {
                return redirect("/index");
            }
            return redirect("/trans");
        }
        if (rel.getStatus() == 2) {
            mv.addObject("code", "y");
            getSession().setAttribute("showCode", Boolean.TRUE);
        } else if (getSession().getAttribute("showCode") != null) {
            mv.addObject("code", "y");
        }
        mv.addObject("account", account);
        token = StrUtils.getRandomString(8);
        mv.addObject("token", token);
        getSession().setAttribute("token", token);
        mv.addObject("msg", rel.getContent());
        mv.addObject("status", rel.getStatus());
        mv.addObject("sid", sid);
        return mv;
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/safeLogin"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object loginBySafe(String account, String password, String code, String cardName, String sid) throws Exception {
        String token = (String) getSession().getAttribute("token");
        boolean validCode = validationCode(code);
        String ip = getIP();
        String agent = UserAgentUtils.getUserAgent(getRequest().getHeader("User-Agent")).toString();

        boolean flag = "y".equals((String) getSession().getAttribute("needValidName"));
        if ((flag) && (cardName == null)) {
            cardName = "";
        }

        Jsoner rel = this.userService.saveIplogin(account, password, validCode, ip, agent, getSession().getId(), sid, token, cardName);
        if (rel.getStatus() == 200) {
            setLogin((User) rel.getContent());
            if (isMobile()) {
                return Jsoner.success("/index");
            }
            return Jsoner.success("/trans");
        }

        return rel;
    }


    @Auth
    @RequestMapping(value = {"/registByCode"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object registerByCode(String code) {

        if (isMobile()) {
            String domain = WebUtils.getDomainName(getRequest());
            domain = domain.replace("https://www.", "https://m.");
            domain = domain.replace("http://www.", "http://m.");
            String url = domain + "/registered?code=" + code;
            return redirect(url);
            //return redirect("/registerByMobile?code=" + code);
        }

        ModelAndView mv = getView("/registByCode");
        mv.addObject("extcode", code);
        mv.addObject("imgs", this.imgRegistService.listByStats(0));
        ExtCode extCode = this.extCodeService.findByExtCode(code);
        mv.addObject("code", extCode);

        String d = DateUtils.formatDate(new Date());
        SysConfig config = this.sysConfigService.find("YESTERDAY");
        if (config == null) {
            Integer n = Integer.valueOf(NumUtils.getRandom(50, 500));
            config = new SysConfig();
            config.setId("YESTERDAY");
            config.setVal(n.toString());
            config.setRemark(d);
            this.sysConfigService.save(config);
        } else if (!d.equals(config.getRemark())) {
            Integer n = Integer.valueOf(NumUtils.getRandom(50, 500));
            config.setVal(n.toString());
            config.setRemark(d);
            this.sysConfigService.update(config);
        }

        mv.addObject("registerNum", config.getVal());

        return mv;
    }

    @Auth
    @RequestMapping(value = {"/registerByMobile"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object registerByMobile(String code) {
        if (!isMobile()) {
            setToMobile();
            return redirect("/registerByMobile?code=" + code);
        }
        ModelAndView mv = getView("/user/register");
        mv.addObject("extcode", code);
        mv.addObject("imgs", this.imgRegistService.listByStats(0));
        ExtCode extCode = this.extCodeService.findByExtCode(code);
        mv.addObject("code", extCode);

        return mv;
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/checkCode"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object checkCode(String code, String account) {
        if (!validationCode(code))
            return Jsoner.success(Integer.valueOf(0));
        if ((StrUtils.hasEmpty(new Object[]{account})) || (this.userService.findByAccount(account) == null)) {
            return Jsoner.success(Integer.valueOf(2));
        }
        return Jsoner.success(Integer.valueOf(1));
    }


    @Auth
    @ResponseBody
    @RequestMapping(value = {"/registByCode"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Object registerByCode(String account, String extcode, String code) {
        if ((StrUtils.hasEmpty(new Object[]{account, extcode})) || (extcode.length() > 32)) {
            return Jsoner.error("参数错误!");
        }

        if (!Pattern.matches("^[a-zA-Z][\\w]{5,11}", account)) {
            return Jsoner.error("账号不合法!");
        }

        if (!validationCode(code)) {
            return Jsoner.error("验证码错误！");
        }

        String ip = getIP();
        String ipAddress = IPSeeker.getInstance().getAddress(ip);
        String userAgent = UserAgentUtils.getUserAgent(getRequest().getHeader("User-Agent")).toString();
        int i = this.userService.saveByCode(account, "aa123456", extcode, ip, ipAddress, userAgent);

        switch (i) {
            case 0:
                setLogin(this.userService.findByAccount(account));
                return Jsoner.success("注册成功!");
            case 1:
                return Jsoner.error("此链接已过有效期!");
            case 2:
                return Jsoner.error("这个ip已经注册过了!");
            case 3:
                return Jsoner.error("不是有效的注册码 !");
            case 4:
                return Jsoner.error("用户名已被注册!");
            case 5:
                return Jsoner.error("此链接返点值不合法!");
        }
        return Jsoner.error("发送错误!");
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/validIp"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object validIp(String account) {
        String ip = getIP();
        boolean i = this.userLoginIpService.getSafeIp(account, ip);
        if (i) {
            return Jsoner.success(Integer.valueOf(1));
        }
        getSession().setAttribute("needValidName", "y");
        return Jsoner.success(Integer.valueOf(0));
    }

    @Auth
    @RequestMapping(value = {"/download"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object download(String t) {
        ModelAndView mv = getView("/download/index");
        mv.addObject("t", t);
        mv.addObject("sysService", this.sysServiceService.findOpen(Integer.valueOf(0)));
        return mv;
    }

    @Auth
    @RequestMapping(value = {"/repairDNS"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object repairDNS(String t) {
        ModelAndView mv = getView("/repairDNS");
        return mv;
    }
    @Auth
    @RequestMapping(value = {"/aappdown/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object aappdown() {
        ModelAndView mv = getViewWithHeadModel("/aappdown/index");
        return mv;
    }

    @RequestMapping(value = {"/lottery/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object lottery() {
        ModelAndView mv = getView("/lottery/index");
        return mv;
    }

}

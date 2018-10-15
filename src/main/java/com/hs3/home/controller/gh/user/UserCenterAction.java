package com.hs3.home.controller.gh.user;

import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.user.UserCodeDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserSafeDao;
import com.hs3.entity.bank.BankUser;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserCode;
import com.hs3.entity.users.UserSafe;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.home.utils.gh.httpRequest.HttpRequestPub;
import com.hs3.service.article.NoticeService;
import com.hs3.service.gh.user.UserCenterService;
import com.hs3.service.sys.LoginIpBlackService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.auth.Auth;
import com.hs3.web.utils.WebUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Stephen Zhou
 * 用户中心
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/userCenter"})
public class UserCenterAction extends HomeAction {

    @Autowired
    private LoginIpBlackService loginIpBlackService;

    private static final Logger logger = LoggerFactory.getLogger(UserCenterAction.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private UserSafeDao userSafeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCodeDao userCodeDao;

    @Autowired
    private BankUserDao bankUserDao;


    /***
     * 特别说明：
     *
     * 由于改进功能的需要给t_user表新增了image、sex、birthday等3个字段，对应的核心工程的User类也新增了对应的get和set
     * userDao新增了updateUserData(User m)方法
     * userDao新增了updateUserPhone(User m)方法
     * userDao新增了updatePasswordZ(String account, String password)方法和updateSafePasswordZ(String account, String securityCode)
     * 由用户需要接收短信、邮箱验证码新增了t_user_code表，对应新增UserCode类和UserCodeDao类
     *
     */


    //我的账户-序1
    private static int time(Date nowDate, Date createTime) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        c1.setTime(nowDate);//要判断的日期
        c2.setTime(createTime);//初始日期
        c3.setTime(createTime);//也给初始日期 把分钟加五
        c3.add(Calendar.MINUTE, 5);
        c2.add(Calendar.MINUTE, -5);//减去五分钟
        logger.info("c1" + DateUtils.format(c1.getTime(), "yyyy-MM-dd HH:mm:ss:S"));
        logger.info("c2" + DateUtils.format(c2.getTime(), "yyyy-MM-dd HH:mm:ss:S"));
        logger.info("c3" + DateUtils.format(c3.getTime(), "yyyy-MM-dd HH:mm:ss:S"));
        if (c1.after(c2) && c1.before(c3)) {
            logger.info("五分钟之内");
            return 1;
        } else {
            logger.info("五分钟之外");
            return 0;
        }
    }

    /***取的頂部使用者資訊***/
    @ResponseBody
    @RequestMapping(value = {"/getTopUserData"}, method = {RequestMethod.GET})
    public JsonNode getTopUserData() {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);
        String image = user.getImage();
        image = image == null || image.trim().equals("") ? "0" : image;

        HashMap<String, Object> topUserData = new HashMap<>();
        topUserData.put("balance", user.getAmount());
        topUserData.put("userId", user.getAccount());
        topUserData.put("image", image);
        topUserData.put("test", user.getTest());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(topUserData));
    }


    //我的账户-序2

    /***取使用者餘額***/
    @ResponseBody
    @RequestMapping(value = {"/getBalance"}, method = {RequestMethod.GET})
    public JsonNode getBalance() {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);

        HashMap<String, Object> topUserData = new HashMap<>();

        topUserData.put("balance", user.getAmount());


//        HashMap<String, Object> topUserData = new HashMap<>();
//        topUserData.put("balance", user_s.getAmount());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(topUserData));
    }


    //我的账户-序3

    /***個人信息***/
    @ResponseBody
    @RequestMapping(value = {"/getUserData"}, method = {RequestMethod.GET})
    public JsonNode getUserData() {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);
        String image = user.getImage();
        image = image == null || image.trim().equals("") ? "0" : image;

        HashMap<String, Object> topUserData = new HashMap<>();
        topUserData.put("userId", user.getAccount());
        topUserData.put("image", image);
        topUserData.put("account", user.getAccount());
        topUserData.put("nickName", user.getNiceName());
        topUserData.put("mobile", user.getPhone());
        topUserData.put("email", user.getEmail());
        topUserData.put("sex", user.getSex());
        topUserData.put("birthday", user.getBirthday());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(topUserData));
    }

    /***使用者資料修改儲存
     *
     * @param image        头像
     * @param nickName    昵称
     * @param sex            年龄
     * @param birthday    生日
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/saveUserData"}, method = {RequestMethod.POST})
    public JsonNode saveUserData(String image, String nickName, Integer sex, String birthday) {
        User user = getLogin();
        return userCenterService.saveUserData(user.getAccount(), image, nickName, sex, birthday);
    }


    //我的账户-序4-0

    /***发送手机验证码
     *
     * @param mobil            手机号
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/sendMobilCode"}, method = {RequestMethod.POST})
    public JsonNode sendMobilCode(String mobil) {
        HashMap<String, String> returnC = new HashMap<>();

        mobil = mobil.trim();
        if (mobil.length() != 11) {
            returnC.put("message", "手机号应为11位数！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (!Pattern.matches("0?(13|14|15|17|18|19)[0-9]{9}", mobil)) {
            returnC.put("message", "请输入有效的手机号！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        int emailCodeInt = (int) ((Math.random() * 9 + 1) * 100000);
        String code = String.valueOf(emailCodeInt);

        User user = getLogin();
        String account = user.getAccount();
        UserCode uc = new UserCode();
        uc.setAccount(account);
        uc.setType(1);
        uc.setCode(code);
        uc.setCreateTime(new Date());
        uc.setStatus(1);
        uc.setComments("");
        userCodeDao.save(uc);

        String emailCode = "【凯胜科技】尊敬的用户，您的验证码为" + code + "，5分钟输入有效！";

        HttpRequestPub hqPub = new HttpRequestPub();
        String result = hqPub.postSMS(mobil, emailCode);
        logger.info("result:" + System.lineSeparator() + result);

        returnC.put("message", "手机验证码已发送！");
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序4

    /***綁定手機
     *
     * @param mobil            手机
     * @param validCode        手机验证码
     * @param securityCode    账户安全码
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/saveBindPhone"}, method = {RequestMethod.POST})
    public JsonNode saveBindPhone(String mobil, String validCode, String securityCode) {

        HashMap<String, String> returnC = new HashMap<>();

        User user = getLogin();
        String account = user.getAccount();
        User user_s = userDao.findByAccount(account);
        String u_securityCode = user_s.getSafePassword();

        if (u_securityCode == null || u_securityCode.trim().equals("")) {
            returnC.put("message", "该账号还未设置安全码，请先设置安全码！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (!securityCode.trim().equals(u_securityCode)) {
            returnC.put("message", "账户安全码输入错误！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (validCode == null || validCode.trim().equals("")) {
            returnC.put("message", "手机验证码不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        int num = userCodeDao.findByAccount(account);

        if (num > 0) {
            UserCode uc = userCodeDao.findTopByAccount(account, 1);
            int status = uc.getStatus();
            if (status == 1) {
                String code = uc.getCode();
                if (validCode.equals(code)) {
                    //判断验证码不超过5分钟
                    Date createTime = uc.getCreateTime();
                    Date nowDate = new Date();
                    int flag = time(nowDate, createTime);
                    if (flag == 1) {//在5分钟之内
                        userCodeDao.updateInfo(uc.getId(), 2, "");
                    } else {
                        returnC.put("message", "手机验证码已过期！");
                        userCodeDao.updateInfo(uc.getId(), 3, "");
                        return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
                    }
                } else {
                    returnC.put("message", "手机验证码错误！");
                    return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
                }
            } else {
                returnC.put("message", "请输入有效手机验证码！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

        } else {
            returnC.put("message", "请输入有效手机验证码！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        return userCenterService.saveBindPhone(account, mobil, securityCode);
    }


    //我的账户-序5

    /***取得綁定手機***/
    @ResponseBody
    @RequestMapping(value = {"/getBindPhone"}, method = {RequestMethod.GET})
    public JsonNode getBindPhone() {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("mobile", user.getPhone());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(userData));
    }


    //我的账户-序6-0

    /***发送邮箱验证码
     *
     * @param email            邮箱
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/sendEmailCode"}, method = {RequestMethod.POST})
    public JsonNode sendEmailCode(String email) {
        HashMap<String, String> returnC = new HashMap<>();

        if (!Pattern.matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}", email)) {
            returnC.put("message", "请输入有效的邮箱！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        int emailCodeInt = (int) ((Math.random() * 9 + 1) * 100000);
        String code = String.valueOf(emailCodeInt);

        User user = getLogin();
        String account = user.getAccount();
        UserCode uc = new UserCode();
        uc.setAccount(account);
        uc.setType(2);
        uc.setCode(code);
        uc.setCreateTime(new Date());
        uc.setStatus(1);
        uc.setComments("");
        userCodeDao.save(uc);

        String emailCode = "您的验证码：" + code + "，请在5分钟内使用！";

        HttpRequestPub hqPub = new HttpRequestPub();
        hqPub.postEmail(email, "账户邮箱验证码", emailCode);

        returnC.put("message", "邮箱验证码已发送！");
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序6

    /***綁定信箱
     *
     * @param email            邮箱
     * @param validCode        邮箱验证码
     * @param securityCode    账户安全码
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/saveBindEmail"}, method = {RequestMethod.POST})
    public JsonNode saveBindEmail(String email, String validCode, String securityCode) {
        User user = getLogin();
        String account = user.getAccount();

        int num = userCodeDao.findByAccount(account);

        HashMap<String, String> returnC = new HashMap<>();
        if (num > 0) {
            UserCode uc = userCodeDao.findTopByAccount(account, 2);
            int status = uc.getStatus();
            if (status == 1) {
                String code = uc.getCode();
                if (validCode.equals(code)) {
                    //判断验证码不超过5分钟
                    Date createTime = uc.getCreateTime();
                    Date nowDate = new Date();
                    int flag = time(nowDate, createTime);
                    if (flag == 1) {//在5分钟之内
                        userCodeDao.updateInfo(uc.getId(), 2, "");
                    } else {
                        returnC.put("message", "邮箱验证码已过期！");
                        userCodeDao.updateInfo(uc.getId(), 3, "");
                        return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
                    }
                } else {
                    returnC.put("message", "邮箱验证码错误！");
                    return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
                }
            } else {
                returnC.put("message", "无可使用邮箱验证码！");
                return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
            }

        } else {
            returnC.put("message", "无有效邮箱验证码！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        return userCenterService.saveBindEmail(account, email, securityCode);
    }


    //我的账户-序7

    /***取得綁定信箱***/
    @ResponseBody
    @RequestMapping(value = {"/getBindEmail"}, method = {RequestMethod.GET})
    public JsonNode getBindEmail() {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("email", user.getEmail());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(userData));
    }


    //我的账户-序8

    /***安全中心狀態***/
    @ResponseBody
    @RequestMapping(value = {"/getSecurityCenterStatus"}, method = {RequestMethod.GET})
    public JsonNode getSecurityCenterStatus() {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);

        //String password = user.getPassword();//密码
        String securityCode = user.getSafePassword();//安全码
        String mobile = user.getPhone();//手机
        int safeInt = userSafeDao.findByAccount(user.getAccount());//密保问题
        String email = user.getEmail();//邮箱
        List<BankUser> bankUserList = bankUserDao.listByAccount(account, 0);

        int passwordFlag = 1;
        int securityCodeFlag = 0;
        int mobileFlag = 0;
        int safeFlag = 0;
        int emailFlag = 0;
        int bankUserFlag = 0;

        if (securityCode != null && !securityCode.equals("")) {
            securityCodeFlag = 1;
        }
        if (mobile != null && !mobile.equals("")) {
            mobileFlag = 1;
        }
        if (safeInt > 0) {
            safeFlag = 1;
        }
        if (email != null && !email.equals("")) {
            emailFlag = 1;
        }
        if (bankUserList.size() > 0) {
            bankUserFlag = 1;
        }

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("password", passwordFlag);
        userData.put("securityCoe", securityCodeFlag);
        userData.put("mobile", mobileFlag);
        userData.put("question", safeFlag);
        userData.put("email", emailFlag);
        userData.put("bankUserFlag", bankUserFlag);
        userData.put("lastLoginTime", (null != user.getLoginTime()) ? DateUtils.format(user.getLoginTime(), DateUtils.format(user.getLoginTime(), DateUtils.FORMAT_DATE_TIME)) : "");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(userData));
    }


    //我的账户-序9

    /***驗證原登陸密碼
     *
     * @param oldPassword            现密码
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/validOldLoginPassword"}, method = {RequestMethod.POST})
    public JsonNode validOldLoginPassword(String oldPassword) {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);

        String password = user.getPassword();//密码

        HashMap<String, String> returnC = new HashMap<>();

        if (!oldPassword.equals(password)) {
            returnC.put("message", "验证失败！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        returnC.put("message", "验证成功！");


        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序10

    /***修改登陸密碼
     *
     * @param newPassword            新密码
     * @param newPassword2        新密码2
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/changeLoginPassword"}, method = {RequestMethod.POST})
    public JsonNode changeLoginPassword(String newPassword, String newPassword2) {
        User user = getLogin();

        HashMap<String, String> returnC = new HashMap<>();

        if (!newPassword.equals(newPassword2)) {
            returnC.put("message", "两次输入的密码不一样！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        userDao.updatePasswordZ(user.getAccount(), newPassword);


        /*
         * 注意此处是否需要改变密码状态之类的信息
         */
        returnC.put("message", "修改成功！");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序11

    /***驗證原安全密碼
     *
     * @param oldPassword            现密码
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/validSecurityCode"}, method = {RequestMethod.POST})
    public JsonNode validSecurityCode(String oldPassword) {
        User user_s = getLogin();
        String account = user_s.getAccount();
        User user = userDao.findByAccount(account);

        String password = user.getSafePassword();//安全密码

        HashMap<String, String> returnC = new HashMap<>();

        if (!oldPassword.equals(password)) {
            returnC.put("message", "验证失败！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        returnC.put("message", "验证成功！");


        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序12

    /***修改安全密碼
     *
     * @param newPassword            新密码
     * @param newPassword2        新密码2
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/setSecurityCode"}, method = {RequestMethod.POST})
    public JsonNode setSecurityCode(String newPassword, String newPassword2) {
        User user = getLogin();

        HashMap<String, String> returnC = new HashMap<>();

        if (!newPassword.equals(newPassword2)) {
            returnC.put("message", "两次输入的密码不一样！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        userDao.updateSafePasswordZ(user.getAccount(), newPassword);


        /*
         * 注意此处是否需要改变密码状态之类的信息
         */

        returnC.put("message", "修改成功！");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序13

    /***設定密保問題
     *
     * @param title1        问题1代号
     * @param answer1        答案1
     * @param title2        问题2代号
     * @param answer2        答案2
     *
     * */
    @ResponseBody
    @RequestMapping(value = {"/setSecurityQuestion"}, method = {RequestMethod.POST})
    public JsonNode setSecurityQuestion(String title1, String answer1, String title2, String answer2) {
        User user = getLogin();
        String account = user.getAccount();

        HashMap<String, String> returnC = new HashMap<>();

        if (title1.trim().equals("") || title2.trim().equals("")) {
            returnC.put("message", "问题代号异常！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (answer1.trim().equals("") || answer2.trim().equals("")) {
            returnC.put("message", "答案不能为空！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        int safeNum = userSafeDao.findByAccount(account);
        if(safeNum > 0){
            returnC.put("message", "该用户已设置了密保问题，不能重复进行设置！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        UserSafe userSafe = new UserSafe();
        userSafe.setAccount(account);
        userSafe.setQstype1(title1);
        userSafe.setAnswer1(answer1);
        userSafe.setQstype2(title2);
        userSafe.setAnswer2(answer2);
        userSafe.setCreatetime(new Date());
        userSafe.setLastmodify(new Date());
        userSafeDao.saveZ(userSafe);

        returnC.put("message", "设定成功！");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
    }


    //我的账户-序13-0

    /***取密保问题***/
    @ResponseBody
    @RequestMapping(value = {"/getUserSafeData"}, method = {RequestMethod.GET})
    public JsonNode getUserSafeData() {
        User user_s = getLogin();
        String account = user_s.getAccount();
        UserSafe userSafe = userSafeDao.findByAccountZ(account);

        if (userSafe == null) {
            HashMap<String, String> returnC = new HashMap<>();
            returnC.put("message", "该用户未设置密保问题！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        HashMap<String, Object> userSafeData = new HashMap<>();
        userSafeData.put("title1", userSafe.getQstype1());
        userSafeData.put("answer1", userSafe.getAnswer1());
        userSafeData.put("title2", userSafe.getQstype2());
        userSafeData.put("answer2", userSafe.getAnswer2());

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(userSafeData));
    }

    /**
     * 找回密码
     *
     * @param account 账号
     * @param code    验证码
     * @return
     */
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/recoverPassword"}, method = {RequestMethod.POST})
    public JsonNode recoverPassword(String account, String code) {
        String token = (String) getSession().getAttribute("token");
        getSession().removeAttribute("token");

        if (!this.loginIpBlackService.vaild(WebUtils.getIP(getRequest()))) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("非法IP"));
        }

        boolean validCode = validationCode(code);
        getSession().setAttribute("token", token);

        if (StrUtils.hasEmpty(account)) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("账号不能为空"));
        }

        if (!Pattern.matches("^[a-zA-Z][\\w]{5,20}", account)) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("请输入有效的用户名"));
        }

        User u = this.userDao.findByAccount(account);
        if (u == null) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("找不到使用者帐户"));
        }

        if ((u.getLoginErrorCount() >= 3) && (!validCode)) {
            ObjectNode result = mapper.createObjectNode();
            result.put("message", "验证码错误");
            result.put("errCount", u.getLoginErrorCount());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(result));
        }

        User user = new User();
        user.setAccount(u.getAccount());
        user.setPasswordStatus(0);
        setLogin(user);


        String securityCode = u.getSafePassword();//安全码
        String mobile = u.getPhone();//手机

        int securityCodeFlag = 0;
        int mobileFlag = 0;

        if (securityCode != null && !securityCode.equals("")) {
            securityCodeFlag = 1;
        }
        if (mobile != null && !mobile.equals("")) {
            mobileFlag = 1;
        }


        HashMap<String, Object> obData = new HashMap<>();
        obData.put("securityCodeFlag", securityCodeFlag);
        obData.put("mobileFlag", mobileFlag);
        obData.put("mobile", mobile);

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(obData));
    }


}

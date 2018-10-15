package com.hs3.service.gh.user;

import com.hs3.dao.report.TeamReportDao;
import com.hs3.dao.report.UserReportDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.report.TeamReport;
import com.hs3.entity.report.UserReport;
import com.hs3.entity.users.User;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.utils.DateUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Stephen Zhou
 * 用户中心业务逻辑层
 */
@Service("userCenterService")
public class UserCenterService {

    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private UserDao userDao;
    @Autowired
    private TeamReportDao teamReportDao;
    @Autowired
    private UserReportDao userReportDao;

    /***使用者資料修改儲存
     *
     * @param account        账户
     * @param image        头像
     * @param nickName    昵称
     * @param sex            年龄
     * @param birthday    生日
     *
     * */
    public JsonNode saveUserData(String account, String image, String nickName, Integer sex, String birthday) {

        User user = userDao.findByAccount(account);
        user.setImage(image);
        user.setNiceName(nickName);
        user.setSex(sex);
        user.setBirthday(birthday);
        userDao.updateUserData(user);

        HashMap<String, String> updateUserDataReturn = new HashMap<String, String>();
        updateUserDataReturn.put("message", "保存成功！");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(updateUserDataReturn));
    }


    /***綁定手機
     *
     * @param account            账户
     * @param mobil            手机
     * @param securityCode    账户安全码
     *
     * */
    public JsonNode saveBindPhone(String account, String mobil, String securityCode) {

        User user = userDao.findByAccount(account);
        String u_securityCode = user.getSafePassword();

        HashMap<String, String> returnC = new HashMap<String, String>();

        if (u_securityCode == null || u_securityCode.trim().equals("")) {
            returnC.put("message", "该账号还未设置安全码，请先设置安全码！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        if (!securityCode.trim().equals(u_securityCode)) {
            returnC.put("message", "账户安全码输入错误！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        user.setPhone(mobil);
        userDao.updateUserPhone(user);

        HashMap<String, String> updateUserDataReturn = new HashMap<String, String>();
        updateUserDataReturn.put("message", "绑定成功！");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(updateUserDataReturn));
    }


    /***綁定信箱
     *
     * @param account            账户
     * @param email            邮箱
     * @param securityCode    账户安全码
     *
     * */
    public JsonNode saveBindEmail(String account, String email, String securityCode) {

        User user = userDao.findByAccount(account);
        String u_securityCode = user.getSafePassword();

        HashMap<String, String> returnC = new HashMap<String, String>();

        if (u_securityCode == null || u_securityCode.trim().equals("")) {
            returnC.put("message", "安全码为空，请现设置安全码！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }


        if (!securityCode.trim().equals(u_securityCode)) {
            returnC.put("message", "账户安全码输入错误！");
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }

        user.setEmail(email);
        userDao.updateUserEmail(user);

        HashMap<String, String> updateUserDataReturn = new HashMap<String, String>();
        updateUserDataReturn.put("message", "绑定成功！");

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(updateUserDataReturn));
    }


}

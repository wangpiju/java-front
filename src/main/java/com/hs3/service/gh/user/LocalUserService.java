package com.hs3.service.gh.user;

import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.extcode.ExtCodeDao;
import com.hs3.dao.lotts.BonusGroupDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserSubsetDao;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.sys.IpStore;
import com.hs3.entity.users.ExtCode;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserSubset;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.service.sys.IpStoreService;
import com.hs3.service.user.UserLoginIpService;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.ip.IPSeeker;
import com.hs3.utils.sec.Des;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author jason.wang
 */
@Service("localUserService")
public class LocalUserService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(LocalUserService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private BankUserDao bankUserDao;
    @Autowired
    private ExtCodeDao extDao;
    @Autowired
    private BonusGroupDao bonusGroupDao;
    @Autowired
    private IpStoreService ipStoreService;
    @Autowired
    private UserLoginIpService userLoginIpService;
    @Autowired
    private UserSubsetDao userSubsetDao;


    //private static final ObjectMapper mapper = new ObjectMapper();
    //查找用戶
    public User findByAccount(String account) {
        return this.userDao.findByAccount(account);
    }

    // 登入
    public JsonNode saveIplogin(String account, String password, boolean validCode, String ip, String agent, String sessionId, String sid, String token, String cardName) {
        if (StrUtils.hasEmpty(new Object[]{account, password})) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("账号或密码不能为空"));
        }

        if (!Pattern.matches("^[a-zA-Z][\\w]{5,20}", account)) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("请输入有效的用户名"));
        }
        User u = this.userDao.findByAccount(account);
        if (u == null) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("找不到使用者帐户"));
        }
        if (u.getLoginStatus() != 0) {
            if (u.getLoginErrorCount() >= 5) {
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("登录密码错误5次，账号已被冻结。请联系客服进行解冻"));
            }

            return mapper.valueToTree(BaseBeanUtils.getFailReturn("该账户存在风险，请及时沟通上级进行调整"));
        }

        if ((u.getLoginErrorCount() >= 3) && (!validCode)) {
            ObjectNode result = mapper.createObjectNode();
            result.put("message", "验证码错误");
            result.put("errCount", u.getLoginErrorCount());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(result));
        }

        if (!password.equals(u.getPassword())) {
            int errorCount = u.getLoginErrorCount() + 1;
            int status = 0;
            if (errorCount >= 5) {
                status = 1;
                logger.info(account + "：登录错误达到：" + errorCount + "次,已冻结");
            }
            this.userDao.updateLoginStatusAndErrorCount(account, null, errorCount, status, "5times_failed_login");
            ObjectNode result = mapper.createObjectNode();
            result.put("message", "账号或密码错误");
            result.put("errCount", errorCount);

            return mapper.valueToTree(BaseBeanUtils.getFailReturn(result));
        }
        if ((cardName != null) && (this.bankUserDao.findCardByAccountAndName(account, cardName) <= 0)) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("姓名不正确"));
        }
        Date dd = new Date();

//        String address = IPSeeker.getInstance().getAddress(ip);
        String address = "未知";
        IpStore ipStore = ipStoreService.findByIp(ip);
        if (ipStore != null) {
            address = ipStoreService.transIpAddress(ipStore);
        }
        String mac = null;
        String hard = null;
        String cpu = null;
        if (!StrUtils.hasEmpty(new Object[]{sid})) {
            try {
                byte[] src = Hex.decodeHex(sid.toCharArray());
                String c = Des.decrypt_des_cbc(src, "sJc@v9#b".getBytes());
                List<String> sidList = ListUtils.toList(c);
                if (sidList.size() == 3) {
                    mac = (String) sidList.get(0);
                    hard = (String) sidList.get(1);
                    cpu = (String) sidList.get(2);
                }
            } catch (Exception e) {
                mac = sid;
            }
        }
        this.userDao.saveUserIp(u.getAccount(), ip, dd, address, agent, u.getParentAccount(), mac, hard, cpu);
        this.userDao.updateLoginStatusAndErrorCount(account, dd, 0, 0, "");
        this.userDao.setSessionId(u.getAccount(), sessionId);
        if (ipStore == null) {
            ipStoreService.ipTransThread(ip);
        }
        u.setSessionId(sessionId);
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(u));
    }

    // 註冊
    public int saveByCode(String account, String password, String extCode, String ip, String ipAddress,
                          String userAgent) {
        ExtCode extcode = this.extDao.findByExtCode(extCode);
        if ((extcode != null) && ("0".equals(extcode.getStatus()))) {
            long validtime = extcode.getValidtime();
            if (validtime != 0L) {
                validtime *= 86400000L;
                long dtime = extcode.getCreatetime().getTime();
                long now = new Date().getTime();
                if (dtime + validtime < now) {
                    return 1;
                }
            }
            User user = null;
            if (("1".equals(extcode.getCanregists())) && (this.extDao.findRegistersByExtCode(extCode, ip) > 0)) {
                return 2;
            }
            if (this.userDao.findRecordByAccount(account) > 0) {
                return 4;
            }
            User parent = this.userDao.findByAccount(extcode.getAccount());
            BonusGroup bonusGroup = this.bonusGroupDao.findById(extcode.getBonusgroupid());

            double max = parent.getRebateRatio().subtract(bonusGroup.getUserMinRatio()).doubleValue();
            if ((extcode.getRebateratio().doubleValue() < 0.0D) || (extcode.getRebateratio().doubleValue() > max)) {
                return 5;
            }
            user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setRebateRatio(extcode.getRebateratio());
            user.setBonusGroupId(bonusGroup.getId());
            user.setUserType(Integer.valueOf(Integer.parseInt(extcode.getUsertype())));
            user.setTest(parent.getTest());
            Date date = new Date();
            user.setRegTime(date);
            user.setLoginTime(date);
            user.setParentAccount(parent.getAccount());
            user.setRootAccount(parent.getRootAccount());
            user.setParentList(parent.getParentList() + account + ",");
            user.setDepositStatus(Integer.valueOf(0));
            user.setRegIp(ip);
            user.setRegIpInfo(ipAddress);
            try {
                this.userDao.save(user);

                //注册成功时记录所属子集数据
                UserSubset userSubset = null;
                String uzParentList = user.getParentList();
                String[] uzParentArr = uzParentList.split(",");
                for(String uzp: uzParentArr){
                    userSubset = new UserSubset();
                    userSubset.setAccount(uzp);
                    userSubset.setSubSetAccount(account);
                    userSubsetDao.save(userSubset);
                }

                this.userDao.saveUserIp(account, ip, new Date(), ipAddress, userAgent, parent.getAccount(), null, null,
                        null);
                this.userDao.saveOnLine(account);
                this.extDao.savelink(extCode, account, new Date(), ipAddress);
                this.extDao.updateLastRegistByCode(extCode, new Date());
            } catch (DuplicateKeyException dupEx) {
                logger.error(dupEx.getMessage(), dupEx);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return 0;
        }
        return 3;
    }

}

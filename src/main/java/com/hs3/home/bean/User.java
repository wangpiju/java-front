package com.hs3.home.bean;


import com.hs3.home.utils.CustomerDateAndTimeDeserialize;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class User implements Serializable {
    private String account;
    private String password;
    private BigDecimal amount;
    private BigDecimal rebateRatio;
    private Integer loginStatus;
    private Integer betStatus;
    private Integer betInStatus;
    private Integer depositStatus;
    private Integer passwordStatus;
    private BigDecimal regchargeAmount;
    private Integer regchargeCount;
    private BigDecimal depositAmount;
    private Integer depositCount;
    private BigDecimal rechargeLowerSouAmount;
    private BigDecimal rechargeLowerTarAmount;
    private BigDecimal rechargeLowerSouAmountTotal;
    private Integer rechargeLowerSouCountTotal;
    private BigDecimal rechargeLowerTarAmountTotal;
    private Integer rechargeLowerTarCountTotal;
    private Integer bonusGroupId;
    private Integer test;
    private Integer userType;
    private String parentAccount;
    private String rootAccount;
    private String parentList;
    private Integer accountRecharge;
    private String safePassword;
    private String email;
    private String qq;
    private String niceName;
    private String regIp;
    private String regIpInfo;
    private String sessionId;
    private String bankGroup;
    private String freezeAccount;
    private String freezeRemark;
    private String phone;
    private String message;
    private String bankStatus;
    private String isBet;
    @JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
    private Date freezeDate;
    @JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
    private Date regTime;
    @JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
    private Date loginTime;
    private Integer userMark;
    private Integer isOnLine;
    private String homeRemark;
    private String adminRemark;
    private Integer withdrawalTimes;
    private Integer contractStatus;
    @JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
    private Date rechargeFirstTime;
    private BigDecimal rechargeFirstAmount;
    private Integer dailyWagesStatus;
    private Integer privateRebate;
    private Integer dailyRuleId;
    private Integer loginErrorCount;
    private String image;
    private Integer sex;
    private String birthday;
    private BigDecimal totalRebate;
    private BigDecimal retrievableRebate;
    private Integer depositRebateCount;
    private BigDecimal depositRebateAmount;

    public Integer getDepositRebateCount() {
        return depositRebateCount;
    }

    public void setDepositRebateCount(Integer depositRebateCount) {
        this.depositRebateCount = depositRebateCount;
    }

    public BigDecimal getDepositRebateAmount() {
        return depositRebateAmount;
    }

    public void setDepositRebateAmount(BigDecimal depositRebateAmount) {
        this.depositRebateAmount = depositRebateAmount;
    }

    public BigDecimal getTotalRebate() {
        return totalRebate;
    }

    public void setTotalRebate(BigDecimal totalRebate) {
        this.totalRebate = totalRebate;
    }

    public BigDecimal getRetrievableRebate() {
        return retrievableRebate;
    }

    public void setRetrievableRebate(BigDecimal retrievableRebate) {
        this.retrievableRebate = retrievableRebate;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIsBet() {
        return this.isBet;
    }

    public void setIsBet(String isBet) {
        this.isBet = isBet;
    }

    public String getBankStatus() {
        return this.bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRebateRatio() {
        return this.rebateRatio;
    }

    public void setRebateRatio(BigDecimal rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    public Integer getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Integer getBetStatus() {
        return this.betStatus;
    }

    public void setBetStatus(Integer betStatus) {
        this.betStatus = betStatus;
    }

    public Integer getBetInStatus() {
        return this.betInStatus;
    }

    public void setBetInStatus(Integer betInStatus) {
        this.betInStatus = betInStatus;
    }

    public Integer getDepositStatus() {
        return this.depositStatus;
    }

    public void setDepositStatus(Integer depositStatus) {
        this.depositStatus = depositStatus;
    }

    public Integer getPasswordStatus() {
        return this.passwordStatus;
    }

    public void setPasswordStatus(Integer passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public Integer getBonusGroupId() {
        return this.bonusGroupId;
    }

    public void setBonusGroupId(Integer bonusGroupId) {
        this.bonusGroupId = bonusGroupId;
    }

    public Integer getTest() {
        return this.test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public Integer getUserType() {
        return this.userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getParentAccount() {
        return this.parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public String getRootAccount() {
        return this.rootAccount;
    }

    public void setRootAccount(String rootAccount) {
        this.rootAccount = rootAccount;
    }

    public String getParentList() {
        return this.parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
    }

    public String getSafePassword() {
        return this.safePassword;
    }

    public void setSafePassword(String safePassword) {
        this.safePassword = safePassword;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getNiceName() {
        return this.niceName == null ? "" : this.niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public String getRegIp() {
        return this.regIp;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    public String getRegIpInfo() {
        return this.regIpInfo;
    }

    public void setRegIpInfo(String regIpInfo) {
        this.regIpInfo = regIpInfo;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBankGroup() {
        return this.bankGroup;
    }

    public void setBankGroup(String bankGroup) {
        this.bankGroup = bankGroup;
    }

    public String getFreezeAccount() {
        return this.freezeAccount;
    }

    public void setFreezeAccount(String freezeAccount) {
        this.freezeAccount = freezeAccount;
    }

    public String getFreezeRemark() {
        return this.freezeRemark;
    }

    public void setFreezeRemark(String freezeRemark) {
        this.freezeRemark = freezeRemark;
    }

    public Date getFreezeDate() {
        return this.freezeDate;
    }

    public void setFreezeDate(Date freezeDate) {
        this.freezeDate = freezeDate;
    }

    public Date getRegTime() {
        return this.regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public BigDecimal getRegchargeAmount() {
        return this.regchargeAmount;
    }

    public void setRegchargeAmount(BigDecimal regchargeAmount) {
        this.regchargeAmount = regchargeAmount;
    }

    public Integer getRegchargeCount() {
        return this.regchargeCount;
    }

    public void setRegchargeCount(Integer regchargeCount) {
        this.regchargeCount = regchargeCount;
    }

    public BigDecimal getDepositAmount() {
        return this.depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Integer getDepositCount() {
        return this.depositCount;
    }

    public void setDepositCount(Integer depositCount) {
        this.depositCount = depositCount;
    }

    public BigDecimal getRechargeLowerSouAmount() {
        return this.rechargeLowerSouAmount;
    }

    public void setRechargeLowerSouAmount(BigDecimal rechargeLowerSouAmount) {
        this.rechargeLowerSouAmount = rechargeLowerSouAmount;
    }

    public BigDecimal getRechargeLowerTarAmount() {
        return this.rechargeLowerTarAmount;
    }

    public void setRechargeLowerTarAmount(BigDecimal rechargeLowerTarAmount) {
        this.rechargeLowerTarAmount = rechargeLowerTarAmount;
    }

    public BigDecimal getRechargeLowerSouAmountTotal() {
        return this.rechargeLowerSouAmountTotal;
    }

    public void setRechargeLowerSouAmountTotal(BigDecimal rechargeLowerSouAmountTotal) {
        this.rechargeLowerSouAmountTotal = rechargeLowerSouAmountTotal;
    }

    public Integer getRechargeLowerSouCountTotal() {
        return this.rechargeLowerSouCountTotal;
    }

    public void setRechargeLowerSouCountTotal(Integer rechargeLowerSouCountTotal) {
        this.rechargeLowerSouCountTotal = rechargeLowerSouCountTotal;
    }

    public BigDecimal getRechargeLowerTarAmountTotal() {
        return this.rechargeLowerTarAmountTotal;
    }

    public void setRechargeLowerTarAmountTotal(BigDecimal rechargeLowerTarAmountTotal) {
        this.rechargeLowerTarAmountTotal = rechargeLowerTarAmountTotal;
    }

    public Integer getRechargeLowerTarCountTotal() {
        return this.rechargeLowerTarCountTotal;
    }

    public void setRechargeLowerTarCountTotal(Integer rechargeLowerTarCountTotal) {
        this.rechargeLowerTarCountTotal = rechargeLowerTarCountTotal;
    }

    public Integer getAccountRecharge() {
        return this.accountRecharge;
    }

    public void setAccountRecharge(Integer accountRecharge) {
        this.accountRecharge = accountRecharge;
    }

    public Integer getUserMark() {
        return this.userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public Integer getIsOnLine() {
        return this.isOnLine;
    }

    public void setIsOnLine(Integer isOnLine) {
        this.isOnLine = isOnLine;
    }

    public String getHomeRemark() {
        return this.homeRemark;
    }

    public void setHomeRemark(String homeRemark) {
        this.homeRemark = homeRemark;
    }

    public String getAdminRemark() {
        return this.adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public Integer getWithdrawalTimes() {
        return this.withdrawalTimes;
    }

    public void setWithdrawalTimes(Integer withdrawalTimes) {
        this.withdrawalTimes = withdrawalTimes;
    }

    public Integer getContractStatus() {
        return this.contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Date getRechargeFirstTime() {
        return this.rechargeFirstTime;
    }

    public void setRechargeFirstTime(Date rechargeFirstTime) {
        this.rechargeFirstTime = rechargeFirstTime;
    }

    public BigDecimal getRechargeFirstAmount() {
        return this.rechargeFirstAmount;
    }

    public void setRechargeFirstAmount(BigDecimal rechargeFirstAmount) {
        this.rechargeFirstAmount = rechargeFirstAmount;
    }

    public Integer getDailyWagesStatus() {
        return this.dailyWagesStatus;
    }

    public void setDailyWagesStatus(Integer dailyWagesStatus) {
        this.dailyWagesStatus = dailyWagesStatus;
    }

    public Integer getPrivateRebate() {
        return this.privateRebate;
    }

    public void setPrivateRebate(Integer privateRebate) {
        this.privateRebate = privateRebate;
    }

    public Integer getDailyRuleId() {
        return this.dailyRuleId;
    }

    public void setDailyRuleId(Integer dailyRuleId) {
        this.dailyRuleId = dailyRuleId;
    }

    public Integer getLoginErrorCount() {
        return this.loginErrorCount;
    }

    public void setLoginErrorCount(Integer loginErrorCount) {
        this.loginErrorCount = loginErrorCount;
    }
}

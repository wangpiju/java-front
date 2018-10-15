package com.hs3.home.controller.gh.agent;

import com.hs3.db.Page;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.users.ExtCode;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserQuota;
import com.hs3.home.bean.InviteCode;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.service.lotts.BonusGroupService;
import com.hs3.service.user.ExtCodeService;
import com.hs3.service.user.UserQuotaService;
import com.hs3.service.user.UserService;
import com.hs3.utils.StrUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author jason.wang 20180410
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/agent"})
public class AgentResource extends HomeAction {

    private static final Logger logger = Logger.getLogger(AgentResource.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private UserService userService;
    @Autowired
    private BonusGroupService bonusGroupService;
    @Autowired
    private ExtCodeService extcodeservice;
    @Autowired
    private UserQuotaService userQuotaService;

    /**
     * 新增邀請碼
     *
     * @param usertype
     * @param rebateratio
     * @param validtime
     * @param extaddress
     * @param extQQ
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/createInviteCode"}, method = {
            org.springframework.web.bind.annotation.RequestMethod.POST})
    public JsonNode createExtCode(int usertype, BigDecimal rebateratio, int validtime, String extaddress,
                                  String extQQ) {
        if ((StrUtils.hasEmpty(
                usertype, rebateratio, validtime, extaddress))
                || ((usertype != 0) && (usertype != 1)) || (extaddress.length() > 100) || (extaddress.contains("<"))
                || (extaddress.contains(">"))) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("参数不合法!"));
        }

        if ((!StrUtils.hasEmpty(extQQ))
                && ((extQQ.length() > 50) || (!Pattern.matches("[0-9]+", extQQ)))) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("QQ不合法!"));
        }

        if (getLogin().getUserType() == 0) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("您没权限做此操作!"));
        }

        BonusGroup bg = this.bonusGroupService.find(getLogin().getBonusGroupId());
        if (bg != null) {
//			double count = Math.min(bg.getRebateRatio().doubleValue() - bg.getUserMinRatio().doubleValue(),
//					bg.getNoneMinRatio().doubleValue() - bg.getUserMinRatio().doubleValue());
            if (
                //rebateratio.doubleValue() > count)||
                    (rebateratio.doubleValue() > getLogin().getRebateRatio().doubleValue()
                            - bg.getUserMinRatio().doubleValue())
                            || (rebateratio.doubleValue() < 0.0D)) {
                return mapper.valueToTree(BaseBeanUtils.getFailReturn("返点值不合法!"));
            }
        } else {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("奖金组错误!"));
        }

        rebateratio = rebateratio.setScale(1, 1);

        ExtCode code = new ExtCode();
        code.setUsertype(String.valueOf(usertype));
        code.setQq(extQQ);
        code.setRebateratio(rebateratio);
        code.setValidtime(validtime);
        code.setExtaddress(extaddress);
        code.setBonusgroupid(getLogin().getBonusGroupId());
        code.setAccount(getLogin().getAccount());
        code.setParnetaccount(getLogin().getParentAccount());
        Map<String, Object> map = new HashMap<>();
        map.put("code", this.extcodeservice.saveExtCode(code));
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(map));
    }

    /**
     * 取當前登入代理獎金組上限
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getExtQuota"}, method = {RequestMethod.GET})
    public JsonNode getExtQuota() {
        HashMap<String, BigDecimal> resultMap = new HashMap<>();
        List<UserQuota> list = this.userQuotaService.listByAccount(getLogin().getAccount());
        //獎金組
        BonusGroup bg = this.bonusGroupService.find(getLogin().getBonusGroupId());
        User user = getLogin();
        //找有配額的
        if (null != list && list.size() > 0)
            for (UserQuota userQuota : list) {
                if (userQuota.getNum() > 0) {
                    resultMap.put("rebateRatio", userQuota.getRebateRatio());
                    break;
                }
                if (user.getRebateRatio().compareTo(bg.getNoneMinRatio()) < 0)
                    resultMap.put("rebateRatio", user.getRebateRatio());
                else
                    resultMap.put("rebateRatio", bg.getNoneMinRatio());
            }
        else {
            if (user.getRebateRatio().compareTo(bg.getNoneMinRatio()) < 0)
                resultMap.put("rebateRatio", user.getRebateRatio());
            else
                resultMap.put("rebateRatio", bg.getNoneMinRatio());
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(resultMap));
    }

    /**
     * 邀請碼列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/inviteCode"}, method = {RequestMethod.GET})
    public JsonNode listUserExtCode(Integer type) {
        if (null == type)
            type = 1;
        Page p = getPageWithParams();
        List<Map<String, Object>> list = this.userService.listUserExtCode(getLogin().getAccount(), p);
        ArrayNode agentArrayNode = mapper.createArrayNode();
        ArrayNode userArrayNode = mapper.createArrayNode();
        if (null != list && 0 < list.size())
            for (Map<String, Object> map : list) {
                try {
                    InviteCode inviteCode = new InviteCode();
                    inviteCode.setId(map.get("id").toString());
                    inviteCode.setDate(map.get("createTime").toString());
                    inviteCode.setCount((Long) map.get("registNum"));
                    inviteCode.setCode(map.get("code").toString());
                    inviteCode.setRebateRatio(map.get("rebateRatio").toString());
                    Integer userType = Integer.valueOf(map.get("userType").toString());
                    if (userType == 1)
                        agentArrayNode.addPOJO(inviteCode);
                    else
                        userArrayNode.addPOJO(inviteCode);
                } catch (Exception e) {
                    logger.error("--> error ",e);
                }
            }
        JsonNode result;
        if (1 == type) {
            result = mapper.valueToTree(BaseBeanUtils.getSuccessReturn(agentArrayNode));
        } else if (2 == type) {
            result = mapper.valueToTree(BaseBeanUtils.getSuccessReturn(userArrayNode));
        } else {
            result = mapper.valueToTree(BaseBeanUtils.getSuccessReturn("錯誤參數"));
        }

        return result;
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = {"/deleteInviteCode"}, method = {RequestMethod.POST})
    @ResponseBody
    public JsonNode deleteInviteCode(Integer id) {
        int i = this.extcodeservice.setStatusByAccount(getLogin().getAccount(), id, 1);
        if (i <= 0) {

            return mapper.valueToTree(BaseBeanUtils.getFailReturn("删除失败！id不存在！"));
        }
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn("删除成功"));
    }

}

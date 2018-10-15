package com.hs3.home.controller.gh.pay;

import com.hs3.entity.bank.BankApi;
import com.hs3.entity.finance.Recharge;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.service.finance.RechargeService;
import com.hs3.service.sys.SysConfigService;
import com.hs3.web.utils.WebUtils;
import com.pays.PayApi;
import com.pays.PayApiFactory;
import com.pays.PayApiParam;
import com.pays.daddy.DaddyPayApi;
import com.pays.jmoney.JmoneyPayApi;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@RequestMapping({"/api/pay/thirdparty"})
public class ThirdpartyResource extends HomeAction {

    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private SysConfigService sysConfigService;

    //發送充值請求
    @ResponseBody
    @RequestMapping(value = {"/deposit"}, method = {RequestMethod.POST})
    public JsonNode deposit(BigDecimal amount, Integer bankId) {
        User user = getLogin();
        List<BankApi> bankApiAllList = this.rechargeService.listBankApi(user.getAccount(), user.getRegchargeAmount(), user.getRegchargeCount());

        BankApi bankApi = getBankApi(bankApiAllList);

        if (null == bankApi) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("找不到启用中的接口"));
        }
        String classKey = bankApi.getClassKey();
        PayApi payApi = PayApiFactory.getInstance(classKey);

        if (payApi == null) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("找不到启用中的接口"));
        }
        Recharge recharge;

        try{
            recharge = this.rechargeService.saveByPay(user, amount, null, null, bankId, bankApi.getId(), payApi, isMobile(), classKey);
        }
        catch (BaseCheckException e){
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(e.getMessage()));
        }

        String domainName = WebUtils.getDomainName(getRequest());
        String baseUrl = bankApi.getShopUrl() + "/" + this.sysConfigService.find("PROJECT_NAME").getVal() + "/" + bankApi.getClassKey();
        //String shopUrl = baseUrl + "/regcharge";
        String payNoticeUrl = baseUrl + "/notify";
        String payReturnUrl = baseUrl + "/callBack";
        String shopNoticeUrl = domainName + "/recharge/rechargePayNotice/" + bankApi.getId();
        if ("jubao".equals(payApi.getId())) {
            shopNoticeUrl = domainName + "/recharge/rechargePayJuBaoNotice";
        }
        String shopReturnUrl = domainName + "/recharge/rechargePayFinish/" + recharge.getId();

        //ModelAndView mv = getView("/recharge/rechargePayForward");
        String reqResult = "";
        //daddy
        if (PayApiFactory.getInstance(classKey) instanceof DaddyPayApi) {
            PayApiParam param = new PayApiParam(recharge.getAmount().setScale(2, 4).toEngineeringString(), recharge.getBankNameCode().toUpperCase(), bankApi.getShopUrl(), payNoticeUrl,
                    shopNoticeUrl, payReturnUrl, shopReturnUrl, bankApi.getShopUrl(), recharge.getId(), bankApi.getMerchantCode(), bankApi.getSign(), bankApi.getPublicKey(), getIP(),
                    bankApi.getEmail(), bankApi.isCredit(), isMobile(), recharge.getTraceId(), bankApi.getDepositMode() + "");
            DaddyPayApi daddyPayApi = (DaddyPayApi) payApi;
            reqResult = daddyPayApi.sendPayReqHtml(param);
        }
        //jmoney
        if (PayApiFactory.getInstance(classKey) instanceof JmoneyPayApi) {
            PayApiParam param = new PayApiParam(recharge.getAmount().setScale(2, 4).toEngineeringString(), recharge.getBankName(), bankApi.getShopUrl(), payNoticeUrl,
                    shopNoticeUrl, payReturnUrl, shopReturnUrl, bankApi.getShopUrl(), recharge.getId(), bankApi.getMerchantCode(), bankApi.getSign(), bankApi.getPublicKey(), getIP(),
                    bankApi.getEmail(), bankApi.isCredit(), isMobile(), recharge.getTraceId(), bankApi.getDepositMode() + "");
            JmoneyPayApi jmoneyPayApi = (JmoneyPayApi) payApi;
            reqResult = jmoneyPayApi.sendPayReqHtml(param);

        }
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(reqResult));
    }

    //取得第三方銀行列表
    @ResponseBody
    @RequestMapping(value = {"/getBanks"}, method = {RequestMethod.GET})
    public JsonNode getBanks(Integer type) {
        User user = getLogin();
        List<BankApi> bankApiAllList = this.rechargeService.listBankApi(user.getAccount(), user.getRegchargeAmount(), user.getRegchargeCount());

        //預設網銀
        if (null == type)
            type = 1;
        boolean isMobile = isMobile();
        BankApi bankApi = getBankApi(bankApiAllList);

        if (null == bankApi) {
            return mapper.valueToTree(BaseBeanUtils.getFailReturn("找不到启用中的接口"));
        }
        String classKey = bankApi.getClassKey();
        PayApi payApi = PayApiFactory.getInstance(classKey);
        Map<String, String> banksAll;

        if (isMobile)
            banksAll = payApi.getAllBanks(false);
        else
            banksAll = payApi.getAllBanks(true);

        ArrayNode arrayNode = mapper.createArrayNode();

        for (Map.Entry<String, String> entry : banksAll.entrySet()) {
            ObjectNode objectNode = mapper.createObjectNode();
            if (type == 2) {
                if (entry.getValue().contains("支付宝")) {
                    //banks.put(entry.getKey(), entry.getValue());
                    objectNode.put("code", entry.getKey());
                    objectNode.put("name", entry.getValue());
                }
            } else if (type == 3) {
                if (entry.getValue().contains("微信")) {
                    // banks.put(entry.getKey(), entry.getValue());
                    objectNode.put("code", entry.getKey());
                    objectNode.put("name", entry.getValue());
                }
            } else if (type == 4) {
                if (entry.getValue().contains("QQ")) {
                    //banks.put(entry.getKey(), entry.getValue());
                    objectNode.put("code", entry.getKey());
                    objectNode.put("name", entry.getValue());
                }
            }
            //網銀
            else {
                if (!entry.getValue().contains("QQ") && !entry.getValue().contains("支付宝") && !entry.getValue().contains("微信")) {
                    objectNode.put("code", entry.getKey());
                    objectNode.put("name", entry.getValue());
                }
            }

            if (null != objectNode.get("code"))
                arrayNode.add(objectNode);
        }

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(arrayNode));
    }

    private BankApi getBankApi(List<BankApi> bankApiList) {
        boolean isMobile = isMobile();
        BankApi bankApi = new BankApi();
        for (BankApi bankApi1 : bankApiList) {
            if (bankApi1.getStatus() == 0) {
                boolean b1 = (isMobile) && ((bankApi1.getIsSupportMobile() == 1) || (bankApi1.getIsSupportMobile() == 2));
                boolean b2 = (!isMobile) && ((bankApi1.getIsSupportMobile() == 1) || (bankApi1.getIsSupportMobile() == 0));
                if ((b1) || (b2)) {
                    bankApi = bankApi1;
                }
            }
        }

        return bankApi;
    }
}

package com.hs3.home.controller.gh.pay;

import com.hs3.home.controller.HomeAction;
import com.hs3.utils.MathUtils;
import com.hs3.web.auth.Auth;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@Scope("prototype")
@RequestMapping({"/api/pay"})
public class PayAction extends HomeAction {

    private static final Logger logPay = Logger.getLogger("com.hs3.pays");

    @Auth
    @ResponseBody
    @RequestMapping(value = {"/notify"}, method = {RequestMethod.POST})
    public void setNotify() throws IOException {

        String resultStr = "FAILURE";

        Map<String, String> params = getParams();
        logRechargePayNoticeParams(params);


        String status =  params.get("Status");//支付结果
        String m_No =  params.get("M_No");//商户编号
        String order_No =  params.get("Order_No");//商户订单号
        String sN =  params.get("SN");//平台订单号
        Object amount_o = params.get("Amount");
        BigDecimal amount = MathUtils.getBigDecimal(amount_o);//订单金额
        String timestamp =  params.get("Timestamp");//返回数据的时间
        String sign =  params.get("Sign");//验签

        try {
            resultStr = "SUCCESS";
        } catch (Exception e) {
            logPay.error(sN + " PayAction setNotify exception:" + e.getMessage(), e);
        }

        getResponse().getWriter().print(resultStr);
    }

    private void logRechargePayNoticeParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder("\n------------------/api/pay-start-------------------\n");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append((String) entry.getKey()).append("=").append((String) entry.getValue()).append("\n");
        }
        sb.append("------------------/api/pay-end-------------------");
        logPay.info(sb.toString());
    }

}

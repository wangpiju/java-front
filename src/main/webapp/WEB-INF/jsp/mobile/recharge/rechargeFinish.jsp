<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0 user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
    <title>账户充值</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 账户充值 -->
<div class="generalPlatArea userRechargeArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">充值</span>
    </div>
    <div class="container">
        <div class="successBox">
            <div class="successInfo">
                <p>
                	<c:if test="${recharge.status == 2 }">
                        <img src="/res/mobile/images/successIcon.png" class="msgIcon" alt=""/>
                        <span class="msgText">恭喜您，充值成功！</span>
                    </c:if>
                    <c:if test="${recharge.status == 3 }">
                        <img src="/res/mobile/images/warnIcon.png" class="msgIcon" alt=""/>
                        <span class="msgText">对不起，您的充值已过期！</span>
                    </c:if>
                    <c:if test="${recharge.status == 1 }">
                        <img src="/res/mobile/images/warnIcon.png" class="msgIcon" alt=""/>
                        <span class="msgText">对不起，您的充值被拒绝，拒绝原因：${recharge.remark }</span>
                    </c:if>
                    <c:if test="${recharge.status == 0 }">
                        <img src="/res/mobile/images/warnIcon.png" class="msgIcon" alt=""/>
                        <span class="msgText">系统正在处理您的充值，若充值附言和金额均准确，通常1分钟内即可到账。若5分钟后仍为到账，请联系<a href="${sysService.link }" class="alipayService fontColorRed" target="_blank"><i></i>在线客服</a></span>
                    </c:if>
                    <c:if test="${recharge.status == 4 }">
                        <img src="/res/mobile/images/successIcon.png" class="msgIcon" alt=""/>
                        <span class="msgText">您的充值订单已撤销！</span>
                    </c:if>
                    <c:if test="${recharge.status == 5 }">
                        <img src="/res/mobile/images/warnIcon.png" class="msgIcon" alt=""/>
                        <span class="msgText">您的充值订单正在处理中！</span>
                    </c:if>
                </p>
            </div>
            <div class="rechDetailIntro">
                <p>
                    <span>充值金额：</span>
                    <span class="fontColorRed">${recharge.amount }元</span>
                </p>
                <p>
                    <span>充值方式：</span>
                    <span>
                    	<c:if test="${recharge.rechargeType == 0 }">银行转账</c:if>
                        <c:if test="${recharge.rechargeType == 1 }">支付宝AA充值</c:if>
                    </span>
                </p>
                <p>
                    <span>订单编号：</span>
                    <span>${recharge.id }</span>
                </p>
            </div>
            <div class="btnGroup">
                <a href="/finance/amountChangeList" class="btn">查看账户明细</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
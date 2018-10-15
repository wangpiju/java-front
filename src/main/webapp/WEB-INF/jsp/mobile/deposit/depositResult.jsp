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
    <title>提现</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 提现 -->
<div class="generalPlatArea userDepositArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">提现</span>
    </div>
    <div class="container">
    	<c:choose>
    		<c:when test="${error != null }">
    			<div class="successInfo">
			        <div class="successInfo">
			            ${error}
			        </div>
			        <div class="finishBtn">
			            <a href="/deposit/safePassword" class="btn backPrev">返回</a>
			        </div>
			    </div>
    		</c:when>
    		<c:otherwise>
    			<!-- 提现成功 -->
		        <div class="SuccessBox">
		            <div class="successInfo">
		                <p>
		                    <img src="/res/mobile/images/successIcon.png" class="msgIcon" alt=""/>
		                    <span class="msgText">您的提现申请已成功！</span>
		                </p>
		                <p class="fontColorGray fontSizeRem09">请耐心等待工作人员审核，感谢您的支持与配合！</p>
		            </div>
		            <div class="btnGroup">
		                <a href="/finance/amountChangeList" class="btn btnRed">查看账户明细</a>
		            </div>
		        </div>
    		</c:otherwise>
    	</c:choose>
    </div>
</div>

</body>
</html>

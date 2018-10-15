<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <title>活动详情</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
    <link rel="stylesheet" href="/res/mobile/css/hs-sendCash.css" />
</head>
<body class="bodyColorEEE">
<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>
<!-- 活动详情 -->
<div class="platMsgArea generalPlatArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">活动详情</span>
        <a href="/activity/index" class="returnBtn">
            <img src="/res/mobile/images/returnIcon.png" />
            <span>返回</span>
        </a>
    </div>
    <div class="container">
	    <c:choose>
	    	<c:when test="${activity.id=='betconsumer'}">
	    		<c:import url="sendCash.jsp"></c:import>
	    	</c:when>
	    	<c:otherwise>
    	        <div class="platMsgDetailBox">
		            <div class="platMsgDetailTitle">
		                <p class="fontColorRed">${activity.title }</p>
		                <p class="fontColorGray fontSizeRem075"><fmt:formatDate value="${activity.beginTime}" type="both" pattern='yyyy-MM-dd HH:mm:ss'/>~<fmt:formatDate value="${activity.endTime}" type="both" pattern='yyyy-MM-dd HH:mm:ss'/></p>
		            </div>
		            <div class="platMsgDetailContent">
		                ${activity.remark}
		            </div>
		            <div class="btnGroup" id="activityNoticeBtn">
		                <c:if test="${activity.needAttend==1 }">
		                    <a href="javascript:;" onclick="add('${activity.id}')" class="btn joinActivity btnRed" id="joinActivity">参加活动</a>
		                </c:if>
		                <c:if test="${activity.needPrize==1 }">
		                    <a href="javascript:;" onclick="bonus('${activity.id}')" class="btn getPrize" id="getPrize">申请奖金</a>
		                </c:if>
		            </div>
        		</div>
	    	</c:otherwise> 
	   	</c:choose>
    </div>
    <div id="mask" class="mask" style="display: none;"></div>
    <div class="dialogArea" id="dialogBox"></div>
</div>
<script type="text/javascript" src="<c:url value='/res/mobile/js/activity/index.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/activity/sendCash.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
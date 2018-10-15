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
    <title>公告详情</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">
<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>
<!-- 公告详情 -->
<div class="platMsgArea generalPlatArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">公告详情</span>
        <a href="/notice/index" class="returnBtn">
            <img src="/res/mobile/images/returnIcon.png" />
            <span>返回</span>
        </a>
    </div>
    <div class="container">
        <div class="platMsgDetailBox">
            <div class="platMsgDetailTitle">
                <p class="fontColorRed">${notice.title }</p>
                <p class="fontColorGray fontSizeRem075"><fmt:formatDate value="${notice.createTime}" type="both" pattern='yyyy-MM-dd HH:mm:ss'/></p>
            </div>
            <div class="platMsgDetailContent">
                ${notice.content }
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
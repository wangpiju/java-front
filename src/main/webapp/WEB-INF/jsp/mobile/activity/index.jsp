<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <title>平台活动</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">
<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>
<!-- 平台活动 -->
<div class="platMsgArea generalPlatArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">平台活动</span>
    </div>
    <div class="container">
        <div class="platMsgBox platMsg-sm">
            <ul class="platMsgList">
                <c:forEach items="${list }" var="a" varStatus="status">
                    <li>
                        <a href="/activity/detailIndex?id=${a.id}" class="platMsgDetail">
                            <!--<img src="<c:url value='${a.icon}'/>" alt="平台活动"/>-->
                            <div class="platMsg-r">
                                <p class="platMsgTitle">
                                    <span class="fontColorRed">[活动]</span>
                                    <span>${a.title}</span>
                                </p>
                                <span class="platMsgDate">
                                    <fmt:formatDate value="${a.beginTime }" type="both" pattern='yyyy-MM-dd HH:mm:ss'/>
                                </span>
                            </div>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/platNotice/index.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
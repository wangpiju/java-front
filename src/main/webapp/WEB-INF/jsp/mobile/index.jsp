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
    <title>首页</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!--首页-->
<div class="generalPlatArea platIndexArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn" id="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <img src="/res/mobile/images/logo-leaf.png" class="platLogo" />
    </div>
    <div class="container">
        <div class="picScrollBox">
            <div class="picScrollList" id="picScrollList">
				<c:forEach items="${imgs}" var="a">
					<a href="${a.link}" title="${a.title}">
						<img src="${a.img}" />
					</a>
				</c:forEach>
            </div>
            <p class="picScrollBtn">
                <span class="active"></span>
                <span></span>
                <span></span>
            </p>
        </div>
        <div class="platNoticeListBox">
            <img src="/res/mobile/images/platNoticeTitle.png" class="platNoticeTitle" />
            <div class="platNoticeContent">
                <ul class="platNoticeList">
                	<c:forEach items="${noticeList }" var="a">
                        <li class="platNotice">
                            <a href="<c:url value='/notice/index?id=${a.id}'/>" class="overflowEllipsis">${a.title }</a>
                        </li>
                	</c:forEach>
                    <!--<li class="platNotice"> -->
                        <!--<span class="platNoticeNew fontSizeRem09">最新</span> -->
                        <!--<a href="javascript:;">玩转11选5，懂这些就足够！</a> -->
                    <!--</li> -->
                </ul>
            </div>
        </div>
        <div class="lotteryTypeListBox">
        	<c:forEach items="${lotts }" var="a">
        	<c:if test="${a.mobileStatus==0 && a.status==0 }">
            <a href="/lotts/${a.id}/index" class="lotteryTypeDetail">
                <img src="/res/mobile/images/lotts/${a.id}.png" class="lottTypeImg" />
                <div class="contentLineBreak">
                    <p class="lottTypeTitle">
                        <span class="fontSizeRem12">${a.title }</span>
<!--                         <img src="/res/mobile/images/indexHot.png" /> -->
                    </p>
                    ${a.remark}
<!--                     <p>猜对一个号码就中奖，简单好玩！</p> -->
<!--                     <p class="fontSizeRem09">全天78期，10分钟就开奖！</p> -->
                </div>
                <img src="/res/mobile/images/rightArrow.png" class="rightArrow" />
            </a>
            </c:if>
        	</c:forEach>
        </div>
    </div>
    <footer class="footer">
        <span class="copyright">©大順版权所有</span>
    </footer>
</div>

</body>
</html>
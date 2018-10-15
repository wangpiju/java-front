<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
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
    <title>我的消息</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 平台消息 -->
<div class="generalPlatArea platMsgArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">我的消息</span>
    </div>
    <div class="container">
        <div class="platMsgBox platMsg-sm">
            <ul class="platMsgList">
            	<c:forEach items="${messageList }" var="message" varStatus="st">
            		<input id="messageSendContet_${message.id }" value="<c:out value="${message.sendContent }"></c:out>" type="hidden" />
	                <li>
                        <a href="/message/messageContentRead?id=${message.id }" class="platMsgDetail">
                            <p class="platMsgTitle ${(user.account != message.sender && message.revStatus == 0) ? 'fontBold' : '' }" id="titleP_${message.id }">
                                <span>
                                    <c:out value="${message.title }" />
                                </span>
                            </p>
                            <p class="fontColorGray" style="display: none;font-size: 0.9rem" id="sendContent_${message.id }">
                                <c:out value="${message.sendContent }" />
                                <br />
                                <c:choose>
                                    <c:when test="${user.account != message.sender && message.sendType != 0 && message.revStatus != 2 }">
                                        <input id="revContent" type="text" class="labelCond" />
                                        <span id="messageReplyA" onclick="messageReply('${message.id }')">回复</span>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" class="labelCond" readonly="readonly" value="${message.revContent }" />
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <span class="platMsgDate">
                                <fmt:formatDate value="${message.sendTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </span>
                        </a>
	                </li>
                </c:forEach>
                <c:if test="${messageList == null || messageList.size() == 0 }">
                	<p class="nullMsg">暂时还没有信息哦~</p>
                </c:if>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
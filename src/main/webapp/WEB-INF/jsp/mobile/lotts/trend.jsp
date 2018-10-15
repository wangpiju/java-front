<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0 user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
    <title>投注页</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>
<!-- 开奖号码 -->
<div class="generalPlatArea lottsNumArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">${lott.title}</span>
        <a href="/lotts/${lott.id }/index" class="returnBtn">
            <img src="/res/mobile/images/returnIcon.png" />
            <span>返回</span>
        </a>
    </div>
    <div class="container">
        <div class="lottsNumBox">
            <table class="lottsNumTable">
                <thead>
                    <tr class="tableHeader">
                        <th>开奖时间</th>
                        <th>期号</th>
                        <th>开奖号码</th>
                    </tr>
                </thead>
                <tbody>
                	<c:forEach items="${list }" var="a">
                	<tr>
                        <td><fmt:formatDate value='${a.openTime}' pattern="MM-dd HH:mm" /></td>
                        <td>${a.seasonId }</td>
                        <td class="fontColorYellow"><c:forEach items="${a.nums}" var="n"> ${n}</c:forEach></td>
                    </tr>
                	</c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
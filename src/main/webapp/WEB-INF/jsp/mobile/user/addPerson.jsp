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
    <title>添加开户</title>
    <link rel="stylesheet" href="<c:url value='/res/mobile/css/hs-mobile.css?ver=${VIEW_VERSION}'/>" />
</head>
<body class="bodyColorEEE">
<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>
    <script type="text/javascript">
	var gloas = {};
	gloas.maxRatio = ${maxRatio};							//最大可调返点
	gloas.playerMaxRatio = ${bonusGroup.playerMaxRatio};	//会员最大返点
	gloas.userMinRatio = ${bonusGroup.userMinRatio};		//用户点差
	gloas.noneMinRatio = ${bonusGroup.noneMinRatio};		//无需配合返点
	gloas.stepRatio = ${stepRatio};							//点差步长
    </script>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/user/addPerson.js?ver=${VIEW_VERSION}'/>"></script>
<!-- 添加开户 -->
<div class="generalPlatArea addPersonArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="<c:url value='/res/mobile/images/mainMenuIcon.png'/>" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">添加开户</span>
    </div>
    <div class="container">
        <div class="addPersonBox">
            <div class="listForm">
            <form id="addUserList">
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>用户类型</span>
                    </p>
                    <div class="labelCondSelect">
                        <select name="userType" class="labelCond" id="addUserType">
                            <option value="0">会员</option>
							<option value="1">代理</option>
                        </select>
                        <i class="selectIcon"></i>
                    </div>
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>用户名</span>
                        <span class="detailTips fontColorGray">以字母开头8~16位数字、字母</span>
                    </p>
                    <input type="text" id="addUserName" name="account" class="labelCond" />
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>默认密码</span>
                    </p>
                    <input type="text" class="labelCond" value="aa123456" name="passWord" readonly />
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>返点</span>
                    </p>
                    <div class="labelCondSelect">
                        <select name="rebateRatio" id="addReservePoint" class="labelCond">
                        </select>
                        <i class="selectIcon"></i>
                    </div>
                </div>
                </form>
            </div>
            <div class="btnGroup">
                <a href="javascript:void(0);" id="submit" class="btn btnRed">提交</a>
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 -->
<div id="mask" class="mask" style="display: none;"></div>
<div class="dialogArea" id="dialogBox"></div>
</body>
</html>

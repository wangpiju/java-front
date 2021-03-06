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
	<title>修改密码</title>
	<link rel="stylesheet" href="<c:url value='/res/mobile/css/hs-mobile.css?ver=${VIEW_VERSION}'/>" />
	<script type="text/javascript" src="<c:url value='/res/home/js/jquery-1.11.0.min.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/jquery.timer.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/math.extends.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/mobile/js/jquery.dialog.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/mobile/js/base.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/mobile/js/user/userSafe.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body class="bodyColorEEE">
<!-- 修改密码 -->
<div class="generalPlatArea userSafeArea">
	<div class="mainNav">
		<span class="headerTitle">修改密码</span>
	</div>
	<input type="hidden" id="userPasswordStatus" value="0" />
	<div class="container">
		<div class="userSafeBox">
			<div class="listForm">
				<form id="modLoginPwd">
					<div class="listDetail" id="oldPassInput">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>旧密码</span>
							<span class="detailTips fontColorGray">8~16位数字、字母</span>
						</p>
						<div class="labelCondSelect">
							<input type="password" id="oldLoginPass" name="oldpass" class="labelCond" placeholder="请输入旧密码" />
							<i class="selectIcon"></i>
						</div>
					</div>
					<div class="listDetail">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>新密码</span>
							<span class="detailTips fontColorGray">8~16位数字、字母</span>
						</p>
						<input type="password" id="newLoginPass" class="labelCond" placeholder="请输入新登录密码" />
					</div>
					<div class="listDetail">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>确认新密码</span>
						</p>
						<input type="password" id="confirmLoginPass" name="newpass" class="labelCond" placeholder="请确认新登录密码" />
					</div>
				</form>
			</div>
			<div class="btnGroup">
				<a href="javascript:void(0);" id="changePassWord" class="btn btnRed">提交</a>
			</div>
		</div>
	</div>
</div>
<!-- 弹窗 -->
<div id="mask" class="mask" style="display: none;"></div>
<div class="dialogArea" id="dialogBox"></div>
</body>
</html>

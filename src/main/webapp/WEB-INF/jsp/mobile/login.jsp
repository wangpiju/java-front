<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="pragma" content="no-cache"/> 
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/> 
	<meta http-equiv="expires" content="0"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0 user-scalable=no">
    <!-- 删除苹果默认的工具栏和菜单栏 -->
    <meta content="yes" name="apple-mobile-web-app-capable">
    <!-- 设置苹果工具栏颜色 -->
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
    <!-- 忽略页面中的数字识别为电话，忽略email识别 -->
    <meta name="format-detection" content="telephone=no, email=no" />
    <title>欢迎使用大順</title>
    <link rel="stylesheet" href="<c:url value='/res/mobile/css/hs-mobile.css?ver=${VIEW_VERSION}'/>" />
	<script type="text/javascript" src="<c:url value='/res/home/js/jquery-1.11.0.min.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/jquery.md5.js?ver=${VIEW_VERSION}'/>"></script>
<%--     <script type="text/javascript" src="<c:url value='/res/home/js/cryptojs3.1.2/rollups/tripledes.js?ver=${VIEW_VERSION}'/>"></script> --%>
<%--     <script type="text/javascript" src="<c:url value='/res/home/js/cryptojs3.1.2/components/mode-ecb.js?ver=${VIEW_VERSION}'/>"></script> --%>
	<script type="text/javascript" src="<c:url value='/res/mobile/js/login.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body>

<!-- 登录窗口 -->
<div class="generalArea loginArea">
    <!--<div class="logoBox">-->
        <!--<img src="<c:url value='/res/mobile/images/logo-leaf.png'/>" alt="大順"/>-->
    <!--</div>-->
    <form action="/login" method="post" id="mobileLoginForm" class="loginBox" autocapitalize="off" autocorrect="off" data-token="${token}">
        <label class="loginUserBar">
            <img src="<c:url value='/res/mobile/images/login-user.png'/>" class="userNameIcon" />
            <input type="text" class="userName" id="userName" placeholder="用户名" />
            <input type="hidden" name="account" id="account" />
            <input type="hidden" name="password" id="password" />
        </label>
        <label class="loginUserBar">
            <img src="<c:url value='/res/mobile/images/login-pwd.png'/>" class="userPwdIcon" />
            <input type="password" class="userPwd" id="userPassword" placeholder="密码" />
        </label>
		<c:if test="${code!=null}">
			<label class="loginUserBar">
				<img src="<c:url value='/res/mobile/images/login-pwd.png'/>" class="userCodeIcon" />
				<input type="text" class="userCode" name="code" placeholder="验证码" maxlength="4"/>
				<img src="<c:url value='/code.jpg'/>" id="verfiy" alt="验证码" class="userCodeImg"/>
			</label>
		</c:if>
        <label class="loginUserBar">
            <span class="errorWarn">${msg}</span>
        </label>
        <label class="loginBtnBox">
            <button type="submit" class="btn" id="login">登录</button>
        </label>
        <label class="loginBtnBox">
            <a href="${sysService.link }" class="btn onlineServices">在线客服</a>
        </label>
		<label class="rememberPwdBar">
			<input type="checkbox" class="rememberPwd" id="rememberPwd"/>
			<span>是否记住密码</span>
		</label>
    </form>
    <!--<div class="loginSloganBox">-->
        <!--<img src="<c:url value='/res/mobile/images/login-slogan.png'/>" alt="选择大順，选择发财"/>-->
    <!--</div>-->
    <!--<div class="forgetPwdBox">-->
        <!--<a href="javascript:;" class="forgetPwd">忘记密码？</a>-->
    <!--</div>-->
</div>
</body>
</html>

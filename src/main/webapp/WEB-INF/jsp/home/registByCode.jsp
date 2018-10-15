<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>大順 - 注册</title>
	<link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>" type="image/x-icon"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<link rel="stylesheet" href="<c:url value='/res/home/css/reset.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="<c:url value='/res/home/css/wk-common.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="<c:url value='/res/home/css/wk-login.css?ver=${VIEW_VERSION}'/>"/>
	<script type="text/javascript"
			src="<c:url value='/res/home/js/jquery-1.11.0.min.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/jquery.timer.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/layer/layer.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/base.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/wk-register.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body>
<div class="main">
	<!--注册区域-->
	<div class="loginArea">
		<div class="loginLeft"></div>
		<div class="loginRight">
			<div class="loginMethod">
				<p>快速注册入口</p>
			</div>
			<div class="loginDetail">
				<input type="text" class="inputCont" name="inviteCode" placeholder="邀请码" id="inviteCode" maxLength=12 value="${extcode}"/>
				<i class="userNameIcon"></i>
			</div>
			<div class="loginDetail">
				<input type="text" class="inputCont" name="userName" placeholder="用户名" id="account" maxLength=12/>
				<i class="userNameIcon"></i>
			</div>
			<%--<div class="loginDetail">--%>
				<%--<input type="password" class="inputCont" name="passWord" placeholder="密码" id="password" maxLength=12/>--%>
				<%--<i class="userPasswordIcon"></i>--%>
			<%--</div>--%>
			<div class="loginDetail">
				<input type="text" class="inputCont" name="code" id="verfiyCode" placeholder="验证码" maxlength="4"/>
				<img src="<c:url value='/code.jpg'/>" id="verfiy" alt="验证码" class="userCodeImg"/>
				<i class="userCodeIcon"></i>
			</div>
			<div class="loginTips">
				<p class="defaultPwd">默认密码：aa123456</p>
			</div>
			<p class="errorTips" id="errorWarn"></p>
			<p class="accountLogin">已有账号，<a href="/login">立即登入</a></p>
			<div class="loginBtnArea">
				<button type="button" class="btn submitBtn" onclick="doRegist()" value="提交注册">提交注册</button>
			</div>
			<%--<p class="accountLogin">已有账号，<a href="/login">立即登入</a></p>--%>
			<input name="extcode" id="extcode" type="hidden" value="${extcode}"/>
		</div>
	</div>
</div>
<!-- 注册成功 -->
<div id="registerSuccess" style="display:none;">
	<div class="layerBox registerSuccessBox">
		<i class="registerSuccessIcon"></i>
		<div class="registerSuccessInfo">
			<p>
				<span>用户名：</span>
				<span id="registerUser"></span>
			</p>
			<p>
				<span>登录密码：aa123456</span>
			</p>
			<p class="fontColorRed">登录平台后，请及时修改密码！</p>
		</div>
		<div class="dialogBtn">
			<a href="javascript:;" class="btn">确定</a>
		</div>
	</div>
</div>
<!--底部-->
<%--<div class="footer">--%>
	<%--<div class="w920">--%>
		<%--<i class="logo"></i>--%>
		<%--<ul class="download">--%>
			<%--<li class="pc"><a href="<c:url value='/download?t=4'/>"><i></i>pc端下载</a></li>--%>
			<%--<li class="ios"><a href="<c:url value='/download?t=2'/>"><i></i>ios端下载</a></li>--%>
			<%--<li class="android"><a href="<c:url value='/download?t=1'/>"><i></i>安卓客户端下载</a></li>--%>
			<%--<li class="auto"><a href="<c:url value='javascript:;'/>"><i></i>自动投注软件</a></li>--%>
		<%--</ul>--%>
		<%--<p>@大順 DH88 版权所有 2010-2018 copyright DH88 Interactive Network Techno Co.&nbsp;&nbsp;|&nbsp;&nbsp;<a--%>
				<%--class="dnsLink" href="/repairDNS" target="_blank">防劫持教程</a></p>--%>
	<%--</div>--%>
<%--</div>--%>
</body>
</html>

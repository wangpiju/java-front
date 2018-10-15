<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>大順 - 交易协定</title>
    <link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>"  type="image/x-icon" />
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="<c:url value='/res/home/css/reset.css?ver=${VIEW_VERSION}'/>"/>
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-login.css?ver=${VIEW_VERSION}'/>" />
</head>

<body class="trans">
<!-- 交易协定 -->
<div class="transMain">
    <div class="transHeader">
        <a class="logo" href="<c:url value='/login'/>">
            <img src="<c:url value='/res/home/images/download/logo.png'/>" alt="logo"/>
        </a>
		<a target="_blank" href="${sysService.link }" class="customerService"><i></i>在线客服</a>
    </div>
    <div class="transArea">
		<div class="transProtocolBox" id="transProtocolBox">
			<h2 class="headerTitle">交易协定</h2>
			<div class="transProtocolDetail">
				<div class="transProtocolIntro transAjaxInfo">${article.content }</div>
			</div>
			<div class="transProtocolBtns">
				<a href="/" class="btn agree">同意</a>
				<a href="/logout" class="btn disagree">不同意</a>
			</div>
		</div>
    </div>
</div>

<!--底部-->
<div class="footer">
	<div class="w920">
		<i class="logo"></i>
		<ul class="download">
			<li class="pc"><a href=""><i></i>pc端下载</a></li>
			<li class="ios"><a href=""><i></i>ios端下载</a></li>
			<li class="android"><a href=""><i></i>安卓客户端下载</a></li>
			<li class="auto"><a href=""><i></i>自动投注软件</a></li>
		</ul>
		<p>@大順 版权所有 2010-2030 copyright JF Techno Co.&nbsp;&nbsp;|&nbsp;&nbsp;<a class="dnsLink" href="/repairDNS" target="_blank">防劫持教程</a></p>
	</div>
</div>
</body>
</html>
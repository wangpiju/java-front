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
    <title>链接开户设定</title>
    <link rel="stylesheet" href="<c:url value='/res/mobile/css/hs-mobile.css?ver=${VIEW_VERSION}'/>" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 链接开户设定 -->
<div class="generalPlatArea addPersonArea">
	<div class="mainNav">
		<a href="javascript:;" class="mainMenuBtn">
			<img src="<c:url value='/res/mobile/images/mainMenuIcon.png'/>" class="mainMenuIcon" />
		</a>
		<span class="headerTitle">链接开户设定</span>
		<div class="headerRightBox">
			<a href="<c:url value='/openUser/regLinkManager'/>" class="userSet"></a>
			<!--<a href="<c:url value='/openUser/addLinkUser'/>" class="userAdd"></a>-->
		</div>
	</div>
	<div class="container">
		<div class="addPersonBox">
			<div class="listForm">
				<form id="linkOpenAcct">
					<div class="listDetail">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>用户类型</span>
						</p>
						<div class="labelCondSelect">
							<select name="usertype" id="openAcctType" class="labelCond">
								<option value="0">会员</option>
								<option value="1">代理</option>
							</select>
							<i class="selectIcon"></i>
						</div>
					</div>
					<div class="listDetail">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>设置返点</span>
						</p>
						<div class="labelCondSelect">
							 <select  class="labelCond setRebate" id="setRebate" name="rebateratio"></select>
							<i class="selectIcon"></i>
						</div>
					</div>
					<div class="listDetail">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>有效期</span>
						</p>
						<div class="labelCondSelect">
							<select id="validTime" name="validtime" class="labelCond">
								<option value="1">1天</option>
                                <option value="3">3天</option>
                                <option value="7">7天</option>
                                <option value="15">15天</option>
                                <option value="30">30天</option>
                                <option value="0">永久</option>
							</select>
							<i class="selectIcon"></i>
						</div>
					</div>
					<div class="listDetail">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>推广渠道</span>
						</p>
						<input type="text" name="extaddress" id="extaddress"  class="labelCond" />
					</div>
					<div class="listDetail">
						<p class="detailTitle fontColorYellow">
							<i class="detailTitleLeft"></i>
							<span>推广QQ</span>
						</p>
						<input type="text" id="extQQ" name="extQQ" class="labelCond" onkeyup="inputNumber(this)" maxlength="11" />
					</div>
					<div class="btnGroup">
						<button type="button" class="btn btnRed" id="createExtCode">提交</button>
					</div>
				</form>
			</div>
		</div>

	</div>
</div>
<!-- 弹窗 -->
<div id="mask" class="mask" style="display: none;"></div>
<div class="dialogArea" id="dialogBox"></div>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/user/addLinkUser.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>

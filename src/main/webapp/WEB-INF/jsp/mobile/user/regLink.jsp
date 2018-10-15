<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
    <title>注册链接管理</title>
    <link rel="stylesheet" href="<c:url value='/res/mobile/css/hs-mobile.css?ver=${VIEW_VERSION}'/>" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<div class="generalPlatArea regLinkControlArea">
	<div class="mainNav">
		<a href="javascript:;" class="mainMenuBtn">
			<img src="<c:url value='/res/mobile/images/mainMenuIcon.png'/>" class="mainMenuIcon" />
		</a>
		<span class="headerTitle">注册链接管理</span>
		<div class="headerRightBox">
			<!--<a href="<c:url value='/openUser/regLinkManager'/>" class="userSet"></a>-->
			<a href="<c:url value='/openUser/addLinkUser'/>" class="userAdd"></a>
		</div>
	</div>
	<div class="container">
		<div class="listForm">
			<div class="regLinkControlBox">
				<c:forEach items="${list}" var="list" varStatus="s">
					<div class="regLinkDetail">
						<label for="clickContent-${s.index+1}" class="regLinkHeader cls">
							<em class="regLinkLeft"><span>${s.index+1}</span></em>
							<div class="regLinkContent">
								<p class="regLinkTitle">
									返点：<fmt:formatNumber type="number" value="${list.rebateRatio}" maxFractionDigits="1"/>%,${list.userType==0?'会员':'代理'},
									<c:choose>
										<c:when test="${list.flag==0}">已过期</c:when>
										<c:when test="${list.validTime==0}">永久</c:when>
										<c:otherwise>${list.validTime}天</c:otherwise>
									</c:choose>
									<!--目前注册次数：${list.registNum }-->
								</p>
								<p class="regLinkTitleTips">推广渠道：${list.extAddress}</p>
								<div class="regLinkDate">
									<span>
										<fmt:parseDate value="${list.createTime} " pattern="yyyy-MM-dd" var="receiveDate"></fmt:parseDate>
										<fmt:formatDate value="${receiveDate}" pattern="yyyy/MM/dd" ></fmt:formatDate>
									</span>
									<i class="downArrow"></i>
								</div>
							</div>
						</label>
						<input type="radio" id="clickContent-${s.index+1}" name="regLinkInput" style="display:none;"/>
						<div class="regLinkContentBox">
							<div class="regLinkList cls">
								<em class="regLinkLeft"></em>
								<div class="regLinkContent">
									<div class="regLinkInfo">
										<span class="regLinkInfoTitle">链接地址:</span>
										<span class="regLink">${list.registAddress }</span>
									</div>
									<div class="regLinkInfo">
										<span class="regLinkInfoTitle"></span>
										<a href="javascript:copyToClipBoard('${list.registAddress}');" class="copyRegLink">复制链接</a>
									</div>
									<div class="regLinkInfo">
										<span class="regLinkInfoTitle">二维码:</span>
										<div class="regLinkQRCode">
											<i class="QRCode"></i>
											<span>长按左图保存二维码，使用微信扫一扫右上角的“相册”扫码，再分享好友或者朋友圈。</span>
										</div>
									</div>
								</div>
							</div>
							<p class="deleteCurrLink">
								<a href="javascript:delUserExtCode('${list.id}');" class="deleteLink">
									<i class="deleteLinkIcon"></i>
									<span>删除链接</span>
								</a>
							</p>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>
<!-- 弹窗 -->
<div id="mask" class="mask" style="display: none;"></div>
<div class="dialogArea" id="dialogBox"></div>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/jquery.qrcode.min.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/user/regLink.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>

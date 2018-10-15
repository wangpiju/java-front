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
    <title>账户充值</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 账户明细 -->
<div class="generalPlatArea userDetailArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">账户明细</span>
    </div>
    <div class="container">
        <div class="userAcctDetailBox">
            <div class="userAcctInfoDiv">
                <p class="fontColorGray userAcctInfoTitle">账户余额</p>
                <p class="fontColorRed userAcctInfoAmount">${user.amount }</p>
            </div>
        </div>
        <div class="userDetailBox">
            <table class="userDetailTable" id="table">
                <thead>
                    <tr class="tableHeader">
                        <th width="110">时间</th>
                        <th width="110">订单类型</th>
                        <th width="155">金额</th>
                    </tr>
                </thead>
                <c:import url="amountChangeBody.jsp"></c:import>
            </table>
			<div class="loadMoreBox">
            	<a href="javascript:loadMore();" class="loadMore" id="loadMore">点击加载更多</a>
			</div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<c:url value='/res/mobile/js/jquery.dialog.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript">
var end = false;
var loading = false;
function loadMore() {
	if (end) {
		$("#loadMore").html("已加载全部");
		return false;
	}
	if (loading) {
		return false;
	}
	loading = true;
	var $loadMore = $("#loadMore");
	var defaultHtml = $loadMore.html();
	$loadMore.html("正在加载...");

	doLoad();
	
	loading = false;
	$loadMore.html(defaultHtml);
}

function doLoad() {
	ajaxExt({
		url:'/finance/amountChangeBody?start=' + ($(".amountChangeCountClass").length),
		method:"get",
		data: "",
		async: false,
		callback:function(data) {
			$("#table tbody").last().after(data);
		}
	});
}

$(window).scroll(function(){
	if($(this).scrollTop() + $(this).height() == $(document).height()){
		loadMore();
	}
});
</script>

</body>
</html>

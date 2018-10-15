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
    <title>投注记录</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>
<!-- 投注记录 -->
<div class="betsListArea generalPlatArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">投注记录</span>
    </div>
    <div class="container">
        <div class="betsListBox">
            <table class="betsListTable" id="table">
                <thead>
                    <tr class="tableHeader">
                        <th width="90">时间</th>
                        <th>彩种</th>
                        <th>金额</th>
                        <th>状态</th>
                    </tr>
                </thead>
                <c:import url="gameRecordBody.jsp"></c:import>
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
		url:'/game/gameRecordBody?start=' + ($(".gameRecordCountClass").length),
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

//撤销投注
$(document).on("click","a.cancelOrder",function(ev){
	var a = $(this);
	var url = a.attr("href");
	ajaxExt({
		url:url,
		callback:function(rel){
			$.alert({text: "撤销成功", icon: 'ok'});
			a.siblings().text("个人撤单");
			a.remove();
		}
	});
	ev.stopPropagation();
	ev.preventDefault();
	return false;
});
</script>

</body>
</html>

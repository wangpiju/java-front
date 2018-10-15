<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 通用右侧导航 -->
<div id="asideRight">
	<a class="sy" href="<c:url value='/' />" title="首页"></a>
	<a class="zxgg" href="<c:url value='/notice/index' />" title="最新公告"></a>
	<a class="yxjl" href="<c:url value='/game/index?tabId=gameBetList' />" title="游戏记录"></a>
	<a class="zhjl" href="<c:url value='/game/index?tabId=trace' />" title="追号记录"></a>
	<a class="glzx" href="<c:url value='/user/index' />" title="管理中心"></a>
	<a class="bbzx" href="<c:url value='/report/index' />" title="报表中心"></a>
	<a class="bzzx" href="<c:url value='/helpCenter/index' />" title="帮助中心"></a>
	<a class="top" href="#top"></a>
</div>
<script>
	$(function(){
		var href = document.location.href;
		if(href == document.location.origin || href == (document.location.origin+'/') || href == (document.location.origin+'/index')){
			$('.sy').addClass('active');
		}else if(href.indexOf('notice/index')>0){
			$('.zxgg').addClass('active');
		}else if(href.indexOf('gameBetList')>0){
			$('.yxjl').addClass('active');
		}else if(href.indexOf('trace')>0){
			$('.zhjl').addClass('active');
		}else if(href.indexOf('report/index')>0){
			$('.bbzx').addClass('active');
		}else if(href.indexOf('user/index')>0){
			$('.glzx').addClass('active');
		}else if(href.indexOf('helpCenter/index')>0){
			$('.bzzx').addClass('active');
		}
	});
</script>
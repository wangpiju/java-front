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
    <title>我的消息</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 平台消息 -->
<div class="generalPlatArea platMsgArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">我的消息</span>
		<a href="/message/messageTable" class="returnBtn">
			<img src="/res/mobile/images/returnIcon.png" />
			<span>返回</span>
		</a>
    </div>
    <div class="tableBox">
		<div class="messageContentDiv" id="messageContentDiv">
			<c:forEach items="${messageContentList }" var="mc">
				<c:choose>
					<c:when test="${user.account != mc.sender }">
						<p class="textLeft">
							<span class="msgUserName">${message.sendType == 0 ? '系统' : mc.sender }</span>
							<!--<span>：</span>-->
							<span class="messageInfo messageLeft">
								<c:out value="${mc.content }"></c:out>
							</span>
						</p>
					</c:when>
					<c:otherwise>
						<p class="textRight">
							<span class="messageInfo messageRight">
								<c:out value="${mc.content }"></c:out>
							</span>
							<!--<span>：</span>-->
							<span class="msgUserName">我</span>
						</p>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
		<!-- 填写发送消息-->
		<div class="msgSendContentDiv">
			<label>
				<textarea class="msgSendContent replyMsgContent" id="msgSendContent" placeholder="请输入回复内容"></textarea>
			</label>
			<!-- 弹窗按钮 -->
			<div class="btnGroup">
				<a href="javascript:;" class="btn showMessageReply btnRed" id="showMessageReply" onclick="messageReply(${message.id });">回复</a>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var specicalStr = "${specicalStr }";
function checkHadSpecialChar(text) {
	for (var i in specicalStr) {
		if (text.indexOf(specicalStr[i]) >= 0) {
			return true;
		}
	}
	return false;
}
function messageReply(id) {
	var $revContentObj = $("#msgSendContent");
	var revContent = $revContentObj.val();
	if (!revContent) {
		$.alert("请输入回复内容！");
		return false;
	}
	if (checkHadSpecialChar(revContent)) {
		$.alert("回复内容不能包含特殊字符！");
		return false;
	}
	
	if (id) {
		Service("/message/messageContentReply","POST",{"id":id, "revContent":revContent},1,function(data){
			if (data == 1) {
				$("#messageContentDiv").append('<p class="textRight"><span class="messageInfo messageRight" style="margin-right: 10px;">' + revContent + '</span><span class="msgUserName">我</span><p>').scrollTop($('#messageContentDiv')[0].scrollHeight);
				$("#msgSendContent").val("").focus();
			}
		});
	}
}
if (contentTimer) {
	clearInterval(contentTimer);
}
var doing = false;
var startTime = '${startTime }';
var contentTimer = setInterval(function() {
	if (doing) {
		return false;
	}
	doing = true;
	ajaxExt({
		url:'/message/findMessageContent?messageId=${message.id }&startTime=' + startTime,
		dataType:'json',
		loading:'',
		noError:true,
		callback:function(data){
			done(data);
		},
		complete:function(){
		}
	});
}, 10000);
function done(data) {
	var list = data.messageContentList;
	startTime = data.startTime;
	var html = "";
	for (var i = 0; i < list.length; i++) {
		var mc = list[i];
		html += '<p class="textLeft"><span class="msgUserName">' + mc.sender + '</span><span class="messageInfo messageLeft">' + mc.content + '</span><p>';
	}
	$("#messageContentDiv").append(html);
	doing = false;
	$('#messageContentDiv').scrollTop($('#messageContentDiv')[0].scrollHeight);
}
</script>

</body>
</html>
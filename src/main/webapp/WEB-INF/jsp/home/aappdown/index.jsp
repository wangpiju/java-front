<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="<c:url value='/res/home/css/appdown.css?ver=${VIEW_VERSION}'/>">
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<script type="text/javascript" src="<c:url value='/res/home/js/qrcode.js?ver=${VIEW_VERSION}'/>"></script>
	<title>大順 - APP下载</title>
</head>
<body>
<div class="appDownload">
	<div class="appBox">
		<input id="text" type="text" value="http://www.baidu.com" style="width:80%;display:none;"/>
		<input id="texta" type="text" value="http://www.baidu.com" style="width:80%;display:none;"/>
		<p class="appDownloadUrl"></p>
		<div class="imgBox">
			<div id="qrcode" style="width:106px; height:106px;"></div>
		</div>
		<div class="imgBoxa">
			<div id="qrcodea" style="width:106px; height:106px;"></div>
		</div>
		<div class="appDownloadTitle">
			<span>Android</span>
			<span>IOS</span>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		var qrcode = new QRCode(document.getElementById("qrcode"), {width: 106, height: 106});
		var qrcodea = new QRCode(document.getElementById("qrcodea"), {width: 106, height: 106});
		var herf = document.location.host.replace('www.', '') ;
		$("#text").attr("value", "https://" + herf + "/app/android.apk");
		$("#texta").attr("value", "http://xmvip.vip/uyH3U4");
		function makeCode() {
			var elText = document.getElementById("text");
			var elTexta = document.getElementById("texta");
			qrcode.makeCode(elText.value);
			qrcodea.makeCode(elTexta.value);
		}
		makeCode();
		$(".appDownloadUrl").html("m." + herf);
	});
</script>
</body>
</html>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>充值未到账</title>
	<link rel="stylesheet" href="<c:url value='/res/home/css/wk-recharge.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>" type="image/x-icon"/>
	<script type="text/javascript">
		var message = '${message}';
		$(function () {
			if (message) {
				$.alert(message);
			}
		});
	</script>
</head>
<body>
<div class="areaBigContainer mainWidth" id="rechargeOutstanding">
	<div class="containerFlex">
		<!-- 列表 -->
		<c:import url="../recharge/rechargeNav.jsp"></c:import>
		<!-- 内容 -->
		<div class="rightArea">
			<div class="accountCentreContent">
				<form id="rechOutForm" action="/recharge/rechargeOutstanding" method="post"
					  enctype="multipart/form-data">
					<div>
						<span class="labelTitle">充值方式：</span>
						<label class="radioLabel" for="rechargeType0">
							<input type="radio" name="rechargeType" value="0" id="rechargeType0" class="rechargeType"
								   checked/>
							银行转账
						</label>
						<label class="radioLabel" for="rechargeType3">
							<input type="radio" name="rechargeType" value="3" id="rechargeType3" class="rechargeType"/>
							支付宝转银行卡
						</label>
						<label class="radioLabel" for="rechargeType1">
							<input type="radio" name="rechargeType" value="1" id="rechargeType1" class="rechargeType"/>
							AA支付宝
						</label>
						<label class="radioLabel" for="rechargeType2">
							<input type="radio" name="rechargeType" value="2" id="rechargeType2" class="rechargeType"/>
							以上都不是请选我
						</label>
					</div>
					<div id="amountBox">
						<span class="labelTitle" style="width: 140px">充值金额：</span>
						<input type="text" class="labelCond" name="amount" id="amount"/>
					</div>
					<div id="nameBox">
						<span class="labelTitle" style="width: 140px">存款人姓名：</span>
						<input type="text" class="labelCond" name="name" id="name"/>
					</div>
					<div id="lastFourBox">
						<span class="labelTitle" style="width: 140px">存款卡号后4位：</span>
						<input type="text" class="labelCond" name="card" id="lastFour" maxlength="4"/>
					</div>
					<div id="aliAccountBox" style="display:none;">
						<span class="labelTitle" style="width: 140px">支付宝账号：</span>
						<input type="text" class="labelCond" name="aliAccount" id="aliAccount"/>
					</div>
					<div id="aliIdBox" style="display:none;">
						<span class="labelTitle" style="width: 140px">支付宝订单号：</span>
						<input type="text" class="labelCond" name="aliId" id="aliId"/>
					</div>
					<div id="aliNameBox" style="display:none;">
						<span class="labelTitle" style="width: 140px">存款支付宝认证姓名：</span>
						<input type="text" class="labelCond" name="aliName" id="aliName"/>
					</div>
					<div id="imgBox">
						<span class="labelTitle" style="width: 140px">上传截图：</span>
						<input type="file" class="labelCond" name="img" id="img" accept="image/png,image/jpeg"/>
					</div>
					<div>
						<span class="labelTitle"></span>
						<a href="javascript:;" class="btn" id="submit">提交</a>
					</div>
				</form>
				<p class="userMsg outstandingMsg">
					温馨提示：如充值5分钟后还未到账，请使用该功能催促！
					<br/>填写完毕后将有专职工作人员处理，请您耐心等待片刻，感谢您的支持与理解！
				</p>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
		src="<c:url value='/res/home/js/finance/rechargeOutstanding.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
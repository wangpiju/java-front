<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>充值-提现</title>
	<link rel="stylesheet" href="<c:url value='/res/home/css/wk-recharge.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
</head>
<body>
<div class="areaBigContainer mainWidth" id="rechargeArea">
	<div class="containerFlex">
		<!-- 列表 -->
		<c:import url="../recharge/rechargeNav.jsp"></c:import>
		<!-- 内容 -->
		<div class="rightArea">
			<div class="accountCentreContent">
				<!-- 选择银行、金额 -->
				<form action="/recharge/rechargeMoney" id="changeBankAreaForm" method="post"
					  onsubmit="return checkForm();">
					<div id="changeBankArea" class="changeBankArea">
						<input type="hidden" id="rechargeType" name="rechargeType" value="1"/>
						<input type="hidden" id="bankApiId" name="bankApiId" value="${bankApi.id }"/>
						<input type="hidden" id="classKey" name="classKey" value="${bankApi.classKey }"/>
						<div class="stepBox">
							<ul class="stepBar">
								<li><span class="step step-1 active"><i>01</i>选择银行并填写充值金额</span></li>
								<li><span class="step step-2"><i>02</i>进行扫码支付</span></li>
								<li><span class="step step-3"><i>03</i>完成</span></li>
							</ul>
							<a href="/helpCenter/index?id=${bankApi.articleId }"" class="rechTeach">如何充值？</a>
						</div>
						<div class="changeBankBox">
							<div class="bank-l">
								<span class="labelTitle">请选择银行卡：</span>
							</div>
							<div class="bank-r">
								<c:forEach items="${bankNameList }" var="bankName">
									<label class="bank">
										<input type="radio" class="btnRadio" id="bankNameId" name="bankNameId"
											   value="${bankName.id }"
											   onclick="selectBank('${bankName.id }', '${bankApi.minAmount }','${bankApi.maxAmount }', this)"/>
										<img src="<c:url value='/res/home/images/bank/${bankName.code }.png'/>"
											 class="bankImg" alt="${bankName.title }"/>
									</label>
								</c:forEach>
							</div>
						</div>
						<div class="changeSumBox">
							<p>
								<span class="labelTitle">充值金额：</span>
								<label for="#" class="rechAmountDetail">
									<input type="text" class="labelCond" disabled="disabled" id="chargeamount"
										   name="chargeamount"/>
									<span>元</span>
									<label class="rechAmountInfo">
										<i class="rechAmountInfo-l"></i>
										<span class="rechAmountInfo-c" id="rechAmountInfo"></span>
										<i class="rechAmountInfo-r"></i>
										<i class="rechAmountInfo-b"></i>
									</label>
								</label>
								<span id="errorSpan" class="fontColorRed"></span>
							</p>
							<c:if test="${not empty bankApi.poundage}">
								<p class="comChargeBox">
									<i></i>本次充值实际到账<span id="actualAmount">0</span>元，
									<em>手续费<span id="commissionCharge">0</span>元（手续费率<span
											id="comChargeRate">${bankApi.poundage}</span>%）</em>
								</p>
							</c:if>
							<p class="userMsg fontColorGray">
								<i class="warnIcon"></i>
								<span id="initialMoney" style="display: none">充值金额：单笔最低充值金额为<span
										id="txtMinAmount"></span>元，最高<span id="txtMaxAmount"></span>元</span>
								<span>充值时限：请在15分钟内完成充值</span>
							</p>
						</div>
						<div class="changeBankBtn">
							<input class="btn" type="submit" value="下一步"/>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<c:import url="../recharge/rechargeUnRecharge.jsp"></c:import>
<c:import url="../recharge/rechargeSafePassword.jsp"></c:import>
<input type="hidden" value="${error }" id="errorMessage"/>
<script type="text/javascript" src="<c:url value='/res/home/js/recharge/recharge.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
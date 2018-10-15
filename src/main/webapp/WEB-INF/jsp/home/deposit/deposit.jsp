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
<div class="areaBigContainer mainWidth" id="depositArea">
	<div class="containerFlex">
		<!-- 列表 -->
		<c:import url="../recharge/rechargeNav.jsp"></c:import>
		<!-- 内容 -->
		<div class="rightArea">
			<div class="accountCentreContent">
				<!-- 提现 -->
				<form action="/deposit/depositMoney" id="depositAreaForm" method="post" onsubmit="return checkForm();">
					<input type="hidden" name="keySafePassword" value="${keySafePassword }"/>
					<div class="depositArea">
						<p class="userMsg fontColorGray depositMsg">
							<i class="warnIcon"></i>
							<span>提现所产生的银行手续费由平台为您免除。提现限额：最低${moneyDepositMin }元，最高${moneyDepositMax }元</span>
						</p>
						<div class="depositBox">
							<p>
								<span class="labelTitle">账户余额：</span>
								<span class="fontColorRed">${user.amount}元</span>
							</p>
							<p>
								<span class="labelTitle">可提现额度：</span>
								<span class="fontColorRed">${actualBalanceZ}元</span>
							</p>
							<div class="depositChangeBank">
								<span class="labelTitle">选择银行卡：</span>
								<p class="depositBanks">
									<c:forEach items="${bankUserList }" var="bankUser">
										<label class="bank">
											<input type="radio" class="btnRadio" id="bankCardId" name="bankCardId"
												   value="${bankUser.id }"/>
											<img src="<c:url value='/res/home/images/bank/${bankNameMap[bankUser.bankNameId].code }.png'/>"
												 class="bankImg" alt="${bankNameMap[bankUser.bankNameId].title }"/>
											<span>开户人姓名：${bankUser.niceName } 银行卡号：${bankUser.card }</span>
										</label>
									</c:forEach>
								</p>
							</div>
							<p>
								<span class="labelTitle">提现金额：</span>
								<label for="#" class="rechAmountDetail">
									<input type="text" class="labelCond depositAmount" id="amount" name="amount"/>
									<span>元</span>
									<label class="rechAmountInfo">
										<i class="rechAmountInfo-l"></i>
										<span class="rechAmountInfo-c" id="rechAmountInfo"></span>
										<i class="rechAmountInfo-r"></i>
										<i class="rechAmountInfo-b"></i>
									</label>
								</label>
								<span class="errorWarn fontColorGray">(您本次最多提现${actualBalanceZ}元，今天还可以提现${countDepositMax - user.depositCount}次)</span>
							</p>
							<label>
								<span class="labelTitle"></span>
								<input class="btn depositBtn" type="submit" value="申请提现"/>
								<span id="errorSpan" class="fontColorRed"></span>
							</label>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var sysMinDeposit = "${moneyDepositMin }";
	var sysMaxDeposit = "${moneyDepositMax }";
	var canDepositMoney = "${actualBalanceZ}";
	var canDepositCount = "${countDepositMax - user.depositCount }";
	var checkMoney = function (obj) {
		var me = obj, v = me.value, index;
		me.value = v = v.replace(/^\.$/g, '');
		index = v.indexOf('.');
		if (index > 0) {
			me.value = v = v.replace(/(.+\..*)(\.)/g, '$1');
			if (v.substring(index + 1, v.length).length > 2) {
				me.value = v = v.substring(0, v.indexOf(".") + 3);
			}
		} else {
			me.value = v = v.replace(/^0/g, '');
		}
		me.value = v = v.replace(/[^\d|^\.]/g, '');
		me.value = v = v.replace(/^00/g, '0');
	};
	var checkForm = function () {
		if (parseInt(canDepositCount) <= 0) {
			$("#errorSpan").html("今天可提现次数:" + canDepositCount);
			return false;
		}
		var bankCardId = $("#bankCardId:checked").val();
		if (!bankCardId) {
			$("#errorSpan").html("请选择银行！");
			return false;
		}
		var amount = $("#amount").val();
		if (!amount || amount <= 0) {
			$("#errorSpan").html("请输入提款金额！");
			return false;
		}
		if (parseFloat(amount) < parseFloat(sysMinDeposit)) {
			$("#errorSpan").html("最低提现额度为：" + sysMinDeposit);
			return false;
		}
		if (parseFloat(amount) > parseFloat(sysMaxDeposit)) {
			$("#errorSpan").html("最高提现额度为：" + sysMaxDeposit);
			return false;
		}

		if (parseFloat(amount) > parseFloat(canDepositMoney)) {
			$("#errorSpan").html("可提现余额不足！");
			return false;
		}
		$("#errorSpan").html("");
		return true;
	};

	//填写充值金额显示大号字体提示
	function rechAmountShow(num) {
		if (num) {
			$('#rechAmountInfo').html(num).parent().show();
		} else {
			$('#rechAmountInfo').parent().hide();
		}
	}
	
	$(function () {
		$("#amount").keyup(function () {
			if (this.value > parseInt(canDepositMoney)) {
				this.value = parseInt(canDepositMoney);
			}
			checkMoney(this);
			checkForm();
			rechAmountShow(this.value);
		}).on('focus', function () {
			if (this.value) {
				$('#rechAmountInfo').parent().show();
			}
		}).on('blur', function () {
			$('#rechAmountInfo').parent().hide();
		});
	});
</script>
</body>
</html>
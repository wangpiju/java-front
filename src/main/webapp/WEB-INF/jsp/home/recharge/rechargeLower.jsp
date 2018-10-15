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
				<c:if test="${user.accountRecharge == 1}">
					<!-- 选择银行、金额 -->
					<form action="" id="rechargeLowerForm" method="post" onsubmit="return checkForm();">
						<div id="changeBankArea" class="changeBankArea">
							<input type="hidden" id="rechargeType" name="rechargeType" value="2"/>
							<div class="changeSumBox">
								<label>
									<span class="labelTitle">目标用户：</span>
									<input type="text" class="labelCond" id="targetUser" name="targetUser"/>
									<span id="errorSpanTargetUser" class="fontColorRed"></span>
								</label>
							</div>
							<div class="changeSumBox">
								<label>
									<span class="labelTitle">充值金额：</span>
									<input type="text" class="labelCond" id="chargeamount" name="chargeamount"/>
									<span>元</span>
									<span id="errorSpanChargeamount" class="fontColorRed"></span>
								</label>
							</div>
							<div class="changeSumBox">
								<label>
									<span class="labelTitle">安全密码：</span>
									<input type="password" class="labelCond" id="sourceUserSafePassword"
										   name="sourceUserSafePassword"/>
									<span id="errorSpanSourceUserSafePassword" class="fontColorRed"></span>
								</label>
							</div>
							<div class="changeBankBtn">
								<input class="btn" type="button" onclick="submitForm();" value="确认"/>
							</div>
						</div>
					</form>
				</c:if>
				<c:if test="${user.accountRecharge != 1}">
					<div class="depositArea userSetInfoContentArea">
						<div class="depositBox" style="color: red">
							您的会员转账功能尚未开通！
						</div>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</div>
<c:import url="../recharge/rechargeSafePassword.jsp"></c:import>
<script type="text/javascript">
	var checkMoney = function (obj) {
		var me = obj, v = me.value, index;
		me.value = v = v.replace(/^\.$/g, '');
		index = v.indexOf('.');
		if (index > 0) {
			me.value = v = v.replace(/(.+\..*)(\.)/g, '$1');
			if (v.substring(index + 1, v.length).length > 2) {
				me.value = v = v.substring(0, v.indexOf(".") + 3);
			}
		}
		me.value = v = v.replace(/[^\d|^\.]/g, '');
		me.value = v = v.replace(/^00/g, '0');
	};
	var checkForm = function () {
		$("#errorSpanTargetUser,#errorSpanChargeamount,#errorSpanSourceUserSafePassword").html("");
		if (!checkUnRecharge()) {
			return false;
		}
		var targetUser = $('#targetUser').val();
		if (!targetUser) {
			$("#errorSpanTargetUser").html("目标用户不能为空！");
			return false;
		}
		var amount = $('#chargeamount').val();
		if (!amount || amount <= 0) {
			$("#errorSpanChargeamount").html("请输入充值金额！");
			return false;
		}
		var targetUser = $('#sourceUserSafePassword').val();
		if (!targetUser) {
			$("#errorSpanSourceUserSafePassword").html("请输入安全密码！");
			return false;
		}
		return true;
	};

	function submitForm() {
		if (checkForm()) {
			ajaxObject('/recharge/rechargeLower?' + $("#rechargeLowerForm").serialize(), "POST", function (data) {
				$("#rechargeLowerForm")[0].reset();
				$.alert(data);
			});
		}
	}

	$(function () {
		var errorMessage = '${error }';
		if (errorMessage) {
			$.alert(errorMessage);
		}
		$("#chargeamount").keyup(function () {
			checkMoney(this);
		});
		$("#targetUser,#chargeamount,#sourceUserSafePassword").blur(function () {
			checkForm();
		});
		checkUnRecharge();
	});

	function checkUnRecharge() {
		if (!checkHadNotSafePassword()) {
			return false;
		}
		return true;
	}
</script>
</body>
</html>
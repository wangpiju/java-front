<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<!-- 账户充值 -->
<div class="generalPlatArea userRechargeArea" id="userRechargeArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">充值</span>
    </div>
    <div class="container">
        <div class="userRechargeListBox">
        	<form action="/recharge/rechargeMoney" id="changeBankAreaForm" method="post" onsubmit="return checkForm();">
        	<input type="hidden" id="rechargeType" name="rechargeType" value="1" />
           	<input type="hidden" id="bankApiId" name="bankApiId" value="${bankApi.id }" />
           	<input type="hidden" id="classKey" name="classKey" value="${bankApi.classKey }" />
            <div class="listForm">
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>充值方式</span>
                    </p>
                    <div class="labelCondSelect">
                        <select class="labelCond" id="selectRechargeTypeId" onchange="selectRechargeType(this.value)">
                        	<c:if test="${hadBankLevel }">
                            	<option value="/recharge/rechargeMoney" selected="selected">银行转账</option>
                            </c:if>
                            <c:forEach items="${bankApiList }" var="ba">
                            	<c:if test="${ba.isSupportMobile == 1 || ba.isSupportMobile == 2 }">
						    		<option value="/recharge/rechargePay/${ba.classKey }/${ba.id }" ${ba.id == bankApi.id ? 'selected' : '' }>${ba.title }</option>
						    	</c:if>
						    </c:forEach>
                        </select>
                        <i class="selectIcon"></i>
                    </div>
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>充值银行</span>
                    </p>
                    <div class="labelCondSelect">
                        <select class="labelCond" id="bankNameId" name="bankNameId" onchange="selectBank();">
                        	<c:forEach items="${bankNameList }" var="bankName">
                            	<option value="${bankName.id }">${bankName.title }</option>
                            </c:forEach>
                        </select>
                        <i class="selectIcon"></i>
                    </div>
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>充值金额</span>
                        <c:forEach items="${bankNameList }" var="bankName">
                        	<input type="hidden" id="bank_minAmount_${bankName.id }" value="${bankApi.minAmount }" />
                        	<input type="hidden" id="bank_maxAmount_${bankName.id }" value="${bankApi.maxAmount }" />
                        </c:forEach>
                        <span class="detailTips fontColorGray" id="initialMoney" style="display: none">单次充值最小金额<span id="txtMinAmount"></span>元，最高<span id="txtMaxAmount"></span>元</span>
                    </p>
                    <div class="labelCondSelect">
                        <input type="tel" class="labelCond" disabled="disabled" id="chargeamount" name="chargeamount"/>
                        <i id="chargeamountSpan" class="selectIcon"></i>
                    </div>
                    <c:if test="${not empty bankApi.poundage}">
						<p class="comChargeBox">本次充值实际到账<span id="actualAmount">0</span>元，手续费<span id="commissionCharge">0</span>元（手续费率<span id="comChargeRate">${bankApi.poundage}</span>%）</p>
					</c:if>
                </div>
            </div>
            <div class="btnGroup">
                <button class="btn btnRed" type="submit">下一步</button>
            </div>
            </form>
        </div>
    </div>
</div>

<c:import url="../recharge/rechargeUnRecharge.jsp"></c:import>
<c:import url="../recharge/rechargeSafePassword.jsp"></c:import>

<script type="text/javascript" src="<c:url value='/res/mobile/js/jquery.dialog.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript">
function selectRechargeType(value) {
	window.location.href = value;
}
function selectBank() {
	var bankNameId = $("#bankNameId").val();
	var minAmount = $("#bank_minAmount_" + bankNameId).val();
	var maxAmount = $("#bank_maxAmount_" + bankNameId).val();
	if (bankNameId) {
		$("#initialMoney").show();
		$("#chargeamount").removeAttr("disabled");
		$("#card").removeAttr("disabled");
		$("#niceName").removeAttr("disabled");
		
		$("#txtMinAmount").html(minAmount);
		$("#txtMaxAmount").html(maxAmount);
	} else {
		$("#initialMoney").hide();
		$("#chargeamount").attr("disabled", "disabled");
		$("#card").attr("disabled", "disabled");
		$("#niceName").attr("disabled", "disabled");
	}
	
	checkUnRecharge();
}

var checkMoney = function(obj) {
	var me = obj,v = me.value,index;
	me.value = v = v.replace(/^\.$/g, '');		
	index = v.indexOf('.');
	if (index > 0) {
		me.value = v = v.replace(/(.+\..*)(\.)/g, '$1');			
		if(v.substring(index + 1, v.length).length > 2) {				
			me.value= v  = v.substring(0, v.indexOf(".") + 3);
		}
	}	
	me.value = v = v.replace(/[^\d|^\.]/g, '');
	me.value = v = v.replace(/^00/g, '0');				
};

var checkForm = function(){		
	if (!checkUnRecharge()) {
		return false;
	}
	
	var selectRechargeType = $("#selectRechargeTypeId").val();
	if (!selectRechargeType) {
		$.alert("未选择充值方式！");
		return false;
	}
	var bankNameId = $("#bankNameId").val();
	if (!bankNameId) {
		$.alert("未选择银行卡");
		return false;
	}
	var amount = $('#chargeamount').val();
	if (!amount || amount <= 0) {
		$("#chargeamountSpan").addClass("wrongIcon").removeClass("rightIcon");
		return false;
	}
	var minmoney = parseFloat($("#txtMinAmount").html());
	var maxmoney = parseFloat($("#txtMaxAmount").html());
	if($.trim(amount) != "") {
		if(parseFloat(amount) > maxmoney || parseFloat(amount) < minmoney) {
			$("#chargeamountSpan").addClass("wrongIcon").removeClass("rightIcon");
			return false;
		} else {
			$("#chargeamountSpan").addClass("rightIcon").removeClass("wrongIcon");
			return true;
		}	
	}
	return false;
};

$(function(){
	var errorMessage = '${error }';
	if (errorMessage) {
		$.alert(errorMessage);
	}
	
	$("#chargeamount").keyup(function(){
		var v = this.value;
		var maxmoney = $("#txtMaxAmount").text();
        maxmoney = parseInt(maxmoney.replace(',','')); // 拿掉数字FORMAT 才能作比较
		if(Number(v) > maxmoney) {
			$(this).val(maxmoney);
		}
		checkMoney(this);
		commissionCharge();
	    checkForm();		
	});
	
	checkUnRecharge();
	
	selectBank();
	$("#chargeamount").focus();
});

function checkUnRecharge() {
	if (!checkHadUnRecharge()) {
		return false;
	}
	if (!checkHadNotSafePassword()) {
		return false;
	}
	return true;
}

function showDiv(id) {
	$("#unRechargeDiv").hide();
	$("#userRechargeArea").hide();
	$("#setSafePassword").hide();
	
	$("#" + id).show();
}

//充值手续费
function commissionCharge(){
	var rate = $('#comChargeRate').text()/100;
	var amount =$('#chargeamount').val();
		amount = amount == '' ? 0 : amount;
	var commissionCharge = (amount * rate).toFixed(4);
	if((commissionCharge < 0.1) && (commissionCharge > 0)){
		commissionCharge = 0.1;
	}
	$('#actualAmount').text((amount - commissionCharge).toFixed(4));
	$('#commissionCharge').text(commissionCharge);
}
</script>
</body>
</html>

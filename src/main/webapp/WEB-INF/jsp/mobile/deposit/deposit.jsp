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
    <title>提现</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 提现 -->
<div class="generalPlatArea userDepositArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">提现</span>
    </div>
    <div class="container">
        <!-- 提现选项 -->
        <div class="userDepositBox">
            <div class="userAcctDetailBox">
                <div class="userAcctInfoDiv">
                    <p class="fontColorGray userAcctInfoTitle">账户余额</p>
                    <p class="fontColorRed userAcctInfoAmount">${user.amount }</p>
                </div>
            </div>
            <form action="/deposit/depositMoney" id="depositAreaForm" method="post" onsubmit="return checkForm();">
            <input type="hidden" name="keySafePassword" value="${keySafePassword }" />
            <div class="userDepositListForm">
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>提款至：</span><span id="errorSpan2" style="color: red"></span>
                    </p>
                    <div class="labelCondSelect">
                        <select class="labelCond" id="bankCardId" name="bankCardId">
                            <option value="">已绑定的银行卡</option>
                            <c:forEach items="${bankUserList }" var="bankUser">
                            	<option value="${bankUser.id }">开户人姓名：${bankUser.niceName } 银行卡号：${bankUser.card }</option>
                            </c:forEach>
                        </select>
                        <i class="selectIcon"></i>
                    </div>
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorYellow">
                        <i class="detailTitleLeft"></i>
                        <span>提款金额：</span><span id="errorSpan" style="color: red"></span>
                        <span class="detailTips fontColorGray">单次提款最低${moneyDepositMin }元，可提款${countDepositMax - user.depositCount }次</span>
                    </p>
                    <input id="amount" name="amount" type="tel" class="labelCond" />
                </div>
                <div class="btnGroup">
                    <input class="btn btnRed" type="submit" value="申请提现" />
                </div>
            </div>
            </form>
        </div>
    </div>
</div>

<c:import url="../recharge/rechargeSafePassword.jsp"></c:import>

<script type="text/javascript" src="<c:url value='/res/mobile/js/jquery.dialog.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript">
	var sysMinDeposit = "${moneyDepositMin }";
	var sysMaxDeposit = "${moneyDepositMax }";
	var canDepositMoney = "${user.amount }";
	var canDepositCount = "${countDepositMax - user.depositCount }";
	var checkMoney = function(obj) {
		var me = obj,v = me.value,index;
		me.value = v = v.replace(/^\.$/g, '');		
		index = v.indexOf('.');
		if (index > 0) {
			me.value = v = v.replace(/(.+\..*)(\.)/g, '$1');			
			if(v.substring(index + 1, v.length).length > 2) {				
				me.value= v  = v.substring(0, v.indexOf(".") + 3);
			}
		} else {
			me.value = v = v.replace(/^0/g, '');
		}		
		me.value = v = v.replace(/[^\d|^\.]/g, '');
		me.value = v = v.replace(/^00/g, '0');				
	};
	
	var checkForm = function() {
		$("#errorSpan").html("");
		$("#errorSpan2").html("");
		
		if (parseInt(canDepositCount) <= 0) {
			$("#errorSpan").html("今天可提现次数:" + canDepositCount);
			return false;
		}
		var bankCardId = $("#bankCardId").val();
		if (!bankCardId) {
			$("#errorSpan2").html("请选择银行！");
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
		
		return true;
	};
	
	$(function(){
		$("#amount").keyup(function(){
			checkMoney(this);
		    checkForm();		
		});
	});
</script>
</body>
</html>

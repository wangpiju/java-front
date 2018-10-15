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
                <input type="hidden" id="receiveBankId" name="receiveBankId" />
				<input type="hidden" id="rechargeWay" name="rechargeWay" value="${rechargeWay}" />
                <input type="hidden" id="rechargeWay" name="waytype" value="${waytype}" />
				<input type="hidden" id="successFlag" name="successFlag" value="0" />

                <c:choose>
                    <c:when test="${waytype == 1}">
                        <div class="rechLeftInfo" style="margin-left:150px;">
                            <img id="QRCodeUrl" src="" style="height: 250px;width: 200px" />
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="changeSumBox">
                                <span class="labelTitle">收款银行：</span>
                                <span id="receiveBankName" name="receiveBankName"></span>
                        </div>
                        <div class="changeSumBox">
                                <span class="labelTitle">收款人姓名：</span>
                                <span id="receiveNiceName" name="receiveNiceName"></span>
                        </div>
                        <div class="changeSumBox">
                                <span class="labelTitle">收款卡号：</span>
                                <span id="receiveCard" name="receiveCard"></span>
                        </div>
                        <div class="changeSumBox">
                                <span class="labelTitle">支行地址：</span>
                                <span id="receiveAddress" name="receiveAddress"></span>
                        </div>
                    </c:otherwise>
                </c:choose>

				<div class="changeSumBox">
					<span class="labelTitle">充值金额：</span>
					<label for="#" class="rechAmountDetail">
						<input type="text" class="labelCond" id="chargeamount" name="chargeamount"/>
						<span>&nbsp;&nbsp;元</span>
						<label class="rechAmountInfo">
							<i class="rechAmountInfo-l"></i>
							<span class="rechAmountInfo-c" id="rechAmountInfo"></span>
							<i class="rechAmountInfo-r"></i>
							<i class="rechAmountInfo-b"></i>
						</label>
					</label>
				</div>
				<div class="changeSumBox">
					<span class="labelTitle">充值人姓名：</span>
					<label for="#" class="rechAmountDetail">
						<input type="text" class="labelCond" id="niceName" name="niceName"/>
					</label>
				</div>
				<div class="changeSumBox">
					<span class="labelTitle">附言：</span>
					<label for="#" class="rechAmountDetail">
						<input type="text" class="labelCond" id="checkCode" name="checkCode"/>
                            &nbsp;&nbsp;<span id="attsecond" style="color: red"></span>
					</label>
				</div>
                </br>
				<div style="margin-left:200px;">
					<input class="btn" id="setPayRequest" type="submit" onclick="setPayRequest()" value="充值申请"/>
				</div>
    <div  class="note" style=" width: 100%;height: 200px;border: 1px solid #e6bf9d;background: #f5ede6;margin-top: 30px;">
    <c:choose>
        <c:when test="${rechargeWay == 5}">
            <p style='margin-left: 30px;margin-top: 10px;color:#f37100;'>
            ※ &nbsp; 温馨提示：<br>
            1、请转到以上收款银行账户。<br>
            2、请正确填写转账银行卡的持卡人姓名和充值金额，以便及时核对。<br>
            3、转账1笔提交1次，请勿重复提交订单。<br>
            4、请务必转账后再提交订单，否则无法及时查到您的款项！</p>
        </c:when>
        <c:otherwise>
            <p style='margin-left: 30px;margin-top: 10px;color:#f37100;'>
            ※ &nbsp; 温馨提示：<br>
            1、请务必正确填写凭证信息。<br>
            2、请正确填写姓名和充值金额，以便及时核对。<br>
            3、转账1笔提交1次，请勿重复提交订单。<br>
            4、请务必转账后再提交订单，否则无法及时查到您的款项！</p>
        </c:otherwise>
    </c:choose>
    </div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="<c:url value='/res/home/js/recharge/rechargeNew.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
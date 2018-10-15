<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<body class="bodyColorEEE">

<c:if test="${null != rechargeList && rechargeList.size() > 0 }">
<!-- 有未完成的申请 -->
<div class="generalPlatArea setFundPwdArea" id="unRechargeDiv">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">充值</span>
    </div>
	<c:forEach items="${rechargeList }" var="re" varStatus="st">
		<div id="container unRechargeDiv_${st.index }">
            <div class="listForm">
                <div class="betConfirmBox" id="tableBoxNone">
                    <p class="detailTitle">
                        <span>温馨提示</span>
                    </p>
                    <!-- 弹窗内容 -->
                    <p class="detailTitle errorWarn">
                        <span>对不起，您尚有一笔充值申请未完成，请完成后再发起</span>
                    </p>
                    <table class="unRechargeTable">
                        <tbody>
                            <tr>
                                <td align="right">充值银行：</td>
                                <td align="left">${re.bankName }
                                    <span style="margin-left: 80px">
                                        <c:if test="${re.rechargeType == 0 }">
                                            <a target="_blank" href="${re.receiveLink }" class="fontColorYellow">登录网上银行付款</a>
                                        </c:if>
                                        <c:if test="${re.rechargeType == 1 }">
		        							<c:if test="${isQrCode }">
		        								<a href="/recharge/rechargePayQrcodeResult?rechargeId=${re.id }" class="fontColorYellow">登录网上银行付款</a>
		        							</c:if>
		        							<c:if test="${!isQrCode }">
		        								<a target="_blank" href="/recharge/rechargePayForward?rechargeId=${re.id }" class="fontColorYellow">登录网上银行付款</a>
		        							</c:if>
		        						</c:if>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">充值金额：</td>
                                <td align="left">${re.amount } 元</td>
                            </tr>
                            <tr>
                                <td align="right">收款银行：</td>
                                <td align="left">${re.receiveBankName }</td>
                            </tr>
                            <c:if test="${re.rechargeType == 0 }">
                                <tr>
                                    <td align="right">收款账户名：</td>
                                    <td align="left">${re.receiveNiceName }</td>
                                </tr>
                                <tr>
                                    <td align="right">收款卡号：</td>
                                    <td align="left">${re.receiveCard }</td>
                                </tr>
                                <tr>
                                    <td align="right">开户银行名称：</td>
                                    <td align="left">${re.receiveAddress }</td>
                                </tr>
                            </c:if>
                            <tr>
                                <td align="right">附言：</td>
                                <td align="left">${re.traceId }</td>
                            </tr>
                        </tbody>
                    </table>

                    <p class="detailTitle errorWarn">
                        <span>* 如您已完成付款，请勿撤销，我们将尽快为您处理。</span>
                    </p>
                    <div class="btnGroup">
                        <a href="javascript:;" onclick="rechargeCancel('${re.id }');" class="btn">撤销申请</a>
                    </div>
                </div>
                <div class="successBox" id="tableBoxSuccess" style="display: none">
                    <div class="successInfo">
                        <p>
                            <img src="/res/mobile/images/successIcon.png" class="msgIcon" alt=""/>
                            <span class="msgText">您的充值申请已撤销成功！</span>
                        </p>
                    </div>
                    <div class="btnGroup">
                        <a href="javascript:window.location.reload(true);" class="btn btnRed">继续充值</a>
                    </div>
                </div>
            </div>
		</div>
	</c:forEach>
</div>
</c:if>

<script type="text/javascript">
	function rechargeCancel(id) {
		ajaxExt({type : "POST",url : '/recharge/rechargeCancel?id=' + id, cache:false, dataType:'json', callback:function(data) {
			if (data == 1) {
				$("#tableBoxNone").hide();		
				$("#tableBoxSuccess").show();		
			} else {
				window.location.reload(true);
			}
		}});
	}
	    		
	function checkHadUnRecharge() {
		var unRecharge = $("#unRechargeDiv")[0];
   		if (unRecharge) {
   			showDiv("unRechargeDiv");
   			return false;
   		} else {
   			showDiv("userRechargeArea");
   			return true;
   		}
   		return true;
	}
</script>
</body>
</html>
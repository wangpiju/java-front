<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
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
				<!-- 充值完成 -->
				<div class="rechargeFinish">
					<div class="finishInfo">
						<c:if test="${recharge.status == 2 }">
							<div class="userMsg fontColorGray">
								<i class="smileFace"></i>
								<span class="fontColorRed finishTitle">恭喜您，充值成功！</span>
							</div>
						</c:if>
						<c:if test="${recharge.status == 3 }">
							<div class="userMsg fontColorGray">
								<i class="warnIcon"></i>
								<span class="finishTitle">对不起，您的充值已过期！</span>
							</div>
						</c:if>
						<c:if test="${recharge.status == 1 }">
							<div class="userMsg fontColorGray">
								<i class="warnIcon"></i>
								<span class="finishTitle">对不起，您的充值被拒绝，拒绝原因：${recharge.remark }</span>
							</div>
						</c:if>
						<c:if test="${recharge.status == 0 }">
							<div class="userMsg fontColorGray">
								<p>
									<i class="warnIcon"></i>
									<span class="finishTitle">系统正在处理您的充值，若充值附言和金额均准确，通常1分钟内即可到账。</span>
								</p>
								<p>
									<span class="finishTitle">若5分钟后仍为到账，请联系<a href="${sysService.link }"
																			  class="onlineServiceCtn fontColorTeal"
																			  target="_blank">在线客服</a></span>
								</p>
							</div>
						</c:if>
						<c:if test="${recharge.status == 4 }">
							<div class="userMsg fontColorGray">
								<i class="warnIcon"></i>
								<span class="finishTitle">您的充值订单已撤销！</span>
							</div>
						</c:if>
						<c:if test="${recharge.status == 5 }">
							<div class="userMsg fontColorGray">
								<i class="warnIcon"></i>
								<span class="finishTitle">您的充值订单正在处理中！</span>
							</div>
						</c:if>
						<p>
							<span class="labelTitle">订单编号：</span>
							<span>${recharge.id }</span>
						</p>
						<p>
							<span class="labelTitle">充值渠道：</span>
							<span>
	                        	<c:if test="${recharge.rechargeType == 0 }">银行转账</c:if>
	                        	<c:if test="${recharge.rechargeType == 1 }">支付宝AA充值</c:if>
	                        </span>
						</p>
						<p>
							<span class="labelTitle">充值金额：</span>
							<span>${recharge.amount }元</span>
						</p>
						<p>
							<span class="labelTitle">充值时间：</span>
							<span><fmt:formatDate value='${recharge.createTime }' pattern="yyyy-MM-dd HH:mm:ss"/></span>
						</p>
					</div>
					<div class="finishBtn">
						<c:choose>
							<c:when test="${recharge.status == 0 || recharge.status == 5 }">
								<a href="/recharge/rechargeFinish?id=${recharge.id }&bankApiId=${bankApi.id }"
								   class="btn">刷新</a>
							</c:when>
							<c:otherwise>
								<a href="/recharge/rechargeMoney" class="btn">继续充值</a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
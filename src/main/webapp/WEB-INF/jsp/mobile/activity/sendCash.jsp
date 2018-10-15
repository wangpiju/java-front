<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="sendCashHeader">
	<img src="/res/mobile/images/activity/sendCashBg.jpg" />
	<span class="consume" id="sendCashConsume"></span>
	<span class="receive" id="sendCashReceive"></span>
</div>
<ul class="sendCashList">
	<c:forEach items="${acivityBetconsumerModel.items}" var="a" varStatus="status">
		<li>
			<a class="getCash" href="javascript:;" id="${a.itemId}">
				<span class="sendCashAmount"><i>￥</i>${a.giveAmount}</span>
				<c:choose>
					<c:when test="${a.betAmount < 100000}">
						<span>满${a.betAmount}元领取</span>
					</c:when>
					<c:otherwise>
						<c:set var="divisor" value="10000" />
						<span>满${a.betAmount/divisor}万元领取</span>
					</c:otherwise>
				</c:choose>
			</a>
		</li>
	</c:forEach>
</ul>
<div class="sendCashActivityRule">
	<h3 class="sendCashActivityTitle">活动规则介绍</h3>
	<p id="sendCashActivityContent">${consumerActivity.remark}</p>
</div>
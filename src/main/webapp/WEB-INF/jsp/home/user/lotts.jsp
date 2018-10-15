<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="index" value="0" />
<c:forEach items="${tree.qun }" var="qun">
	<c:forEach items="${qun.groups }" var="group" varStatus="groupStatus">
		<c:forEach items="${group.players }" var="player" varStatus="status">
			<tr class="listDetail">
				<c:if test="${groupStatus.first && status.first}">
					<td rowspan="${qun.playerCount }">${qun.title }</td>
				</c:if>
				<c:if test="${status.first }">
					<td rowspan="${group.playerCount }">${group.title }</td>
				</c:if>
				<c:set var="p" value="${tree.playerBonusMap[player.id] }"/>
					<c:if test="${p!=null}">
						<td>${p.title}</td>
						<td>${p.saleStatus==0?'销售':'停售'}</td>
						<c:choose>
						   <c:when test="${u.rebateRatio>0}">
						      <td><fmt:formatNumber value='${p.bonus * (p.bonusRatio+ u.rebateRatio - (bonusGroup.rebateRatio - p.rebateRatio)) / 100 }' type="number" pattern="#.###"/></td>
						      <td><fmt:formatNumber value='${p.bonus * p.bonusRatio / 100 }' type="number" pattern="#.###"/>+<fmt:formatNumber value='${u.rebateRatio - (bonusGroup.rebateRatio - p.rebateRatio) }' type="number" pattern="#.###"/>%</td>
						   </c:when>
						   <c:otherwise>
						      	<td><fmt:formatNumber value='${p.bonus * (p.bonusRatio+ u.rebateRatio - (bonusGroup.rebateRatio - p.rebateRatio)) / 100 }' type="number" pattern="#.###"/></td>
						   </c:otherwise>
						</c:choose>
					
					</c:if>
			</tr>
		</c:forEach>
	</c:forEach>
</c:forEach>
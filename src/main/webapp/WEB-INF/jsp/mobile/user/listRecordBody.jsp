<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<c:forEach items="${listRecordList }" var="item">
	<tr class="userListStart">
		<td>
			<input type="hidden" id="userRebateRatio" value="${userRebateRatio}" />
			<c:choose>
			   <c:when test="${item.isOnLine==1 }">
				  <i class="userOutlineIcon"></i>
			   </c:when>
			   <c:otherwise>
				  <i class="userOnlineIcon"></i>
			   </c:otherwise>
			</c:choose>
			<a href="javascript:;" onclick="seniorSearch('${item.account }')">${item.account }</a>
		</td>
		<td><fmt:formatNumber type="number" value="${item.rebateRatio } " maxFractionDigits="1"/></td>
		<td class="fontColorRed">${item.amount }</td>
	</tr>
	<c:if test="${item.parentAccount eq user.account }">
		<tr>
			<td colspan="3" class="userOperateGroup">
				<c:choose>
					<c:when test="${item.userType == 0||(item.userType == 1&& item.rebateRatio<=userBonusGroup.noneMinRatio) }">
						<c:choose>
							<c:when test="${(item.userType == 0&&item.rebateRatio==userBonusGroup.playerMaxRatio)||item.rebateRatio==(userRebateRatio-globalBonusGroup.userMinRatio)}">
								<a href="javascript:;" class="userOperate"  onclick="accountRecharge('${item.account}')">下级充值</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:;" class="userOperate" onclick="showQueryRebateArea('${item.account}','${item.niceName}','${item.rebateRatio}','${item.userType}','${userBonusGroup.userMinRatio }','${userBonusGroup.noneMinRatio }')">返点调配</a>
								<a href="javascript:;" class="userOperate"  onclick="accountRecharge('${item.account}')">下级充值</a>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:when test="${item.rebateRatio >= userBonusGroup.noneMinRatio && item.rebateRatio==(userRebateRatio-globalBonusGroup.userMinRatio) }">
						<a href="javascript:;" class="userOperate" onclick="showQuotaDistArea('${item.account}','${item.rebateRatio}')">配额分配</a>
						<a href="javascript:;" class="userOperate"  onclick="accountRecharge('${item.account}')">下级充值</a>
					</c:when>
					<c:when test="${item.userType == 1 }">

						<a href="javascript:;" class="userOperate" onclick="showQueryRebateArea('${item.account}','${item.niceName}','${item.rebateRatio}','${item.userType}','${userBonusGroup.userMinRatio }','${userBonusGroup.noneMinRatio }')">返点调配</a>
						<a href="javascript:;" class="userOperate" onclick="showQuotaDistArea('${item.account}','${item.rebateRatio}')">配额分配</a>
						<a href="javascript:;" class="userOperate"  onclick="accountRecharge('${item.account}')">下级充值</a>
					</c:when>
				</c:choose>
			</td>
		</tr>
	</c:if>
</c:forEach>
<script type="text/javascript">if ('${listRecordList.size() }' == 0) {end = true;}</script>
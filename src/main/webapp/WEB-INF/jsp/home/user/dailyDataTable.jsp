<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
			<input type="hidden" id="pageCountDailyData" value="${p.rowCount }" />
			<table class="dividendListTable" >
            	<thead>
                       <tr class="listHeader">
                            <th width="20%">用户名</th>
	                        <th width="10%">日期</th>
		                    <th width="10%">有效人数（个）</th>
		                    <th width="10%">亏损情况</th>
		                    <th width="20%">投注金额</th>
		                    <th width="10%">日薪比例（%）</th>
		                    <th width="20%">日薪金额</th>
                       </tr>
                     </thead>
                	<tbody>
	                	<c:forEach items="${dailyDataList }" var="dailyData" varStatus="st">
	                		<tr>
	                			<td>${dailyData.account }</td>
	                			<td><fmt:formatDate value="${dailyData.createTime }" pattern="yyyy-MM-dd"/></td>
	                			<td>${dailyData.validAccountCount }</td>
	                			<td>${dailyData.lossStatus == 0 ? "亏损" : "未亏损" }</td>
	                			<td>${dailyData.betAmount }</td>
	                			<td>${dailyData.ruleRate }</td>
	                			<td>${dailyData.dailyAmount }</td>
	                		</tr>
	              		</c:forEach>
                	</tbody>
            </table>
            <c:if test="${dailyDataList == null || dailyDataList.size() == 0 }">
                <div class="nullMsg">
                	<i class="nullMsgImg"></i>
                    <span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
                </div>
            </c:if>
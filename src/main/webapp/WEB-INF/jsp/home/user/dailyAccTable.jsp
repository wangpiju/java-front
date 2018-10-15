<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<input type="hidden" id="pageCountDailyAcc" value="${p.rowCount }" />
<table class="dailyAccTable" >
	<thead>
       <tr class="listHeader">
	      <th width="15%">用户名</th>
	      <th width="10%">日薪比例（%）</th>
	      <th width="10%">启始金额</th>
	      <th width="10%">有效人数要求（个）</th>
	      <th width="10%">亏损要求</th>
	      <th width="10%">封顶金额</th>
	      <th width="20%">处理时间</th>
	      <th width="15%">操作</th>
	   </tr>
	 </thead>
   	 <tbody>
    	<c:forEach items="${dailyAccList }" var="dailyAcc" varStatus="st">
    		<tr>
    			<td>${dailyAcc.account }</td>
    			<td>${dailyAcc.rate }</td>
    			<td>${dailyAcc.betAmount }</td>
    			<td>${dailyAcc.validAccountCount == 0 ? "无要求" : dailyAcc.validAccountCount }</td>
    			<td>${dailyAcc.lossStatus == 0 ? "有" : "无" }</td>
    			<td>${dailyAcc.limitAmount <= 0 ? "不限制" : dailyAcc.limitAmount }</td>
    			<td><fmt:formatDate value="${dailyAcc.changeTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    			<td>
    				<c:if test="${user.account != dailyAcc.account }"><a class="fontColorTheme modifyDailyRule" href="javascript:modifyDailyRule('${dailyAcc.id }','${dailyAcc.account }','${dailyAcc.rate }','${dailyAcc.betAmount }','${dailyAcc.validAccountCount }','${dailyAcc.limitAmount }','${dailyAcc.lossStatus }');">修改日薪规则</a></c:if>
    			</td>
    		</tr>
  		</c:forEach>
   	</tbody>
</table>
<c:if test="${dailyAccList == null || dailyAccList.size() == 0 }">
   <div class="nullMsg">
   	<i class="nullMsgImg"></i>
       <span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
   </div>
</c:if>
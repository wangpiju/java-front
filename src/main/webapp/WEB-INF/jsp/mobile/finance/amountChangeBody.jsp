<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<tbody>
	<c:forEach items="${financeChangeList }" var="item">
	<tr class="amountChangeCountClass">
         <td><fmt:formatDate value="${item.changeTime }" pattern="MM-dd HH:mm"/></td>
         <td>${item.remark }</td>
         <td class="${item.changeAmount > 0 ? 'fontColorRed' : 'fontColorGreen' }">${item.changeAmount }</td>
     </tr>
	</c:forEach>
</tbody>

<script type="text/javascript">if ('${financeChangeList.size() }' == 0) {end = true;}</script>
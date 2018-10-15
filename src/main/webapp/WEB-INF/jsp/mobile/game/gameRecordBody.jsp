<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<tbody>
	<c:forEach items="${gameRecordList }" var="item">
	<tr class="gameRecordCountClass">
         <td><fmt:formatDate value="${item.createTime }" pattern="MM-dd HH:mm"/></td>
         <td class='sonBreak'><span>${item.lotteryName }</span><span class='fontSizeRem075'>${item.playName }</span></td>
         <td class='fontColorRed'>${item.amount }</td>
		 <c:choose>
		    <c:when test="${item.status== 0}">
		       <td class='sonBreak'><span>等待开奖</span><a  class='cancelOrder fontColorTheme' href='/lotts/${item.lotteryId }/cancelOrder?ids=${item.id }'>撤单</a></td>
		    </c:when>
		     <c:when test="${item.status== 1}">
		     <td class='sonBreak fontColorRed'><span>已中奖</span><span>${item.win }</span>元</td>
		    </c:when>
		     <c:when test="${item.status== 2}">
		     <td class='sonBreak'><span>未中奖</span></td>
		    </c:when>
		     <c:when test="${item.status== 3}">
		     <td class='sonBreak'><span>未开始</span><a class='cancelOrder fontColorTheme' id='cancelOrderByTrace' href='/lotts/${item.lotteryId }/cancelOrder?ids=${item.id }'>撤单</a></td>
		    </c:when>
		     <c:when test="${item.status== 4}">
		     <td class='sonBreak'><span>个人撤单</span></td>
		    </c:when>
		     <c:when test="${item.status== 5}">
		     <td class='sonBreak'><span>系统撤单</span></td>
		    </c:when>
		     <c:when test="${item.status== 6}">
		     <td class='sonBreak'><span>未开奖</span></td>
		    </c:when>
		    <c:when test="${item.status== 7}">
		     <td class='sonBreak'><span>恶意注单</span></td>
		    </c:when>
		     <c:when test="${item.status== 8}">
		     <td class='sonBreak'><span>暂停</span></td>
		    </c:when>
		     <c:when test="${item.status== 9}">
		     <td class='sonBreak'><span>追中撤单</span></td>
		    </c:when>
		 </c:choose>
     </tr>
	</c:forEach>
</tbody>

<script type="text/javascript">if ('${gameRecordList.size() }' == 0) {end = true;}</script>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="pagination" id="pagination">
	<div class="page-l">
		<!-- 		<span>每页显示</span> <select id="pageShowNum" name="pageShowNum"> -->
		<!-- 			<option value="10">10条</option> -->
		<!-- 			<option value="25">25条</option> -->
		<!-- 			<option value="50">50条</option> -->
		<!-- 		</select> -->
		<!--                             <input type="text" class="pageShowNum"/> -->
		<span>共</span> <span id="totalPage">0</span> <span>页</span> <span class="operate" id="goBack"
																		  style="display: none"></span>
	</div>
	<div class="page-r">
		<div class="page-home">
			<a href="?${p.params}1" class="homePage" onclick="firstPage()">首页</a>
		</div>
		<div class="page-prev">
			<a href="?${p.params}${p.prev}" class="pagePrevious">上一页</a>
		</div>
		<div class="page-list">
			<ul>
				<li><a href="?${p.params}1" class="page active">1</a></li>
				<li><a href="?${p.params}2" class="page">2</a></li>
				<li><a href="?${p.params}3" class="page">3</a></li>
				<li><a href="?${p.params}4" class="page">4</a></li>
				<li><a href="?${p.params}5" class="page">5</a></li>
			</ul>
		</div>
		<div class="page-next">
			<a href="?${p.params}${p.prev}" class="pageNext">下一页</a>
		</div>
		<div class="page-end">
			<a href="?${p.params}${p.pageCount}" class="endPage">尾页</a>
		</div>
	</div>
</div>
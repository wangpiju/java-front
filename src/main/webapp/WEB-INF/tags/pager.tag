<%@ tag pageEncoding="UTF-8" description="显示分页" %>
<%@ attribute name="url" type="java.lang.String" required="false" description="路径名称" %>
<%@ attribute name="p" type="com.hs3.db.Page" required="true" description="分页对象" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pagination">
    <div class="page-l">
        <span>每页显示</span>
        <select id="pageShowNum" name="pageShowNum"
                onchange="javascript:window.location.href = '?' + '<%=p.getParams(new String[]{"page", "rows"}) %>' + '&page=1&rows=' + this.value">
            <option value="10"
                    <c:if test="${p.pageSize == 10 }">selected="selected"</c:if> >10条
            </option>
            <option value="25"
                    <c:if test="${p.pageSize == 25 }">selected="selected"</c:if> >25条
            </option>
            <option value="50"
                    <c:if test="${p.pageSize == 50 }">selected="selected"</c:if> >50条
            </option>
        </select>
        <span>第 ${p.nowPage } 页</span><span>，共</span> <span id="totalPage">${p.pageCount }</span> <span>页</span> <span
            class="operate" id="goBack"></span>
    </div>
    <div class="page-r">
        <div class="page-home">
            <a href="${url}?${p.params}1" class="homePage" onclick="firstPage()">首页</a>
        </div>
        <div class="page-prev">
            <a href="${url}?${p.params}${p.prev}" class="pagePrevious">上一页</a>
        </div>
        <div class="page-list">
            <ul>
                <c:forEach items="${p.showPages }" var="sItem">
                    <li><a href="${url}?${p.params}${sItem}"
                           class="page <c:if test='${sItem == p.nowPage }'>active</c:if>">${sItem}</a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="page-next">
            <a href="${url}?${p.params}${p.next}" class="pageNext">下一页</a>
        </div>
        <div class="page-end">
            <a href="${url}?${p.params}${p.pageCount}" class="endPage">尾页</a>
        </div>
    </div>
</div>
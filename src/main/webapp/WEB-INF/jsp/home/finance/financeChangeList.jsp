<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mx"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>银行充值</title>
    <link rel="stylesheet" href="<c:url value='/res/home/css/hs-accountCentre.css?ver=${VIEW_VERSION}'/>"/>
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>"  type="image/x-icon" />
    <script type="text/javascript" src="<c:url value='/res/home/js/My97DatePicker/WdatePicker.js?ver=${VIEW_VERSION}'/>"></script>
    <script type="text/javascript" src="<c:url value='/res/home/js/math.extends.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body>

<div class="accountCentreMain">
    <div class="accountCentreHeader">
        <div class="areaBigContainer">
            <p class="titleBar">
                <i class="titleLeft"></i>
                <span class="titleRight">银行充提</span>
            </p>
            <ul class="centreNav">
                <li class="centreNavDetail">
                    <a href="/recharge/rechargeList">充值列表</a>
                </li>
                <li class="centreNavDetail">
                    <a href="/deposit/depositList">提现列表</a>
                </li>
                <li class="centreNavDetail active">
                    <a href="/finance/financeChangeList">帐变列表</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="container">
        <div class="areaBigContainer">
            <div class="accountCentreContent">
                <div class="userListArea">
                    <!-- 查询条件 -->
                    <form class="searchBar userListSearch" action="/finance/financeChangeList" method="post">
                        <label for="#">
                            <span class="labelTitle">帐变时间：</span>
                            <input type="text" class="labelCond userName Wdate" id="startTime" name="startTime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
                            ~
                            <input type="text" class="labelCond userName Wdate" id="endTime" name="endTime" value="${endTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
                        </label>
                        <label for="#">
                            <span class="labelTitle">用户名：</span>
                            <input type="text" class="labelCond userName" id="changeUser" name="changeUser" value="${cond.changeUser }" />
                            <input type="checkbox" id="isIncludeChildFlag" name="isIncludeChildFlag" onclick="javascript:if(this.value==0){this.value=1;} else {this.value=0;};" <c:choose><c:when test="${isIncludeChild }">checked value="1"</c:when><c:otherwise>value="0"</c:otherwise></c:choose> />包含下级
                        </label>
                        <label for="#">
                            <span class="labelTitle">帐变类型：</span>
                            <select name="accountChangeTypeId">
                                <option value="-1">不限</option>
                                <c:forEach items="${accountChangeTypeList }" var="accountChangeType">
                                    <option value="${accountChangeType.id }" <c:if test="${accountChangeType.id == cond.accountChangeTypeId}">selected</c:if> >${accountChangeType.name }
                                </c:forEach>
                            </select>
                        </label>
                        <input type="submit" class="btn" id="search" value="搜索" />
                    </form>
                    <div class="tableBox" id="listBox">
                        <table class="userListTable" id="userListTable">
                            <tr class="listHeader">
                                <th width="5%">序号</th>
                                <th width="10%">帐变用户</th>
                                <th width="10%">帐变编号</th>
                                <th width="5%">帐变类型</th>
                                <th width="15%">帐变时间</th>
                                <th width="10%">收入</th>
                                <th width="10%">支出</th>
                                <th width="10%">帐变金额</th>
                                <th width="10%">用户余额</th>
                                <th width="10%">备注</th>
                                <th width="5%">状态</th>
                            </tr>
                            <c:forEach items="${financeChangeList }" var="financeChange" varStatus="st">
                                <tr class="listDetail">
                                    <td>${st.index + 1 }</td>
                                    <td>${financeChange.changeUser }</td>
                                    <td>${financeChange.financeId }</td>
                                    <td>
                                        <c:forEach items="${accountChangeTypeList }" var="fct">
                                            <c:if test="${fct.id == financeChange.accountChangeTypeId}">${fct.name }</c:if>
                                        </c:forEach>
                                    </td>
                                    <td>${financeChange.changeTime }</td>
                                    <td>${financeChange.changeAmount >= 0 ? financeChange.changeAmount : 0 }</td>
                                    <td>${financeChange.changeAmount < 0 ? financeChange.changeAmount : 0 }</td>
                                    <td id="tdChangeAmount_${st.index + 1 }">${financeChange.changeAmount }</td>
                                    <td>${financeChange.balance }</td>
                                    <td>${financeChange.remark }</td>
                                    <td id="tdStatus_${financeChange.id }">
                                        <c:if test="${financeChange.status == 0 }">未处理</c:if>
                                        <c:if test="${financeChange.status == 1 }">拒绝</c:if>
                                        <c:if test="${financeChange.status == 2 }">完成</c:if>
                                        <c:if test="${financeChange.status == 3 }">已过期</c:if>
                                        <c:if test="${financeChange.status == 4 }">已撤销</c:if>
                                        <c:if test="${financeChange.status == 5 }">正在处理</c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr class="listDetail">
                                <td colspan="5"></td>
                                <td><span id="tdIn" class="fontColorRed"></span></td>
                                <td><span id="tdOut" class="fontColorRed"></span></td>
                                <td colspan="4">帐变金额：<span id="tdAll" class="fontColorTheme"></span></td>
                            </tr>
                        </table>
                        <c:if test="${financeChangeList == null || financeChangeList.size() == 0 }">
                            <!-- 没有消息 -->
                            <div class="nullMsg" id="nullMsg">
                                <i class="nullMsgImg"></i>
                                <span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
                            </div>
                        </c:if>
                        <!-- 分页 -->
                        <mx:pager p="${p }" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
$(function(){
	var o = 0, i = 0, a = 0;
	// 统计
	$("#listBox td[id^='tdChangeAmount_']").each(function() {
		var m = parseFloat($(this).html());
		if (m < 0) {
			o = Math.add(o, m);
		} else {
			i = Math.add(i, m);
		}
		a = Math.add(a, m);
	});
	$("#tdOut").html(o);
	$("#tdIn").html(i);
	$("#tdAll").html(a);
});
</script>
</body>
</html>
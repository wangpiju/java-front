<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mx" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>银行充值</title>
	<link rel="stylesheet" href="<c:url value='/res/home/css/hs-accountCentre.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>" type="image/x-icon"/>
	<script type="text/javascript"
			src="<c:url value='/res/home/js/My97DatePicker/WdatePicker.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body>
<div class="accountCentreMain">
	<div class="accountCentreHeader">
		<div class="areaBigContainer">
			<p class="titleBar">
				<i class="titleLeft"></i>
				<span class="titleRight">银行充提</span>
			</p>
			<!-- 导航 -->
			<c:import url="../recharge/rechargeNav.jsp"></c:import>
		</div>
	</div>
	<div class="container">
		<div class="areaBigContainer">
			<div class="accountCentreContent">
				<div class="userListArea">
					<!-- 查询条件 -->
					<form class="searchBar userListSearch" action="/recharge/rechargeList" method="post">
						<label for="#">
							<span class="labelTitle">充值时间：</span>
							<input type="text" class="labelCond userName Wdate" id="startTime" name="startTime"
								   value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
							~
							<input type="text" class="labelCond userName Wdate" id="endTime" name="endTime"
								   value="${endTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
						</label>
						<label for="#">
							<span class="labelTitle">交易号：</span>
							<input type="text" class="labelCond userName" id="serialNumber" name="serialNumber"
								   value="${cond.serialNumber }"/>
						</label>
						<label for="#">
							<span class="labelTitle">附言：</span>
							<input type="text" class="labelCond userName" id="traceId" name="traceId"
								   value="${cond.traceId }"/>
						</label>
						<input type="submit" class="btn" id="search" value="搜索"/>
					</form>
					<!-- 用户数据 -->
					<div class="tableBox" id="userListBox">
						<table class="userListTable" id="userListTable">
							<tr class="listHeader">
								<th width="5%">序号</th>
								<th width="10%">附言</th>
								<th width="10%">充值金额</th>
								<th width="15%">充值时间</th>
								<th width="15%">处理时间</th>
								<th width="10%">交易号</th>
								<th width="20%">备注</th>
								<th width="10%">状态</th>
								<th width="10%">操作</th>
							</tr>
							<c:forEach items="${rechargeList }" var="recharge" varStatus="st">
								<tr class="listDetail">
									<td>${st.index + 1 }</td>
									<td>${recharge.traceId }</td>
									<td>${recharge.amount }</td>
									<td>${recharge.createTime }</td>
									<td>${recharge.lastTime }</td>
									<td>${recharge.serialNumber }</td>
									<td>${recharge.remark }</td>
									<td id="tdStatus_${recharge.id }">
										<c:if test="${recharge.status == 0 }">未处理</c:if>
										<c:if test="${recharge.status == 1 }">拒绝</c:if>
										<c:if test="${recharge.status == 2 }">完成</c:if>
										<c:if test="${recharge.status == 3 }">已过期</c:if>
										<c:if test="${recharge.status == 4 }">已撤销</c:if>
										<c:if test="${recharge.status == 5 }">正在处理</c:if>
									</td>
									<td id="tdOperate_${recharge.id }">
										<c:if test="${recharge.status == 0 }">
											<a href="javascript:;" onclick="rechargeCancel('${recharge.id }');"
											   class="fontColorTheme">撤销</a>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</table>
						<c:if test="${rechargeList.size() == 0 }">
							<!-- 没有消息 -->
							<div class="nullMsg" id="nullMsg">
								<i class="nullMsgImg"></i>
								<span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
							</div>
						</c:if>
						<!-- 分页 -->
						<mx:pager p="${p }"/>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	function rechargeCancel(id) {
		ajaxObject('/recharge/rechargeCancel?id=' + id, "POST", function (data) {
			if (data == 1) {
				$("#tdStatus_" + id).html("已撤销");
				$("#tdOperate_" + id).html("");
			} else {
				window.location.reload(true);
			}
		});
	}
</script>
</body>
</html>
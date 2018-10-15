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
			<c:import url="../recharge/rechargeNav.jsp"></c:import>
		</div>
	</div>
	<div class="container">
		<div class="areaBigContainer">
			<div class="accountCentreContent">
				<div class="userListArea">
					<!-- 查询条件 -->
					<form class="searchBar userListSearch" action="/deposit/depositList" method="post">
						<label for="#">
							<span class="labelTitle">提现时间：</span>
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
								<th width="10%">提现金额</th>
								<th width="15%">提现时间</th>
								<th width="15%">处理时间</th>
								<th width="10%">交易号</th>
								<th width="20%">备注</th>
								<th width="10%">状态</th>
								<th width="10%">操作</th>
							</tr>
							<c:forEach items="${depositList }" var="deposit" varStatus="st">
								<tr class="listDetail">
									<td>${st.index + 1 }</td>
									<td>${deposit.traceId }</td>
									<td>${deposit.amount }</td>
									<td>${deposit.createTime }</td>
									<td>${deposit.lastTime }</td>
									<td>${deposit.serialNumber }</td>
									<td>${deposit.remark }</td>
									<td id="tdStatus_${deposit.id }">
										<c:if test="${deposit.status == 0 }">未处理</c:if>
										<c:if test="${deposit.status == 1 }">拒绝</c:if>
										<c:if test="${deposit.status == 2 }">完成</c:if>
										<c:if test="${deposit.status == 3 }">已过期</c:if>
										<c:if test="${deposit.status == 4 }">已撤销</c:if>
										<c:if test="${deposit.status == 5 }">正在处理</c:if>
									</td>
									<td id="tdOperate_${deposit.id }">
										<c:if test="${deposit.status == 0 }">
											<a href="javascript:depositCancel('${deposit.id }');"
											   class="fontColorTheme">撤销</a>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</table>
						<c:if test="${depositList.size() == 0 }">
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
	function depositCancel(id) {
		ajaxObject('/deposit/depositCancel?id=' + id, "POST", function (data) {
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
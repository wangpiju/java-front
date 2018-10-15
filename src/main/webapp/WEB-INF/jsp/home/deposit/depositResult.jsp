<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>充值-提现</title>
	<link rel="stylesheet" href="<c:url value='/res/home/css/wk-recharge.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
</head>
<body>
<div class="areaBigContainer mainWidth" id="depositArea">
	<div class="containerFlex">
		<!-- 列表 -->
		<c:import url="../recharge/rechargeNav.jsp"></c:import>
		<!-- 内容 -->
		<div class="rightArea">
			<div class="accountCentreContent">
				<c:choose>
					<c:when test="${error != null }">
						<div class="depositFinish">
							<div class="finishInfo">
									${error}
							</div>
							<div class="finishBtn">
								<a href="/deposit/safePassword" class="btn">返回</a>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<!-- 提现成功 -->
						<div class="depositFinish">
							<div class="finishInfo">
								<p>
									<i class="smileFace"></i>
									<span class="fontColorRed finishTitle">您的提现申请已经成功！</span>
								</p>
								<p class="tips">请耐心等待工作人员审核，感谢您的支持与配合!</p>
							</div>
							<div class="finishBtn">
								<a href="<c:url value='/report/index?tabId=financeList'/>" class="btn">银行充提</a>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>
</body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>充值-提现</title>
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-recharge.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
</head>

<body>
	<div class="areaBigContainer mainWidth" id="rechargeArea">
		<div class="containerFlex">
			<!-- 列表 -->
			<c:import url="../recharge/rechargeNav.jsp"></c:import>
			<!-- 内容 -->
			<div class="rightArea">
	        	<div class="accountCentreContent">
		        	<div class="depositArea userSetInfoContentArea">
			            <div class="depositBox">
			            	<p class="fontColorRed" style="text-align:center;">
			            		<i class="warnIcon"></i>
		                        <span>${message }</span>
		                    </p>
		            	</div>
		            </div>
	            </div>
			</div>
		</div>
	</div>
</body>
</html>
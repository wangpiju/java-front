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
	        	<c:if test="${user.depositStatus != 2}">
		            <!-- 提现 -->
		            <form action="/deposit/safePassword" method="post" onsubmit="return checkForm();">
			            <div class="depositArea userSetInfoContentArea">
			                <div class="depositBox depositsafeBox">
			                    <p>
			                        <span class="labelTitle">安全密码：</span>
			                        <input type="password" id="safePassword" onblur="checkSafePassword();" name="safePassword" class="labelCond" />
			                        <span id="errorSpan" class="fontColorRed"></span>
			                    </p>
			                    <label>
			                        <span class="labelTitle"></span>
			                        <input class="btn" type="submit" value="提交" />
			                    </label>
			                </div>
			            </div>
		            </form>
	            </c:if>
	            <c:if test="${user.depositStatus == 2 || user.depositStatus == 3}">
		            <div class="depositArea userSetInfoContentArea">
			            <div class="depositBox">
			            	<p class="fontColorRed" style="text-align:center;">
			            		<i class="warnIcon"></i>
		                        <span>您的提现功能已被冻结！</span>
		                    </p>
		            	</div>
		            </div>
	            </c:if>
            </div>
		</div>
	</div>
</div>

<c:import url="../recharge/rechargeSafePassword.jsp"></c:import>

<script type="text/javascript">
	var error = '${error }';
	if (error) {
		alert(error);
	}
	var checkForm = function() {
		return checkNeetSet() && checkSafePassword();		
	}
	
	function checkNeetSet() {
		var needSet = $("#setSafePassword")[0];
		if (needSet) {
			layer.closeAll();
		    layer.open({
		        type: 1,
		        skin: 'setSafePswLayer',
		        shift: 5,
		        area:['755px','520px'],
		        title:false,
		        content:$('#setSafePassword')
		    });
			return false;
		}
		return true;
	}
	
	function checkSafePassword() {
		$("#errorSpan").html("");
		var safePassword = $("#safePassword").val();
		if (!safePassword) {
			$("#errorSpan").html("请输入安全密码");
			return false;
		}
		
		return true;
	}
	
	$(function() {
		checkNeetSet();
	});
</script>
</body>
</html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>提现未到账</title>
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-recharge.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>"  type="image/x-icon" />
    <script type="text/javascript" src="<c:url value='/res/home/js/My97DatePicker/WdatePicker.js?ver=${VIEW_VERSION}'/>"></script>
    <script type="text/javascript">
    	var message = '${message}';
    	$(function() {if (message) {$.alert(message);}});
    </script>
</head>
<body>
	<div class="areaBigContainer mainWidth" id="depositOutstanding">
		<div class="containerFlex">
			<!-- 列表 -->
			<c:import url="../recharge/rechargeNav.jsp"></c:import>
			<!-- 内容 -->
			<div class="rightArea">
        		<div class="accountCentreContent">	
        			<form id="depOutForm" action="/deposit/depositOutstanding" method="post" enctype="multipart/form-data">     			
                         <div id="amountBox">
	                        <span class="labelTitle">提款金额：</span>
	                        <input type="text" class="labelCond" name="amount" id="amount" />
                         </div>
                         <div id="lastFourBox">
	                        <span class="labelTitle">提款卡号后4位：</span>
	                        <input type="text" class="labelCond" name="card" id="lastFour" maxlength="4" />
                         </div>
                         <div id="depositTimeBox">
	                        <span class="labelTitle">提款时间：</span>
	                        <input type="text" class="labelCond Wdate" name="depositTime" id="depositTime" onClick="WdatePicker({startDate: '%y-%M-%d %H:%m:00',dateFmt:'yyyy-MM-dd HH:mm:dd'})" style="border:none;"/>
                         </div>
                         <div id="imgBox">
	                  		<span class="labelTitle">上传截图：</span>
	                  		<input type="file" class="labelCond" name="img" id="img" accept="image/png,image/jpeg" />
	                     </div>
                         <div>
							<span class="labelTitle"></span>
							<a href="javascript:;" class="btn" id="submit">提交</a>
                         </div>
        			</form>
        			<p class="userMsg outstandingMsg">
                    	温馨提示：如提款5分钟后还未到账，请使用该功能催促！
                    	<br/>填写完毕后将有专职工作人员处理，请您耐心等待片刻，感谢您的支持与理解！
	                </p>        			
        		</div>
       		</div>	
		</div>
	</div>
	<script type="text/javascript" src="<c:url value='/res/home/js/finance/depositOutstanding.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
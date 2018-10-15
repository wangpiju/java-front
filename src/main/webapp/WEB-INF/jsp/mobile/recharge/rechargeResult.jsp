<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0 user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
    <title>账户充值</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 账户充值 -->
<div class="generalPlatArea userRechargeArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">充值</span>
    </div>
    <div class="container">
        <div class="userRechAfmInfoBox" id="rechargeResult">
        	<c:if test="${empty classKey }">
				<div class="rechAfmInfo">
					<div class="rechAfmInfoTitle">
						<span class="fontColorYellow">充值信息</span>
					</div>
					<div class="rechAfmInfoContent">
						<div class="rechAfmInfoDetail">
							<span class="rechDetailTitle">充值方式：</span>
							<span class="fontColorGray">
								<c:if test="${recharge.rechargeType == 0 }">银行转账</c:if>
								<c:if test="${recharge.rechargeType == 1 }">快捷充值</c:if>
							</span>
						</div>
						<div class="rechAfmInfoDetail">
							<span class="rechDetailTitle">充值银行：</span>
							<span class="fontColorGray bank">${recharge.bankName }</span>
						</div>
						<div class="rechAfmInfoDetail">
							<span class="rechDetailTitle">充值金额：</span>
							<span class="fontColorRed">${recharge.amount }元</span>
						</div>
					</div>
				</div>
				<div class="rechAfmInfo">
					<div class="rechAfmInfoTitle">
						<span class="fontColorYellow">收款账号</span>
					</div>
					<div class="rechAfmInfoContent">
						<div class="rechAfmInfoDetail">
							<span class="rechDetailTitle">收款银行：</span>
							<span class="fontColorGray">${recharge.receiveBankName }<br/>${recharge.receiveCard }</span>
						</div>
						<div class="rechAfmInfoDetail">
							<span class="rechDetailTitle">开户姓名：</span>
							<span class="fontColorGray">${recharge.receiveNiceName }</span>
						</div>
						<div class="rechAfmInfoDetail">
							<span class="rechDetailTitle">附言：</span>
							<span class="fontColorRed">${recharge.traceId }</span>
						</div>
					</div>
				</div>
				<div class="affirmInfoTips">
					<p class="errorWarn fontSizeRem12">
						<span>请于</span>
						<span id="rechMinute">${sysExpireTime }</span>
						<span>:</span>
						<span id="rechSecond">00</span>
						<span>内完成充值</span>
					</p>
				</div>
				<div class="btnGroup">
					<a target="_blank" href="${recharge.receiveLink }" class="btn btnRed" id="onlineBankPay">登录网上银行付款</a>
					<a href="javascript:;" class="btn" id="endImproveInfo" style="background-color:#e89b00;">完成后务必点我完善信息</a>
				</div>
        	</c:if>
        	<c:if test="${!empty classKey }">
				<div class="rechQRCode">
					<c:choose>
			    		<c:when test="${classKey == 'alipay2bank' }">
				    		<div class="rechAfmInfo">
								<div class="rechAfmInfoTitle">
									<span class="fontColorYellow">充值信息</span>
								</div>
								<div class="rechAfmInfoContent">
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">充值金额：</span>
										<span class="fontColorRed">${recharge.amount }元</span>
									</div>
								</div>
							</div>
							<div class="rechAfmInfo">
								<div class="rechAfmInfoTitle">
									<span class="fontColorYellow">收款账号</span>
								</div>
								<div class="rechAfmInfoContent">
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">收款人姓名：</span>
										<span class="fontColorGray">${bankApi.email }</span>
									</div>
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">收款银行：</span>
										<span class="fontColorGray">${bankApi.sign }</span>
									</div>
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">收款卡号：</span>
										<span class="fontColorGray">${bankApi.merchantCode }</span>
									</div>
								</div>
							</div>
							<div class="btnGroup">
								<a href="javascript:;" class="btn btnRed" id="endImproveInfoAli2Bank">完成后务必点我完善信息</a>
							</div> 
			    		</c:when>
			    		<c:otherwise>
			    			<div class="rechAfmInfo">
								<div class="rechAfmInfoTitle">
									<span class="fontColorYellow">充值信息</span>
								</div>
								<div class="rechAfmInfoContent">
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">充值方式：</span>
										<span class="fontColorGray">支付宝</span>
									</div>
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">充值金额：</span>
										<span class="fontColorRed">${recharge.amount }元</span>
									</div>
								</div>
							</div>
							<div class="rechAfmInfo">
								<div class="rechAfmInfoTitle">
									<span class="fontColorYellow">收款账号</span>
								</div>
								<div class="rechAfmInfoContent">
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">支付宝账号：</span>
										<span class="fontColorGray">${bankApi.merchantCode }</span>
									</div>
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">收款人姓名：</span>
										<span class="fontColorGray">${bankApi.email }</span>
									</div>
									<div class="rechAfmInfoDetail">
										<span class="rechDetailTitle">附言：</span>
										<span class="fontColorRed">${recharge.traceId }</span>
									</div>
								</div>
							</div>
							<div class="btnGroup">
								<a href="rechargeFinish?id=${recharge.id }&bankApiId=${bankApi.id }" class="btn btnRed">我已通过支付宝转账完毕</a>
							</div>
			    		</c:otherwise>
		    		</c:choose>
                    <div class="alipayTip">
                        <h3>*重要说明</h3>
                        <p>转账完成后，您的充值将自动到账，若5分钟后仍为到账，请联系在线客服;</p>
                        <p>务必使用页面显示的附言进行充值，非页面显示的附言绝不可使用，以免遭遇资金损失</p>
                    </div>
				</div>
        	</c:if>
        </div>
        
        <!-- 完善信息start -->
    	<div id="confirmRecharge" style="display:none;">
			<form id="formSetCard" method="post">
				<input type="hidden" name="id" id="rechargeId" value="${recharge.id }">
				<div class="listForm">
					<c:choose>
			    		<c:when test="${classKey == 'alipay2bank' }">
			    			<div class="listDetail">
			    				<p class="detailTitle fontColorYellow">
			    					<i class="detailTitleLeft"></i>
			    					<span>存款支付宝姓名</span>
	    						</p>
								<input type="text" name="niceName" class="labelCond">
								<input type="hidden" name="card" class="labelCond" value="0000">
			    			</div>
			    		</c:when>
			    		<c:otherwise>
			    			<div class="listDetail">
			    				<p class="detailTitle fontColorYellow">
			    					<i class="detailTitleLeft"></i>
			    					<span>存款卡姓名</span>
	    						</p>
								<input type="text"  name="niceName" class="labelCond">
			    			</div>
			    			<div class="listDetail">
			    				<p class="detailTitle fontColorYellow">
			    					<i class="detailTitleLeft"></i>
			    					<span>存款卡卡号后4位</span>
	    						</p>
								<input type="text"  name="card" class="labelCond">
			    			</div>
			    		</c:otherwise>
		    		</c:choose>
				</div>
				<div class="btnGroup">
					<button type="submit" class="btn btnRed">提交</button>
				</div>
			</form>
    	</div>
    	<!-- 完善信息end -->
    </div>
</div>

</body>
<script type="text/javascript">
var end_v = '${expireTime }';
var now_v = '${nowTime }';
$(function(){
	if(end_v && now_v) {
		var end_time=new Date(end_v).getTime();	
		var now_time= new Date(now_v).getTime();	
		var didf_time = (end_time-now_time)/1000;	
		countDown(didf_time, null, null, "#rechMinute", "#rechSecond");
	}
	
	/* 完善信息start  */
	$('#onlineBankPay').on('click',function(){
		$.alert('请您在新弹出的网银页面中完成转账后，再点击“我已转账完毕按钮”，提交存款信息');
	});
	
	$('#endImproveInfo,#endImproveInfoAli2Bank').on('click',function(){
		$('#rechargeResult').hide();
		$('#confirmRecharge').show();
	});
	
	$('#returnRechResult').on('click',function(){
		$('#rechargeResult').show();
		$('#confirmRecharge').hide();
	});
	
	$("#formSetCard").submit(function(){
		var d = serializeObject(this);
		var method = $(this).attr("method");
		ajaxExt({
			method:method,
			url:'/recharge/saveRechargeInfo',
			dataType:'json',
			data:d,
			callback:function(rel) {
				$.alert("您的存款信息提交完毕，请您1分钟后核实游戏额度");
			}
		});
		return false;
	});
	/* 完善信息end  */
});

function countDown(sys_second,day_elem,hour_elem,minute_elem,second_elem){	
	var timer = setInterval(function(){
		if (sys_second > 1) {
			sys_second -= 1;
			var day = Math.floor((sys_second / 3600) / 24);
			var hour = Math.floor((sys_second / 3600) % 24);
			var minute = Math.floor((sys_second / 60) % 60);
			var second = Math.floor(sys_second % 60);
			day_elem && $(day_elem).text(day);//计算天
			$(hour_elem).text(hour<10?"0"+hour:hour);//计算小时
			$(minute_elem).text(minute<10?"0"+minute:minute);//计算分
			$(second_elem).text(second<10?"0"+second:second);// 计算秒
		} else { 
			clearInterval(timer);
			location.href="/recharge/rechargeFinish?id=${recharge.id }";
		}
	}, 1000);
}
</script>
</html>
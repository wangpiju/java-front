<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<script type="text/javascript">
		var errorMessage = '${error }';
		if (errorMessage) {
			alert(errorMessage);
			window.location.href = '/recharge/rechargeMoney';
		}
	</script>
	<meta charset="UTF-8">
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
			<c:if test="${empty classKey }">
				<!-- 银行充值 -->
				<div class="accountCentreContent alipayBox">
					<!-- 选择银行、金额 -->
					<form action="" id="confirmRechargeArea" method="post">
						<!-- 确认充值信息 -->
						<div class="confirmRechargeArea">
							<div class="stepBox">
								<ul class="stepBar">
									<li><span class="step step-1 active"><i>01</i>选择银行并填写充值金额</span></li>
									<li><span class="step step-2 active"><i>02</i>确认充值信息</span></li>
									<li><span class="step step-3"><i>03</i>登陆网银汇款</span></li>
								</ul>
								<a href="/helpCenter/index?id=${bankArticleId }" class="rechTeach">如何充值？</a>
							</div>
							<div class="confirmRechargeBox bankRech">
								<div class="cfmRechSecond">
									<c:choose>
										<c:when test="${recharge.bankNameCode == 'weixin'}">
											<h2>请扫描下面的二维码进行充值支付</h2>
										</c:when>
										<c:otherwise>
											<h2>请登录您的${recharge.bankName}，向下方收款人转账</h2>
										</c:otherwise>
									</c:choose>
											<div class="rechLeft">
												<!--<div class="rechargeInfo">-->
												<!--<h3 class="infoTitle">充值确认</h3>-->
												<!--<p class="infoDetail">-->
												<!--<span class="labelTitle">充值银行：</span>-->
												<!--<img src="<c:url value='/res/home/images/bank/${recharge.bankNameCode }.png'/>" alt="${recharge.bankName }" class="rechargeBank"/>-->
												<!--</p>-->
												<!--<p class="infoDetail">-->
												<!--<span class="labelTitle">充值金额：</span>-->
												<!--<span class="fontColorRed">${recharge.amount }</span>-->
												<!--<span>元</span>-->
												<!--</p>-->
												<!--<p class="infoDetail">-->
												<!--<span class="labelTitle">需用银行卡：</span>-->
												<!--<span>可使用任意一张${recharge.bankName }卡进行汇款</span>-->
												<!--</p>-->
												<!--</div>-->
										<c:choose>
											<c:when test="${recharge.bankNameCode == 'weixin'}">
												<div class="rechLeftInfo" style="text-align:center;">
													<img src="<c:url value='/res/payQRCode/alipayQRCode.jpg'/>" style="height: 300px;width: 250px" alt="${recharge.bankName}"/>
												</div>
											</c:when>
											<c:otherwise>
												<div class="rechLeftInfo">
													<!--<h3 class="infoTitle">收款方信息 以下信息是确保您充值到帐的重要信息</h3>-->
													<!--<p class="infoDetail">-->
													<!--<span class="labelTitle">收款银行：</span>-->
													<!--<span class="fontColorRed">${recharge.receiveBankName }</span>-->
													<!--<a href="javascript:copyToClipboard('${recharge.receiveBankName }');" copyText="${recharge.receiveBankName }" class="copy">复制</a>-->
													<!--</p>-->
													<p class="infoDetail">
														<span class="labelTitle">收款姓名：</span>
														<span class="fontColorRed">${recharge.receiveNiceName }</span>
														<a href="javascript:copyToClipboard('${recharge.receiveNiceName }');"
														   copyText="${recharge.receiveNiceName }" class="copy">复制</a>
													</p>
													<p class="infoDetail">
														<span class="labelTitle">收款卡号：</span>
														<span class="fontColorRed">${recharge.receiveCard }</span>
														<a href="javascript:copyToClipboard('${recharge.receiveCard }');"
														   copyText="${recharge.receiveCard }" class="copy">复制</a>
													</p>
													<i class="alipayNameTips"></i>
												</div>
											</c:otherwise>
										</c:choose>
										<div class="rechLeftInfo bankRechAmount">
											<p class="infoDetail">
												<span class="labelTitle">充值金额：</span>
												<span class="fontColorRed">${recharge.amount }</span>
												<span>元</span>
												<a href="javascript:copyToClipboard('${recharge.amount }');"
												   copyText="${recharge.amount }" class="copy">复制</a>
											</p>
											<p class="infoDetail remarkDetail">
												<span class="labelTitle">附言：</span>
												<i class="zoom"></i>
												<span class="fontColorRed remark">${recharge.traceId }</span>
												<a href="javascript:copyToClipboard('${recharge.traceId }');"
												   copyText="${recharge.traceId }" class="copy">复制</a>
											</p>
											<!--<p class="payeeExplain">-->
											<!--<span>附言在部分网站以“备注”，“用途”等名词出现，请务必正确填写此项信息，填写错误或不填写会影响充值到帐。</span>-->
											<!--</p>-->
											<!--<p class="infoDetail">-->
											<!--<span class="labelTitle">开户银行名称：</span>-->
											<!--<span class="fontColorRed">${recharge.receiveAddress }</span>-->
											<!--<a href="javascript:copyToClipboard('${recharge.receiveAddress }');" copyText="${recharge.receiveAddress }" class="copy">复制</a>-->
											<!--</p>-->
											<i class="alipayAmountTips"></i>
										</div>
										<!--<div class="rechargeTime">-->
										<!--<p class="rechargeCountDown fontColorRed">-->
										<!--<span id="rechMinute">${sysExpireTime }</span>-->
										<!--<span>:</span>-->
										<!--<span id="rechSecond">00</span>-->
										<!--</p>-->
										<!--<p class="rechTimeIntro"><span>为保障充值成功</span></p>-->
										<!--<p>-->
										<!--<span>请在</span>-->
										<!--<span class="fontColorRed">${sysExpireTime }</span>-->
										<!--<span>分钟之内完成支付</span>-->
										<!--</p>-->
										<!--</div>-->
									</div>
									<div class="confirmRechargeBtn">
										<c:choose>
											<c:when test="${recharge.bankNameCode == 'weixin'}">
												<div class="rechLeftInfo" style="text-align:center;">
													<a href="javascript:;" class="btn endImproveInfo" id="endImproveInfo">完成后务必点我完善信息</a>
												</div>
											</c:when>
											<c:otherwise>
												<a target="_blank" href="${recharge.receiveLink }" class="btn"
												   id="loginBankRech">登录网上银行付款</a>
												<a href="javascript:;" class="btn endImproveInfo" id="endImproveInfo">完成后务必点我完善信息</a>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="userMsg fontColorGray">
										<!--<p><span class="fontColorRed">*</span>重要说明：</p>-->
										<!--<p>转账完成后，您的充值将自动到账，若5分钟后仍为到账，请联系<a href="${sysService.link }" class="onlineServiceCtn fontColorTeal" target="_blank">在线客服</a></p>-->
										<!--<p>务必使用页面显示的附言进行充值，非页面显示的附言绝不可使用，以免遭遇资金损失</p>-->
										<img src="<c:url value='/res/home/images/recharge/bank/alipayTip-${recharge.bankNameCode}.jpg'/>"
											 onerror="this.style='display: none;'"/>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</c:if>
			<c:if test="${!empty classKey }">
				<c:choose>
					<c:when test="${classKey == 'alipay2bank' }">
						<div class="accountCentreContent alipayBox">
							<div class="confirmRechargeArea">
								<div class="stepBox">
									<ul class="stepBar">
										<li><span class="step step-1 active"><i>01</i>选择银行并填写充值金额</span></li>
										<li><span class="step step-2 active"><i>02</i>确认充值信息</span></li>
										<li><span class="step step-3"><i>03</i>完成</span></li>
									</ul>
									<a href="/helpCenter/index?id=${bankApi.articleId }"" class="rechTeach">如何充值？</a>
								</div>
								<div class="confirmRechargeBox">
									<div class="cfmRechSecond">
										<h2>请登录您的支付宝，向下方收款人转账</h2>
										<div class="rechLeft"
											 style="float: none;margin: 0 auto; color: red;margin-top: -30px;margin-bottom: 10px;white-space: nowrap;">
											<p>1.晚11点~次日凌晨1点为支付宝清算时间，支付宝转银行卡到账严重延迟，此时间请选择其他充值方式！</p>
											<p>2.充值时，若支付宝提示“第2天到账”或“次日24点前到账”，请勿付款！</p>
											<p>3.平台收款卡不定期变更，存错平台不予承担损失！</p>
										</div>
										<div class="rechLeft" style="float: none;margin: 0 auto;">
											<div class="rechLeftInfo">
												<p class="infoDetail">
													<span class="labelTitle">收款人姓名：</span>
													<span>${bankApi.email }</span>
													<a href="javascript:copyToClipboard('${bankApi.email}');"
													   copyText="${bankApi.email}" class="copy">复制</a>
												</p>
												<p class="infoDetail">
													<span class="labelTitle">收款银行：</span>
													<span>${bankApi.sign }</span>
													<a href="javascript:copyToClipboard('${bankApi.sign}');"
													   copyText="${bankApi.sign}" class="copy">复制</a>
												</p>
												<p class="infoDetail">
													<span class="labelTitle">收款卡号：</span>
													<span>${bankApi.merchantCode }</span>
													<a href="javascript:copyToClipboard('${recharge.receiveCard }');"
													   copyText="${recharge.receiveCard }" class="copy">复制</a>
												</p>
												<p class="infoDetail">
													<span class="labelTitle">充值金额：</span>
													<b class="fontColorRed">${recharge.amount }</b>
													<a href="javascript:copyToClipboard('${recharge.amount }');"
													   copyText="${recharge.amount }" class="copy">复制</a>
												</p>
											</div>
										</div>
									</div>
									<div class="confirmRechargeBtn">
										<a href="javascript:;" class="btn endImproveInfo" id="endImproveInfoAli2Bank">完成后务必点我完善信息</a>
									</div>
								</div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="accountCentreContent alipayBox">
							<div class="confirmRechargeArea">
								<div class="stepBox">
									<ul class="stepBar">
										<li><span class="step step-1 active"><i>01</i>选择银行并填写充值金额</span></li>
										<li><span class="step step-2 active"><i>02</i>进行扫码支付</span></li>
										<li><span class="step step-3"><i>03</i>完成</span></li>
									</ul>
									<a href="/helpCenter/index?id=${bankApi.articleId }"" class="rechTeach">如何充值？</a>
								</div>
								<div class="confirmRechargeBox">
									<div class="cfmRechSecond">
										<h2>请登录您的支付宝，向下方收款人转账</h2>
										<div class="rechLeft" style="width: 100%">
											<div class="rechLeftInfo">
												<p class="infoDetail">
													<span class="labelTitle">收款人姓名：</span>
													<span>${bankApi.email }</span>
													<a href="javascript:copyToClipboard('${bankApi.email}');"
													   copyText="${bankApi.email}" class="copy">复制</a>
												</p>
												<p class="infoDetail">
													<span class="labelTitle">支付宝账号：</span>
													<span>${bankApi.merchantCode }</span>
													<a href="javascript:copyToClipboard('${recharge.receiveCard }');"
													   copyText="${recharge.receiveCard }" class="copy">复制</a>
												</p>
												<i class="alipayNameTips"></i>
											</div>
											<div class="rechLeftInfo alipayRechAmount">
												<p class="infoDetail">
													<span class="labelTitle">转账金额：</span>
													<b class="fontColorRed">${recharge.amount }</b>
													<a href="javascript:copyToClipboard('${recharge.amount }');"
													   copyText="${recharge.amount }" class="copy">复制</a>
												</p>
												<p class="infoDetail postscript">
													<span class="labelTitle">附言：</span>
													<b class="postscriptCont fontColorRed copyText">${recharge.traceId }</b>
													<!--<i><img src="<c:url value='/res/home/images/recharge/alipayTip.jpg'/>" /></i>-->
													<a href="javascript:copyToClipboard('${recharge.traceId }');"
													   copyText="${recharge.traceId }" class="copy">复制</a>
												</p>
												<i class="alipayAmountTips"></i>
											</div>
										</div>
									</div>
									<div class="confirmRechargeBtn">
										<a href="rechargeFinish?id=${recharge.id }&bankApiId=${bankApi.id }"
										   class="btn">我已通过支付宝转账完毕</a>
									</div>
									<div class="userMsg fontColorGray">
										<!--<p><span class="fontColorRed">*</span>重要说明：</p>-->
										<!--<p>转账完成后，您的充值将自动到账，若5分钟后仍为到账，请联系<a href="${sysService.link }" class="onlineServiceCtn fontColorTeal" target="_blank">在线客服</a></p>-->
										<!--<p>务必使用页面显示的附言进行充值，非页面显示的附言绝不可使用，以免遭遇资金损失</p>-->
										<img src="<c:url value='/res/home/images/recharge/alipayTip.jpg'/>"/>
									</div>
								</div>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</div>
</div>
<div class="layui-layer-content" id="loginBankRechDialog" style="width: 450px; display: none">
	<p class="lottTipLayerTitle">温馨提示</p>
	<div class="layerBox">
		<p class="fontColorRed">* 请您在新弹出的网银页面中完成转账后，再点击“我已转账完毕按钮”，提交存款信息</p>
		<div class="loginBankRechBox">
			<label class="checkboxDetail">
				<input type="checkbox" class="checkbox"/>
				<span>我知道啦，下次不需要再提醒了</span>
			</label>
			<a href="javascript:;" class="btn" id="notShowAgain">我知道了</a>
		</div>
	</div>
</div>
<div class="layui-layer-content" id="confirmRechargeDialog" style="width: 450px; display: none">
	<p class="lottTipLayerTitle">温馨提示</p>
	<div class="layerBox" id="tableBoxNone">
		<!-- 弹窗内容 -->
		<p class="fontColorRed">
			<c:choose>
				<c:when test="${recharge.bankNameCode == 'weixin'}">
					* 请填写您在刚才微信扫描支付中所使用微信账户的相关信息
				</c:when>
				<c:otherwise>
					* 请填写您在刚才转账中，所使用的存款支付宝实名认证信息（不是昵称！填错不自动到！）
				</c:otherwise>
			</c:choose>
		</p>
		<form id="formSetCard" method="post">
			<input type="hidden" name="id" value="${recharge.id }">
			<c:choose>
				<c:when test="${classKey == 'alipay2bank' }">
					<div class="labelDiv">
						<span class="labelTitle">存款支付宝姓名:</span>
						<input type="text" name="niceName" class="labelCond"/>
					</div>
					<input type="hidden" name="card" value="0000">
				</c:when>
				<c:when test="${recharge.bankNameCode == 'weixin'}">
					<div class="labelDiv">
						<span class="labelTitle">姓名:</span>
						<input type="text" name="niceName" class="labelCond"/>
					</div>
					<div class="labelDiv">
						<span class="labelTitle">微信号后4位:</span>
						<input type="text" name="card" class="labelCond"/>
					</div>
				</c:when>
				<c:otherwise>
					<div class="labelDiv">
						<span class="labelTitle">存款卡姓名:</span>
						<input type="text" name="niceName" class="labelCond"/>
					</div>
					<div class="labelDiv">
						<span class="labelTitle">存款卡卡号后4位:</span>
						<input type="text" name="card" class="labelCond"/>
					</div>
				</c:otherwise>
			</c:choose>
			<!-- 弹窗按钮 -->
			<div class="dialogBtn">
				<button type="submit" class="btn">提交</button>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="<c:url value='/res/home/js/zclip/zclip.min.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript">
	//复制内容到剪贴板
	function copyToClipboard(txt) {
	}

	var end_v = '${expireTime }';
	var now_v = '${nowTime }';
	$(function () {
		$(".copy").each(function () {
			$(this).zclip({
				path: '/res/home/js/zclip/zclip.swf',
				copy: $(this).attr("copyText"),
				afterCopy: function () {
					$.alert("已复制到剪贴板，请使用Ctrl+V粘贴");
				}
			});
		});
		if (end_v && now_v) {
			var end_time = new Date(end_v).getTime();
			var now_time = new Date(now_v).getTime();
			var didf_time = (end_time - now_time) / 1000;
			countDown(didf_time, null, null, "#rechMinute", "#rechSecond");
		}
		$("#formSetCard").submit(function () {
			var d = serializeObject(this);
			var method = $(this).attr("method");
			ajaxExt({
				method: method,
				url: '/recharge/saveRechargeInfo',
				dataType: 'json',
				data: d,
				callback: function (rel) {
					$.alert("您的存款信息提交完毕，请您1分钟后核实游戏额度");
				}
			});
			return false;
		});
		//登录网上银行付款弹窗
		$('#loginBankRech').click(function () {
			var cookie = document.cookie.split(';');//array,所有cookie
			var bankRechNotShowVal = 'false';
			$(cookie).each(function () {
				if (this.indexOf('bankRechNotShow') > -1) {
					bankRechNotShowVal = this.split('=')[1];
				}
			});
			if (bankRechNotShowVal == 'false') {
				$.dialog('#loginBankRechDialog');
			}
		});
		$('#endImproveInfo,#endImproveInfoAli2Bank').click(function () {
			$.dialog('#confirmRechargeDialog');
		});
		//不再显示
		$('#notShowAgain').click(function () {
			var checked = $('#loginBankRechDialog .checkbox:checked');
			if (checked.length) {
				document.cookie = 'bankRechNotShow=true;path=/';
				checked.removeAttr('checked');
			}
			layer.closeAll();
		});
	});

	function countDown(sys_second, day_elem, hour_elem, minute_elem, second_elem) {
		var timer = setInterval(function () {
			if (sys_second > 1) {
				sys_second -= 1;
				var day = Math.floor((sys_second / 3600) / 24);
				var hour = Math.floor((sys_second / 3600) % 24);
				var minute = Math.floor((sys_second / 60) % 60);
				var second = Math.floor(sys_second % 60);
				day_elem && $(day_elem).text(day);//计算天
				$(hour_elem).text(hour < 10 ? "0" + hour : hour);//计算小时
				$(minute_elem).text(minute < 10 ? "0" + minute : minute);//计算分
				$(second_elem).text(second < 10 ? "0" + second : second);// 计算秒
			} else {
				clearInterval(timer);
				location.href = "/recharge/rechargeFinish?id=${recharge.id }";
			}
		}, 1000);
	}
</script>
</body>
</html>
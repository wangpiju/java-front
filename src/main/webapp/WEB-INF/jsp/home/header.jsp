<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!--头部-->
<div class="header">
	<div class="header-top">
		<div class="header-top-content">
			<p>
				<%--<a href="<c:url value='/index'/>">Hi,欢迎来到大順彩票！</a>--%>
				<a href="javascript:;">Hi,欢迎来到大順娛樂！</a>
			</p>
			<c:if test="${null == user}">
				<ul class="header-top-content-nav-befo">
					<li><a href="/login">亲，请登录</a></li>
					<li><a href="/registByCode">用户注册</a></li>
					<li>
						<a href="${sysService.link}" class="customerService" target="_blank"><i></i>在线客服</a>
					</li>
				</ul>
			</c:if>
			<c:if test="${null != user}">
				<ul class="header-top-content-nav-befo">
					<li>
						<a href="javascript:;" class="nav-befo-img">
							<img src='/res/home/images/heardimg/${user.image ? user.image : 0}.jpg'
								 alt="">${user.account}
						</a>
					</li>
					<li class="security">
						<a href="/user/index">我的账户
							<i class="el-icon-arrow-down"></i>
						</a>
						<div>
							<i class="el-icon-caret-top"></i>
							<ul>
								<li>
									<a href="<c:url value='/game/index?tabId=gameBetList' />">投注记录</a>
								</li>
								<c:if test="${user.userType==1 }">
								<li><a href="<c:url value='/user/index?tabId=teamManageArea' />">团队管理</a></li>
								</c:if>
								<li><a href="<c:url value='/user/index?tabId=safe' />">个人信息</a></li>
								<c:if test="${user.userType==1 }">
								<li><a href="<c:url value='/user/index?tabId=agent' />">代理中心</a></li>
								</c:if>
							</ul>
						</div>
					</li>
					<li class="amount">余额：
						<span class="amount-shows">已隐藏&nbsp;&nbsp;<i class="amount-show">显示</i></span>
						<span class="amount-hides">${user.amount}&nbsp;<i class="amount-hide">&nbsp;隐藏</i> </span>
					</li>
					<li class="ebankPay">
						<a href="javascript:;"
						   onclick="javascript:if('${user.depositStatus}' == 1 || '${user.depositStatus}' == 3){$.alert('您的充值功能已被冻结！');}else{window.location.href='/rechargeNew/rechargeMoneyBank'}">充值
							<i class="el-icon-arrow-down"></i>
						</a>
						<div>
							<i class="el-icon-caret-top"></i>
							<ul>
								<li>
									<a href="/rechargeNew/rechargeMoney?rechargeWay=5">银行转账</a>
								</li>

								<li>
									<a href="/rechargeNew/rechargeMoney?rechargeWay=1">微信支付A</a>
								</li>
								<%--<li>
									<a href="/rechargeNew/rechargeMoney?rechargeWay=11">微信支付B</a>
								</li>--%>
                                <%--<li>
                                    <a href="/rechargeNew/rechargeMoney?rechargeWay=2">支付宝B</a>
                                </li>--%>
								<li>
									<a href="/recharge/rechargePay?type=qq">QQ钱包</a>
								</li>
                                <%--<li>--%>
                                    <%--<a href="/recharge/rechargePay">网银支付</a>--%>
                                <%--</li>--%>
							</ul>
						</div>
					</li>
					<li>
						<a href="javascript:;" onclick="userWithdraw()">提现</a>
					</li>
					<li>
						<a href="<c:url value='/logout'/>">退出</a>
					</li>
					<li>
						<a href="${sysService.link}" class="customerService" target="_blank"><i></i>在线客服</a>
					</li>
				</ul>
			</c:if>
		</div>
	</div>
	<div class="header-nav">
		<div class="header-nav-content">
			<a class="logo" href="<c:url value='/index'/>"></a>
			<ul class="header-nav-content-nav">
				<li class="hrefSy">
					<a href="<c:url value='/index'/>">首页</a>
				</li>
				<li class="hrefLottery">
					<a href="/lotts/lottery/index">彩票大厅</a>
				</li>
				<li class="hrefActive">
					<a href="/activity/index">活动中心</a>
				</li>
				<li class="hrefAppdown">
					<a href="/aappdown/index">手机购彩</a>
				</li>
				<%--<li><a href="javascript:void(0);">我的账户</a></li>--%>
				<li class="hrefHelpcenter">
					<a href="/helpCenter/index">帮助指南</a>
				</li>
			</ul>
		</div>
	</div>
	<div class="w1150"></div>
</div>
<script type="text/javascript">
	$(function () {
		//获取未读站内信数量
		ajaxObject('/message/messageCountUnRead', "POST", function (data) {
			$('.msg a').text(data)
			if (data > 0) {
				$("#platNoticeNumSpan").addClass("platNoticeNum").text(data);
			} else {
				$("#platNoticeNumSpan").removeClass("platNoticeNum").text("");
			}
		});
		$(".security").hover(function () {
			$(".security div").show();
		}, function () {
			$(".security div").hide();
		});
		$(".ebankPay").hover(function () {
			$(".ebankPay div").show();
		}, function () {
			$(".ebankPay div").hide();
		});
		//菜单点击
		var href = document.location.href;
		var _this = $(this);
		if (href == document.location.origin + '/index') {
			$(".header-nav-content-nav li").removeClass("acitve");
			$('.hrefSy').addClass('acitve');
		} else if (href == document.location.origin + '/lotts/lottery/index') {
			$(".header-nav-content-nav li").removeClass("acitve");
			$('.hrefLottery').addClass('acitve');
		} else if (href == document.location.origin + '/activity/index') {
			$(".header-nav-content-nav li").removeClass("acitve");
			$('.hrefActive').addClass('acitve');
		} else if (href == document.location.origin + '/aappdown/index') {
			$(".header-nav-content-nav li").removeClass("acitve");
			$('.hrefAppdown').addClass('acitve');
		} else if (href == document.location.origin + '/helpCenter/index') {
			$(".header-nav-content-nav li").removeClass("acitve");
			$('.hrefHelpcenter').addClass('acitve');
		}
		//显示、隐藏金额
		$(".amount-hides").hide();
		$(".amount-show").click(function () {
			$(".amount-shows").hide();
			$(".amount-hides").show();
		});
		$(".amount-hide").click(function () {
			$(".amount-shows").show();
			$(".amount-hides").hide();
		});


	});

	function userWithdraw () {

        var code_v;
        var message_v;

        var url = "/api/proxy/getWithdrawFlag";

        $.ajax({
            type: "GET",
            async: false,
            url: document.location.origin + url,
            dataType: "json",
            success: function(datas){
                var json = eval(datas); //数组
                $.each(json, function (index) {
                    if(index == 'code'){
                        code_v = json[index];
                    }
                    if(index == 'data'){
                        var message = json[index].message;
                        message_v = message;
                    }
                });
            }

        });

        if(code_v != null && code_v == 0){
            alert(message_v);
            return;
		}

        if('${user.depositStatus}' == 2 || '${user.depositStatus}' == 3){
            alert('您的提现功能已被冻结！');
        }else{
            window.location.href='/deposit/safePassword';
        }
    }

</script>
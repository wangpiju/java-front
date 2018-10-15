$(function () {
	
	//初始化轮播效果
	$('#userAcctBg a').eq(0).addClass('active');
	$('#userAcctBgList span').eq(0).addClass('active');
	//banner:hover时停止轮播
	userAcctBgScroll();
	$('#userAcctBg').hover(function () {
		$(this).stopTime('userAcctBg');
	}, userAcctBgScroll);
	//轮播按钮点击效果
	$('#userAcctBgList span').click(function () {
		var index = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		$('#userAcctBg').stopTime('userAcctBg');
		$('#userAcctBg a').eq(index).addClass('active').siblings().removeClass('active');
		acctIndex = index;
		userAcctBgScroll();
	});
	//网站公告、中奖播报
	initNoticeScroll('#webSiteNotice');
	initNoticeScroll('#awardsNotice');
	$('body').everyTime('1s', 'A', refererTime); // 1秒
	//公告弹窗
	var userPassStatus = $('#userPasswordStatus').val();//用户密码状态
	if (falg > 0) {
		layer.open({
			type: 1,
			skin: 'generalLayer',
			shift: 5,
			area: ['960px'],
			title: '公告详情',
			content: $('#platNoticeContentBox'),
			success: function () {
				$('.layui-layer-close, .closePlatNotice').on('click', function () {
					layer.closeAll();
					// if (userPassStatus == 0) {
					// 	improveInfoDialog();//显示完善用户资料弹窗
					// }
				});
			}
		});
	} else if (userPassStatus == 0) {
		//improveInfoDialog();//显示完善用户资料弹窗
	}
	//帮助中心hover效果
	$('#helpList a').on('mouseover', function () {
		var index = $(this).index() + 1;
		$(this).parent().removeClass().addClass('state-' + index);
	});
});

//初始化网站公告、中奖播报
function initNoticeScroll(id) {
	var maxWidth = noticeWidth(id);
	if (maxWidth > $(id).width()) {
		$(id).find('.notice').each(function () {
			$(id).append($(this).clone(true));
		});
		//设置宽度并绑定hover事件
		$(id).width(noticeWidth(id)).hover(function () {
			$(this).stopTime(this.id);
		}, function () {
			noticeScroll('#' + this.id);
		}).attr('data-width', maxWidth);
		noticeScroll(id);
	}
}

//所有公告的宽度
function noticeWidth(id) {
	var width = 0;//重置并重新计算
	$(id).find('.notice').each(function () {
		width += $(this).outerWidth(true);
	});
	return width;
}

//公告滚动
function noticeScroll(id) {
	var str = id.substr(1);
	$(id).everyTime('3cs', str, function () {
		var marginLeft = parseInt($(this).css('margin-left'));
		var lastMargin = parseInt($(this).find('.notice:last-child').css('margin-left'));
		var totalWidth = $(id).attr('data-width');
		if (marginLeft + lastMargin <= -totalWidth) {
			marginLeft = 0;
		}
		$(this).css('margin-left', --marginLeft);
	});
}

var acctIndex = 1;//banner轮播计数器
//banner轮播
function userAcctBgScroll() {
	$('#userAcctBg').everyTime('3s', 'userAcctBg', function () {
		var acctBgTotal = $(this).find('img').length - 1;
		$(this).find('a').eq(acctIndex).addClass('active').siblings().removeClass('active');
		$('#userAcctBgList span').eq(acctIndex).addClass('active').siblings().removeClass('active');
		if (acctIndex < acctBgTotal) {
			acctIndex++;
		} else {
			acctIndex = 0;
		}
	}, 0, true);
}

function numberFormat(n) {
	if (n < 10)
		return "0" + n;
	else
		return n;
}

// 倒计时
function refererTime() {
	$(".vlott span[data-time]").each(function (i, n) {
		var second = parseInt($(this).attr("data-time"));
		second -= 1;
		if (second >= 0) {
			var h = parseInt(second / 3600);
			var m = parseInt((second % 3600) / 60);
			var s = parseInt(second % 60);
			$(this).attr("data-time", second);
			var t = numberFormat(h) + ":" + numberFormat(m) + ":"
				+ numberFormat(s);
			$(this).text(t);
		} else {
			getInfo();
		}
	});
}

function getInfo() {
	ajaxExt({
		loading: '',
		noError: true,
		url: 'info',
		callback: function (data) {
			for (var n in data) {
				var k = data[n];
				$(".vlott span[data-key='" + k.id + "']").attr("data-time", k.time);
			}
		}
	});
}

//
// //显示完善用户资料弹窗
// function improveInfoDialog() {
//
// 	//完善用户资料
// 	layer.open({
// 		type: 1,
// 		skin: 'userImproveInfoLayer',
// 		shift: 5,
// 		area: ['755px', '413px'],
// 		title: false,
// 		content: $('#userImproveInfoArea')
// 	});
// }
function improveInfoForm() {
	if ($('#improveInfoForm div:eq(0)>input').val() == "") {
		$('#improveInfoForm div:eq(0)>span:eq(1)').addClass("fontColorRed");
		$('#improveInfoForm div:eq(0)>span:eq(1)').text("请输入新的登录密码");
		$('#improveInfoForm div:eq(0)>input').focus();
		return;
	}
	var regPass = /.{8,16}/g;
	if (!regPass.test($('#improveInfoForm div:eq(0)>input').val())) {
		$('#improveInfoForm div:eq(0)>span:eq(1)').addClass("fontColorRed");
		$('#improveInfoForm div:eq(0)>span:eq(1)').text("密码格式不正确");
		$('#improveInfoForm div:eq(0)>input').focus();
		return;
	}
	if ($('#improveInfoForm div:eq(1)>input').val() != $('#improveInfoForm>div>input').val()) {
		$('#improveInfoForm div:eq(1)>span:eq(1)').addClass("fontColorRed");
		$('#improveInfoForm div:eq(1)>span:eq(1)').text("密码输入不一致");
		$('#improveInfoForm div:eq(1)>input').focus();
		return;
	}
	//var regEmail = /\w{1,30}@\w{1,20}.com$/g;
	//if(!regEmail.test($('#improveInfoForm div:eq(4)>input').val())&&$('#improveInfoForm div:eq(4)>input').val()!=""){
	//	$('#improveInfoForm div:eq(4)>span:eq(1)').addClass("fontColorRed");
	//	$('#improveInfoForm div:eq(4)>span:eq(1)').text("邮箱格式不正确！");
	//	$('#improveInfoForm div:eq(4)>input').focus();
	//	return;
	//}
	var args = $('#improveInfoForm').serialize();
	Service("/safe/changePassWord", "POST", args, 1, function (data) {
		$.alert(data);
		document.getElementById("improveInfoForm").reset();
		$('#userImproveInfoArea').hide();
	});
}

//中间下,点击事件
$(function () {
	$(".autoTabUl li").click(function () {
		var index = $(this).index();
		$('.autoTabContent ul').eq(index).addClass("showautoTabContentUl").siblings().removeClass("showautoTabContentUl");
		$(this).addClass("active").siblings().removeClass("active");
	});
	//调用----循环向上滚动效果
	$("#FontScroll").scrollTop({speed: 30});
});
//循环向上滚动效果
(function ($) {
	$.fn.scrollTop = function (options) {
		var defaults = {
			speed: 30
		}
		var opts = $.extend(defaults, options);
		this.each(function () {
			var $timer;
			var scroll_top = 0;
			var obj = $(this);
			var $height = obj.find("ul").height();
			obj.find("ul").clone().appendTo(obj);
			obj.hover(function () {
				clearInterval($timer);
			}, function () {
				$timer = setInterval(function () {
					scroll_top++;
					if (scroll_top > $height) {
						scroll_top = 0;
					}
					obj.find("ul").first().css("margin-top", -scroll_top);
				}, opts.speed);
			}).trigger("mouseleave");
		})
	}
})(jQuery);
//获取当前奖项开奖号码
$(function () {
	// 江苏快3
	$.ajax({
		type: "get",
		url: "/api/lottery/getPastOpen",
		data: {lotteryId: "jsk3", count: 1},
		dataType: "json",
		async: false,
		cache: false,
		success: function (data) {
			var a, b, c, sum, seasonId, sumString = 0;
			a = data.data[0].n1;
			b = data.data[0].n2;
			c = data.data[0].n3;
			seasonId = data.data[0].seasonId;
			sum = a + b + c;
			sumString = a + ',' + b + ',' + c;
			$(".autoTabContentUl1 .sum").text(sum);
			$(".autoTabContentUl1 .seasonId").text(seasonId);
			$(".autoTabContentUl1 .sumString").text(sumString);
			$(".autoTabContentUl1 .img1").attr('src', "/res/home/images/dice/" + a + ".png");
			$(".autoTabContentUl1 .img2").attr('src', "/res/home/images/dice/" + b + ".png");
			$(".autoTabContentUl1 .img3").attr('src', "/res/home/images/dice/" + c + ".png");
		}
	});
	// 重庆时时彩
	$.ajax({
		type: "get",
		url: "/api/lottery/getPastOpen",
		data: {lotteryId: "cqssc", count: 1},
		dataType: "json",
		async: false,
		cache: false,
		success: function (data) {
			var a, b, c, d, e, seasonId, sumString = 0;
			a = data.data[0].n1;
			b = data.data[0].n2;
			c = data.data[0].n3;
			d = data.data[0].n4;
			e = data.data[0].n5;
			seasonId = data.data[0].seasonId;
			sum = a + b + c;
			sumString = a + ',' + b + ',' + c + ',' + d + ',' + e;
			$(".autoTabContentUl2 .n1").text(a);
			$(".autoTabContentUl2 .n2").text(b);
			$(".autoTabContentUl2 .n3").text(c);
			$(".autoTabContentUl2 .n4").text(d);
			$(".autoTabContentUl2 .n5").text(e);
			$(".autoTabContentUl2 .seasonId").text(seasonId);
			$(".autoTabContentUl2 .sumString").text(sumString);
		}
	});
	//宏发快3
	$.ajax({
		type: "get",
		url: "/api/lottery/getPastOpen",
		data: {lotteryId: "dfk3", count: 1},
		dataType: "json",
		async: false,
		cache: false,
		success: function (data) {
			var a, b, c, sum, seasonId, sumString = 0;
			a = data.data[0].n1;
			b = data.data[0].n2;
			c = data.data[0].n3;
			seasonId = data.data[0].seasonId;
			sum = a + b + c;
			sumString = a + ',' + b + ',' + c;
			$(".autoTabContentUl4 .sum").text(sum);
			$(".autoTabContentUl4 .seasonId").text(seasonId);
			$(".autoTabContentUl4 .sumString").text(sumString);
			$(".autoTabContentUl4 .img1").attr('src', "/res/home/images/dice/" + a + ".png");
			$(".autoTabContentUl4 .img2").attr('src', "/res/home/images/dice/" + b + ".png");
			$(".autoTabContentUl4 .img3").attr('src', "/res/home/images/dice/" + c + ".png");
		}
	});
	// 广东11选5
	$.ajax({
		type: "get",
		url: "/api/lottery/getPastOpen",
		data: {lotteryId: "gd11x5", count: 1},
		dataType: "json",
		async: false,
		cache: false,
		success: function (data) {
			var a, b, c, d, e, seasonId, sumString = 0;
			a = data.data[0].n1;
			b = data.data[0].n2;
			c = data.data[0].n3;
			d = data.data[0].n4;
			e = data.data[0].n5;
			seasonId = data.data[0].seasonId;
			sumString = a + ',' + b + ',' + c + ',' + d + ',' + e;
			$(".autoTabContentUl3 .n1").text(a);
			$(".autoTabContentUl3 .n2").text(b);
			$(".autoTabContentUl3 .n3").text(c);
			$(".autoTabContentUl3 .n4").text(d);
			$(".autoTabContentUl3 .n5").text(e);
			$(".autoTabContentUl3 .seasonId").text(seasonId);
			$(".autoTabContentUl3 .sumString").text(sumString);
		}
	});
	$.ajax({
		type: "get",
		url: "/api/lottery/getLastDayWinList",
		dataType: "json",
		async: false,
		cache: false,
		success: function (data) {
			var daylist = data.data;
			for (var i = 0; i < $(".middle-box-right-middle-Ul li").length; i++) {
				console.log(daylist[i].img)
				$(".champion img").attr("src", "/res/home/images/heardimg/" + daylist[0].img + ".jpg");
				$(".champion .nickname").html(capitalize(daylist[0].account));
				$(".champion .gain").html(daylist[0].bonus);
				$(".runnerUp img").attr("src", "/res/home/images/heardimg/" + daylist[1].img + ".jpg");
				$(".runnerUp .nickname").html(capitalize(daylist[1].account));
				$(".runnerUp .gain").html(daylist[1].bonus);
				$(".thirdPlace img").attr("src", "/res/home/images/heardimg/" + daylist[2].img + ".jpg");
				$(".thirdPlace .nickname").html(capitalize(daylist[2].account));
				$(".thirdPlace .gain").html(daylist[2].bonus);
			}
		}
	});
});

function capitalize(value) {
	var start = value.slice(0, 1);
	var end = value.slice(-1);
	return start + "***" + end;
}

//购彩大厅
$(function () {
	$('.lotteList-bott ul').eq(0).show().siblings().hide();
	$(".lotteList-top ul li").click(function () {
		var index = $(this).index();
		$('.lotteList-bott ul').eq(index).show().siblings().hide();
		$(this).addClass("active").siblings().removeClass("active");
	});
	$('.lotteListUl li .pop').hide();
	$(".lotteList-hot li").hover(function (event) {
		var index = $(this).index();
		$(".lotteList-hot li .pop").eq(index).show().siblings().hide();
	}, function (event) {
		var index = $(this).index();
		$(".lotteList-hot li .pop").eq(index).hide();
		$(".lotteList-hot li .Nopop").eq(index).show();
	})
	$(".lotteList-all li").hover(function (event) {
		var index = $(this).index();
		$(".lotteList-all li .pop").eq(index).show().siblings().hide();
	}, function (event) {
		var index = $(this).index();
		$(".lotteList-all li .pop").eq(index).hide();
		$(".lotteList-all li .Nopop").eq(index).show();
	})
	$(".lotteList-k3 li").hover(function (event) {
		var index = $(this).index();
		$(".lotteList-k3 li .pop").eq(index).show().siblings().hide();
	}, function (event) {
		var index = $(this).index();
		$(".lotteList-k3 li .pop").eq(index).hide();
		$(".lotteList-k3 li .Nopop").eq(index).show();
	})
	$(".lotteList-ssc li").hover(function (event) {
		var index = $(this).index();
		$(".lotteList-ssc li .pop").eq(index).show().siblings().hide();
	}, function (event) {
		var index = $(this).index();
		$(".lotteList-ssc li .pop").eq(index).hide();
		$(".lotteList-ssc li .Nopop").eq(index).show();
	})
	$(".lotteList-klc li").hover(function (event) {
        var index = $(this).index();
        $(".lotteList-klc li .pop").eq(index).show().siblings().hide();
    }, function (event) {
        var index = $(this).index();
        $(".lotteList-klc li .pop").eq(index).hide();
        $(".lotteList-klc li .Nopop").eq(index).show();
    })
    $(".lotteList-x11x5 li").hover(function (event) {
        var index = $(this).index();
        $(".lotteList-x11x5 li .pop").eq(index).show().siblings().hide();
    }, function (event) {
        var index = $(this).index();
        $(".lotteList-x11x5 li .pop").eq(index).hide();
        $(".lotteList-x11x5 li .Nopop").eq(index).show();
    })
});
//购彩大厅 轮播图
$(function () {
	var num = 0;
	var t = 0;
	var ulwidth = $(".lotteBox-top-banner-Ul").width();
	var liwidth = $(".lotteBox-top-banner-Ul li").width();
	var lilength = $(".lotteBox-top-banner-Ul li").length;
	t = setInterval(function () {
		if (num < lilength) {
			$(".lotteBox-top-banner-Ul").css({"left": -liwidth * num + "px"});
			num++;
		} else {
			num = 0;
			$(".lotteBox-top-banner-Ul").css({"left": -liwidth * num + "px"});
			num = 1;
		}
	}, 1400);
});

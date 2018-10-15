/**
 * Created by Administrator on 2016/1/14.
 */

var ieTrendLine = "";//IE浏览器走势图直线
var commonTrendLine;//非IE浏览器走势图直线
var aTrList = $('.lottsTrendTable tbody tr');//每期开奖
var numTotalCount = $(aTrList).find('td').eq(1).html().split(',').length;//开奖号码的数量，用于判断遗漏条数量、号温球
var numPlaces = $('.trendLine').eq(0).attr('colspan');
numTotalCount = numTotalCount > 5 ? numPlaces * 5 : numTotalCount * numPlaces;//最大渲染5个开奖号码的遗漏条
var trendLotteryNumTotal = $('.trendNumTotal');//走势图开奖号码统计列表
var numAppearTotalCount = $(trendLotteryNumTotal).eq(0);//号码出现总总次数
var maxOmitCount = $(trendLotteryNumTotal).eq(1);//最大遗漏值
var maxContinueAppearCount = $(trendLotteryNumTotal).eq(2);//最大连出值

$(function(){
	drawTrend();

	// 辅助线按钮-显示隐藏
	trendSplitLineCheck($('#trendSplitLine'));
	$('#trendSplitLine').change( function() {
		trendSplitLineCheck(this);
	});

	//遗漏条加载
	trendOmissionBar();
	// 遗漏、遗漏条按钮-显示隐藏
	trendOmissionCheck();
	$('#trendOmission,#trendOmissionBar').change( function(){
		trendOmissionCheck();
	});

	// 走势按钮-显示隐藏
	trendLineCheck($('#trendLine'));
	$('#trendLine').change( function () {
		trendLineCheck(this);
	});

	// 加载号温球状态
	lottsNumState();
	// 号温按钮-显示隐藏
	trendNumWarmColdCheck($('#trendNumWarmCold'));
	$('#trendNumWarmCold').change( function () {
		trendNumWarmColdCheck(this);
	});
	
});

//显示隐藏走势图
function trendHideSearchBar(ele){
	var searchBar = $('#trendSearchBarArea');
	if ( $(searchBar).is(':visible') ) {
		$(ele).addClass('active').find('span').html('显示功能区');
	} else {
		$(ele).removeClass('active').find('span').html('隐藏功能区');
	}
	$(searchBar).slideToggle();
}

//绘制走抛图
function drawTrend() {
	var x1 = 0,
		y1 = 0,
		x2 = 0,
		y2 = 0;

	//创建canvas面板
	createCanvas($('#lotteryTrendArea'));

	//获取位数
	var trendLineNum = $('.trendLine').length;
	for ( var a = 1; a <= trendLineNum; a++ ) {

		//获取显示期数总条数
		var trendBallList = $(".trendBall-" + a);
		for (var i = 0; i < trendBallList.length; i++) {
			//计算坐标的起始位置，并处于元素中心
			var w1 = trendBallList[i].clientWidth;
			var h1 = trendBallList[i].clientHeight;
			x1 = trendBallList[i].offsetLeft + w1 / 2;
			y1 = trendBallList[i].offsetTop + h1 / 2;
			if (i + 1 < trendBallList.length) {
				var w2 = trendBallList[i + 1].clientWidth;
				var h2 = trendBallList[i + 1].clientHeight;
				x2 = trendBallList[i + 1].offsetLeft + w2 / 2;
				y2 = trendBallList[i + 1].offsetTop + h2 / 2;
			}

			//开始绘制走势
			commonLine(x1, y1, x2, y2);

			x1 = x2;
			y1 = y2;
		}

	}
}

//常用浏览器走势图-非IE
function commonLine(x1, y1, x2, y2) {
	commonTrendLine.beginPath();
	commonTrendLine.moveTo(x1, y1);
	commonTrendLine.lineTo(x2, y2);
	commonTrendLine.closePath();
	commonTrendLine.stroke();
	commonTrendLine.restore();
}

//创建canvas区域
function createCanvas(ele){
	var w = $(ele).width();
	var h = $(ele).height();
	var oCanvas = document.createElement('canvas');
	oCanvas.id = 'lotteryTrendCanvas';
	oCanvas.className = 'lotteryTrendCanvas';
	oCanvas.width = w;
	oCanvas.height = h;
	commonTrendLine = oCanvas.getContext('2d');
	commonTrendLine.strokeStyle = 'red';
	commonTrendLine.lineWidth = 2;
	$(ele).append(oCanvas);
}

// 辅助线显示隐藏
function trendSplitLineCheck(ele) {
	if ($(ele).is(':checked')) {
		$('#lotteryTrendArea').removeClass('lotteryTrendNoBorder');
	} else {
		$('#lotteryTrendArea').addClass('lotteryTrendNoBorder');
	}
}

// 遗漏、遗漏条显示隐藏
function trendOmissionCheck() {
	var omission = $('#trendOmission').is(':checked');
	var omissionBar = $('#trendOmissionBar').is(':checked');
	if ( omission ) {
		$('.lottsTrendTable').css('color', '#b9b9b9');
		if ( !omissionBar ) {
			$('.trendBg-cyan').css({'background':'transparent', 'color':'#b9b9b9'});
		}
	} else {
		$('.lottsTrendTable').css('color', '#fff');
		if ( !omissionBar ) {
			$('.trendBg-cyan').css({'background':'transparent', 'color':'#fff'});
		}
	}
	if ( omissionBar ) {
		$('.trendBg-cyan').css({'background':'#e7e7e7', 'color':'#b9b9b9'});
	}
}

//号温球加载
function lottsNumState(){
	//循环遍历号温球颜色
	for(var i = 0; i < numTotalCount; i++){
		//获取每列的中奖球数
		var rowLottsBallCount = $(numAppearTotalCount).find('td').eq( i+2 ).html();
		//判断是否为中奖号码，并加载号温状态
		if ( rowLottsBallCount >= 1 && rowLottsBallCount <= 2 ){//1-2次为冷号
			loadingBallState('numBallCold', i);//加载号温球状态
		} else if ( rowLottsBallCount == 3 ){//3次为温号
			loadingBallState('numBallWarm', i);//加载号温球状态
		} else if ( rowLottsBallCount >= 4 ){//4次及以上为热号
			loadingBallState('numBallHot', i);//加载号温球状态
		}
	}
}
//加载号温球状态
function loadingBallState(state, row){
	//每列中奖球数
	$(aTrList).each(function(){
		var rowLottsBall = $(this).find('td').eq(row+2);
		if($(rowLottsBall).attr('class').indexOf('trendBall') > -1) {
			$(rowLottsBall).addClass(state);
		}
	})
}

//遗漏条加载
function trendOmissionBar(){
	//一列一列生成
	for (var a = 0; a < numTotalCount; a++){
		//倒序遍历遗漏条
		for (var b = aTrList.length - 1; b >= 0; b--){
			var ele = $(aTrList).eq(b).find('td').eq(a+2);
			var currentNumClass = $(ele).attr('class').indexOf('trendBall');//判断中奖号码球是否存在
			if (currentNumClass < 0) {
				$(ele).addClass('trendBg-cyan');
			} else {
				break;
			}
		}
	}
}

// 走势图显示隐藏
function trendLineCheck(ele) {
	if ($(ele).is(':checked')) {
		$('#lotteryTrendCanvas').show();
	} else {
		$('#lotteryTrendCanvas').hide();
	}
}

// 号温显示隐藏
function trendNumWarmColdCheck(ele) {
	if ($(ele).is(':checked')) {
		$('.lottsTrendTable [class*=numBall]').removeAttr('style');
	} else {
		$('.lottsTrendTable [class*=numBall]').css('background-image', 'url(/res/home/images/trend/ball-red.png)');
	}
}
$(function(){
    /* 顶部老虎机-跑马灯动画 */
    //function lights(){
    //    $('.lightsBox i').each(function() {
    //        var lightName = $(this).attr('class');
    //        var num = ~~lightName.substr(lightName.length - 1, 1);
    //        if (num == 1) {
    //            $(this).attr('class', 'light4');
    //        }
    //        else {
    //            $(this).attr('class', 'light' + (num - 1));
    //        }
    //    });
    //}
    //setInterval(lights,300);
});

/* 点击开奖球动画 */
function betBallAnimate(){
    $('#lotteryBall').animate({top:'30px'},300).animate({top:'-52px'},300);
    $('.rocker i').animate({height:'11px',top:'19px'},300).animate({height:'29px',top:0},300);
}

/* 中奖弹框 */
function layer1(win, order){
    var content = "<div class='lottWinBox'><div class='lottWinInfo'><h1>恭喜您中奖啦！</h1><p>本轮投注获得奖金</p><p><span class='highLight' id='lottWinAmount'>"+win+"</span>元</p></div><div class='lottWinBtn'><a href='javascript:;' class='btn againBtn'>再来一次</a><a href='javascript:;' class='btn returnBtn'>重新选号</a></div></div>";
    var index = layer.open({
        type: 1,
        skin: 'lottWinLayer',
        shift: 5,
        area:['410px','270px'],
        title:' ',
        content: content,
        success:function(){
        	$('.againBtn').on('click',function(){
        		betAgain(1, order.bounsType, order.seasonId, order.amount, order.count, order.order);
        		layer.close(index);
        	});
        	
        	$('.returnBtn').on('click',function(){
        		layer.close(index);
        	});
        }
    });
}
/* 未中奖弹框 */
function layer2(win, order){
    var content = "<div class='lottNotWinBox'><h1>很遗憾，您本轮未中奖</h1><div class='lottNotWinBtn'><a href='javascript:;' class='btn againBtn'>再来一次</a><a href='javascript:;' class='btn againBtnX2'>再来一次(倍数<span class='highLight'>X2</span>)</a><a href='javascript:;' class='btn againBtnX5'>再来一次(倍数<span class='highLight'>X5</span>)</a><a href='javascript:;' class='btn returnBtn'>重新选号</a></div></div>";
    var index = layer.open({
        type: 1,
        skin: 'lottNotWinLayer',
        shift: 5,
        area:['410px','270px'],
        title:' ',
        content: content,
        success:function(){
        	$('.againBtn').on('click',function(){
        		betAgain(1, order.bounsType, order.seasonId, order.amount, order.count, order.order);
        		layer.close(index);
        	});
        	
        	$('.againBtnX2').on('click',function(){
        		betAgain(2, order.bounsType, order.seasonId, order.amount, order.count, order.order);
        		layer.close(index);
        	});
        	
        	$('.againBtnX5').on('click',function(){
        		betAgain(5, order.bounsType, order.seasonId, order.amount, order.count, order.order);
        		layer.close(index);
        	});
        	
        	$('.returnBtn').on('click',function(){
        		layer.close(index);
        	});
        }
    });
}

/* 投注金额提示弹框 */
function layer3(n){
    var content = "<p class='lottTipLayerTitle'>提示</p><div class='lottTipBox'><p>进行第<span class='highLight' id='lotteryCount'>" + n + "</span>次开奖</p></div>";
    layer.open({
        type: 1,
        skin: 'lottTipLayer',
        shift: 5,
        area:['350px','180px'],
        title:' ',
        content: content
    });
}

/* 中奖金额提示弹框 */
function layer4(n, win) {
    var content = "<p class='lottTipLayerTitle'>提示</p><div class='lottTipBox'><p>本次中奖金额：<span id='winLottAmount' class='highLight'>" + win + "</span>元</p></div>";
    layer.open({
        type: 1,
        skin: 'lottTipLayer',
        shift: 5,
        area: ['350px', '180px'],
        title: ' ',
        content: content
    });
}

/* 其他通用提示弹框 */
function layer5(string) {
    var content = "<p class='lottTipLayerTitle'>提示</p><div class='lottTipBox'><p>" + string + "</p><div class='lottTipBtn'><a href='javascript:;' class='btn yesBtn'>确认</a></div></div>";
    var index = layer.open({
        type: 1,
        skin: 'lottTipLayer',
        shift: 5,
        area: ['350px', '180px'],
        title: ' ',
        content: content,
        success: function () {
            $('body').on('click', '.yesBtn', function () {
                layer.close(index);
            });
        }
    });
}

$.alert = function(d) {
	layer.closeAll();// 关闭所有弹出框
	layer5(d);
	betDone();
};
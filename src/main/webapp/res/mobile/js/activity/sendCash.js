$(function(){
	getBetconsumer();
	$('#sendCashActivityContent').html($('#sendCashActivityContent').text());
	$('.getCash').bind('click',function(){
		var id = $(this).attr('id');
		ajaxExt({
			url: '/activity/bonus/betconsumer?item=' + id,
			callback: function(data){
				getBetconsumer();
			}
		});
	});
});

//获取投注送现金数据
function getBetconsumer(){
	ajaxExt({
		url: '/activity/betconsumer',
		callback: function(data){
			$('#sendCashConsume').text(data.consumerAmount);
			$('#sendCashReceive').text(data.giveAmount);
			eachBetconsumer(data);
		}
	});
}

//遍历所有的投注送现金券，区分是否已领取
function eachBetconsumer(data){
	for(var i in data.hasGiveItmes){
		var id = data.hasGiveItmes[i];
		$('#'+id).parent().addClass('active');
		$('.sendCashList li.active a').unbind('click');
	}
}
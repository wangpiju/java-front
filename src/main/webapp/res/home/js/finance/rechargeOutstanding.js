$(function(){
	
	//0-银行转账；1-AA支付宝；2-以上都不是请选我
	$('.rechargeType').on('change',function(){
		var type = $(this).val();
		$('#rechOutForm input[type="text"]').val('');
		switch(type){
			case '0':
				$('#rechOutForm div').show();
				$('#aliAccountBox,#aliIdBox,#aliNameBox').hide();
				break;
			case '1':
				$('#rechOutForm div').show();
				$('#nameBox,#lastFourBox,#aliNameBox').hide();
				break;
			case '2':
				$('#rechOutForm div').show();
				$('#aliAccountBox,#aliIdBox,#nameBox,#lastFourBox,#aliNameBox').hide();
				break;
			case '3':
				$('#rechOutForm div').show();
				$('#aliAccountBox,#aliIdBox,#nameBox,#lastFourBox').hide();
				break;
		}
	});
	
	$('#submit').on('click',function(){
		if($('#amount').val() == ''){
			$.alert('请填写充值金额！');
			return false;
		}
		if($('[name="rechargeType"]:checked').val() == '0'){
			if($('#name').val() == ''){
				$.alert('请填写存款人姓名！');
				return false;
			}
			if($('#lastFour').val() == '' || $('#lastFour').val().length < 4){
				$.alert('请填写存款卡号后4位！');
				return false;
			}
		}
		if($('[name="rechargeType"]:checked').val() == '1'){
			if($('#aliAccount').val() == ''){
				$.alert('请填写支付宝账号！');
				return false;
			}
			if($('#aliId').val() == ''){
				$.alert('请填写支付宝订单号！');
				return false;
			}
		}
		$("#rechOutForm").submit();
	});
});

var checkMoney = function(obj) {
	var me = obj,v = me.value,index;
	me.value = v = v.replace(/^\.$/g, '');
	index = v.indexOf('.');
	if (index > 0) {
		me.value = v = v.replace(/(.+\..*)(\.)/g, '$1');
		if(v.substring(index + 1, v.length).length > 2) {
			me.value= v  = v.substring(0, v.indexOf(".") + 3);
		}
	}
	me.value = v = v.replace(/[^\d|^\.]/g, '');
	me.value = v = v.replace(/^00/g, '0');
};

$("#amount").keyup(function(){
	checkMoney(this);
});

$("#lastFour").keyup(function(){
	checkMoney(this);
});
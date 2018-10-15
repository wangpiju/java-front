$(function(){
	$('#submit').on('click',function(){
		if($('#amount').val() == ''){
			$.alert('请填写提款金额！');
			return false;
		}
		if($('#lastFour').val() == '' || $('#lastFour').val().length < 4){
			$.alert('请填写提款卡号后4位！');
			return false;
		}
		if($('#depositTime').val() == ''){
			$.alert('请填写提款时间！');
			return false;
		}
		$("#depOutForm").submit();
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
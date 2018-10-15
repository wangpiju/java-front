$(function(){
	$('.upDown').on('click',function(){
		if($(this).hasClass('active')){
			$(this).parents('li').find('.content').hide();
			$(this).removeClass('active').html('<i></i>展开');
		}else{
			$(this).parents('li').find('.content').show();
			$(this).addClass('active').html('<i></i>收起');
		}
	});
});

function add(id){
	ajaxExt({
		url:'/activity/add/'+id,
		type:"get",
		data: '',
		dataType:'json',
		callback:function(data){
			$.alert(data);
			$("#joinActivity").remove();
		}
	});
}

function bonus(id){
	ajaxExt({
		url:'/activity/bonus/'+id,
		type:"get",
		data: '',
		dataType:'json',
		callback:function(data){
			$.alert(data);
			$("#getPrize").remove();
		}
	});
}
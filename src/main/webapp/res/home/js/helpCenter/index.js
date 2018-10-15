$(function(){
	var urlId = document.location.search.slice(4);
	if(urlId){
		var a = $('.nav[data-id=' + urlId + ']');
		showDetailByAjax(urlId,a);
	}
	
	$('.nav,.textUnderline').on('click',function(){
		var id = $(this).attr('data-id');
		showDetailByAjax(id,$(this));
	});
});

function showDetail(id,groupName){
	alert(groupName);
	var param ="id="+id+"&groupName="+escape(encodeURIComponent(groupName));
	var squrl="/helpCenter/getContentById?"+param; 
	window.open(squrl);
}

function showDetailByAjax(id,elem){
	$('.nav').removeClass('active');
	elem.addClass('active');
	elem.parents('.leftListDetail').find('.leftListBigItem').click();
	args2="&id="+id;
	ajaxExt({
		url:'/helpCenter/getContentByAjax',
		method:"get",
		dataType:"json",
		data: args2,
		callback:function(data) {				
			$(".indexBox").hide();
			$("#helpCenterCommonTitle").html(data.title);
			$("#helpCenterCommonTime").html(data.createTime);
			$("#helpCenterCommonContent").html(data.content);
			$("#helpCenterCommonDetail").show();
		}
	});
	
}
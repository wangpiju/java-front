function add(id){
	ajaxObject('/activity/add/'+id,"get",function(data){
		$.alert({text: data, icon: 'ok'});
		$("#joinActivity").remove();
	});
}

function bonus(id){
	ajaxObject('/activity/bonus/'+id,"get",function(data){
		$.alert({text: data, icon: 'ok'});
		$("#getPrize").remove();
	});
}
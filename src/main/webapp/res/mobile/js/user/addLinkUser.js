$(document).ready(function(){
   
	init();
	
	$('#extaddress').keyup(function(){
		$($('#extaddress').next()).text("");
	});
	
	$('#createExtCode').click(function(){ //生成注册链接
		
		if($('#extaddress').val() == ""){
			$.alert("请输入推广渠道");
			$('#extaddress').focus();
			return;
		}
		var args = $('#linkOpenAcct').serialize();

		/*Service("/openUser/createExtCode","POST",args,1,function(data){
			$.alert("操作成功");
			init();
			document.getElementById("linkOpenAcct").reset();
		});*/
		
//		var url='/openUser/createExtCode?'+args;
		
		
		ajaxExt({
			url:'/openUser/createExtCode',
			method:'post',
			dataType:'json',
			data:args,
			callback:function(a){
				$.alert({text: "操作成功", icon: 'ok'});
				init();
				document.getElementById("linkOpenAcct").reset();
			}
		    
		});
		/*ajaxObject(url, "post", function(data){
			$.alert("操作成功");
			init();
			document.getElementById("linkOpenAcct").reset();
		});*/
		
	});
})


function init(){
	 $('#setRebate').empty();
		var min = 0;
		
		Service("/openUser/getExtQuota","GET",null,1,function(data){
			min = data.rebateRatio;
			for(;min>=0;min=Math.sub(min,0.1)){
				$("#setRebate").append("<option value=\""+(min).toFixed(1)+"\">"+(min).toFixed(1)+"%</option>");
			}
		});
}
function inputNumber(obj){
	obj.value = obj.value.replace(/\D/g,'');
}
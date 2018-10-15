$(document).ready(function(){

	var userPassStatus = $('#userPasswordStatus').val();

		if(userPassStatus == 0){
			$('#oldPassInput').hide();
		}else{
			$('#oldPassInput').show();
		}

	$("#changePassWord").click(function(){ //改密码
		
		if($('#oldLoginPass').val() == "" && userPassStatus != 0){
			$('#oldLoginPass').focus();
			return;
		}
		var regPass = /.{8,16}/g;
    	if(!regPass.test($('#oldLoginPass').val()) && userPassStatus != 0){
    		$('#oldLoginPass').focus();
    		return;
    	}
		if($('#newLoginPass').val() == ""){
			$('#newLoginPass').focus();
			return;
		}
		var regPass2 = /.{8,16}/g;
		if(!regPass2.test($('#newLoginPass').val())){
    		$('#newLoginPass').focus();
    		return;
    	}
		if($('#confirmLoginPass').val() != $('#newLoginPass').val()){
			$('#confirmLoginPass').focus();
			return;
		}
		
		var args = $('#modLoginPwd').serialize();
		Service("/safe/changePassWord","POST",args,1,function(data){
			$.alert({text:data,icon:'ok'});
			document.getElementById("modLoginPwd").reset();
			$('#oldPassInput').show();
			if(userPassStatus == 0){
				userPassStatus = 1;
			}
			$(document).oneTime('2s', function(){
				location.href = '/login';
			});
		});
	});
	
});
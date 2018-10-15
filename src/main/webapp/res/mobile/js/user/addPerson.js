$(document).ready(function(){
	loadUserQuota();
	//添加用户（用户类型改变）
	$("#addUserType").change(function(){
		loadUserQuota();
	});
	
	$('#submit').click(function(){
		if($('#addUserName').val() == ""){
			$('#addUserName').attr("placeHolder","请输入用户名")
			$('#addUserName').focus();
			return;
		}
		var regAccount = /^[a-zA-Z]\w{5,11}/g;
		if(!regAccount.test($('#addUserName').val())){
			$('#addUserName').focus();
			return;
		}
		var args = $('#addUserList').serialize();
		Service("/openUser/regist","POST",args,1,function(data){
			$.alert({text:data,icon:'ok'});
			document.getElementById("addUserName").value="";
		});
	});
});

function loadUserQuota(){
	var val = $('#addUserType').val();
	var m = null;
	if (val == 0) {
		m = (gloas.maxRatio < gloas.playerMaxRatio ? gloas.maxRatio : gloas.playerMaxRatio);
	} else {
		m = gloas.maxRatio;
	}
	var a = '';
	for(var n=m;n>=0;n = Math.sub(n,gloas.stepRatio)){
		a += '<option value="'+n+'">' + n + '%</option>';
	}
	$("#addReservePoint").html(a);
}

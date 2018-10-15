$(function(){
	//查看契约免责声明弹层
	$('a#showContractDisclaimer').on('click',function(){
		var index = layer.open({
	        type: 1,
	        skin: 'lottTipLayer',
	        shift: 5,
	        area: ['810px','610px'],
	        title: false,
	        content: $('#contractDisclaimerLayer'),
	        success: function () {
	        	$('.closeBtn').on('click',function(){
	        		layer.close(index);
	        	});
	        }
	    });
	});
});

function agree(account,parentAccount){
	ajaxObject('/contract/agree?account=' +account+"&parentAccount="+parentAccount ,"POST",function(data){
		tipLayer(data);
	});
}

function refuse(account,parentAccount){
	ajaxObject('/contract/refuse?account=' +account+"&parentAccount="+parentAccount ,"POST",function(data){
		tipLayer(data);
	});
}

function tipLayer(data){
	var content = "<p class='lottTipLayerTitle'>提示</p><div class='lottTipBox'><p class='msg'>" + data + "</p><div class='lottTipBtn'><a href='javascript:;' class='btn closeBtn'>关闭</a></div></div>";
    var index = layer.open({
        type: 1,
        skin: 'lottTipLayer',
        shift: 5,
        area: ['370px', '220px'],
        title: false,
        content: content,
        success: function () {
        	$('.closeBtn').on('click',function(){
        		layer.close(index);
        		location.reload();
        	});
        },
    	cancel:function(){
    		location.reload();
    	}
    });
}
$(document).ready(function(){

	//生成二维码
	//$('.regLink').each(function(){
	//	$(this).parent().parent().find('.QRCode').qrcode({ text: this.innerHTML, foreground: '#007133' });
	//});
	$('.QRCode').each(function(){
		var html = $(this).parents('.regLinkContent').find('.regLink').html();
		$(this).qrcode({ text: html, foreground: '#007133' });
		//var image = canvasToImage(this.getElementsByTagName('canvas'));

		//将canvas中的二维码转换为图片
		var canvas = document.getElementsByTagName('canvas')[$('.QRCode').index(this)];
		var image = new Image();
		image.src = canvas.toDataURL("image/png");
		$(canvas).after(image);
	});

});

//删除链接
function delUserExtCode(id){
	$.confirm({
		title: '警告',
		text: '确认删除此链接吗?',
		btn: 2,
		callback: function(){
			Service("/user/delExtCode","GET","id="+id,1,function(data){
				$.alert({text: "操作成功！", icon: 'ok'});
				//loadUserExtCode();
				window.location.reload();
			});
		}
	});
}

function copyToClipBoard(address){
	try{
		window.clipboardData.setData("Text",address);     
		$.alert({text: "复制成功！", icon: 'ok'});
	}catch(e){
		$.alert("您的浏览器不支持此功能，请手动复制！");
	}
	     
}

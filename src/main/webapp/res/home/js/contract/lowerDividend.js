$(document).ready(function(){
	//下级分红查询
	$("#search").click(function(){
		var args = $('#dividendSearchArgs').serialize();
		ajaxExt({
			url:'/contract/list',
			method:"get",
			data: args,
			dataType : 'json',
			callback:function(data) {
				hs.pagination.refleshPages(data.total, "dividendSearchArgs");
				$("#dividendListTable tr:gt(0)").remove();
				var rows = data.rows;
				if(rows.length>0){
					$("#nullMsg").css("display","none");
				}else{
					$("#nullMsg").css("display","block");
				}
				for(var i = 0;i < rows.length;i++){
					var tr = $("<tr class=\"tableDetail\"></tr>");
					$(tr).append("<td>"+rows[i].account+"</td>");
					$(tr).append("<td>"+rows[i].startDate+"</td>");
					$(tr).append("<td>"+rows[i].endDate+"</td>");
					$(tr).append("<td>"+rows[i].cumulativeSales+"</td>");
					$(tr).append("<td>"+rows[i].dividend+"</td>");
					$(tr).append("<td>"+rows[i].cumulativeProfit+"</td>");
					$(tr).append("<td>"+rows[i].dividendAmount+"</td>");
					switch(rows[i].status){
					 case 0:  $(tr).append("<td>尚未发放</td>");break;
					 case 1: $(tr).append("<td>发放完毕</td>");break;
					 case 2: $(tr).append("<td>不需分红</td>");break;
					 case 3:  $(tr).append("<td>逾期未发放</td>");break;
					 case 4: $(tr).append("<td>強制发放完毕</td>");break;
					 }
					if(rows[i].status==0){
						$(tr).append("<td id=\"tdStatus_131\"><a href='javascript:void(0);' class='fontColorTheme' onclick=\"payout('"+rows[i].id+"','"+rows[i].account+"','"+rows[i].parentAccount+"','"+rows[i].dividendAmount+"')\">分红</a></td>");
					}else{
						$(tr).append("<td id=\"tdStatus_131\"></td>");
					}
				
					$('#dividendListHeader').append(tr);
				}
			
			}
		})
	});
	
});


function payout(id,account,parentAccount,dividendAmount){
	var args ="&id="+id+"&account="+account+"&parentAccount="+parentAccount+"&dividendAmount="+dividendAmount;
	$.getJSON("/contract/payout",args,function(data){
		if(data.status==200){
			tipLayer(data.content);
			
		}else{			
			$.alert(data.content);
		}
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
        		$('#lowerDividend #search').trigger('click');
        	});
        },
    	cancel:function(){
    		$('#lowerDividend #search').trigger('click');
    	}
    });
}

/*------------------------
 * 用于可输入下拉框及非可输入下拉框
 -----------------------*/
//显示隐藏可输入下拉框
function inputSelectFrame(ele) {
	var inputOptions = $(ele).siblings('.inputOptions');
	$(inputOptions).is(':hidden') ? $(inputOptions).show() : $(inputOptions).hide();
	$(ele).parent().hover(function(){},function(){
		$(inputOptions).hide();
	});
}
//选择并赋值
function inputSelectConfirm(ele, val, callback) {
	$(ele).parent().siblings('.labelCond').val(val).next('.labelCond').html(ele.innerHTML);
	inputSelectFrame($(ele).parent().siblings('.inputSelectArrow'));
	if(callback){
		callback(val);
	}
}
/*---end---*/
$(function(){
	//查看契约规则弹层
	$('a#showContractRule').on('click',function(){
		var index = layer.open({
	        type: 1,
	        skin: 'lottTipLayer',
	        shift: 5,
	        area: ['810px','610px'],
	        title: false,
	        content: $('#contractRuleLayer'),
	        success: function () {
	        	$('.closeBtn').on('click',function(){
	        		layer.close(index);
	        	});
	        }
	    });
	});
});

//添加契约规则
function addContract(){
	var n = $("#setContractList li").length;
	var limit =ruleNum-1;
	if(n>limit){
		return;
	}
	var div = "<li>";
	div+='<a href="javascript:;" class="removeBtn" onClick="deleteContract(this)"></a>';
	if(n==1){
		div+='<label class="listTitle">契约规则<span class="ruleNum">二</span>：</label>';
	}else if(n==2){
		div+='<label class="listTitle">契约规则<span class="ruleNum">三</span>：</label>';
	}else if(n==3){
		div+='<label class="listTitle">契约规则<span class="ruleNum">四</span>：</label>';
	}else if(n==4){
		div+='<label class="listTitle">契约规则<span class="ruleNum" >五</span>：</label>';
	}else if(n==5){
		div+='<label class="listTitle">契约规则<span class="ruleNum">六</span>：</label>';
	}else if(n==6){
		div+='<label class="listTitle">契约规则<span class="ruleNum">七</span>：</label>';
	}else if(n==7){
		div+='<label class="listTitle">契约规则<span class="ruleNum">八</span>：</label>';
	}else if(n==8){
		div+='<label class="listTitle">契约规则<span class="ruleNum">九</span>：</label>';
	}else if(n==9){
		div+='<label class="listTitle">契约规则<span class="ruleNum">十</span>：</label>';
	}
	div+='<span class="inputBox">';
	if(contractConfigType){
		div+='<label>半月累计销量：</label>';	
	}else{
		div+='<label>一月累计销量：</label>';	
	}
	div+='<input type="text"  class="labelCond cumulativeSales" name="contractRuleList['+ n +'].cumulativeSales" value="" onkeyup="checkCumulativeSales(this)"/></span>'
	div+='<span class="inputBox"><label>投注人数：</label><input type="text"  class="labelCond humenNum" name="contractRuleList['+ n +'].humenNum" value="" onkeyup="checkHumenNum(this)" />人</span>';
	div+='<span class="inputBox"><label>分红：</label><input type="text" class="labelCond dividend" name="contractRuleList['+ n +'].dividend" value="" onkeyup="checkDividend(this)" />%</span>';
	div+='<p class="ruleError"></p>';
	div+="</li>";
	$("#setContractList").append(div);
}
//删除契约规则
function deleteContract(a) {
	$(a).parent("li").remove();
	$("#setContractList li").each(function(i,n){
		if(i==1){
			$(this).find(".ruleNum").text("二");
		}else if(i==2){
			$(this).find(".ruleNum").text("三");
		}else if(i==3){
			$(this).find(".ruleNum").text("四");
		}else if(i==4){
			$(this).find(".ruleNum").text("五");
		}else if(i==5){
			$(this).find(".ruleNum").text("六");
		}else if(i==6){
			$(this).find(".ruleNum").text("七");
		}else if(i==7){
			$(this).find(".ruleNum").text("八");
		}else if(i==8){
			$(this).find(".ruleNum").text("九");
		}else if(i==9){
			$(this).find(".ruleNum").text("十");
		}
		
		$(this).find(".cumulativeSales").attr("name","contractRuleList["+i+"].cumulativeSales");
		$(this).find(".humenNum").attr("name","contractRuleList["+i+"].humenNum");
		$(this).find(".dividend").attr("name","contractRuleList["+i+"].dividend");
	});
}
//修改契约
function edit(){
	$(".setContractContent .labelCond").prop("disabled",false);
	$("#addContract").show();
	$("#reModifyBtn").prop("disabled",false);
	$("#modifyBtn").prop("disabled",true);
}

function submitForm() {
	if (checkForm()) {
		ajaxObject('/contract/addOrUpdate?' + $("#contractForm").serialize(),"POST",function(data){
			tipLayer(data);
		});
	}
}

function checkForm(){
	var a = true;
	$("#setContractList li").each(function(i,n){
		if(i==0){
			var gtdBonuses=$(this).find(".gtdBonuses").val();
		  
		      if(!(Number(gtdBonuses)>=1&&Number(gtdBonuses)<=100)){  
		    	$(this).find(".error").text('请设定保底分红百分比在1.00到100.00之间');
		        a=false;
		        return;
		      }  
		    
			//var gtdBonusesCycle=$(this).find(".gtdBonusesCycle").val();
			
		}else if(i==1){
			var gtdBonuses=$(this).prev().find(".gtdBonuses").val();
			var cumulativeSales =$(this).find(".cumulativeSales").val();
			var dividend=$(this).find(".dividend").val();
			var humenNum =$(this).find(".humenNum").val();
			if(cumulativeSales.length==0||humenNum.length==0){
				$(this).find(".ruleError").text('请正确填写规则，栏位不可空白。 '); 
				a=false;
				return;
			}
			if(Number(dividend)<=Number(gtdBonuses)){
				$(this).find(".ruleError").text('分红比请大于保底分红的分红比!');  
				a=false;
				return;
			}
		}else{
			var preCumulativeSales =$(this).prev().find(".cumulativeSales").val();
			var preDividend=$(this).prev().find(".dividend").val();
			var preHumenNum =$(this).prev().find(".humenNum").val();
			var cumulativeSales =$(this).find(".cumulativeSales").val();
			var dividend=$(this).find(".dividend").val();
			var humenNum =$(this).find(".humenNum").val();
			if(cumulativeSales.length==0||humenNum.length==0){
				$(this).find(".ruleError").text('请正确填写规则，栏位不可空白。 '); 
				a=false;
				return;
			}
			if(Number(dividend)<=Number(preDividend)){
				$(this).find(".ruleError").text('分红比请大于保底分红的分红比!'); 
				a=false;
				return;
			}
			if(Number(cumulativeSales)<=Number(preCumulativeSales)){
				$(this).find(".ruleError").text('累积销量请大于前一个规则的累积销量!'); 
				a=false;
				return;
			}
			if(Number(humenNum)<Number(preHumenNum)){
				$(this).find(".ruleError").text('投注人数请大于等于前一个规则的投注人数!'); 
				a=false;
				return;
			}
		}
		
	});
	return a;
}
//撤销契约
function undo(account,parentAccount,contractStatus){
	ajaxObject('/contract/undo?account=' +account+"&parentAccount="+parentAccount+"&contractStatus="+contractStatus,"POST",function(data){
		tipLayer(data)
	});
}

function checkGtdBonuses(a){
	var gtdBonuses=$(a).val();
	if(gtdBonuses.length!=0){  
	    var reg = /^[0-9]+(.[0-9]{0,2})?$/;  
		if(!reg.test(gtdBonuses)){ 
			$(a).parent().siblings(".error").text("请输入有效数字，不可为负数，小数点限制2位。");
		}else{
			$(a).parent().siblings(".error").text('');
		}
	}else{
		$(a).parent().siblings(".error").text("请输入分红比例。");
	}
}
function checkGtdBonusesCycle(a){
	var gtdBonusesCycle=$(a).val();
	if(gtdBonusesCycle.length!=0){  
	    var reg =/^\+?[1-9][0-9]*$/;
		if(!reg.test(gtdBonusesCycle)){ 
			$(a).parent().siblings(".error").text("保底周期，请输入自然整数（1,2,3....）");
		}else{
			$(a).parent().siblings(".error").text('');
		}
	}
}
function checkCumulativeSales(a){
	var cumulativeSales=$(a).val();
	if(cumulativeSales.length!=0){  
	    var reg =/^[1-9]\d*(\.\d+)?$/; 
		if(!reg.test(cumulativeSales)){ 
			$(a).parent().siblings(".ruleError").text("请输入有效数字，销售量不可为零");
		}else{
			$(a).parent().siblings(".ruleError").text('');
		}
	}
}

function checkHumenNum(a){
	var humenNum=$(a).val();
	if(humenNum.length!=0){  
	    var reg =/^\+?[1-9][0-9]*$/;
		if(!reg.test(humenNum)){ 
			$(a).parent().siblings(".ruleError").text("请输入自然整数（1,2,3....）");
		}else{
			$(a).parent().siblings(".ruleError").text('');
		}
	}
}

function checkDividend(a){
	var dividend=$(a).val();
	if(dividend.length!=0){  
	    var reg = /^[0-9]+(.[0-9]{0,2})?$/;  
		if(!reg.test(dividend)){ 
			$(a).parent().siblings(".ruleError").text("请输入有效数字，不可为负数，小数点限制2位。");
		}else{
			$(a).parent().siblings(".ruleError").text('');
		}
	}else{
		$(a).parent().siblings(".ruleError").text("请输入保底红利。");
	}
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

$(document).ready(function(){
	listByPage(1);
	
//	控制公告详情的字体大小
	$('.noticeFontSize').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		platNoticeFont(this.id);
	})
	
});


var totalPage;
function listByPage(pageNo){
	if( totalPage && pageNo > totalPage ) {
		pageNo = totalPage;
	}
	args ="&pageNo="+pageNo+"&pageSize="+10;
	ajaxExt({
		url:'/notice/list',
		method:"get",
		data: args,
		callback:function(pageModel) {
			totalPage = pageModel.totalPages;
			var data = pageModel.list;	
			var platList = "";
			for(var i = 0;i < data.length;i++){
				var a = " <a href='javascript:;' class='nav overflowEllipsis' onClick='showDetail("+data[i].id+")'><span class='listTitle overflowEllipsis'>"+data[i].title;
				var s =data[i].createTime;
					a = a + "</span><span class='listTime'>"+s.substr(5,5)+"</span></a>";
				platList = platList + a;
			}
			$('#platformNoticeList').html(platList);
		   if(pageModel.totalPages>0){
			   $("#currentPage").text("当前页："+pageModel.pageNo+"");
			   $("#totalPages").text("共"+pageModel.totalPages+"页  ") 
		   }else{
			   $("#currentPage").text("当前页："+0+"");
			   $("#totalPages").text("共"+0+"页  ") 
		   }
		   
		  var txt ="<a href=\"javascript:;\" class=\"pagePrev\" onClick=\"listByPage('"+pageModel.previousPageNo+"')\">&lt;</a>";
		  txt+=" <a href=\"javascript:;\" class=\"pageNext\" onClick=\"listByPage('"+pageModel.nextPageNo+"')\">&gt;</a>";
		  $("#PrevAndNext").html(txt);
		  
		}
	});
}
function showDetail(id){
	args ="&id="+id;
	ajaxExt({
		url:'/notice/getContentById',
		method:"get",
		data: args,
		callback:function(data) {	
			$("#noticeDetailText").html(data.content);
			$("#noticeDetailTitle").text(data.title);
			$("#noticeDetailDate").text(data.createTime);
		}
	});
}

//控制字体大小
function platNoticeFont ( size ){
	$('#noticeDetailText').removeClass("smallFont14 mediumFont18 bigFont22").addClass(size);
}
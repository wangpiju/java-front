/**
 * Created by Administrator on 2016/3/3.
 */

$(function(){

	//设置轮播图区域的宽高
	var placeholder = $('.picScrollList a').eq(1).clone(true).css({
		'position': 'relative',
		'visibility': 'hidden'
	});
	$('.picScrollList').append(placeholder);

});
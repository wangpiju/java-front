$("#send").click(function () {
	$.ajax({
		type: "GET",
		url: "/getHotLotteryList",
		dataType: "json",
		data: {},
		success: function (res) {
			//console.log(res);
		},
		error: function (err) {
			//console.log(err);
		}
	});
});
$("#send").click(function () {
	$.ajax({
		type: "GET",
		url: "/getHotLotteryList",
		dataType: "json",
		data: {},
		success: function (res) {
			//console.log(res);
		},
		error: function (err) {
			//console.log(err);
		}
	});
});
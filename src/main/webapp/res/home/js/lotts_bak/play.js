var BaseLottery = {
	split : /\s+|,|，|；|;/g,
	defaultIndex:0,
	all_single : function(s,regs,trues) {
		var content = [],err = [],cf = [],ncf=[];
		var hash = {};
		var lines = s.split(this.split);
		for ( var i in lines) {
			var line = lines[i];
			
			var hasErr = false;
			for(var r in regs){
				var reg = regs[r];
				var res = trues[r];
				if(reg.test(line) == res){
					err.push(line);
					hasErr = true;
					break;
				}
			}
			if (!hasErr) {
				content.push(line);
				if (!hash[line]) {
					//不重复的内容
					ncf.push(line);
					hash[line] = true;
				} else {
					//重复的内容
					cf.push(line);
				}
			}
		}

		return {
			content : content.join(" "),
			count : content.length,
			ncf : ncf.join(" "),
			cf : cf.join(" "),
			err : err.join(" ")
		};
	},
	combin : function(num, len) {
		if (num < len)
			return 0;
		var nums = 1, lens = 1;
		for (var i = 0; i < len; i++) {
			nums *= (num - i);
			lens *= (len - i);
		}
		return nums / lens;
	},
	// 是否包含：arr是否包含num
	contains : function(arr, num) {
		for ( var n in arr) {
			var m = arr[n];
			if (m == num)
				return true;
		}
		return false;
	},
	// 将内容格式化成数组
	toList : function(s) {
		var rel = [];
		if (s == "-") {
			return rel;
		}
		//单行用逗号隔开
		if (s.indexOf("|") < 0) {
			return s.split(",");
		}

		var lines = s.split("|");
		for ( var n in lines) {
			if (lines[n] != '-') {
				var line = lines[n].split(",");
				rel.push(line);
			}
		}
		return rel;
	},
	// 格式化显示和提交参数,lineSplit=行分隔符，numSplit=号码分隔符
	format : function(s, lineSplit, numSplit) {
		var rel = s.replace(/,/g, numSplit);
		rel = rel.replace(/\|/g, lineSplit);
		return rel;
	},
	// 根据玩法id,选中内容，给出玩法信息{标题，注数，内容(格式化后的)}
	run : function(playId, s) {
		s = $.trim(s);
		s = s.replace(/\s+/g, " ");
		var func = 'this.' + playId + '("' + s + '")';
		var rel = eval(func);
		rel.id = playId;
		return rel;
	},
	
	runRandom : function(playId){
		var func = 'this.'+ playId+'_random()';
		var rel = eval(func);
		rel.id = playId;
		return rel;
	},
	
	random : function (Min,Max)
	{   
		var Range = Max - Min;   
		var Rand = Math.random();   
		return(Min + Math.round(Rand * Range));   
	}
};
var audio5js = new Audio5js({
	swf_path: '/res/home/js/audio/audio5js.swf',
	ready: function () {
		this.load('/res/home/dd.mp3');
		//this.play();
		//playing
		//pause
	}
});
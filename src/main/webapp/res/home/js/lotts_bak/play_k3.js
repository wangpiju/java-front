var lottery = {
	defaultIndex:0,
	defaultPlayer:'k3_star1',
	//单挑一骰
	k3_star1 : function(s) {
		var lines = this.toList(s);
		var rel = 0;
		for ( var n in lines) {
			rel += lines[n].length;
		}
		var txt = this.format(s, ",", ",");
		return {
			content : txt,
			count : rel
		};
	},
	
	//二不同
	k3_star2_same_not : function(s) {
		s = s.replace(/\|-/g,"");	//后面的去掉
		s = s.replace(/-\|/g,"");	//前面的去掉
		s = s.replace(/\|/g,",");	//分隔替换
		var lines = this.toList(s);
		var rel = lines.length;
		return {
			content : s,
			count : rel
		};
	},
	
	//二同
	k3_star2_same : function(s) {
		return this.k3_star2_same_not(s);
	},
	
	//和值
	k3_star3_and : function(s){
		var bet = [1, 3, 6, 10, 15, 21, 25, 27,
			27, 25, 21, 15, 10, 6, 3, 1];
		s = s.replace(/\|-/g,"");	//后面的去掉
		s = s.replace(/-\|/g,"");	//前面的去掉
		s = s.replace(/\|/g,",");	//分隔替换
		var lines = this.toList(s);
		var rel = 0;
		for(var i in lines){
			rel += bet[lines[i]-3];
		}
		return {
			content : s,
			count : rel
		};
	},
	//三连号
	k3_star3_link:function(s){
		var lines = this.toList(s);
		var rel = lines.length;
		return {
			content : s,
			count : rel
		};
	},
	//三不同
	k3_star3_same_not:function(s){
		return this.k3_star2_same_not(s);
	},
	k3_star3_same:function(s){
		return this.k3_star2_same_not(s);
	}
};

lottery = jQuery.extend(BaseLottery,lottery);
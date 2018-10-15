var lottery = {
	defaultIndex:0,
	defaultPlayer:'n3_star3',
	// 定位胆
	n3_star1_dwd : function(s) {
		var lines = this.toList(s);
		var rel = 0;
		for ( var n in lines) {
			rel += lines[n].length;
		}
		var txt = this.format(s, ",", "");
		return {
			content : txt,
			count : rel
		};
	},

	// 时时彩 前二直选
	n3_star2_front : function(s) {
		var lines = this.toList(s);
		var rel = lines.length == 2 ? 1 : 0;
		for ( var n in lines) {
			rel *= lines[n].length;
		}
		var txt = this.format(s, ",", "") + ",-";
		return {
			content : txt,
			count : rel
		};
	},

	// 时时彩 后二直选
	n3_star2_last : function(s) {
		var rel = this.n3_star2_front(s);
		var txt = rel.content;
		rel.content = "-," + this.format(s, ",", "");
		return rel;
	},

	// 时时彩 前二直选 单式
	n3_star2_front_single : function(s) {
		var reg = /^\d{2}$/;
		return this.all_single(s,[reg],[false])
	},

	n3_star2_last_single : function(s) {
		var rel = this.n3_star2_front_single(s);
		return rel;
	},

	// 时时彩 前二直选 和值
	n3_star2_front_and : function(s) {
		var allCount = [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2,
				1 ];

		var lines = this.toList(s);
		var count = 0;
		for ( var n in lines) {
			try {
				count += allCount[parseInt(lines[n])];
			} catch (e) {
				count = 0;
				break;
			}
		}
		return {
			content : s,
			count : count
		};
	},

	n3_star2_last_and : function(s) {
		var rel = this.n3_star2_front_and(s);
		return rel;
	},

	// 时时彩 前二直选 跨度
	n3_star2_front_kd : function(s) {
		var allCount = [ 10, 18, 16, 14, 12, 10, 8, 6, 4, 2 ];

		var lines = this.toList(s);
		var count = 0;

		for ( var n in lines) {
			try {
				count += allCount[parseInt(lines[n])];
			} catch (e) {
				count = 0;
				break;
			}
		}
		return {
			content : s,
			count : count
		};
	},

	n3_star2_last_kd : function(s) {
		var rel = this.n3_star2_front_kd(s);
		return rel;
	},

	// 时时彩 前二组选
	n3_star2_front_group : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2);
		return {
			content : s,
			count : count
		};
	},

	n3_star2_last_group : function(s) {
		var rel = this.n3_star2_front_group(s);
		return rel;
	},

	// 时时彩 前二组选 单式
	n3_star2_front_group_single : function(s) {
		var reg = /^\d{2}$/;
		var reg2 = /(\d)\1/;
		return this.all_single(s,[reg,reg2],[false,true]);
	},

	n3_star2_last_group_single : function(s) {
		var rel = this.n3_star2_front_group_single(s);
		return rel;
	},

	// 时时彩 前二组选和值
	n3_star2_front_group_and : function(s) {
		var allCount = [ 1, 1, 2, 2, 3, 3, 4, 4, 5, 4, 4, 3, 3, 2, 2, 1, 1 ];
		var lines = this.toList(s);
		var count = 0;

		for ( var n in lines) {
			try {
				count += allCount[parseInt(lines[n]) - 1];
			} catch (e) {
				count = 0;
				break;
			}
		}
		return {
			content : s,
			count : count
		};
	},
	n3_star2_last_group_and : function(s) {
		var rel = this.n3_star2_front_group_and(s);
		return rel;
	},

	// 时时彩 前二组选包胆
	n3_star2_front_group_contains : function(s) {
		var lines = this.toList(s);
		var count = lines.length * 9;
		return {
			content : s,
			count : count
		};
	},

	n3_star2_last_group_contains : function(s) {
		var rel = this.n3_star2_front_group_contains(s);
		return rel;
	},

	// 时时彩 前三直选
	n3_star3 : function(s) {
		var lines = this.toList(s);
		var rel = lines.length == 3 ? 1 : 0;
		for ( var n in lines) {
			rel *= lines[n].length;
		}
		var txt = this.format(s, ",", "");
		return {
			content : txt,
			count : rel
		};
	},

	// 时时彩 三星直选 单式
	n3_star3_single : function(s) {
		var reg = /^\d{3}$/;
		return this.all_single(s,[reg],[false]);
	},

	// 三星直选和值
	n3_star3_and : function(s) {
		var allCount = [ 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69, 73, 75,
				75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 ];
		var lines = this.toList(s);
		var count = 0;
		for ( var n in lines) {
			try {
				count += allCount[parseInt(lines[n])];
			} catch (e) {
				count = 0;
				break;
			}
		}
		return {
			content : s,
			count : count
		};
	},

	// 三星直选跨度
	n3_star3_kd : function(s) {
		var allCount = [ 10, 54, 96, 126, 144, 150, 144, 126, 96, 54 ];
		var lines = this.toList(s);
		var count = 0;
		for ( var n in lines) {
			try {
				count += allCount[parseInt(lines[n])];
			} catch (e) {
				count = 0;
				break;
			}
		}
		return {
			content : s,
			count : count
		};
	},

	// 三星组选
	n3_star3_group3 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2) * 2;
		return {
			content : s,
			count : count
		};
	},

	// 三星组六
	n3_star3_group6 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 3);
		return {
			content : s,
			count : count
		};
	},

	n3_star3_group3_single : function(s) {
		var reg = /^\d{2}$/;
		var reg2 = /(\d)\1/;
		var rel = this.all_single(s,[reg,reg2],[false]);
		rel.count *=2;
		return rel;
	},

	// 组6
	n3_star3_group6_single : function(s) {
		var reg = /^\d{3}$/;
		var reg2 = /(\d)\d?\1/;

		return this.all_single(s,[reg,reg2],[false,true]);
	},

	// 三星组选 和值
	n3_star3_group_and : function(s) {
		var lines = this.toList(s);
		var allCount = [ 1, 2, 2, 4, 5, 6, 8, 10, 11, 13, 14, 14, 15, 15, 14,
				14, 13, 11, 10, 8, 6, 5, 4, 2, 2, 1 ];
		var count = 0;
		for ( var n in lines) {
			try {
				count += allCount[parseInt(lines[n]) - 1];
			} catch (e) {
				count = 0;
				break;
			}
		}
		return {
			content : s,
			count : count
		};
	},

	// 三星组选 包胆
	n3_star3_group_contains : function(s) {
		var lines = this.toList(s);
		var count = lines.length * 54;
		return {
			content : s,
			count : count
		};
	},

	// 混合组选
	n3_star3_group36_single : function(s) {
		var reg = /^\d{3}$/;
		var reg2 = /(\d)\1\1/;
		return this.all_single(s,[reg,reg2],[false,true]);
	},
	// 前三 一码不定位
	n3_star3_none1 : function(s) {
		var lines = this.toList(s);
		var count = lines.length;
		return {
			content : s,
			count : count
		};
	},

	// 前三 二码不定位
	n3_star3_none2 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2);
		return {
			content : s,
			count : count
		};
	}
};

lottery = jQuery.extend(BaseLottery,lottery);
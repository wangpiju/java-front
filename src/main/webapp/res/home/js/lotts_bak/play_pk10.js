var lottery = {
//		split : /,|，|；|;/g,
	defaultIndex:1,
	defaultPlayer:'pk10_star1_dwd',
	openNumLen : 1,
	getC : function(lines, index, allSize, cache) {
		var line = lines[index];
		var size = line.length;
		var count = 0;
		for (var i = 0; i < size; i++) {
			var n = line[i];
			if (!(n in cache)) {
				if (index + 1 < allSize) {
					cache[n] = true;
					count += this.getC(lines, index + 1, allSize, cache);
					delete (cache[n]);
				} else {
					// 最后一行
					count += 1;
				}
			}
		}
		return count;
	},

	getCount : function(lines) {
		return this.getC(lines, 0, lines.length, {});
	},
	
	run : function(playId, s) {
		s = $.trim(s);
		s = s.replace(/ +/g, "_");
		s = s.replace(/[\s+，；;]/g, ",");
		s = s.replace(/,+/g, ",");
		s = s.replace(/_+/g, "");
		var func = 'this.' + playId + '("' + s + '")';
		var rel = eval(func);
		rel.id = playId;
		return rel;
	},
	
	//龙虎
	pk10_side_lh:function(s){
		var titles = ["1V10","2V9","3V8","4V7","5V6"];
		var lines = this.toList(s);
		var rel = 0;
		for ( var n in lines) {
			rel += lines[n].length;
		}
		s = this.format(s, "|", "");
		var n = s.split("|");
		for(var i in n){
			if(n[i]!="-"){
				n[i] = "["+titles[i]+"]"+n[i];
			}
		}
		var txt= n.join();
		return {
			content : txt,
			count : rel
		};
	},
	
	//大小
	pk10_side_ds:function(s){
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
	
	//冠亚和大小
	pk10_side_gy_and:function(s){
		var lines = this.toList(s);
		return {
			content : s,
			count : lines.length
		};
	},
	
	//定位胆
	pk10_star1_dwd :function(s){
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
	//定位胆
	pk10_star1_dwd_last :function(s){
		return this.pk10_star1_dwd(s);
	},
	
	pk10_star2_and :function(s){
		var betCount = [2,2,4,4,6,6,8,8,10,8,8,6,6,4,4,2,2];
		var lines = this.toList(s);
		var rel = 0;
		for ( var n in lines) {
			var i = parseInt(lines[n])-3;
			rel += betCount[i];
		}
		return {
			content : s,
			count : rel
		};
	},
	pk10_star2 :function(s){
		var lines = this.toList(s);
		var rel = 0;
		if(lines.length==2){
			rel = this.getCount(lines);
			var txt = this.format(s, ",", "");
		}
		return {
			content : txt,
			count : rel
		};
	},
	pk10_star2_dj :function(s){
		var rel = this.pk10_star2(s);
		return rel;
	},
	pk10_star3 :function(s){
		var lines = this.toList(s);
		var rel = 0;
		if(lines.length==3){
			rel = this.getCount(lines);
			var txt = this.format(s, ",", "");
		}
		return {
			content : txt,
			count : rel
		};
	},
	pk10_star3_single :function(s){
		var reg1 = /^(0[1-9]|10){3}$/;
		var reg2 = /(\d{2})(\d{2})*\1/;
		return this.all_single(s,[reg1,reg2],[false,true]);
	},
	pk10_star3_dj :function(s){
		var rel = this.pk10_star3(s);
		return rel;
	},
	pk10_star4 :function(s){
		var lines = this.toList(s);
		var rel = 0;
		if(lines.length==4){
			rel = this.getCount(lines);
			var txt = this.format(s, ",", "");
		}
		return {
			content : txt,
			count : rel
		};
	},
	pk10_star4_single :function(s){
		var reg1 = /^(0[1-9]|10){4}$/;
		var reg2 = /(\d{2})(\d{2})*\1/;
		return this.all_single(s,[reg1,reg2],[false,true]);
	},
	pk10_star4_dj :function(s){
		var rel = this.pk10_star4(s);
		return rel;
	},
	pk10_star5 :function(s){
		var lines = this.toList(s);
		var rel = 0;
		if(lines.length==5){
			rel = this.getCount(lines);
			var txt = this.format(s, ",", "");
		}
		return {
			content : txt,
			count : rel
		};
	},
	pk10_star5_single :function(s){
		var reg1 = /^(0[1-9]|10){5}$/;
		var reg2 = /(\d{2})(\d{2})*\1/;
		return this.all_single(s,[reg1,reg2],[false,true]);
	},
	pk10_star5_dj :function(s){
		var rel = this.pk10_star5(s);
		return rel;
	},
};
lottery = jQuery.extend(BaseLottery,lottery);
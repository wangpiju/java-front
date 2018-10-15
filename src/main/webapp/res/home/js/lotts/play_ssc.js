var lottery = {
	defaultIndex:5,
	defaultPlayer:'ssc_star3_last',
	mulAll :function(lines,len){
		var nums = [];
		for(var n=0;n<lines.length;n++){
			var a = lines[n].length;
			if(a>0){
				nums.push(a);
			}
		}
		//位数不足
		if(nums.length<len) return 0;
		var maxSize = nums.length;
		var indexs = [];
		for(var n=0;n<len;n++){
			indexs[n]=n;
		}
		var allCount = 0;
		do{
			var count = 1;
			//当前位置计算
			for(var n=0;n<len;n++){
				count *= nums[indexs[n]];
			}
			allCount+=count;
			
			//下个位置
			for(var n=len-1;n>=0;n--){
				//最后一位下移
				indexs[n]++;
				if(indexs[n]<maxSize){
					if(n==len-1){ //最后一行
						break;
					} else {
						//把该行后面的重置
						for(var m=n+1;m<len;m++){
							indexs[m]=indexs[m-1]+1;
						}
						if(indexs[len-1]>=maxSize){
							continue;
						}
						break;
					}
				}
				//不正常则用前面一行再换
			}
			
		}while(indexs[0]<maxSize-len+1);
		return allCount;
	},
	
	//任选2 复式直选
	ssc_star2_any : function(s){
		var lines = this.toList(s);
		var rel = this.mulAll(lines,2);
		var txt = this.format(s, ",", "");
		
		return {content : txt,count : rel};
	},
	//任2 直选单式
	ssc_star2_any_single:function(s){
		return this.ssc_star2_front_single(s);
	},
	//任选2组选
	ssc_star2_any_group : function(s){
		return this.ssc_star2_front_group(s);
	},
	//任选2 组选单式
	ssc_star2_any_group_single:function(s){
		return this.ssc_star2_front_group_single(s);
	},
	//任选3直选复式
	ssc_star3_any : function(s){
		var lines = this.toList(s);
		var rel = this.mulAll(lines,3);
		var txt = this.format(s, ",", "");
		
		return {content : txt,count : rel};
	},
	//任选3直选单式
	ssc_star3_any_single : function(s){
		return this.ssc_star3_front_single(s);
	},
	//任选3 组三
	ssc_star3_any_group3 : function(s){
		return this.ssc_star3_front_group3(s);
	},
	//任选3 组6
	ssc_star3_any_group6 : function(s){
		return this.ssc_star3_front_group6(s);
	},
	//任选3 组6
	ssc_star3_any_group36_single : function(s){
		return this.ssc_star3_front_group36_single(s);
	},
	//任选4直选复式
	ssc_star4_any : function(s){
		var lines = this.toList(s);
		var rel = this.mulAll(lines,4);
		var txt = this.format(s, ",", "");
		
		return {
			content : txt,
			count : rel
		};
	},
	//任4 直选单式
	ssc_star4_any_single:function(s){
		return this.ssc_star4_front_single(s);
	},
	//龙虎和
	ssc_side_lhh : function(s){
		var titles = ["万千","万百","万十","万个","千百","千十","千个","百十","百个","十个"];
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
		return {content : txt,count : rel};
	},
	// 定位胆
	ssc_star1_dwd : function(s) {
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
	ssc_star2_front : function(s) {
		var lines = this.toList(s);
		var rel = lines.length == 2 ? 1 : 0;
		for ( var n in lines) {
			rel *= lines[n].length;
		}
		var txt = this.format(s, ",", "") + ",-,-,-";
		return {
			content : txt,
			count : rel
		};
	},

	// 时时彩 后二直选
	ssc_star2_last : function(s) {
		var rel = this.ssc_star2_front(s);
		var txt = rel.content;
		rel.content = "-,-,-," + this.format(s, ",", "");
		return rel;
	},

	// 时时彩 前二直选 单式
	ssc_star2_front_single : function(s) {
		return this.all_single(s,[/^\d{2}$/],[false]);
	},

	ssc_star2_last_single : function(s) {
		return this.ssc_star2_front_single(s);
	},

	// 时时彩 前二直选 和值
	ssc_star2_front_and : function(s) {
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

	ssc_star2_last_and : function(s) {
		var rel = this.ssc_star2_front_and(s);
		return rel;
	},

	// 时时彩 前二直选 跨度
	ssc_star2_front_kd : function(s) {
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

	ssc_star2_last_kd : function(s) {
		return this.ssc_star2_front_kd(s);
	},

	// 时时彩 前二组选
	ssc_star2_front_group : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2);
		return {
			content : s,
			count : count
		};
	},

	ssc_star2_last_group : function(s) {
		var rel = this.ssc_star2_front_group(s);
		return rel;
	},

	// 时时彩 前二组选 单式
	ssc_star2_front_group_single : function(s) {
		return this.all_single(s,[/^\d{2}$/,/(\d)\1/],[false,true]);
	},

	ssc_star2_last_group_single : function(s) {
		return this.ssc_star2_front_group_single(s);
	},

	// 时时彩 前二组选和值
	ssc_star2_front_group_and : function(s) {
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
	ssc_star2_last_group_and : function(s) {
		return this.ssc_star2_front_group_and(s);
	},

	// 时时彩 前二组选包胆
	ssc_star2_front_group_contains : function(s) {
		var lines = this.toList(s);
		var count = lines.length * 9;
		return {
			content : s,
			count : count
		};
	},

	ssc_star2_last_group_contains : function(s) {
		return this.ssc_star2_front_group_contains(s);
	},

	// 时时彩 前三直选
	ssc_star3_front : function(s) {
		var lines = this.toList(s);
		var rel = lines.length == 3 ? 1 : 0;
		for ( var n in lines) {
			rel *= lines[n].length;
		}
		var txt = this.format(s, ",", "") + ",-,-";
		return {
			content : txt,
			count : rel
		};
	},

	ssc_star3_mid : function(s) {
		var rel = this.ssc_star3_front(s);
		rel.content = "-," + this.format(s, ",", "") + ",-";

		return rel;
	},

	ssc_star3_last : function(s) {
		var rel = this.ssc_star3_front(s);
		rel.content = "-,-," + this.format(s, ",", "");
		return rel;
	},

	// 时时彩 前三直选 单式
	ssc_star3_front_single : function(s) {
		return this.all_single(s,[/^\d{3}$/],[false]);
	},

	ssc_star3_mid_single : function(s) {
		var rel = this.ssc_star3_front_single(s);
		return rel;
	},

	ssc_star3_last_single : function(s) {
		var rel = this.ssc_star3_front_single(s);
		return rel;
	},

	// 三星直选和值
	ssc_star3_front_and : function(s) {
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

	ssc_star3_mid_and : function(s) {
		var rel = this.ssc_star3_front_and(s);
		return rel;
	},

	ssc_star3_last_and : function(s) {
		var rel = this.ssc_star3_front_and(s);
		return rel;
	},

	// 三星直选跨度
	ssc_star3_front_kd : function(s) {
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

	ssc_star3_mid_kd : function(s) {
		var rel = this.ssc_star3_front_kd(s);
		return rel;
	},

	ssc_star3_last_kd : function(s) {
		var rel = this.ssc_star3_front_kd(s);
		return rel;
	},

	// 三星组选
	ssc_star3_front_group3 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2) * 2;
		return {
			content : s,
			count : count
		};
	},

	ssc_star3_mid_group3 : function(s) {
		var rel = this.ssc_star3_front_group3(s);
		return rel;
	},

	ssc_star3_last_group3 : function(s) {
		var rel = this.ssc_star3_front_group3(s);
		return rel;
	},

	// 三星组六
	ssc_star3_front_group6 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 3);
		return {
			content : s,
			count : count
		};
	},

	ssc_star3_mid_group6 : function(s) {
		var rel = this.ssc_star3_front_group6(s);
		return rel;
	},

	ssc_star3_last_group6 : function(s) {
		var rel = this.ssc_star3_front_group6(s);
		return rel;
	},

	ssc_star3_front_group3_single : function(s) {
		var rel = this.all_single(s,[/^\d{2}$/,/(\d)\1/],[false,true]);
		rel.count *=2;
		return rel;
	},

	ssc_star3_mid_group3_single : function(s) {
		return this.ssc_star3_front_group3_single(s);
	},

	ssc_star3_last_group3_single : function(s) {
		return this.ssc_star3_front_group3_single(s);
	},

	// 组6
	ssc_star3_front_group6_single : function(s) {
		return this.all_single(s,[/^\d{3}$/,/(\d)\d?\1/],[false,true]);
	},

	ssc_star3_mid_group6_single : function(s) {
		return this.ssc_star3_front_group6_single(s);
	},

	ssc_star3_last_group6_single : function(s) {
		return this.ssc_star3_front_group6_single(s);
	},

	// 三星组选 和值
	ssc_star3_front_group_and : function(s) {
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

	ssc_star3_mid_group_and : function(s) {
		return this.ssc_star3_front_group_and(s);
	},

	ssc_star3_last_group_and : function(s) {
		return this.ssc_star3_front_group_and(s);
	},
	// 三星组选 包胆
	ssc_star3_front_group_contains : function(s) {
		var lines = this.toList(s);
		var count = lines.length * 54;
		return {
			content : s,
			count : count
		};
	},

	ssc_star3_mid_group_contains : function(s) {
		return this.ssc_star3_front_group_contains(s);
	},

	ssc_star3_last_group_contains : function(s) {
		return this.ssc_star3_front_group_contains(s);
	},

	// 混合组选
	ssc_star3_front_group36_single : function(s) {
		return this.all_single(s,[/^\d{3}$/,/^(\d)\1\1$/],[false,true]);
	},
	ssc_star3_mid_group36_single : function(s) {
		return this.ssc_star3_front_group36_single(s);
	},
	ssc_star3_last_group36_single : function(s) {
		return this.ssc_star3_front_group36_single(s);
	},

	// 前三 一码不定位
	ssc_star3_front_none1 : function(s) {
		var lines = this.toList(s);
		var count = lines.length;
		return {
			content : s,
			count : count
		};
	},

	ssc_star3_mid_none1 : function(s) {
		return this.ssc_star3_front_none1(s);
	},

	ssc_star3_last_none1 : function(s) {
		return this.ssc_star3_front_none1(s);
	},

	// 前三 二码不定位
	ssc_star3_front_none2 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2);
		return { content : s, count : count };
	},

	ssc_star3_mid_none2 : function(s) {
		return this.ssc_star3_front_none2(s);
	},

	ssc_star3_last_none2 : function(s) {
		return this.ssc_star3_front_none2(s);
	},

	// 四星
	ssc_star4_front : function(s) {
		var lines = this.toList(s);
		var rel = lines.length == 4 ? 1 : 0;
		for ( var n in lines) {
			rel *= lines[n].length;
		}
		var txt = this.format(s, ",", "") + ",-";
		return { content : txt, count : rel };
	},
	ssc_star4_last : function(s) {
		var rel = this.ssc_star4_front(s);
		rel.content = "-," + this.format(s, ",", "");
		return rel;
	},

	// 时时彩 前四直选 单式
	ssc_star4_front_single : function(s) {
		return this.all_single(s,[/^\d{4}$/],[false]);
	},

	ssc_star4_last_single : function(s) {
		return this.ssc_star4_front_single(s);
	},

	// 组选4
	ssc_star4_front_group4 : function(s) {
		var lines = this.toList(s);
		var count = 0;

		if (lines.length == 2) {
			var line1 = lines[0];
			var line2 = lines[1];

			for ( var n in line1) {
				for ( var m in line2) {
					if (line1[n] != line2[m]) {
						count++;
					}
				}
			}
		}
		return {
			content : this.format(s, ",", ""),
			count : count
		};
	},

	ssc_star4_last_group4 : function(s) {
		return this.ssc_star4_front_group4(s);
	},

	// 组选6
	ssc_star4_front_group6 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2);
		return {
			content : s,
			count : count
		};
	},

	ssc_star4_last_group6 : function(s) {
		return this.ssc_star4_front_group6(s);
	},

	// 组选12
	ssc_star4_front_group12 : function(s) {
		var lines = this.toList(s);
		var count = 0;

		if (lines.length == 2) {
			var line1 = lines[0];
			var line2 = lines[1];

			for ( var n in line1) {
				var lenCount = 0;
				for ( var m in line2) {
					if (line1[n] != line2[m]) {
						lenCount++;
					}
				}
				count += this.combin(lenCount, 2);
			}
		}
		return {
			content : this.format(s, ",", ""),
			count : count
		};
	},

	ssc_star4_last_group12 : function(s) {
		return this.ssc_star4_front_group12(s);
	},

	// 组选24
	ssc_star4_front_group24 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 4);
		return { content : s, count : count };
	},

	ssc_star4_last_group24 : function(s) {
		return this.ssc_star4_front_group24(s);
	},

	// 一码不定位
	ssc_star4_front_none1 : function(s) {
		var lines = this.toList(s);
		var count = lines.length;
		return { content : s, count : count };
	},

	ssc_star4_last_none1 : function(s) {
		return this.ssc_star4_front_none1(s);
	},

	// 二码不定位
	ssc_star4_front_none2 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2);
		return { content : s, count : count };
	},

	ssc_star4_last_none2 : function(s) {
		return this.ssc_star4_front_none2(s);
	},

	// 五星
	ssc_star5 : function(s) {
		var lines = this.toList(s);
		var rel = lines.length == 5 ? 1 : 0;
		for ( var n in lines) {
			rel *= lines[n].length;
		}
		var txt = this.format(s, ",", "");
		return { content : txt, count : rel };
	},

	// 时时彩 五星直选 单式
	ssc_star5_single : function(s) {
		return this.all_single(s,[/^\d{5}$/],[false]);
	},

	ssc_star5_group5 : function(s) {
		var lines = this.toList(s);
		var count = 0;
		if (lines.length == 2) {
			var line1 = lines[0];
			var line2 = lines[1];

			for ( var n in line1) {
				count += line2.length;

				var num = line1[n];
				if (this.contains(line2, num)) {
					count -= 1;
				}
			}
		}

		var txt = this.format(s, ",", "");
		return { content : txt, count : count };
	},

	ssc_star5_group10 : function(s) {
		return this.ssc_star5_group5(s);
	},

	ssc_star5_group20 : function(s) {
		var lines = this.toList(s);
		var count = 0;
		if (lines.length == 2) {
			var line1 = lines[0];
			var line2 = lines[1];

			for ( var n in line1) {
				var nowCount = line2.length;

				var num = line1[n];
				if (this.contains(line2, num)) {
					nowCount -= 1;
				}
				count += this.combin(nowCount, 2);
			}
		}

		var txt = this.format(s, ",", "");
		return {
			content : txt,
			count : count
		};
	},

	ssc_star5_group30 : function(s) {
		var lines = this.toList(s);
		var count = 0;
		if (lines.length == 2) {
			var line1 = lines[0];
			var line2 = lines[1];

			for ( var n in line2) {
				var nowCount = line1.length;

				var num = line2[n];
				if (this.contains(line1, num)) {
					nowCount -= 1;
				}
				count += this.combin(nowCount, 2);
			}
		}

		var txt = this.format(s, ",", "");
		return {
			content : txt,
			count : count
		};
	},

	ssc_star5_group60 : function(s) {
		var lines = this.toList(s);
		var count = 0;
		if (lines.length == 2) {
			var line1 = lines[0];
			var line2 = lines[1];

			for ( var n in line1) {
				var nowCount = line2.length;

				var num = line1[n];
				if (this.contains(line2, num)) {
					nowCount -= 1;
				}
				count += this.combin(nowCount, 3);
			}
		}

		var txt = this.format(s, ",", "");
		return {
			content : txt,
			count : count
		};
	},

	ssc_star5_group120 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 5);

		return {
			content : s,
			count : count
		};
	},
	// 趣味
	ssc_star5_other1 : function(s) {
		var lines = this.toList(s);
		var count = lines.length;
		return {
			content : s,
			count : count
		};
	},
	ssc_star5_other2 : function(s) {
		return this.ssc_star5_other1(s);
	},
	ssc_star5_other3 : function(s) {
		return this.ssc_star5_other1(s);
	},
	ssc_star5_other4 : function(s) {
		return this.ssc_star5_other1(s);
	},

	// 不定位
	ssc_star5_none1 : function(s) {
		var lines = this.toList(s);
		var count = lines.length;
		return {
			content : s,
			count : count
		};
	},
	ssc_star5_none2 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 2);
		return {
			content : s,
			count : count
		};
	},
	ssc_star5_none3 : function(s) {
		var lines = this.toList(s);
		var count = this.combin(lines.length, 3);
		return {
			content : s,
			count : count
		};
	},
	
	ssc_dxds:function(s){
		var lines = this.toList(s);
		var rel = 0;
		for ( var n in lines) {
			rel += lines[n].length;
		}
		var txt = this.format(s, ",", "+");
		return {
			content : txt,
			count : rel
		};
	},
	
	ssc_side_tema : function(s) {
		var lines = this.toList(s);
		var count = lines.length;
		return {
			content : s,
			count : count
		};
	},
	
	//定位胆随机数
	ssc_star1_dwd_random : function() {
		var num = ['-','-','-','-','-'];
		var x = this.random(0,4);  //位置随机
		var y = this.random(0,9);  //号码随机
		num[x] = y;
		var txt = num.join(",");
		return { content:txt, count:1 }
	},

	
	ssc_star2_front_random : function() {
		var num = ['-','-','-','-','-'];
		num[0] = this.random(0,9);  //第一位随机
		num[1] = this.random(0,9);  //第二位随机
		var txt = num.join(",");
		return { content:txt, count:1 }
	},

	
	ssc_star2_last_random : function() {
		var num = ['-','-','-','-','-'];
		num[3] = this.random(0,9);  //第一位随机
		num[4] = this.random(0,9);  //第二位随机
		var txt = num.join(",");
		return { content:txt, count:1 }
	},

	
	ssc_star2_front_and_random : function() {
		var allCount = [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2,1 ];
		var n = this.random(0,18);  //第一个数
		var count = allCount[n];
		return { content:n, count:count }
	},

	
	ssc_star2_last_and_random : function() {
		return this.ssc_star2_front_and_random();
	},

	
	ssc_star2_front_kd_random : function() {
		var allCount = [ 10, 18, 16, 14, 12, 10, 8, 6, 4, 2 ];
		var n = this.random(0,9);  //第一个数
		var count = allCount[n];
		return { content:n, count:count }
	},

	
	ssc_star2_last_kd_random : function() {
		return this.ssc_star2_front_kd_random();
	},
	
	ssc_star2_front_group_random:function(){
		var n1 = this.random(0,9);
		var n2 = this.random(0,9);
		while(n1==n2){
			n2 = this.random(0,9);
		}
		var txt = n1+","+n2;
		return {content:txt,count:1};
	},

	ssc_star2_last_group_random:function(){
		return this.ssc_star2_front_group_random();
	},

	ssc_star2_front_group_and_random:function(){
		var allCount = [ 1, 1, 2, 2, 3, 3, 4, 4, 5, 4, 4, 3, 3, 2, 2, 1, 1 ];
		var n = this.random(1,17);
		var count = allCount[n-1];
		return {content:n,count:count};
	},
	
	ssc_star2_last_group_and_random : function() {
		return this.ssc_star2_front_group_and_random();
	},
	
	// 时时彩 前二组选包胆
	ssc_star2_front_group_contains_random : function() {
		var n = this.random(0,9);
		return {content:n,count:9};
	},
	
	// 时时彩 前二组选包胆随机
	ssc_star2_last_group_contains_random : function() {
		return this.ssc_star2_front_group_contains_random();
	},
	
	ssc_star3_front_random : function() {
		var num = ['-','-','-','-','-'];
		num[0] = this.random(0,9);  //第一位随机
		num[1] = this.random(0,9);  //第二位随机
		num[2] = this.random(0,9);  //第二位随机
		var txt = num.join(",");
		return { content:txt, count:1 }
	},
	
	ssc_star3_mid_random : function() {
		var num = ['-','-','-','-','-'];
		num[1] = this.random(0,9);  //第一位随机
		num[2] = this.random(0,9);  //第二位随机
		num[3] = this.random(0,9);  //第二位随机
		var txt = num.join(",");
		return { content:txt, count:1 }
	},
	
	ssc_star3_last_random : function() {
		var num = ['-','-','-','-','-'];
		num[2] = this.random(0,9);  //第一位随机
		num[3] = this.random(0,9);  //第二位随机
		num[4] = this.random(0,9);  //第二位随机
		var txt = num.join(",");
		return { content:txt, count:1 }
	},
	
	ssc_star3_front_and_random : function() {
		var allCount = [ 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69, 73, 75,75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 ];
		var n = this.random(0,27);  //第一位随机
		var count = allCount[n];
		return { content:n, count:count}
	},
	
	ssc_star3_mid_and_random : function() {
		return this.ssc_star3_front_and_random();
	},
	
	ssc_star3_last_and_random : function() {
		return this.ssc_star3_front_and_random();
	},
	
	ssc_star3_front_kd_random : function() {
		var allCount = [ 10, 54, 96, 126, 144, 150, 144, 126, 96, 54 ];
		var n = this.random(0,9);
		var c = allCount[n];
		return { content : n, count : c };
	},
	
	ssc_star3_mid_kd_random : function() {
		return this.ssc_star3_front_kd_random();
	},
	
	ssc_star3_last_kd_random : function() {
		return this.ssc_star3_front_kd_random();
	},
	
	ssc_star3_front_group3_random:function(){
		var n1 = this.random(0,9);
		var n2 = this.random(0,9);
		while(n1==n2){
			n2 = this.random(0,9);
		}
		var txt = n1+","+n2;
		return {content:txt,count:2};
	},
	
	ssc_star3_mid_group3_random:function(){
		return this.ssc_star3_front_group3_random();
	},
	
	ssc_star3_last_group3_random:function(){
		return this.ssc_star3_front_group3_random();
	},
	
	ssc_star3_front_group6_random:function(){
		var n;
		var hash = {};
		var nums = [];
		nums.push( this.random(0,9));
		hash[n] = true;
		do{
			n = this.random(0,9);
			if(!hash[n]){
				nums.push(n);
				hash[n] = true;
			}
		}while(nums.length!=3);
		
		var txt = nums.join(",");
		return {content:txt,count:1};
	},
	
	ssc_star3_mid_group6_random : function(){
		return this.ssc_star3_front_group6_random();
	},
	
	ssc_star3_last_group6_random : function(){
		return this.ssc_star3_front_group6_random();
	},
	
	ssc_star3_front_group_and_random : function() {
		var allCount = [ 1, 2, 2, 4, 5, 6, 8, 10, 11, 13, 14, 14, 15, 15, 14,
				14, 13, 11, 10, 8, 6, 5, 4, 2, 2, 1 ];
		var s = this.random(1,26);
		var count = allCount[s-1];
		return {
			content : s,
			count : count
		};
	},
	
	ssc_star3_mid_group_and_random:function(){
		return this.ssc_star3_front_group_and_random();
	},
	
	ssc_star3_last_group_and_random:function(){
		return this.ssc_star3_front_group_and_random();
	},
	
	ssc_star3_front_group_contains_random : function() {
		var s = this.random(0,9);
		var count = 54;
		return {
			content : s,
			count : count
		};
	},

	ssc_star3_mid_group_contains_random:function(){
		return this.ssc_star3_front_group_contains_random();
	},

	ssc_star3_last_group_contains_random:function(){
		return this.ssc_star3_front_group_contains_random();
	},
	
	ssc_star3_front_none1_random:function(){
		var n = this.random(0,9);
		return {content:n,count:1};
	},
	
	ssc_star3_mid_none1_random:function(){
		return this.ssc_star3_front_none1_random();
	},
	
	ssc_star3_last_none1_random:function(){
		return this.ssc_star3_front_none1_random();
	},
	
	ssc_star3_front_none2_random:function(){
		var n1,n2;
		n1 = this.random(0,9);
		n2 = this.random(0,9);
		while(n1 == n2){
			n2 = this.random(0,9);
		}
		var n = n1+","+n2;
		return {content:n,count:1};
	
		return this.ssc_star3_front_none1_random();
	},
	
	ssc_star3_mid_none2_random:function(){
		return this.ssc_star3_front_none2_random();
	},
	
	ssc_star3_last_none2_random:function(){
		return this.ssc_star3_front_none2_random();
	},
	
	ssc_star4_front_random:function(){
		var num = ['-','-','-','-','-'];
		num[0] = this.random(0,9);  
		num[1] = this.random(0,9);
		num[2] = this.random(0,9);
		num[3] = this.random(0,9);
		var txt = num.join(",");
		return { content:txt, count:1 }
	},
	
	ssc_star4_last_random:function(){
		var nums = [];
		for(var i=0;i<4;i++){
			nums.push(this.random(0,9));
		}
		var n = "-,"+ nums.join(",");
		return {content:n,count:1};
	},
	
	ssc_star4_front_group4_random:function(){
		var n1 = this.random(0,9);
		var n2 = this.random(0,9);
		while(n1 == n2){
			n2 = this.random(0,9);
		}
		var n = n1+","+n2;
		return {content:n,count:1};
	},
	
	ssc_star4_last_group4_random:function(){
		return this.ssc_star4_front_group4_random();
	},
	
	ssc_star4_front_group6_random:function(){
		var n1 = this.random(0,9);
		var n2 = this.random(0,9);
		while(n1 == n2){
			n2 = this.random(0,9);
		}
		var n = n1+","+n2;
		return {content:n,count:1};
	},
	
	ssc_star4_last_group6_random:function(){
		return this.ssc_star4_front_group6_random();
	},
	
	ssc_star5_random:function(){
		var num = ['-','-','-','-','-'];
		num[0] = this.random(0,9);
		num[1] = this.random(0,9);
		num[2] = this.random(0,9);
		num[3] = this.random(0,9);
		num[4] = this.random(0,9);  
		var txt = num.join(",");
		return { content:txt, count:1 }
	}
	
};

lottery = jQuery.extend(BaseLottery,lottery);
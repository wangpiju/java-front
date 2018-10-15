<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.sjc168.com/fn" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>首页</title>
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-lottery.css?ver=${VIEW_VERSION}'/>"/>
    <link rel="stylesheet" href="<c:url value='/res/home/css/${theme}.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-lottery-pk10.css?ver=${VIEW_VERSION}'/>"/>
	<script type="text/javascript" src="<c:url value='/res/home/js/math.extends.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/lotts/play.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/lotts/play_${lottBase.groupId}.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/lotts/fire.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/lotts/lottery.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/ajaxfileupload.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript">
		var hs = hs || {};
		hs.lottery = function() {};
		hs.lottery.betInOpen = ${lott.betInStatus == 0 && user.betInStatus == 0 && betInConfig.status == 0};
		hs.lottery.betInCount = ${betInConfig.gameCountMax };
		hs.lottery.betInAmountMix = ${betInConfig.amountMin };
		hs.lottery.betInAmountMax = ${betInConfig.amountMax };
		hs.lottery.gameSecondMax =  ${betInConfig.gameSecondMax };
		hs.lottery.pk10 = ${lott.groupName=='北京赛车'?'true':'false'};
		var account ="${user.account}";
	</script>
	<style>
		/*body {*/
			/*background: url('/res/home/images/lott-pk10/pk10-bg.jpg') center no-repeat;*/
        	/*background-size: 100% 100%;*/
		/*}*/
	</style>
</head>
<body>
	<div class="main" id="userRatio" data-ratio="${user.rebateRatio}">
		<input type="hidden" id="theme" value="${theme}" data-lottery="${fn:getSelf(lott.id)}"/>

        <!--彩种头部-->
        <div class="lotteryHeader">
            <div class="mainWidth">
                <!--彩种Logo-->
                <div class="lotteryLogo">
                    <img src="<c:url value='/res/home/images/lottTypes/cz_${lott.id}-${theme}.png'/>" class="betsType" />
                    <span class="betsType">${lott.title }</span>
                    <!--<img src="<c:url value='/res/home/images/lotteryLogo-ssc.png'/>" class="betsType" />
                    <img src="<c:url value='/res/home/images/lotteryLogo-115.png'/>" class="betsType" style="display: none;" />-->
                    <!--<p class="currLottNumBox">-->
                        <!--<span>当前已开</span>-->
                        <!--<span class="openCount" id="openCount">${seasonCount.openCount }</span>-->
                        <!--<span>/</span>-->
                        <!--<span id="allCount">${seasonCount.allCount }</span>-->
                        <!--<span>期</span>-->
                    <!--</p>-->
                </div>

				<!--开奖倒计时-->
				<div class="countDownBox">
					<p class="countDownTitle">
						<span>第</span> <span class="fontColorTheme" id="saleSeasonId">${current.seasonId }</span> <span>期 剩余投注时间</span>
					</p>
					<div class="countDown" id="saleAllSecond" data-second="${allSecond }">
						<label class="hour"> <a href="javascript:;" id="saleHour">${hour}</a> <span>:</span></label>
						<label class="minute"> <a href="javascript:;" id="saleMintue">${minute}</a> <span>:</span></label>
						<label class="second"> <a href="javascript:;" id="saleSecond">${second }</a></label>
					</div>
				</div>

                <!--开奖号码-->
                <div class="lotteryNumArea">
                    <p id="maxPlan" data-plan="${lott.maxPlan}">
                        <span>第</span> <span class="fontColorTheme" id="openSeasonId">${lastOpen.seasonId}</span> <span>期 开奖结果</span>
                    </p>
                    <!--<a href="trend" class="lotteryTrend" target="_blank">开奖走势</a>-->
                    <div class="lotteryNumBox">
                        <div class="lotteryNumList">
                            <!---新开奖号码动画-->
                            <ul class="lotteryNum ${lott.groupId}" id="openNums" data-num="${lottBase.numList}">
                                <c:forEach items="${lastOpen.nums}" var="a">
                                    <li class="lottNumDetail">
                                        <c:forEach items="${lottBase.numList}" var="b">
                                            <p class="<c:if test='${a==b}'>active</c:if>">
                                                <span class="bigNum${b}"></span>
                                            </p>
                                        </c:forEach>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <!--<p class="lottTypeTips overflowEllipsis" id="openNumStatus">-->
						<!--<c:forEach items="${numStatus}" var="a">-->
                            <!--<span class="lottTypeInfo">${a}</span>-->
                        <!--</c:forEach>-->
                    <!--</p>-->
                </div>
				<!-- 玩法介绍、开奖走势图 -->
                <%--<div class="explainExample">--%>
                    <%--<a href="javascript:;">--%>
                        <%--<i class="explainLogo"></i>--%>
                        <%--<span>玩法介绍</span>--%>
                    <%--</a>--%>
                    <%--<div class="tips" id="playRemark"></div>--%>
                    <%--<a href="trend" target="_blank">--%>
                        <%--<i class="trendLogo"></i>--%>
                        <%--<span>开奖走势图</span>--%>
                    <%--</a>--%>
                <%--</div>--%>
            </div>
        </div>

        <!--彩种内容-->
        <div class="lotteryContent">
            <!--彩种选号区域-->
            <div class="mainWidth">
				<!--彩种导航-->
				<div class="lottNavBox">
					<div class="lottNavList">
						<span class="scollLeft"><i class="el-icon-arrow-left"></i></span>
						<div class="lottNavListScoll">
							<ul class="lottNavListScollBox" style="left: 0px;">
								<c:forEach items="${lotts}" var="tolott" varStatus="lottsid">
									<c:if test="${tolott.groupId== lott.groupId }">
										<li class="lottNavListLi" id="${tolott.id}">${tolott.title}</li>
									</c:if>
								</c:forEach>
							</ul>
						</div>
						<span class="scollRight"><i class="el-icon-arrow-right"></i></span>
					</div>
					<ul class="lottNav">
						<c:forEach items="${lottBase.qun }" var="qun" varStatus="st">
							<c:if test="${qun.playerCount>0 }">
								<li class="lottNavDetail" data-show="#group_${st.index }" data-hide=".lottTypeBox">
									<a href="javascript:;">${qun.title }</a>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
                <div class="lottChangeNumArea clr">
                    <div class="lottLeft">
                        <!-- 彩种组 -->
                        <c:forEach items="${lottBase.qun }" var="qun" varStatus="st">
                        	<c:if test="${qun.playerCount>0 }">
                            <div class="lottTypeBox" id="group_${st.index}" style="display: none;">
                                <c:forEach items="${qun.groups}" var="group">
                                	<c:if test="${group.playerCount>0 }">
                                    <div class="lottTypeList">
                                        <span class="lottTypeTitle">${group.title}</span>
                                        <ul class="lottType">
                                            <c:forEach items="${group.players }" var="player">
                                                <c:set var="p" value="${lottBase.playerBonusMap[player.id] }"/>
                                                <c:if test="${p!=null && p.saleStatus==0 }">
                                                <li class="lottTypeDetail" data-hide=".changeNumList" data-show="#${p.id }" data-remark="${p.remark}"
                                                    data-example="${p.example}" data-title="${player.fullTitle}" data-ratio="${ (bonusGroup.rebateRatio - p.rebateRatio) }" data-MaxBonus="${fn:getMaxBonus(player.bonusStr,p.bonusRatio,user.rebateRatio,bonusGroup.rebateRatio,p.rebateRatio)}" data-bonus="${fn:getMinBonus(player.bonusStr,p.bonusRatio)}"><a href="javascript:;">${p.title }</a></li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                            </c:if>
                        </c:forEach>

                        <!--选号区域-->
                        <div class="lottChangeNumMain">
                            <div class="lottChangeNumBox">
                                <!--选号头部-->
								<div class="explainExample">
									<i class="explainLogo"></i>
									<span class="tips" id="playRemark"></span>
									<label class="singleMaxBonus">
										<span>单注最高奖金</span>
										<span id="playBonus"></span>
										<span>元</span>
									</label>
								</div>
								<%--<div class="explainExample">--%>
                                    <%--<a href="javascript:;">--%>
                                        <%--<i class="explainLogo"></i>--%>
                                        <%--<span>玩法介绍</span>--%>
                                    <%--</a>--%>
                                    <%--<div class="tips" id="playRemark"></div>--%>
									<%--<a href="javascript:;" class="changeNumExample">--%>
										<%--<i class="exampleLogo"></i>--%>
										<%--<span>选号示例</span>--%>
									<%--</a>--%>
									<%--<div class="tips" id="playExample"></div>--%>
									<%--<label class="singleMaxBonus">--%>
										<%--<span>单注最高奖金</span>--%>
										<%--<span id="playBonus"></span>--%>
										<%--<span>元</span>--%>
									<%--</label>--%>
								<%--</div>--%>
                                <!--选号列表-->
                                <c:forEach items="${lottBase.players }" var="player">
                                    <!-- 号码盘 -->
                                    <div class="changeNumList changeNumList-${lott.id}" style="display: none;" id="${player.id }" data-anySelect="${player.anySelect }">
                                        <c:if test="${player.anyList!=null }">
                                            <c:forEach items="${player.anyList }" var="a" varStatus="stat">
                                                <c:choose>
                                                    <c:when test="${player.anySelect>stat.index  }">
                                                        <input class="anySelect" type="checkbox" checked="checked" value="${a }"/>${a }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input class="anySelect" type="checkbox" value="${a }"/>${a }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </c:if>
                                        <c:choose>
                                            <c:when test="${player.numView!=null }">
                                                <c:forEach items="${player.numView }" var="line">
                                                    <!-- 行 -->
                                                    <div class="changeNumDetail${line.towLine? ' changeNumDouble':'' } clr">
                                                        <div class="changeNum clr">
                                                            <span class="detailTitle">${line.title }</span>
                                                            <ul class="numList" data-max="${line.maxSelect }">
                                                                <c:forEach items="${line.nums }" var="n" varStatus="st">
                                                                    <li>
                                                                        <a href="javascript:;" data-num="${n}" class="smallNum smallNum${fn:getTitle(n)}"></a>
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>
                                                        <div class="changeNumBtn">
                                                            <ul class="btnList">
                                                                <c:if test="${line.hasBut }">
                                                                    <li><a href="javascript:;" class="numBtn numBtnAll">全</a></li>
                                                                    <li><a href="javascript:;" class="numBtn numBtnBig">大</a></li>
                                                                    <li><a href="javascript:;" class="numBtn numBtnSmall">小</a></li>
                                                                    <li><a href="javascript:;" class="numBtn numBtnOdd">单</a></li>
                                                                    <li><a href="javascript:;" class="numBtn numBtnEven">双</a></li>
                                                                    <li><a href="javascript:;" class="numBtn numBtnClear">清</a></li>
                                                                </c:if>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- 单式 -->
                                                <div class="changeNumSingle">
                                                    <div class="importBetList">
                                                        <a href="javascript:;" class="btn uploadFile">导入注单</a>
                                                    </div>
                                                    <div class="singleNumBox">
                                                        <textarea class="singleNum"></textarea>
                                                        <div class="singleMsg">
                                                            <p>说明：</p>
                                                            <p>1.号码无需分割。</p>
                                                            <p>2.每一注号码之间的间隔符支持回车 空格[ ] 逗号[ , ] 分号[ ; ]</p>
                                                            <p>3.文件格式必须是.txt格式</p>
                                                            <p>4. 文件较大时会导致上传时间较长，请耐心等待！</p>
                                                            <p>5.导入文本内容后将覆盖文本框中现有的内容。</p>
                                                        </div>
                                                    </div>
                                                    <div class="singleBtns">
                                                        <a href="javascript:;" class="btn errBtn" data-playId="#${player.id }">删除错误项</a>
                                                        <a href="javascript:;" class="btn cfBtn" data-playId="#${player.id }">删除重复项</a>
                                                        <a href="javascript:;" class="btn clearBtn" data-playId="#${player.id }">清空文本框</a>
                                                        <span class="msg" style="display: none;">您导入的号码有错误项和重复项，已为您标记，试试功能键由系统修正错误</span>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                                <!-- 文件上传 -->
                                <div id="fileDiv" style="display:none;">
                                    <input type="file" name="file" id="file"/>
                                </div>
                            </div>

                            <!--确认投注-->
                            <div class="confirmBetsBox modifyBets">
								<div class="betsInfoBox">
									<div class="betsInfo" style="display: none">
										<div class="amountTypeDetail">
											<a href="javascript:;" class="amountType active" data-unit="1">元</a>
											<%--<a href="javascript:;" class="amountType" data-unit="0.1">角</a>--%>
											<%--<a href="javascript:;" class="amountType" data-unit="0.01">分</a>--%>
											<%--<a href="javascript:;" class="amountType" data-unit="0.001">元</a>--%>
										</div>
										<div class="changeMultDetail">
											<span>投注金额</span>
											<!-- 可输入下拉框 -->
											<div class="inputSelectDiv">
												<a href="javascript:;" class="reduceMult" onclick="reduceMult(this)"></a>
												<input type="text" class="labelCond" id="betPrice1" value="1"/>
												<a href="javascript:;" class="addMult" onclick="addMult(this)"></a>
											</div>
										</div>
									</div>
								</div>
                                <div class="betsBtn">
									<div class="changeMultDetail">
										<span>投注金额</span>
										<!-- 可输入下拉框 -->
										<div class="inputSelectDiv">
											<a href="javascript:;" class="reduceMult" onclick="reduceMult(this)"></a>
											<input type="text" class="labelCond" id="betPrice" value="1"
												   onkeyup="inputNumber(this)"/>
											<a href="javascript:;" class="addMult" onclick="addMult(this)"></a>
										</div>
									</div>
                                    <%--<div class="betsBtnLabel">--%>
                                        <a href="javascript:;" class="btn appendNumBtn" id="addSelectNum">
                                            <i class="appNumIcon"></i>
                                            <span>添加号码栏</span>
                                        </a>
                                    <%--</div>--%>
                                    <a href="javascript:;" class="btn confirmBets" id="promptlyBet">
                                        <i class="confirmBetsIcon"></i>
                                        <span>立即投注</span>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 当前期开奖排行 -->
                    <div class="lottRight">
                        <div class="accountAndLottery">
                            <!--当前期开奖排行-->
                            <div class="beforeLottNumBox">
                                <table class="beforeLottNumList" >
                                    <c:forEach items="${lastOpen.nums}" var="c" varStatus="status">
										<tr class="beforeLottNumDetail1">
											<td>第${status.index + 1}名</td>
											<!-- class="currLottNum${n}" ${n}为开奖号码 -->
											<td><span class="currLottNum${c}"></span></td>
										</tr>
                                    </c:forEach>
                                </table>
                            </div>

                            <!--查看完整走势-->
                            <!--<p class="fullTrendBox">-->
                                <!--<a href="trend" class="fullTrend textUnderline" target="_blank">查看完整走势</a>-->
                            <!--</p>-->
                        </div>
                    </div>
                </div>
                <!-- 追号区域、最近投注-->
                <div class="appNumLottListArea clr">
                    <!--追号模块-->
                    <div class="appNumArea">
                        <!--彩种追号信息-->
                        <div class="appNumBox">
                            <table class="appNumList">
                                <thead>
                                <tr class="appNumHeader">
                                    <th width="120">玩法</th>
                                    <th>号码</th>
                                    <th width="40">模式</th>
                                    <th width="60">注</th>
                                    <th width="60">倍</th>
                                    <th width="100">金额</th>
                                    <th width="40" class="appNumClear fontColorTheme">清空</th>
                                </tr>
                                </thead>
                                <tbody id="selectBetPanle"></tbody>
                            </table>
                        </div>
                        <!--确认追号-->
                        <div class="confirmAppNumBox modifyBets">
                            <div class="appNumInfo">
                                <label>
                                    <span>总注数：</span> <span id="appNumCount" class="fontColorTheme">0</span> <span>注</span>
                                </label>
                                <label>
                                    <span>总金额：￥</span> <span id="appNumAmount" class="fontColorTheme">0.00</span> <span>元</span>
                                </label>
                                <label class="currAmountDetail">
                                    <span>余额：</span> <span class="userBalance fontColorTheme" id="appNumUserBalance">${user.amount}</span> <span>元</span>
                                </label>
                            </div>
                            <div class="betsBtn">
								<%--<a href="javascript:;" class="btn appendNumBtn" id="appBet">追号</a>--%>
                                <%--<a href="javascript:;" class="btn confirmBets" id="betConfirm">确认投注</a>--%>
								<a href="javascript:;" class="btn confirmBets" id="betConfirm">
									<i class="confirmBetsIcon"></i>
									<span>确认投注</span>
								</a>
                            </div>
                        </div>
                    </div>
                    <!-- 最近投注、最近追号 -->
                    <div class="userLotteryListArea">
                        <!--中奖排行-->
                        <!--<div class="beforeLottNumBox">-->
                            <!--<h3 class="beforeLottNumHeader lotteryList">-->
                                <!--<i class="lotteryListIcon"></i>-->
                                <!--<span>中奖排行</span>-->
                            <!--</h3>-->
                            <!--<table class="beforeLottNumList" id="lotteryList">-->
                                <!--<c:forEach items="${winList }" var="a" varStatus="stat">-->
                                    <!--<tr class="beforeLottNumDetail">-->
                                        <!--<td>${stat.index+1 }</td>-->
                                        <!--<td width="20%" class="lotteryListImg">-->
                                            <!--<img src="<c:url value='/res/home/images/lottery/betList/${a.lotteryName}.png'/>"/>-->
                                        <!--</td>-->
                                        <!--<td width="30%">${a.account }</td>-->
                                        <!--<td width="50%">-->
                                            <!--<span class="fontColorRed">${a.winAmount }</span>-->
                                            <!--<span>元</span>-->
                                        <!--</td>-->
                                    <!--</tr>-->
                                <!--</c:forEach>-->
                            <!--</table>-->
                        <!--</div>-->
                        <!-- 开奖列表、最近投注、最近追号等 -->
                        <div class="beforeLottNumBox accountLottBox">
							<div class="zzzzz">
								<h3 class="beforeLottNumHeader clr">
									<a href="javascript:;" class="lottInfoNavDetail active" data-show="#beforeLottNumList">今日开奖</a>
									<a href="javascript:;" class="lottInfoNavDetail" data-show="#betRecord">最近投注</a>
									<%--<a href="javascript:;" class="lottInfoNavDetail" data-show="#traceRecord">最近追号</a>--%>
								</h3>
								<div class="explainExample">
									<a href="trend" target="_blank">
										<i class="trendLogo"></i>
										<span>开奖走势图</span>
									</a>
								</div>
							</div>

                            <div class="beforeLottNumContent">
                                <input type="hidden" id="lotteryId" value="${lott.id}" />
                                <table class="beforeLottNumList" id="beforeLottNumList">
                                    <thead>
                                    <tr class="listHeader">
                                        <th>奖期</th>
                                        <th>开奖号码</th>
                                    </tr>
                                    </thead>
                                    <tbody id="openList">
                                    <c:forEach items="${openList}" var="a" begin="0" end="4">
                                        <tr class="beforeLottNumDetail">
												<%--奖期--%>
                                            <td class="smallFont12">${a.seasonId }</td>
												<%--开奖号码--%>
                                            <c:choose>
                                                <c:when test="${lott.groupName == '快3'}">
                                                    <td class="fontColorTheme beforeLottNum k3">
                                                        <c:forEach items="${a.nums }" var="n">
                                                            <span class="k3-${n}"></span>
                                                        </c:forEach>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="fontColorTheme beforeLottNum">
                                                        <c:forEach items="${a.nums }" var="n">
                                                            <span>${n}</span>
                                                        </c:forEach>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <table class="beforeLottNumList" id="betRecord" style="display:none;">
                                    <thead>
                                    <tr class="listHeader">
                                        <th>彩种</th>
                                        <%--<th>玩法</th>--%>
                                        <th>投注金额</th>
                                        <th>奖金</th>
                                    </tr>
                                    </thead>
                                    <tbody id="betTable">
                                    <c:forEach items="${bets }" var="bet">
                                        <tr class="beforeLottNumDetail">
                                            <td class="betListLottName overflowEllipsis">${bet.seasonId}</td>
                                            <%--<td class="betListPlayName overflowEllipsis">${bet.playName}</td>--%>
                                            <td>￥<fmt:formatNumber maxFractionDigits="4" groupingUsed="false" value="${bet.amount}" /></td>
                                            <td>
												<c:if test="${bet.status == 1 }"><fmt:formatNumber maxFractionDigits="4" groupingUsed="false" value="${bet.win}" /></c:if>
											</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <table class="beforeLottNumList" id="traceRecord" style="display:none;">
                                    <thead>
                                    <tr class="listHeader">
                                        <!--<th width="140">时间</th>-->
                                        <th width="100">彩种</th>
                                        <th width="120">开始期号</th>
                                        <!-- 										<th width="120">玩法</th> -->
                                        <!--<th width="120">取消数</th>-->
                                        <!--<th width="100">方案金额</th>-->
                                        <!--<th width="60">追中即停</th>-->
                                        <th width="100">总金额</th>
                                        <!--<th width="60">状态</th>-->
                                        <!-- <th width="40">操作</th> -->
                                    </tr>
                                    </thead>
                                    <tbody id="traceTable">
                                    <c:forEach items="${traces }" var="trace">
                                        <tr class="beforeLottNumDetail">
                                            <!--<td><fmt:formatDate value='${trace.createTime}' pattern="yyyy-MM-dd HH:mm:ss" /></td>-->
                                            <td>${trace.lotteryName}</td>
                                            <td>${fn:getShortSeasonId(trace.startSeason) }</td>
                                            <%-- 											<td>${trace.palyName}</td> --%>
                                            <!--<td>${trace.cancelTraceNum }</td>-->
                                            <!--<td>￥<fmt:formatNumber groupingUsed="false" value="${trace.traceAmount}" />元</td>-->
                                            <!--<td>${trace.isWinStop==1?'是':'否' }</td>-->
                                            <td class="fontColorTheme"><fmt:formatNumber groupingUsed="false" value="${trace.traceAmount}" /></td>
                                            <!--<td>${trace.status==0?'进行中':'已完成' }</td>-->
                                            <%--<td><c:if test="${trace.status ==0 }"> --%>
                                            <%--<a class="cancelOrder" href="cancelOrder?ids=${trace.id}"></a> --%>
                                            <%--</c:if></td> --%>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

	</div>

	<div class="dialogBoxContent" id="msgBox" style="display: none; width:400px;">
		<p class="dialogTitle">
			<span>温馨提示</span> <a href="javascript:;" class="closeDialog"></a>
		</p>
		<div class="tableBox">
			<div class="msg" id="msgContent"></div>
			<div class="dialogBtn">
				<a href="javascript:;" class="btn closeBtn">确认</a>
			</div>
		</div>
	</div>
	<div class="layui-layer-content" id="traceBox" style="display: none;">
        <p class="lottTipLayerTitle">追号设置</p>
		<div class="layerBox">
			<ul class="appNumNavList">
				<li><a href="javascript:;" class="appNumNav active" data-show="#trace1" data-hide=".appendBox">同倍追号</a></li>
				<li><a href="javascript:;" class="appNumNav" data-show="#trace2" data-hide=".appendBox">利润率追号</a></li>
				<li><a href="javascript:;" class="appNumNav" data-show="#trace3" data-hide=".appendBox">翻倍追号</a></li>
			</ul>
			<div class="sameMultAppNumBox">
				<!-- 同倍追号 -->
				<div class="appendBox" id="trace1" >
					<div class="createAppNumBar">
						<span>连续追：</span>
						<ul class="appendIssue">
							<li><a href="javascript:;" data-num="5" class="active">5期</a></li>
							<li><a href="javascript:;" data-num="10">10期</a></li>
							<li><a href="javascript:;" data-num="15">15期</a></li>
							<li><a href="javascript:;" data-num="20">20期</a></li>
						</ul>
						<label for="#">
							<span>已选：</span>
							<input type="text" class="labelCond selectedIssue" value="5" id="traceNum1" />
							<span>期</span>
						</label>
						<label for="#">
							<span>倍数：</span>
							<!-- 可输入下拉框 -->
							<div class="inputSelectDiv">
                                <!--<a href="javascript:;" class="reduceMult" onclick="reduceMult(this)"></a>-->
								<input type="text" class="labelCond fontColorCyan" id="tracePrice1" value="1"/>
								<!--<a href="javascript:;" class="inputSelectArrow" onclick="inputSelectFrame(this)"></a>-->
								<!--<div class="inputOptions fontColorCyan">-->
									<!--<a href="javascript:;" onclick="inputSelectConfirm(this)">1</a>-->
									<!--<a href="javascript:;" onclick="inputSelectConfirm(this)">5</a>-->
									<!--<a href="javascript:;" onclick="inputSelectConfirm(this)">10</a>-->
									<!--<a href="javascript:;" onclick="inputSelectConfirm(this)">20</a>-->
								<!--</div>-->
                                <!--<a href="javascript:;" class="addMult" onclick="addMult(this)"></a>-->
							</div>
							<span>倍</span>
						</label>
						<a href="javascript:;" class="btn createAppNum" id="traceCreate1">生成追号计划</a>
					</div>
					<div class="cfmAppNumTableBox">
						<table class="cfmAppNumTable">
							<thead>
								<tr class="listHeader">
									<th>序号</th>
									<th style="text-align: left;"><input type="checkbox" class="checkAll" data-group="seasonId" checked /> <span>追号期次</span></th>
									<th>倍数</th>
									<th>金额</th>
									<th>预计开奖时间</th>
								</tr>
							</thead>
							<tbody class="traceList"></tbody>
						</table>
					</div>
				</div>


				<!-- 利润率追号-->
				<div id="trace2" class="appendBox" style="display: none;">
					<div class="createAppNumBar">
						<label for="#"> <span>最低收益率：</span> <input type="text" id="traceBate" class="labelCond selectedIssue" value="10" /> <span>%</span></label>
						<label for="#"> <span>追号期数：</span> <input type="text" class="labelCond selectedIssue" value="10" id="traceNum2" /> </label>
						<a href="javascript:;" class="btn createAppNum" id="traceCreate2">生成追号计划</a> <span class="fontColorRed appNumWarn">注意：利润率计算使用当前用户最大奖计算</span>
					</div>
					<div class="cfmAppNumTableBox">
						<table class="cfmAppNumTable">
							<thead>
								<tr class="listHeader">
									<th>序号</th>
									<th style="text-align: left;"><input type="checkbox" class="checkAll" data-group="seasonId" checked /> <span>追号期次</span></th>
									<th>倍数</th>
									<th>金额</th>
									<th>奖金</th>
									<th>预期盈利金额</th>
									<th>预期盈利率</th>
								</tr>
							</thead>
							<tbody class="traceList"></tbody>
						</table>
					</div>
				</div>

				<!-- 翻倍追号-->
				<div id="trace3" class="appendBox" style="display: none;">
					<div class="createAppNumBar">
						<label for="#"> <span>起始倍数：</span> <input type="text" class="labelCond selectedIssue" value="1" id="traceBeginMul3" /></label>
						<label for="#"> <span>隔</span> <input type="text" class="labelCond selectedIssue" value="1" id="traceMidSeason3" /> <span>期</span></label>
						<label for="#"> <span>倍X</span> <input type="text" class="labelCond selectedIssue" value="2" id="traceMul3" /></label>
						<label for="#"> <span>追号期数：</span> <input type="text" class="labelCond selectedIssue" value="10" id="traceNum3" /></label>
						<a href="javascript:;" class="btn createAppNum" id="traceCreate3">生成追号计划</a>
					</div>
					<div class="cfmAppNumTableBox">
						<table class="cfmAppNumTable">
							<thead>
								<tr class="listHeader">
									<th>序号</th>
									<th style="text-align: left;"><input type="checkbox" class="checkAll" data-group="seasonId" checked /> <span>追号期次</span></th>
									<th>倍数</th>
									<th>金额</th>
									<th>预计开奖时间</th>
								</tr>
							</thead>
							<tbody class="traceList"></tbody>
						</table>
					</div>
				</div>
				<div class="cfmAppNumInfo">
					<div class="cfmAppNumInfoDetail">
						<span id="traceAllNum">0</span> <span class="fontColorGray">期数</span>
					</div>
					<div class="cfmAppNumInfoDetail">
						<span id="traceAllCount">0</span> <span class="fontColorGray">注数</span>
					</div>
					<div class="cfmAppNumInfoDetail">
						<span class="fontColorRed" id="traceAllMoney">0.00</span> <span class="fontColorGray">追号方案总金额(元)</span>
					</div>
					<div class="cfmAppNumInfoDetail">
						<span class="userBalance">${user.amount }</span> <span class="fontColorGray">账户可用余额(元)</span>
					</div>
					<div class="cfmAppNumInfoDetail">
						<span class="seasonTime">00:00:00</span> <span class="fontColorGray">本期投注截止</span>
					</div>
				</div>
				<div class="cfmAppNumBtn dialogBtn">
					<button class="btn" id="traceSubmit">确认追号投注</button>
					<label>
                        <input type="checkbox" id="traceWinStop" value="1" checked />
                        <span>中奖后停止追号</span>
					</label>
				</div>
			</div>
		</div>
	</div>

	<!-- 投注确认 -->
	<div class="layui-layer-content" id="betConfirmBox" style="display: none; width: 460px;">
		<p class="lottTipLayerTitle">温馨提示</p>
		<div class="layerBox betConfirmBox">
			<form action="" id="betConfirmForm"></form>
			<p>
				<strong>请核准您的投注信息</strong>
			</p>
			<p>
				<label><span>彩种：${lott.title}</span></label> <label>期号：<span id="saleSeasonId1">${current.seasonId}</span></label>
			</p>
			<div class="betCfmDetail">
				<span class="betCfmDetailTitle">详情：</span>
				<div class="cfmBetsTableBox">
					<table class="cfmBetsTable" id="betConfirmTable"></table>
				</div>
			</div>
			<p>
				<label> <span>付款总金额：</span> <span class="fontColorRed fontBold" id="betConfirmAmount">0.0</span> <span>元</span>
				</label>
			</p>
			<p>
				<label> <span>付款账户：${user.account }</span>
				</label>
			</p>
			<div class="dialogBtn">
				<a href="javascript:;" class="btn closeBtn" id="betSubmit">确认</a>
			</div>
		</div>
	</div>

    <!-- 完善用户资料 -->
    <div class="dialogBoxContent userImproveInfoArea" style="display:none;">
        <div class="improveHeader">
            <div class="improveLogoBox">
                <i class="improveLogo"></i>
            </div>
            <div class="improveHeaderInfo">
                <p class="improveHeaderTitle">
                    <span>亲爱的</span>
                    <span>${user.account }</span>
                    <span>,感谢您选择大順平台，请完善以下个人信息</span>
                </p>
                <span class="improveHeaderTips">为了让大順更好的给您提供服务，请修改密码并牢记，不要泄露给其他人</span>
            </div>
            <a href="javascript:;" class="closeDialog"></a>
        </div>
        <div class="improveContent">
            <form action="" class="improveInfoForm">
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>昵称</span>
                    <input type="text" class="labelCond" placeholder="由字母或数字或汉字组成" />
                    <span class="labelMsg">最小4位字符</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>新的登录密码</span>
                    <input type="text" class="labelCond" placeholder="您当前为默认密码登录，存在隐患" />
                    <span class="labelMsg fontColorRed">格式不对，请检查重新设置</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>确认登录密码</span>
                    <input type="text" class="labelCond" placeholder="确认登录密码，需一致" />
                    <span class="labelMsg fontColorRed">与登录密码不一致</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle">QQ</span>
                    <input type="text" class="labelCond" placeholder="当您遗忘密码时方便找回密码" />
                    <span class="labelMsg">（非必填项）</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle">邮箱</span>
                    <input type="text" class="labelCond" placeholder="当您遗忘密码时方便找回密码" />
                    <span class="labelMsg fontColorRed">邮箱格式不对，请重新输入</span>
                </div>
                <div class="formBtns">
                    <span class="labelTitle"></span>
                    <input type="submit" class="btn" value="提交" />
                </div>
            </form>
        </div>
    </div>

    <!-- 完善安全密码、安全信息 -->
    <div class="dialogBoxContent setFundPwdSafeInfoArea" style="display:none;">
        <div class="improveHeader">
            <div class="improveLogoBox">
                <i class="improveLogo"></i>
            </div>
            <div class="improveHeaderInfo">
                <p class="improveHeaderTitle">
                    <span>尊敬的</span>
                    <span>lishao001</span>
                    <span>,请设置您的安全密码，为您账户的资金保驾护航</span>
                </p>
                <span class="improveHeaderTips">当涉及到“充值”、“提款”、“银行卡绑定”的操作时，需要安全密码验证。以确定是您本人进行操作。</span>
            </div>
            <a href="javascript:;" class="closeDialog"></a>
        </div>
        <div class="improveContent">
            <form action="" class="improveInfoForm">
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>安全密码</span>
                    <input type="text" class="labelCond" placeholder="不能与登录密码重复,最少8位字母或数字" />
                    <span class="labelMsg fontColorRed">格式不对，请检查重新设置</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>确认安全密码</span>
                    <input type="text" class="labelCond" placeholder="确认安全密码，需一致" />
                    <span class="labelMsg fontColorRed">格式不对，请检查重新设置</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>设置安全信息1</span>
                    <select class="labelCond">
                        <option value="0">请选择</option>
                    </select>
                    <span class="labelMsg">找回安全密码的重要凭证！</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>答案</span>
                    <input type="text" class="labelCond" />
                </div>
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>设置安全信息2</span>
                    <select class="labelCond">
                        <option value="0">请选择</option>
                    </select>
                    <span class="labelMsg">找回安全密码的重要凭证！</span>
                </div>
                <div class="labelDiv">
                    <span class="labelTitle"><strong class="fontColorRed">*</strong>答案</span>
                    <input type="text" class="labelCond" />
                </div>
                <div class="formBtns">
                    <span class="labelTitle"></span>
                    <input type="submit" class="btn" value="提交" />
                </div>
            </form>
        </div>
    </div>

	<!-- 彩中彩游戏，每日提醒一次 -->
	<!--<div class='dialogBoxContent awardsLottNotice' id='awardsLottNotice' style="display:none;">-->
		<!--<p class='awardsLottTitle'>-->
			<!--<a href='javascript:;' class='closeDialog'></a>-->
		<!--</p>-->
		<!--<div class="awardsNoticeContent">-->
			<!--<p>尊敬的用户：</p>-->
			<!--<p class="awardsNoticeInfo">您好，大順隆重推出“彩中彩”游戏，如果您有投注注单中奖了，将会触发“彩中彩”。彩中彩更加刺激更加激情，中奖概率随之上涨。</p>-->
			<!--<p>如果您喜欢彩中彩，请选择“开启”</p>-->
			<!--<p>如果您不喜欢彩中彩，请选择“关闭”</p>-->
			<!--<p class="friendTips">温馨提示：投注页面有关闭和开启“彩中彩”的开关哦</p>-->
		<!--</div>-->
		<!--<div class="awardsLottBtn">-->
			<!--<a href="javascript:;" class="btn playAwardsSwitch">激情开启</a>-->
			<!--<a href="javascript:;" class="btn stopAwardsSwitch">残忍拒绝</a>-->
			<!--<label>-->
				<!--<input type="checkbox" class="labelCond" />-->
				<!--<span>记住我的选择</span>-->
			<!--</label>-->
		<!--</div>-->
	<!--</div>-->

	<!-- 中奖弹窗 -->
    <div class="awardsMsgGroup" id="awardsMsgGroup">
    	<ul class="awardsLottListBox" id="awardsMsgGroupWin">
    	</ul>
    </div>

    <ul id="winExampleHtml" style="display: none">
		<li class="awardsLottDetail" id="winExample">
			<i class="closeAwardsDialogBtn" data-id="winExample"></i>
			<span>恭喜您中奖了！</span>
			<span id="winExample-amount"></span><span>元</span>
		</li>
	</ul>

	<div id="betExampleHtml">
		<!-- 彩中彩游戏窗口 -->
	    <div class='awardsLottDialog' id='betExample' style="display:none;">
			<embed src='/dd.mp3' autoplay='true' loop='1' style='display: none;'></embed>
			<p class='awardsLottTitle'>
				<i class="awardsLottLogo"></i>
				<label for="#" class="awardsCountDown">
					<span>0</span>
					<span>:</span>
					<span id="betExample-countDown"></span>
				</label>
				<label for="#" class="currAwardsAmount">
					<span>恭喜您中奖</span>
					<span id="betExample-amount"></span>
					<span>元</span>
				</label>
				<a href='javascript:;' class='closeAwardsLott' data-id='betExample'></a>
			</p>
			<div class='awardsLottContent' id="awardsLottGameArea-betExample">
				<div class="awardsGameArea">
					<p class="awardsGameTitle">选择我的好运金额</p>
					<div class="awardsGameContent">
						<div class="awardsGameTypeList">
							<div class="awardsGameType" id="awardsGameType-betExample-1">
								<label class="gameAmountDetail">
									<span class="gameAmount">1</span>
									<span>元</span>
								</label>
								<span class="awardsBtn">开始</span>
							</div>
							<div class="awardsGameType" id="awardsGameType-betExample-5">
								<label class="gameAmountDetail">
									<span class="gameAmount">5</span>
									<span>元</span>
								</label>
								<span class="awardsBtn">开始</span>
							</div>
							<div class="awardsGameType" id="awardsGameType-betExample-10">
								<label class="gameAmountDetail">
									<span class="gameAmount">10</span>
									<span>元</span>
								</label>
								<span class="awardsBtn">开始</span>
							</div>
							<div class="awardsGameType" id="awardsGameType-betExample-half">
								<label class="gameAmountDetail">
									<span>押一半</span>
								</label>
								<span class="awardsBtn">开始</span>
							</div>
							<div class="awardsGameType" id="awardsGameType-betExample-all">
								<label class="gameAmountDetail">
									<span>全部</span>
								</label>
								<span class="awardsBtn">开始</span>
							</div>
						</div>
						<div class="awardsGameMultBox">
							<span class="awardsGameAmount" id="betExample-gameAmount"></span>
							<span>元</span>
							<span>X</span>
							<div class="awardsGameMultDetail">
								<div class="awardsGameMult awardsGame-l">
									<span id="betExample-mult-l">9</span>
								</div>
								<div class="awardsGameMult awardsGame-c">
									<span id="betExample-mult-c">9</span>
								</div>
								<div class="awardsGameMult awardsGame-r">
									<span id="betExample-mult-r">9</span>
								</div>
							</div>
							<span>倍</span>
						</div>
					</div>
				</div>
				<div class="awardsGameArea">
					<p class="awardsGameTitle currGameTitle">本局结果</p>
					<div class="awardsGameContent">
						<div class="awardsGameResult">
							<label for="#">
								<span>本轮结果</span>
								<span class="awardsResultAmount" id="betExample-resultAmount"></span>
								<span>元</span>
							</label>
							<label for="#">
								<span>可挑战次数：</span>
								<span class="fontColorTheme" id="betExample-gameCount"></span>
							</label>
						</div>
					</div>
				</div>
				<div class="awardsGameArea">
					<div class="awardsLottResultDetail remainAmount">
						<span>剩余额度</span>
						<span class="currResultAmount" id="betExample-currResultAmount"></span><span>元</span>
					</div>
					<div class="awardsLottResultDetail" id="prizeList">
						<a href="/game/index?tabId=betInList" class="fontColorTheme" target="_blank">中奖记录</a>
					</div>
					<div class="awardsLottResultDetail" id="gameRuleBtn">
						<span class="fontColorTheme">游戏规则</span>
					</div>
				</div>
			</div>
			<div class='awardsLottContent awardsWarnDialog' id="awardsWarn" style="display:none;">
				<div class="awardsLottWarnBox">
					<span>温馨提示：</span>
					<div class="awardsLottWarnInfo">
						<p>您本轮的挑战次数/余额不足</p>
						<p>请投注大順彩种，中奖后将再次激活彩中彩</p>
					</div>
					<div class="awardsLottBtn">
						<a href="javascript:;" class="btn">确定</a>
						<span id="awardsWarnTime"></span><span>秒后自动关闭</span>
					</div>
				</div>
			</div>
			<div class="awardsGameRuleBox">
				<i class="awardsRuleBtn"></i>
				<div class="awardsGameRule" style="display:none;">
					<p>彩中彩游戏规则介绍：</p>
					<p>
投注彩票，中奖激活彩中彩游戏页面，可以选择“1元”“5元”“10元”“押一半”“全部”五种投注方式，中奖倍数从0.01倍-9.99倍不等。
举例：彩中彩投注10元，中奖倍数为9.99，则您获得10元*9.99倍=99.9元
					</p>
				</div>
			</div>
		</div>
		<!-- 彩中彩警告窗口 -->
		<!--<div class='awardsLottDialog awardsWarnDialog' id='awardsWarn' style="display:none;">-->
			<!--<p class='awardsLottTitle'>-->
				<!--<i class="awardsLottLogo"></i>-->
				<!--<a href='javascript:;' class='closeAwardsLott' data-id='awardsWarn'></a>-->
			<!--</p>-->
			<!--<div class='awardsLottContent'>-->
				<!--<div class="awardsLottWarnBox">-->
					<!--<span>温馨提示：</span>-->
					<!--<div class="awardsLottWarnInfo">-->
						<!--<p>您本轮的余额不足</p>-->
						<!--<p>请投注大順彩种，中奖后将再次激活彩中彩</p>-->
					<!--</div>-->
					<!--<div class="awardsLottBtn">-->
						<!--<a href="javascript:;" class="btn">确定</a>-->
						<!--<a href="javascript:;" class="btn cancel">取消</a>-->
						<!--<span>5秒后自动关闭</span>-->
					<!--</div>-->
				<!--</div>-->
			<!--</div>-->
		<!--</div>-->
		<!-- 中奖弹窗，点击参加彩中彩游戏 -->
		<!--<div class='awardsLottDialog awardsNoticeDialog' id='awardsNoticeDialog' style="display:none;">-->
			<!--<p class='awardsLottTitle'>-->
				<!--<a href='javascript:;' class='closeAwardsLott' data-id='awardsNoticeDialog'></a>-->
			<!--</p>-->
			<!--<p class="awardsNoticeTitle currAwardsNotice">-->
				<!--<span>运气爆棚，恭喜您中得：</span>-->
				<!--<span>100元</span>-->
			<!--</p>-->
			<!--<span class="playAwardsLottery"></span>-->
			<!--<p class="awardsLottTips">-->
				<!--<span>5秒后自动关闭</span>-->
				<!--<a href="javascript:;" class="btn afterAlert">下次再说</a>-->
			<!--</p>-->
		<!--</div>-->
	</div>

    <!-- 游戏记录详情弹框 -->
    <div id="generalDetailArea" style="display:none;">
        <div class="detailHeaderBox">
            <h3 class="detailHeaderTitle" id="delLotteryName"></h3>
            <div class="headerDetail">
                <p>
                    <span>期号：</span>
                    <span id="delSeasonId"></span>
                </p>
                <p>
                    <span>投注时间：</span>
                    <span id="delCreateTime"></span>
                </p>
                <p>
                    <span>投注总金额：</span>
                    <span id="delAmounts"></span>
                </p>
                <p>
                    <span>方案编号：</span>
                    <span id="number"></span>
                </p>

            </div>
            <div class="detailNumBox">
                <span><i></i>投注内容：</span>
                <div class="detailNumTextArea" id="delContentPlan" style="OVERFLOW: auto;">
                    <div id="delContent" style="word-break: break-all;"></div>
                </div>
            </div>
        </div>
        <div class="detailBody">
            <table class="detailTable overflowTable">
                <tr class="listHeader">
                    <th width="120">玩法</th>
                    <th width="50">注数</th>
                    <th width="50">倍数</th>
                    <th width="100">投注金额</th>
                    <th width="50">模式</th>
                    <th width="180">开奖号码</th>
                    <th width="50">备注</th>
                    <th width="120">奖金</th>
                </tr>
                <tr class="listDetail" id="betRecond">
                    <td><span id="delPlayName"></span></td>
                    <td><span id="delBetCount"></span></td>
                    <td><span id="delPrice"></span></td>
                    <td><span class="fontColorTheme" id="delAmount"></span></td>
                    <td><span id="delUnit"></span></td>
                    <td><span  id="delOpenNum"></span></td>
                    <td><span id="delBonusType"></span></td>
                    <td><span class="fontColorTheme" id="delWin"></span></td>
                </tr>
            </table>
        </div>
        <div class="dialogBtn">
            <a href="javascript:;" class="btn closeBtn" >关闭</a>
            <span id="cancelOrder"></span>
            <a href="javascript:;" class="btn againBtn" id="reBet">再次投注</a>
        </div>
    </div>
    <!-- 追号详情 -->
    <div id="traceSummaryList" style="display: none;">
        <div class="detailHeaderBox">
            <div class="headerDetail">
                <h3 class="detailHeader-l" id="summaryLotteryName"></h3>
                <div class="detailHeader-r">
                    <p>
                        <label for="#">
                            <span>起始期号：</span>
                            <span id="summaryStartSeasonId"></span>
                        </label>
                        <label for="#">
                            <span>追号时间：</span>
                            <span id="summaryCreateTime"></span>
                        </label>
                    </p>
                    <p>
                        <label for="#">
                            <span>进度：</span>
                            <span id="summaryProcess"></span>
                        </label>
                        <label for="#">
                            <span>已追号金额：</span>
                            <span id="summaryFinishTraceAmount"></span>
                        </label>
                        <label for="#">
                            <span>追号方案金额：</span>
                            <span id="summaryTraceAmount">￥10.00元</span>
                        </label>
                    </p>
                    <p>
                        <label for="#">
                            <span>终止追号条件：</span>
                            <span id="summaryIsWinStop"></span>
                        </label>
                        <label for="#">
                            <span>已获奖金：</span>
                            <span id="summaryWinAmount"></span>
                        </label>
                        <label for="#">
                            <span>追号编号：</span>
                            <span id="summaryId"></span>
                        </label>
                    </p>
                </div>
            </div>
            <div class="tableBox headerDetail">
                <h3 class="detailHeader-l">追号方案</h3>
                <div class="detailHeader-c">
                    <table class="detailHeaderTable overflowTable" id="schemeListTable">
                        <thead>
                        <tr class="listHeader">
                            <th>玩法</th>
                            <th width="200">投注内容</th>
                            <th>注数</th>
                            <th>倍数</th>
                            <th>模式</th>
                        </tr>
                        </thead>
                        <tbody  id="detailHeader-c"></tbody>
                    </table>
                </div>
                <!--<div class="headerBtn" id="endAppNum">-->
                <!--  <a href="javascript:;" class="btn endAppNum">终止追号</a> -->
                <!--</div>-->
            </div>
        </div>
        <div class="tableBox detailBody">
            <table class="detailTable overflowTable" id="summaryListTable">
                <thead>
                <tr class="listHeader" >
                    <th width="100">期号</th>
                    <th width="50">追号倍数</th>
                    <th width="75">投注金额</th>
                    <th width="250">当期开奖号码</th>
                    <th width="75">奖金</th>
                    <th width="100">状态</th>
                    <th width="130">操作项</th>
                </tr>
                </thead>
                <tbody id="detailHeader-d"></tbody>
            </table>
        </div>
        <div class="dialogBtn" id="appNumIntroBtn">
            <a href="javascript:;" class="btn closeBtn" >关闭</a>
        </div>
    </div>

    <!-- 追号中投注详情 -->
    <div id="contentDetailsArea" style="display:none;">
        <div class="detailNumTextArea" id="contentDetailsPlan" style="border:1px solid #ccc;overflow: auto;">
            <div id="contentDetails" style="word-break: break-all;"></div>
        </div>
        <!--<div class="dialogBtn">-->
        <!--<a href="javascript:;" class="btn closeBtn" >确定</a>-->
        <!--</div>-->
    </div>

    <!-- 左侧浮动：所有游戏 -->
    <%@include file="/WEB-INF/jsp/home/left.jsp" %>
</body>
</html>

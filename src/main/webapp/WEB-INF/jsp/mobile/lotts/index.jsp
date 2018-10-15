<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.sjc168.com/fn" prefix="fn"%><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0 user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
    <title>投注页</title>
    <link rel="stylesheet" href="/res/mobile/css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<%@include file="/WEB-INF/jsp/mobile/left.jsp" %>

<!-- 投注页 -->
<div class="generalPlatArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="/res/mobile/images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">${lott.title}</span>
        <a href="<c:url value='/lotts/${lott.id}/trend'/>" class="lotteryNumLink">
			<span>开奖号码</span>
		</a>
    </div>
    <div class="container">
    
    	<!-- 选号投注页-start -->
        <div class="lotteryBetsBox" id="lotteryBetsBox">
            <!-- 当前玩法-start -->
            <div class="playMethodBox fontSizeRem09">
                <div class="currPlayMethodDetail">
                    <span>当前玩法：</span>
                    <a href="javascript:;" class="currPlayMethod" id="currPlayMethod">
                        <span id="playName"></span>
                        <img src="/res/mobile/images/betsDropDownIcon.png" class="dropDownIcon" />
                    </a>
                </div>
                <div class="playMethodGroupList" id="playMethodGroupList" style="display: none;">
                	<c:forEach items="${lottBase.qun }" var="qun" varStatus="st">
                		<c:if test="${qun.playerCount>0 }">
		                	<div class="playMethodGroup">
		                        <div class="playMethodGroupTitle">
		                            <span>${qun.title }</span>
		                        </div>
		                        <div class="playMethodList">
		                        	<c:forEach items="${qun.groups}" var="group">
		                        		<c:forEach items="${group.players }" var="player">
		                        			<c:set var="p" value="${lottBase.playerBonusMap[player.id] }"/>
		                        			<c:if test="${p!=null && p.saleStatus==0 && p.mobileStatus==0 }">
		                        			 <a href="javascript:;" data-parent="${qun.title }" class="playMethod" data-title="${player.fullTitle }" data-show="#${p.id }" data-MaxBonus="${p.bonus * (p.bonusRatio+ user.rebateRatio - (bonusGroup.rebateRatio - p.rebateRatio)) / 100 }" data-bonus="${p.bonus * p.bonusRatio / 100 }">
				                                <span>${player.groupName}${player.title}</span>
				                                <i></i>
				                            </a>
				                            </c:if>
		                        		</c:forEach>
		                           
		                        	</c:forEach>
		                        </div>
		                    </div>
	                    </c:if>
					</c:forEach>
                </div>
            </div><!-- 当前玩法-end -->

            <!-- 剩余投注时间倒计时-start -->
            <div class="betsRemainTimeBox fontSizeRem08">
                <p class="betsRemainTime" id="saleAllSecond" data-second="${allSecond }">
                    <span>第</span>
                    <span class="fontColorRed saleSeasonId">${current.seasonId}</span>
                    <span>期剩余投注:</span>
                    <span class="fontColorRed saleHour">${hour}</span>
                    <span>时</span>
                    <span class="fontColorRed saleMintue">${minute}</span>
                    <span>分</span>
                    <span class="fontColorRed saleSecond">${second}</span>
                    <span>秒</span>
                </p>
                <!--<a href="javascript:;" class="shakeShake">
                    <img src="/res/mobile/images/shakeShake.png" alt=""/>
                </a> -->
            </div><!-- 剩余投注时间倒计时-end -->

			<!-- 上一期开奖号码-start -->
			<a href="<c:url value='/lotts/${lott.id}/trend'/>" class="betsBeforeNumBox fontSizeRem08">
				<p>
					<label for="#" class="betsBeforeNum">
						<span class="fontColorRed lastSeasonId">${lastOpen.seasonId}</span>
						<span>期：</span>
 					</label>
					<label for="#" class="lastSeasonNum ${lott.id}">
						<c:forEach items="${lastOpen.nums}" var="a">
							<span>${a}</span>
						</c:forEach>
					</label>
				</p>
			</a><!-- 上一期开奖号码-end -->
			
			<c:forEach items="${lottBase.players }" var="player">
				<c:set var="p" value="${lottBase.playerBonusMap[player.id] }" />
		        <c:if test="${p!=null && p.saleStatus==0 && p.mobileStatus==0 }">
                    <!-- 投注号码-start -->
                    <div class="betsChangeNumBox" id="${player.id }" data-anySelect="${player.anySelect }" style="display:none;">
						<div class="betsPlaysIntro cls">
							<label class="betsPlaysIntroDetail">
								<i class="betsPlaysIntroIcon"></i>
								<span>玩法介绍</span>
							</label>
							<span class="betsBonusNumber">最高奖金：${fn:getMaxBonus(player.bonusStr,p.bonusRatio,user.rebateRatio,bonusGroup.rebateRatio,p.rebateRatio)}</span>
                        	<p class="betsChangeNumTips" style="display:none;">${p.remark}</p>
						</div>
                        <div class="changeNumList">
							<div class="anySelectBox">
								<c:if test="${player.anyList!=null }">
									<c:forEach items="${player.anyList }" var="a" varStatus="stat">
										<label>
											<c:choose>
												<c:when test="${player.anySelect>stat.index}">
													<input class="anySelect" type="checkbox" checked="checked" value="${a}"/>
													<span>${a}</span>
												</c:when>
												<c:otherwise>
													<input class="anySelect" type="checkbox" value="${a}"/>
													<span>${a}</span>
												</c:otherwise>
											</c:choose>
										</label>
									</c:forEach>
								 </c:if>
							</div>

							<c:choose>
								<c:when test="${player.numView!=null }">
									<c:forEach items="${player.numView }" var="line">
										<div class="changeNumDetail">
											<div class="changeNumTitle fontSizeRem12">
												<span>${line.title }</span>
												<c:if test="${line.hasBut }">
													<div class="changeNumBtnList">
														<a href="javascript:;" class="changeNumBtn numBtnBig">大</a> <a href="javascript:;" class="changeNumBtn numBtnSmall">小</a> <a href="javascript:;" class="changeNumBtn numBtnOdd">单</a> <a
															href="javascript:;" class="changeNumBtn numBtnEven">双</a> <a href="javascript:;" class="changeNumBtn numBtnAll">全</a> <a href="javascript:;" class="changeNumBtn">清</a>
													</div>
												</c:if>
											</div>
											<div class="changeNumContent" data-max="${line.maxSelect }">
												<c:forEach items="${line.nums }" var="n" varStatus="st">
													<a href="javascript:;" class="changeNum" data-num="${n}">${n}</a>
												</c:forEach>
											</div>
										</div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<!-- 单式 -->
									<textarea class="singleTextArea" placeholder="说明：每一注号码之间的间隔符支持常见的单式格式。如：回车，空格，逗号，分号等。注：大順单式玩法自动过滤错误投注号码。"></textarea>
								</c:otherwise>
							</c:choose>

								<div class="clearChangeNumBtn">
                                <a href="javascript:;" class="clearChangeNum">
                                    <img src="/res/mobile/images/ashBin.png" alt=""/>
                                    <span>清空选号</span>
                                </a>
                            </div>
                        </div>
                    </div><!-- 投注号码-end -->
                </c:if>
			</c:forEach>
			
			<!-- 开始投注-start -->
	        <div class="fixBox startBetsBox">
	        	<i class="startBets-t"></i>
	            <p class="betsInfoDetail">
	                <span>共</span>
	                <span id="betsCount">0</span>
	                <span>注，共</span>
	                <span class="fontColorRed" id="betsAmountCount">0</span>
	                <span>元</span>
	            </p>
	            <div class="startBetsBtns">
	                <div class="betsTypeBtns">
	                    <a href="javascript:;" class="active" data-unit="1">元</a>
	                    <a href="javascript:;" data-unit="0.2">角</a>
	                    <a href="javascript:;" data-unit="0.02">分</a>
	                    <a href="javascript:;" data-unit="0.002">厘</a>
	                </div>
	                <a href="javascript:;" class="betsBtn" id="joinBetsBar">加入投注栏</a>
	            </div>
	        </div><!-- 开始投注-end -->
			
        </div><!-- 选号投注页-end -->
        
        <!-- 确认投注页-start -->
        <div class="cfmBetsBox" id="cfmBetsBox" style="display: none;">

            <!-- 剩余投注时间倒计时-start -->
            <div class="betsRemainTimeBox fontSizeRem08">
                <p class="betsRemainTime">
                    <span>第</span>
                    <span class="fontColorRed saleSeasonId" id="saleSeasonId">${current.seasonId }</span>
                    <span>期剩余投注：</span>
                    <span class="fontColorRed saleHour">${hour}</span>
                    <span>时</span>
                    <span class="fontColorRed saleMintue">${minute}</span>
                    <span>分</span>
                    <span class="fontColorRed saleSecond">${second }</span>
                    <span>秒</span>
                </p>
            </div><!-- 剩余投注时间倒计时-end -->

			<!-- 上一期开奖号码-start -->
			<a href="<c:url value='/lotts/${lott.id}/trend'/>" class="betsBeforeNumBox fontSizeRem08">
				<p>
					<label for="#" class="betsBeforeNum">
						<span class="fontColorRed lastSeasonId">${lastOpen.seasonId}</span>
						<span>期：</span>
					</label>
					<label for="#" class="lastSeasonNum ${lott.id}">
						<c:forEach items="${lastOpen.nums}" var="a">
							<span>${a}</span>
						</c:forEach>
					</label>
				</p>
			</a><!-- 上一期开奖号码-end -->

            <!-- 随机选号按钮-start -->
            <div class="randomChangeBtnBox">
                <a href="javascript:;" class="randomChangeBtn createRandom" data-count="1">
                    <i></i>
                    <span>机选1注</span>
                </a>
                <a href="javascript:;" class="randomChangeBtn createRandom" data-count="5">
                    <i></i>
                    <span>机选5注</span>
                </a>
                <a href="javascript:;" class="randomChangeBtn changeAgainBtn" id="changeAgainBtn">
                    <i class="changeAgainIcon"></i>
                    <span>继续选号</span>
                </a>
            </div><!-- 随机选号按钮-end -->

            <!-- 确认投注信息-start -->
            <div class="betsNumShowBox">
                <div class="betsNumShowList" id="selectBetPanle"></div>
                <div class="betsNumShowClear">
                    <a href="javascript:;" class="clearChangeList" id="betsNumShowClear">
                        <i class="ashBinBtn"></i>
                        <span>清空列表</span>
                    </a>
                </div>
                <i class="betsNumShowBorder"></i>
            </div><!-- 确认投注信息-end -->
            
	        <!-- 确认投注-start -->
	        <div class="fixBox confirmBetsBox">
	            <div class="betsOtherInfo">
	                <p class="betsOtherDetail">
	                    <span>投</span>
	                    <input type="tel" id="betPrice" class="labelCond" value="1" />
	                    <span>倍</span>
	                </p>
	                <p class="betsOtherDetail">
	                    <span>追</span>
	                    <input type="tel" id="tracePrice" class="labelCond" value="1" />
	                    <span>期</span>
	                    <label class="awardsAfterDetail" id="betsAppNumBox" style="display:none;">
	                        <input type="checkbox" id="winStop" checked />
	                        <span>中奖后停止追号</span>
	                        <i class="awardsAfterIcon"></i>
	                    </label>
	                </p>
	            </div>
	            <div class="cfmBetsInfo">
	                <div class="cfmBetsInfoList fontSizeRem09">
	                    <p>
	                        <span>共</span>
	                        <span id="appNumCount">0</span>
	                        <span>注，共</span>
	                        <span class="fontColorRed" id="appNumAmount">0</span>
	                        <span>元</span>
	                    </p>
	                    <p class="fontColorGray">
	                        <span>可用余额</span>
	                        <span class="fontBold userBalance">${user.amount}</span>
	                        <span>元</span>
	                    </p>
	                </div>
	                <a href="javascript:;" class="betsBtn" id="betConfirm">确认投注</a>
	            </div>
	        </div><!-- 确认投注-end -->
	        
	    </div><!-- 确认投注页-end -->
        
    </div>
</div>

<!-- 通用弹窗 -->
<div class="dialogBox cfmBetsDialogBox" id="betConfirmBox" style="display: none;">
	<p class="dialogTitle">投注确认</p>
	<div class="dialogContent">
		<form style="display:none;" action="" id="betConfirmForm"></form>
		<p>
			<span>彩种:</span><span>${lott.title}</span>
		</p>
		<p>
			<span>投注金额:</span>
			<span class="fontColorRed" id="betConfirmAmount"></span>
			<span>元</span>
		</p>
		<p>
			<span>追号:</span>
			<span id="betConfirmTrace"></span>
			<span>期</span>
			<span id="betConfirmNum"></span>
			<span>注</span>
			<span id="betConfirmPrice"></span>
			<span>倍</span>
		</p>
		<p>
			<span>起止期:</span>
			<span id="betConfirmTraceList"></span>
		</p>
	</div>
	<div class="dialogBtns">
		<a href="javascript:;" class="closeBtn">取消</a><!-- 如果只有一个按钮，需要加上class“btn100”，以达到宽度100% -->
		<a href="javascript:;" class="fontColorRed" id="betSubmit">确定</a>
	</div>
</div>

<script type="text/javascript" src="<c:url value='/res/home/js/audio/audio5.js?ver=${VIEW_VERSION}'/>"></script>

<script type="text/javascript" src="<c:url value='/res/home/js/lotts/play.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/lotts/play_${lottBase.groupId}.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/lotts/lottery.js?ver=${VIEW_VERSION}'/>"></script>
</body>
</html>
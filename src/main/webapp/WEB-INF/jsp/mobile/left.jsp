<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="<c:url value='/res/home/js/jquery-1.11.0.min.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/jquery.timer.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/math.extends.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/jquery.dialog.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/base.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/mobile/js/hs-mobile.js?ver=${VIEW_VERSION}'/>"></script>
<!-- 平台主菜单 -->
<div class="platMainNavBox" id="platMainNavBox" style="display:none;">
    <div class="platMainNavShow">
        <div class="platMainNavTop">
            <div class="userUsableBalance">
                <p class="fontSizeRem09">可用余额</p>
                <p class="userBalance">${user.amount}</p>
            </div>
        </div>
        <div class="platMainNavContent">
            <ul class="platMainNavList">
                <li class="platMainNavDetail">
                    <a href="/" class="platMainNav">
                        <i class="platMainNav01"></i>
                        <span>彩种大厅</span>
                        <i class="rightArrow"></i>
                    </a>
                </li>
                <li class="platMainNavDetail">
                    <a href="javascript:;" class="platMainNav">
                        <i class="platMainNav02"></i>
                        <span>账户中心</span>
                        <i class="rightArrow"></i>
                    </a>
                    <ul class="platSubNavList" style="display: none;">
                        <!--<li class="platSubNavDetail">-->
                            <!--<a href="/recharge/rechargeMoney" class="platSubNav">-->
                                <!--<i class="platMainNav05"></i>-->
                                <!--<span>账户充值</span>-->
                            <!--</a>-->
                        <!--</li>-->
                        <!--<li class="platSubNavDetail">-->
                            <!--<a href="/deposit/safePassword" class="platSubNav">-->
                                <!--<i class="platMainNav06"></i>-->
                                <!--<span>提款申请</span>-->
                            <!--</a>-->
                        <!--</li>-->
                        <!--<li class="platSubNavDetail">-->
                            <!--<a href="/user/bindCard" class="platSubNav">-->
                                <!--<i class="platMainNav07"></i>-->
                                <!--<span>银行卡绑定</span>-->
                            <!--</a>-->
                        <!--</li>-->
                        <!--<li class="platSubNavDetail">-->
                            <!--<a href="/finance/amountChangeList" class="platSubNav">-->
                                <!--<i class="platMainNav08"></i>-->
                                <!--<span>账户明细</span>-->
                            <!--</a>-->
                        <!--</li>-->
                        <li class="platSubNavDetail">
                            <!-- <a href="/game/index" class="platSubNav"> -->
                            <a href="/game/gameRecordList" class="platSubNav">
                                <i class="platMainNav09"></i>
                                <span>投注记录</span>
                            </a>
                        </li>
                        <li class="platSubNavDetail">
                            <a href="/safe/accountSafe" class="platSubNav">
                                <i class="platMainNav10"></i>
                                <span>账户安全</span>
                            </a>
                        </li>
                        <c:if test="${user.userType==1 }">
                        <li class="platSubNavDetail">
                            <a href="/user/list" class="platSubNav">
                                <i class="platMainNav16"></i>
                                <span>用户列表</span>
                            </a>
                        </li>
                        <li class="platSubNavDetail">
                            <a href="/openUser/addPerson" class="platSubNav">
                                <i class="platMainNav11"></i>
                                <span>开设账户</span>
                            </a>
                        </li>
                         <li class="platSubNavDetail">
                            <a href="/openUser/addLinkUser" class="platSubNav">
                                <i class="platMainNav15"></i>
                                <span>链接开户</span>
                            </a>
                        </li>
                        <li class="platSubNavDetail">
                            <a href="/openUser/regLinkManager" class="platSubNav">
                                <i class="platMainNav11"></i>
                                <span>注册链接管理</span>
                            </a>
                        </li>
                        </c:if>
                       
                    </ul>
                </li>
				<li class="platMainNavDetail">
					<a href="javascript:;" class="platMainNav">
						<i class="platMainNav17"></i>
						<span>银行充提</span>
						<i class="rightArrow"></i>
					</a>
					<ul class="platSubNavList" style="display: none;">
						<li class="platSubNavDetail">
							<a href="/recharge/rechargeMoney" class="platSubNav">
								<i class="platMainNav05"></i>
								<span>账户充值</span>
							</a>
						</li>
						<li class="platSubNavDetail">
							<a href="/deposit/safePassword" class="platSubNav">
								<i class="platMainNav06"></i>
								<span>提款申请</span>
							</a>
						</li>
						<li class="platSubNavDetail">
							<a href="/user/bindCard" class="platSubNav">
								<i class="platMainNav07"></i>
								<span>银行卡绑定</span>
							</a>
						</li>
						<li class="platSubNavDetail">
							<a href="/finance/amountChangeList" class="platSubNav">
								<i class="platMainNav08"></i>
								<span>账户明细</span>
							</a>
						</li>
					</ul>
				</li>
				<li class="platMainNavDetail">
					<a href="javascript:;" class="platMainNav">
						<i class="platMainNav03"></i>
						<span>平台消息</span>
						<i class="rightArrow"></i>
					</a>
					<ul class="platSubNavList" style="display: none;">

						<li class="platSubNavDetail">
							<a href="/message/messageTable" class="platSubNav">
								<i class="platMainNav13"></i>
								<span>我的消息</span>
							</a>
						</li>
						<li class="platSubNavDetail">
							<a href="/notice/index" class="platSubNav">
								<i class="platMainNav12"></i>
								<span>平台公告</span>
							</a>
						</li>
						<li class="platSubNavDetail">
							<a href="/activity/index" class="platSubNav">
								<i class="platMainNav12"></i>
								<span>平台活动</span>
							</a>
						</li>
					</ul>
				</li>
				<li class="platMainNavDetail">
					<a href="${sysService.link }" class="platMainNav" target="_blank">
						<i class="platMainNav02"></i>
						<span>在线客服</span>
						<i class="rightArrow"></i>
					</a>
				</li>
            </ul>
        </div>
        <div class="userQuitBox">
	        <a href="/logout">
	            <i class="quitIcon"></i>
	            <span>退出</span>
	        </a>
	        <a href="/pc" class="userSkipPc">
	            <i class="pcIcon"></i>
	            <span>电脑版</span>
	        </a>
	    </div>
    </div>
</div>

<div class="platMainNavMask" style="display: none;"></div>


<!-- 弹窗 -->
<div id="mask" class="mask" style="display: none;"></div>
<div class="dialogArea" id="dialogBox"></div>

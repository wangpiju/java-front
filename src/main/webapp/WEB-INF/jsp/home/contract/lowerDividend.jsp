<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.sjc168.com/fn" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mx"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>下级分红</title>
    <link rel="stylesheet" href="<c:url value='/res/home/css/hf-platOperate.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <script type="text/javascript" src="<c:url value='/res/home/js/math.extends.js?ver=${VIEW_VERSION}'/>"></script>
    <script src="<c:url value='/res/home/js/contract/lowerDividend.js?ver=${VIEW_VERSION}'/>"></script>
    <script type="text/javascript" src="<c:url value='/res/home/js/pagerAjax.js?ver=${VIEW_VERSION}'/>"></script>
</head>

<body>
    <div class="platOperateArea">
    	<div class="platOperateHeader">
    		<div class="clr areaContainer">
				<label class="titleBar">
					<i class="titleLeft userCenterIcon"></i>
					<span class="titleRight">代理中心</span>
				</label>
				<!-- 账户中心 导航 -->
				<ul class="centreNav">
					<li class="centreNavDetail" id="mySafeCenter">
						<a href="<c:url value='/agent/index?tabId=userList'/>">用户列表</a>
					</li>
					<li class="centreNavDetail" id="topBankCard">
						<a href="<c:url value='/agent/index?tabId=addUser'/>">站内开户</a>
					</li>
					<li class="centreNavDetail" id="topLottInfo">
						<a href="<c:url value='/agent/index?tabId=extCode'/>">链接开户</a>
					</li>
					<li class="centreNavDetail active" id="topMsg">
						<a href="<c:url value='/contract/dividendIndex'/>">下级分红</a>
					</li>
					<li class="centreNavDetail">
						<a href="<c:url value='/agent/index?tabId=team'/>">团队统计</a>
					</li>
				</ul>
			</div>
    	</div>
		<div class="platOperateMain agentMain">
			<div class="areaContainer">
				<!-- 下级分红-start -->
				<div class="platOperateBox downBonusArea" id="lowerDividend">
					<div class="platOperateContainer">
						<div class="platOperateContent">
							<form class="searchBar userListSearch" id="dividendSearchArgs">
		                        <label for="#">
									<span class="labelTitle">用户名：</span>
									<input type="text" class="labelCond userName" id="userName" name="account">
		                        </label>
		                        <label for="#">
		                        	<span class="labelTitle">日期：</span>
		                        	<!-- 不可输入下拉框 -->
									<label class="inputSelectDiv selectCond" style="width:180px;">
										<input type="hidden" class="labelCond" id="dividendDate" name="dividendDate" value=""/>
										<span class="labelCond">全部</span>
										<a href="javascript:;" class="inputSelectArrow" onclick="inputSelectFrame(this)"></a>
										<label class="inputOptions">
											<a href="javascript:;" onclick="inputSelectConfirm(this,'')">全部</a>
											<c:forEach items="${dividendDateList}" var="a" varStatus="st">
												<a href="javascript:;" onclick="inputSelectConfirm(this,'${a}')">${a}</a>
		                        	    	</c:forEach>
										</label>
									</label>
								</label>
		                        <label for="#">
									<span class="labelTitle">状态：</span>
		                           	<label class="inputSelectDiv selectCond" style="width:110px;">
										<input type="hidden" class="labelCond" id="dividendStatus" name="dividendStatus" value=""/>
										<span class="labelCond">全部</span>
										<a href="javascript:;" class="inputSelectArrow" onclick="inputSelectFrame(this)"></a>
										<label class="inputOptions">
											<a href="javascript:;" onclick="inputSelectConfirm(this,'')">全部</a>
											<a href="javascript:;" onclick="inputSelectConfirm(this,0)">尚未发放</a>
											<a href="javascript:;" onclick="inputSelectConfirm(this,1)">发放完毕</a>
											<a href="javascript:;" onclick="inputSelectConfirm(this,2)">不需分红</a>
											<a href="javascript:;" onclick="inputSelectConfirm(this,3)">逾期未发放</a>
											<a href="javascript:;" onclick="inputSelectConfirm(this,4)">強制发放完毕</a>
										</label>
									</label>
		                        </label>
								<a href="javascript:;" class="btn2" id="search">查询</a>
		                    </form>
							<!-- 下级分红列表 -->
							<div class="downBonusBox" id="downBonusBox">
								<div class="tableArea">
									<table class="tableBox platMsgTable" id="dividendListTable">
					            		<thead>
						            		<tr class="tableHeader">
							                	<th>账户名</th>
							                    <th>开始日期</th>
							                    <th>结束日期</th>
							                    <th>累积销售量</th>
							                    <th>分红比</th>
							                    <th>累计盈亏</th>
							                    <th>应发分红</th>
							                    <th>状态</th>
							                    <th>操作</th>
						             		</tr>
						             	</thead>	
						             	<tbody id="dividendListHeader">
											
					             		</tbody>
					           		</table>
				           		</div>
				           		 <!-- 没有消息 -->
			                    <div class="nullMsg" id="nullMsg">
			                        <i class="nullMsgImg"></i>
			                        <span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
			                    </div>
		                    </div>
	                    </div>
	                    <!-- 分页 -->
	                    <mx:pagerAjax formId="dividendSearchArgs" btnId="search"></mx:pagerAjax>
                    </div>
           		</div>
           		<!-- 下级分红-end -->
           		
			</div>
		</div>
    </div>
</body>
</html>
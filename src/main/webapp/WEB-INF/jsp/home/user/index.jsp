<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mx"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>管理中心</title>
<link rel="stylesheet" href="<c:url value='/res/home/css/wk-accountCentre.css?ver=${VIEW_VERSION}'/>" />
<link rel="stylesheet" href="<c:url value='/res/home/css/jquery-ui.min.css?ver=${VIEW_VERSION}'/>" />
<script type="text/javascript" src="<c:url value='/res/home/js/jquery-ui.min.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/math.extends.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/zclip/zclip.min.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/jquery.qrcode.min.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/My97DatePicker/WdatePicker.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript" src="<c:url value='/res/home/js/pagerAjax.js?ver=${VIEW_VERSION}'/>"></script>
<script type="text/javascript">
	var needBindCard = "${needBindCard }";
	var specicalStr = "${specicalStr }";
	var gloas = {};
	gloas.tabId = '#${tabId}';
	gloas.currentAccount = "${user.account}";					//当前用户
	gloas.maxRatio = ${maxRatio};							//最大可调返点
	gloas.playerMaxRatio = ${bonusGroup.playerMaxRatio};	//会员最大返点
	gloas.userMinRatio = ${bonusGroup.userMinRatio};		//用户点差
	gloas.noneMinRatio = ${bonusGroup.noneMinRatio};		//无需配合返点
	gloas.stepRatio = ${stepRatio};							//点差步长
	gloas.accountRecharge = ${user.accountRecharge==0?'false':'true'};	//下级充值功能
	gloas.hasSafeWord = ${ user.safePassword == null|| user.safePassword==''?'false':'true'};
	gloas.lockStatus = ${user.bankStatus==0?'false':'true'};
	gloas.userType = ${user.userType};//用户类型
	gloas.userType = ${user.userType};
	gloas.contractStatus = ${(user.contractStatus == 1 ||user.contractStatus == 3||user.contractStatus == 9)?'true':'false' }; //契约功能
	gloas.dailyWagesStatus = ${user.dailyWagesStatus==0?'true':'false'};
	gloas.dailyWagesTrans = ${null == dailyRule && null != user.dailyRuleId };// 日工资仅传递的权限
	gloas.dailyWagesOpen = ${null != dailyRule };// 日工资开通日薪的权限
</script>
<script type="text/javascript" src="<c:url value='/res/home/js/user/index.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body>

<!-- 账户中心 -->
<div class="areaBigContainer mainWidth" id="userCenterArea">
	<div class="containerFlex">
		<label id="userAccount" style="display:none">${user.account}</label>
		<!-- 列表 -->
		<div class="leftListArea">
			<c:if test="${user.userType==1 }">
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail teamManageA" data-id="#teamManageArea">团队管理</a>
				<div class="leftListItem" style="display:none;">
					<a class="nav active" href="javascript:;" data-id="#financeList">会员管理</a>
					<a class="nav" href="javascript:;" data-id="#addUser">新增会员</a>
					<a class="nav" href="javascript:;" data-id="#lowerDividend">下级分红</a>
					<a class="nav" href="javascript:;" data-id="#teamInfo">团队概况</a>
					<a class="nav" href="javascript:;" data-id="#extCode">注册链接</a>
				</div>
			</div>
			</c:if>
			<c:if test="${user.userType==1 }">
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail" data-id="#agent">代理中心</a>
				<div class="leftListItem" style="display:none;">
					<a class="nav active" href="javascript:;" data-id="#agentReport">代理报表</a>
					<%--<a class="nav" href="javascript:;" data-id="#myCard"></a>--%>
				</div>
			</div>
			</c:if>
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail userCenterA" data-id="#safe">个人中心</a>
				<div class="leftListItem" style="display:none;">
					<a class="nav active" href="javascript:;" data-id="#personInfo">个人信息</a>
					<a class="nav" href="javascript:;" data-id="#myCard" id="myBankCard">我的银行卡</a>
					<a class="nav" href="javascript:;" data-id="#modLoginPwd">修改登录密码</a>
					<a class="nav" href="javascript:;" data-id="#modAccountPwd">修改安全密码</a>
					<a class="nav" href="javascript:;" data-id="#safeQA">安全问答</a>
					<a class="nav" href="javascript:;" data-id="#findSafeByQa">找回安全密码</a>
					<c:if test="${user.contractStatus == 0||user.contractStatus == 1||user.contractStatus == 9}">
						<a class="nav" href="<c:url value='/contract/findMyContract'/>">我的契约</a>
					</c:if>
				</div>
			</div>
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail msgA" data-id="#msg">消息管理</a>
			</div>
		</div>
		<!-- 内容 -->
		<div class="rightArea">
               <div class="accountCentreContent">
               		<c:if test="${user.userType==1 }">
               		<!-- 团队管理 -->
               		<div class="teamManageBox bigCommonBox" id="teamManageArea" style="display: none;">
               			<div class="safeCenter">
		                   <!-- 会员管理 -->
		                   <div class="userListArea" id="financeList">
		                       <!-- 查询条件 -->
		                       <form class="searchBar userListSearch" id="userSearchForm">
			                       <input type="hidden" name="nextAccount" id="nextAccount"/>
		                           <label for="#">
										<span class="labelTitle">用户名：</span>
										<input type="text" class="labelCond userName" id="account" name="account" onkeyup="this.value=this.value.replace(/\W/g,'');" />
		                           </label>
								   <label for="#">
									   <span class="labelTitle">用户余额：</span>
									   <input type="text" class="labelCond searchAmount" name="beginAmount" onkeyup="inputNumber(this)" />
									   <span>-</span>
		                               <input type="text" class="labelCond searchAmount" name="endAmount" onkeyup="inputNumber(this)" />
		                               <span>元</span>
		                           </label>
								   <label for="#">
										<span class="labelTitle">用户组：</span>
										<select name="userType" class="labelCond selectCond">
			                                   <option value="2">全部用户</option>
			                                   <option value="1">代理</option>
			                                   <option value="0">会员</option>
			                           	</select>
			                        </label>
			                        <label for="#">
			                        	<span class="labelTitle">在线状态：</span>
										<select name="isOnLine" class="labelCond selectCond">
			                                   <option value="0">全部显示</option>
			                                   <option value="2">在线</option>
			                           	</select>
			                        </label>
									<a href="javascript:void(0);" class="btn" id="userSearchSubmit">搜索</a>
									<a href="javascript:void(0);" style="display:none;" id="userSearchSubmit2">搜索2</a>
									<span class="operate" id="goBack" style="display: none"></span>
									<c:if test="${null != dailyRule }">
									<a href="javascript:void(0);" class="btn dailyManagerBtn" id="dailyManager">日薪管理</a>
									</c:if>
									<c:if test="${null == dailyRule && null != user.dailyRuleId }">
										<span class="dailyManagerTips">关于契约日薪：您具备了开通下级日薪的权限，在用户列表的操作中可以进行开通，给下级开通后，契约日薪即传递出去了。</span>
									</c:if>
		                       </form>
								<p class="userBreadCrumb" id="testing"></p>
		                       <!-- 用户数据 -->
		                       <div class="tableBox userListBox" id="userListBox">
		                           <table class="userListTable">
		                           	<thead>
		                               <tr class="listHeader">
		                                   <th width="15%">用户信息</th>
		                                   <th>人数</th>
		                                   <th width="15%">余额</th>
		                                   <th width="15%">团队余额</th>
		                                   <th>最后登录时间</th>
		                                   <th>日薪信息</th>
		                                   <c:if test="${user.contractStatus == 1 ||user.contractStatus == 3 }">
										       <th>契约状态</th>
										   </c:if>
		                                   <th>备注</th>
		                                   <th>操作</th>
		                               </tr></thead>
		                               <tbody id="userListTable"></tbody>
		                               <tfoot>
		                               	<tr>
		                               		<td colspan="3" class='fontColorTheme'>本页团队余额总计：</td>
		                               		<td class='fontColorTheme' id="teamBalance"></td>
		                               	</tr>
		                               </tfoot>
		                           </table>
		                           <!-- 没有消息 -->
		                           <div class="nullMsg" id="UserNullMsg">
		                               <i class="nullMsgImg"></i>
									<span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
		                           </div>
		                           <!-- 分页 -->
		                           <mx:pagerAjax formId="userSearchForm" btnId="userSearchSubmit2"></mx:pagerAjax>
		                       </div>
		                   </div>
		                   <!-- 新增会员 -->
		                   <div class="addUserArea" id="addUser" style="display: none;">
		                       <!-- 添加条件 -->
		                       <div class="addUserBox">
		                           <form class="addUserList" id="userAddForm">
		                               <div>
		                                   <span class="labelTitle">用户类型：</span>
		                                   <select id="addUserType" class="labelCond selectCond userType" name="userType">
		                                       <option value="0">会员</option>
		                                       <option value="1">代理</option>
		                                   </select>
		                               </div>
		                               <div>
		                                   <span class="labelTitle">用户名：</span>
		                                   <input class="labelCond userName" placeholder="字母开头6-12位数字、字母" id="addAccount" name="account" onkeyup="this.value=this.value.replace(/\W/g,'');" maxLength=12 />
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <div>
		                                   <span class="labelTitle">默认密码：</span>
		                                   <input id="addDefaultPwd" class="labelCond defaultPwd" value="aa123456" name="passWord" tabindex="-1" readonly />
		                               </div>
		                               <div>
		                                   <span class="labelTitle">设置返点：</span>
		                                   <select id="addRebateRatio" class="labelCond selectCond reservePoint" name="rebateRatio">
		                                   </select>
		                               </div>
		                               <div>
		                                   <button class="btn" type="submit">确认开户</button>
		                               </div>
		                           </form>
		                           <!-- 我的配额 -->
		                           <div class="myQuotaBox">
		                               <table class="myQuotaTable">
		                               	<thead>
		                               		<tr>
		                                       <th class="myQuotaTitle" colspan="4">我的配额</th>
		                               		</tr>
		                               		<tr>
												<th>返点</th>
												<th>剩余配额</th>
												<th>返点</th>
												<th>剩余配额</th>
											</tr>
		                               	</thead>
		                               	<tbody id="myQuotaTable"></tbody>
		                               </table>
		                           </div>
		                       </div>
		                       <%--<div class="tableBox">--%>
		                           <%--<table class="lottTypeIntroTable">--%>
		                           	<%--<thead>--%>
		                               <%--<tr>--%>
		                                   <%--<th width="150">彩种</th>--%>
		                                   <%--<th width="150">奖金组</th>--%>
		                                   <%--<th width="350">返点范围</th>--%>
		                                   <%--<th width="150">高奖金</th>--%>
		                                   <%--<th width="350">高返点</th>--%>
		                               <%--</tr>--%>
		                               <%--</thead>--%>
		                               <%--<tbody id="lottsInfo">--%>
		                               	<%--<c:forEach items="${showLotts }" var="a">--%>
		                               	<%--<c:set var="lottRatio" value="0"/>--%>
		                               	<%--<c:choose>--%>
		                               		<%--<c:when test="${a.key == '3d' }"><c:set var="lottRatio" value="1.6"/></c:when>--%>
		                               		<%--<c:when test="${a.key == 'pl3' }"><c:set var="lottRatio" value="1.6"/></c:when>--%>
		                               		<%--<c:when test="${a.key == 'pk10' }"><c:set var="lottRatio" value="0.3"/></c:when>--%>
		                               		<%--<c:when test="${a.key == 'k3' }"><c:set var="lottRatio" value="0.2"/></c:when>--%>
		                               		<%--<c:otherwise><c:set var="lottRatio" value="0"/></c:otherwise>--%>
		                               	<%--</c:choose>--%>
		                               	<%--<c:set var="nowRatio" value="${maxRatio - lottRatio }"/>--%>
		                               	<%--<c:if test="${nowRatio <0 }">--%>
		                               	<%--<c:set var="nowRatio" value="0"/>--%>
		                               	<%--</c:if>--%>
										<%--<tr data-ratio="${lottRatio }" class="listDetail">--%>
											<%--<td>${a.value }</td>--%>
											<%--<td>${bonusGroup.title}</td>--%>
											<%--<td>0-${nowRatio}%</td>--%>
											<%--<td data-ratio="${bonusGroup.bonusRatio }">--%>
												<%--<span class="highBonusRebate"><fmt:formatNumber groupingUsed="false" value="${2000/100 * (bonusGroup.bonusRatio + nowRatio)}"/></span>--%>
											<%--</td>--%>
											<%--<td data-ratio="${2000/100* (bonusGroup.bonusRatio)}">--%>
												<%--<span class="highBonusRebate"><fmt:formatNumber groupingUsed="false" value="${2000/100 * (bonusGroup.bonusRatio)}"/>+${nowRatio}%</span>--%>
											<%--</td>--%>
										<%--</tr>--%>
									<%--</c:forEach>--%>
		                               <%--</tbody>--%>
		                           <%--</table>--%>
		                       <%--</div>--%>
		                   </div>
		                   
		                   <c:if test="${null != dailyRule }">
		                   <!-- 日薪管理 -->
		                   <div class="dailyBox" id="daily" style="display: none;">
		                   		<form class="tabBox">
			                   		<a href="javascript:void(0);" class="btn" id="dailyBtn1" data-id="#dailyData">日薪信息</a>
			                       	<a href="javascript:void(0);" class="btn active" id="dailyBtn2" data-id="#dailyAcc">日薪管理</a>
			                       	<a href="javascript:void(0);" class="btn" id="dailyBtn3" data-id="#dailyAdd">新增下级日薪</a>
			                       	<a href="javascript:void(0);" id="dailyManagerBack">《&nbsp;返回会员管理</a>
		                       	</form>
		                       	<div style="display: none;" id="dailyData">
		                       		<div class="personalDaily">
		                       			<h2>个人日薪信息</h2>
					                    <div class="tableBox">
					                    	<table class="dividendListTable">
					                           	<thead>
					                               <tr class="listHeader">
					                                    <th width="20%">用户名</th>
									                    <th width="20%">日薪比例（%）</th>
									                    <th width="10%">有效人数要求（个）</th>
									                    <th width="10%">亏损要求</th>
									                    <th width="20%">封顶金额</th>
									                    <th width="20%">开通时间</th>
					                               </tr>
					                             </thead>
					                             <tbody>
					                             	<tr>
					                             		<td>${dailyAcc.account }</td>
					                             		<td>${dailyAcc.rate }</td>
					                             		<td>${dailyAcc.validAccountCount == 0 ? "无要求" : dailyAcc.validAccountCount }</td>
					                             		<td>${dailyAcc.lossStatus == 0 ? "有" : "无" }</td>
					                             		<td>${dailyAcc.limitAmount <= 0 ? "不限制" : dailyAcc.limitAmount }</td>
					                             		<td><fmt:formatDate value="${dailyAcc.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					                             	</tr>
					                             </tbody>
					                          </table>
					                    </div>
		                       		</div>
				                    <div class="dailyDetailsBox">
				                    	<h2>日薪领取明细</h2>
			                       		<form class="searchBar" id="dailyDataForm">
					                        <label for="#">
												<span class="labelTitle">用户名：</span>
												<input type="text" class="labelCond userName" name="account">
					                        </label>
					                        <label for="#">
												<span class="labelTitle">结算日期：</span>
												<input type="text" class="labelCond Wdate" value="${startTime == null ? webDefaultBeginTime : startTime }" name="begin" onClick="WdatePicker({startDate: '%y-%M-%d',dateFmt:'yyyy-MM-dd'})"/>
						                        <span>~</span>
						                        <input type="text" class="labelCond Wdate" value="${endTime == null ? webDefaultEndTime : endTime }" name="end" onClick="WdatePicker({startDate: '%y-%M-%d',dateFmt:'yyyy-MM-dd'})" />
					                        </label>
					                        <a href="javascript:ajaxLoad('dailyDataTableBox','/daily/dailyDataTable', $('#dailyDataForm').serialize(), function() {hs.pagination.refleshPages($('#pageCountDailyData').val(), 'dailyDataForm', 'dailyDataSearch');});" class="btn" id="dailyDataSearch">查询</a>
					                    </form>
					                    <div class="tableBox" id="dailyDataTableBox">
					                    	<table class="dividendListTable">
					                           	<thead>
					                               <tr class="listHeader">
					                                    <th width="20%">用户名</th>
					                                    <th width="10%">日期</th>
									                    <th width="10%">有效人数（个）</th>
									                    <th width="10%">亏损情况</th>
									                    <th width="20%">投注金额</th>
									                    <th width="10%">日薪比例（%）</th>
									                    <th width="20%">日薪金额</th>
					                               </tr>
					                             </thead>
					                           </table>
					                           <!-- 没有消息 -->
					                           <div class="nullMsg" >
					                                <i class="nullMsgImg"></i>
													<span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
					                           </div>
					                    </div>
					                    <mx:pagerAjax formId="dailyDataForm" btnId="dailyDataSearch"></mx:pagerAjax>
			                       	</div>
		                       	</div>
		                       	<div style="display: none;" id="dailyAcc">
		                       		<h2>下级日薪信息</h2>
		                       		<form class="searchBar" id="dailyAccForm">
				                        <label for="#">
											<span class="labelTitle">用户名：</span>
											<input type="text" class="labelCond userName" name="account"><a href="javascript:;" onclick="ajaxLoad('dailyAccTableBox','/daily/dailyAccTable', $('#dailyAccForm').serialize(), function() {hs.pagination.refleshPages($('#pageCountDailyAcc').val(), 'dailyAccForm', 'dailyAccSearch');})" class="btn" id="dailyAccSearch">查询</a>
				                        </label>
				                    </form>
				                    <div class="tableBox" id="dailyAccTableBox">
				                    	<table class="dailyAccTable">
				                           	<thead>
				                               <tr class="listHeader">
				                                    <th width="15%">用户名</th>
									                <th width="10%">日薪比例（%）</th>
									                <th width="10%">启始金额</th>
									                <th width="10%">有效人数要求（个）</th>
									                <th width="10%">亏损要求</th>
									                <th width="10%">封顶金额</th>
									                <th width="20%">处理时间</th>
									                <th width="15%">操作</th>
				                               </tr>
				                             </thead>
				                           </table>
				                           <!-- 没有消息 -->
				                           <div class="nullMsg" >
				                                <i class="nullMsgImg"></i>
												<span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
				                           </div>
				                    </div>
				                    <mx:pagerAjax formId="dailyAccForm" btnId="dailyAccSearch"></mx:pagerAjax>
		                       	</div>
		                        <div style="display: none;" id="dailyAdd">
		                		   <h2>新增下级日薪</h2>
		                           <form class="" id="dailyForm">
		                               <div>
		                                   <span class="labelTitle">用户名：</span>
		                                   <input class="labelCond" id="dailyAccount" name="account" />
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <div>
		                                   <span class="labelTitle">日薪比例（%）：</span>
		                                   <select class="labelCond selectCond reservePoint" id="dailyRate" name="rate">
		                                   		<c:forEach begin="${dailyRule.minRate * 100 }" end="${dailyAcc.rate * 100 }" var="rate" step="1">
		                                   			<option value="<fmt:formatNumber minFractionDigits="2" value="${rate / 100 }"/>"><fmt:formatNumber minFractionDigits="2" value="${rate / 100 }"/></option>
		                                   		</c:forEach>
		                                   </select>
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <div>
		                                   <span class="labelTitle">启始金额：</span>
		                                   <input class="labelCond" id="dailyBetAmount" name="betAmount" onkeyup="checkMoney(this)" />（团队/个人投注销量高于该金额，才会拿到相应的日工资金额（销量*日薪比例））
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <c:if test="${dailyRule.validAccountCount == 0 }">
			                               <div>
			                                   <span class="labelTitle">有效人数要求：</span>
			                                   <input class="labelCond" id="validAccountCount" name="validAccountCount" onkeyup="inputNumber(this)" />（可分配范围 0 ~ 3000）
			                                   <span class="errorWarn"></span>
			                               </div>
		                               </c:if>
		                               <c:if test="${dailyRule.validAccountCount != 0 }">
		                               <input class="labelCond" id="validAccountCount" name="validAccountCount" type="hidden" value="0" />
		                               </c:if>
		                               <div>
		                                   <span class="labelTitle">封顶金额：</span>
		                                   <input class="labelCond" id="limitAmount" name="limitAmount" onkeyup="checkMoney(this)" />（如果不填写，则不限制封顶金额！）
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <c:if test="${dailyRule.lossStatus == 0 }">
		                               <div>
		                                   <span class="labelTitle">亏损要求：</span>
		                                   <label><input type="radio" name="lossStatus" value="0" />要求</label>&emsp;
		                                   <label><input type="radio" name="lossStatus" value="1" checked="checked" />不要求</label>
		                                   <span>（如要求亏损，则该用户团队处于亏损状态下，才有日薪！）</span>
		                               </div>
		                               </c:if>
		                               <c:if test="${dailyRule.lossStatus != 0 }">
		                               <input class="labelCond" name="lossStatus" type="hidden" value="1" />
		                               </c:if>
		                               <div class="btnBox">
		                                   <button class="btn" type="submit">确定</button>
		                               </div>
		                           </form>
		                       </div>
		                       <div style="display: none;" id="modifyDailyLayer">
		                           <form id="modifydailyForm">
		                           		<input type="hidden" class="labelCond" id="moDailyId" name="id" />
		                               <div>
		                                   <span class="labelTitle">用户名：</span>
		                                   <input class="labelCond" id="moDailyAccount" readonly="readonly" name="account" />
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <div>
		                                   <span class="labelTitle">日薪比例（%）：</span>
		                                   <select class="labelCond selectCond reservePoint" id="moDailyRate" name="rate">
		                                   		<c:forEach begin="${dailyRule.minRate * 100 }" end="${dailyAcc.rate * 100 }" var="rate" step="1">
		                                   			<option value="<fmt:formatNumber minFractionDigits="2" value="${rate / 100 }"/>"><fmt:formatNumber minFractionDigits="2" value="${rate / 100 }"/></option>
		                                   		</c:forEach>
		                                   </select>
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <div>
		                                   <span class="labelTitle">启始金额：</span>
		                                   <input class="labelCond" id="moDailyBetAmount" name="betAmount" onkeyup="checkMoney(this)" />（团队/个人投注销量高于该金额，才会拿到相应的日工资金额（销量*日薪比例））
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <c:if test="${dailyRule.validAccountCount == 0 }">
			                               <div>
			                                   <span class="labelTitle">有效人数要求：</span>
			                                   <input class="labelCond" id="moValidAccountCount" name="validAccountCount" onkeyup="inputNumber(this)" />（可分配范围 0 ~ 3000）
			                                   <span class="errorWarn"></span>
			                               </div>
		                               </c:if>
		                               <c:if test="${dailyRule.validAccountCount != 0 }">
		                               <input class="labelCond" id="moValidAccountCount" name="validAccountCount" type="hidden" value="0" />
		                               </c:if>
		                               <div>
		                                   <span class="labelTitle">封顶金额：</span>
		                                   <input class="labelCond" id="moLimitAmount" name="limitAmount" onkeyup="checkMoney(this)" />（如果不填写，则不限制封顶金额！）
		                                   <span class="errorWarn"></span>
		                               </div>
		                               <c:if test="${dailyRule.lossStatus == 0 }">
		                               <div>
		                                   <span class="labelTitle">亏损要求：</span>
		                                   <label><input type="radio" name="lossStatus" value="0" />要求</label>&emsp;
		                                   <label><input type="radio" name="lossStatus" value="1" />不要求</label>
		                                   <span>（如要求亏损，则该用户团队处于亏损状态下，才有日薪！）</span>
		                               </div>
		                               </c:if>
		                               <c:if test="${dailyRule.lossStatus != 0 }">
		                               <input class="labelCond" name="lossStatus" type="hidden" value="1" />
		                               </c:if>
		                               <div class="dialogBtn">
		                                   <button class="btn" id="submitModifyDaily">确定</button>
		                               </div>
		                           </form>
		                       </div>
		                   </div>
		                   </c:if>
		                   
		                   <!-- 下级分红-start -->
							<div class="teamInfoArea downBonusArea" id="lowerDividend" style="display: none;">
									<form class="searchBar" id="dividendSearchArgs">
				                        <label for="#">
											<span class="labelTitle">用户名：</span>
											<input type="text" class="labelCond userName" id="userName" name="account">
				                        </label>
										<label for="#"> 
					                        <span class="labelTitle">日期：</span>
											<select name="dividendDate" class="labelCond selectCond" style='width:190px'>
										        <c:forEach items="${dividendDateList}" var="a" varStatus="st">
													 <option value="${a}">${a}</option>
			                        	    	</c:forEach>
				                           	</select>
			                            </label>
				                        <label for="#"> 
					                        <span class="labelTitle">状态：</span>
											<select name="dividendStatus" class="labelCond selectCond">
				                                   <option value=" ">全部</option>
				                                   <option value="0">尚未发放</option>
				                                   <option value="1">发放完毕</option>
				                                   <option value="2">不需分红</option>
				                                   <option value="3">逾期未发放</option>
				                                   <option value="4">強制发放完毕</option>
				                           	</select>
			                           </label>
									   <a href="javascript:void(0);" class="btn" id="dividendSearch">查询</a>
				                    </form>
				                    <!-- 下级分红列表 -->
				                    <div class="tableBox" id="downBonusBox">
				                           <table class="dividendListTable">
				                           	<thead>
				                               <tr class="listHeader">
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
				                               <tbody id="dividendListHeader"></tbody>
				                           </table>
				                           <!-- 没有消息 -->
				                           <div class="nullMsg" id="dividendNullMsg">
				                                <i class="nullMsgImg"></i>
												<span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
				                           </div>
				                           <!-- 分页 -->
				                           <mx:pagerAjax formId="dividendSearchArgs" btnId="dividendSearch"></mx:pagerAjax>
		                            </div>			         
			           		</div>
			           		<!-- 下级分红-end -->
           		
		                   <!-- 团队概况 -->
		                   <div class="teamInfoArea" id="teamInfo" style="display: none;">
		                   		<!-- 查询条件 -->
		                       <form class="searchBar" id="teamInfoSearchForm">
		                            <label for="#">
										<span class="labelTitle">用户名：</span>
										<input type="text" class="labelCond userName" name="account" onkeyup="this.value=this.value.replace(/\W/g,'');" />
		                            </label>
				                    <label>
				                        <span class="labelTitle">时间：</span>
				                        <input type="text" class="labelCond Wdate" value="${startTime == null ? webDefaultBeginTime : startTime }" name="begin" onClick="WdatePicker({startDate: '%y-%M-%d',dateFmt:'yyyy-MM-dd'})"/>
				                        <span>(00:00:00)</span><span>~</span>
				                        <input type="text" class="labelCond Wdate" value="${endTime == null ? webDefaultEndTime : endTime }" name="end" onClick="WdatePicker({startDate: '%y-%M-%d',dateFmt:'yyyy-MM-dd'})" />
				                        <span>(23:59:59)</span>
				                    </label>
		                       		<input type="hidden" id="teamInfoStatus" name="status" value="0" />
		                       		<input type="hidden" id="teamInfoAccount" name="childAccount"/>
									<a href="javascript:void(0);" class="btn" id="teamInfoSearchSubmit">搜索</a>
		                       </form>
		                       <p class="userBreadCrumb" id="testing2"></p>
		                       <!-- 团队概况数据 -->
		                       <div class="tableBox" id="teamInfoListBox">
		                           <table class="teamInfoListTable">
		                           	<thead>
		                               <tr class="listHeader">
		                                   <th width="15%">用户名</th>
		                                   <th>团队人数</th>
		                                   <th>团队余额</th>
		                                   <th width="15%">充值总额</th>
		                                   <th width="15%">提现总额</th>
<!-- 		                                   <th>日工资</th> -->
		                                   <th>实际盈亏</th>
		                                   <th>新注册人数</th>
		                                   <th>首次充值人数</th>
		                                   <th>有效用户</th>
		                               </tr>
		                               </thead>
		                               <tbody id="teamInfoListTable"></tbody>
		                           </table>
		                           <!-- 没有消息 -->
		                           <div class="nullMsg" id="teamInfoNullMsg">
		                                <i class="nullMsgImg"></i>
										<span class="fontColorGray">没有符合条件的记录，请更改查询条件！</span>
		                           </div>
		                           <!-- 分页 -->
		                           <mx:pagerAjax formId="teamInfoSearchForm" btnId="teamInfoSearchSubmit"></mx:pagerAjax>
		                       </div>
		                   </div>
		                   <!-- 注册链接 -->
		                   <div class="linkOpenAcctArea" id="extCode" style="display: none;">
		                       <div class="linkOpenAcctBox">
		                       		<form class="linkOpenAcct" id="extCodeForm">
		                                <p class="headerTitle">新建链接开户入口</p>
		                                <div>
											<span class="labelTitle">用户类型：</span>
											<select class="labelCond selectCond" id="codeUserType" name="usertype">
			                                	<option value="0">会员</option>
			                                    <option value="1">代理</option>
			                               	</select>
		                                </div>
										<div>
											<span class="labelTitle">设置返点：</span>
											<select class="labelCond selectCond" id="codeRebateRatio" name="rebateratio"></select>
				                        </div>
										<div>
											<span class="labelTitle">有效期：</span>
											<select class="labelCond selectCond" name="validtime">
		                                         <option value="1">1天</option>
		                                         <option value="3">3天</option>
		                                         <option value="7">7天</option>
		                                         <option value="15">15天</option>
		                                         <option value="30">30天</option>
		                                         <option value="0">永久</option>
		                                 	</select>
		                                 </div>
										 <div>
											<span class="labelTitle">推广渠道：</span>
											<input type="text" class="labelCond" id="extAddress" name="extaddress" placeholder="如：QQ、论坛等" />
											<span class="errorWarn"></span>
		                                 </div>
										 <div>
											<span class="labelTitle">推广QQ：</span>
											<input type="text" class="labelCond" name="extQQ" onkeyup="inputNumber(this)" maxLength=50 />
											<span class="errorWarn"></span>
		                                 </div>
										 <div>
											<button class="btn" type="submit">生成链接</button>
		                                 </div>
		                             </form>
		                             <div class="openAcctExplainBox">
		                                 <div class="explain">
		                                     <p>您可以建立一系列不同返奖率的自主注册入口，然后将入口地址分发给您的用户。</p>
		                                     <p>用户访问这些入口地址即可自行注册在您的团队中，省去您手动逐个开户的麻烦。</p>
		                                     <p>
		                                         <span class="fontColorRed">注：</span>您还可在不同的宣传途径放置不同的入口，以统计哪些途径更为有效。
		                                     </p>
		                                 </div>
		                                 <p class="tips">*生成链接成功后，会在已建立的入口中显示</p>
		                             </div>
		                       </div>
		                       <div class="beforeOpenAcctBox">
		                           <p class="beforeTitle">已建立的开户入口</p>
		                           <table class="beforeAcctLinkTable" id="extCodeTable"></table>
		                       </div>
		                   </div>             			
               			</div>
               		</div>
               		</c:if>
				   <c:if test="${user.userType==1 }">
				   <!-- 代理中心 -->
				   <div class="userSafeArea bigCommonBox" id="agent" style="display: none;">
					   <div class="safeCenter">
							<div class="agentReport" id="agentReport" style="display: block;">
								<div class="safeCenterBox">
									<div class="newTab agentNewTab" id="timeType">
										<ul>
											<li class="active">今天</li>
											<li>昨天</li>
											<li>本月</li>
											<li>上月</li>
										</ul>
									</div>
									<div class="agentSech" id="agentSech">
										<input type="text" class="agentSechInp" placeholder="请输入账户名"><button class="agentSechBtn">搜索</button>
									</div>
									<div class="agentList" id="agentList">
										<ul>
											<li><em class="ajax1">￥0.00</em><span>投注金额</span></li>
											<li><em class="ajax2">￥0.00</em><span>中奖金额</span></li>
											<li><em class="ajax3">￥0.00</em><span>活动礼金</span></li>
											<li><em class="ajax4">￥0.00</em><span>团队返点</span></li>
											<li><em class="ajax5">￥0.00</em><span>团队盈利</span></li>
										</ul>
										<ul>
											<li><em class="ajax6">0人</em><span>投注人数</span></li>
											<li><em class="ajax7">0人</em><span>首充人数</span></li>
											<li><em class="ajax8">0人</em><span>注册人数</span></li>
											<li><em class="ajax9">0人</em><span>下级人数</span></li>
											<li><em class="ajax10">￥0.00</em><span>团队余额</span></li>
										</ul>
										<ul>
											<li><em class="ajax11">￥0.00</em><span>充值金额</span></li>
											<li><em class="ajax12">￥0.00</em><span>提现金额</span></li>
											<li><em class="ajax13">￥0.00</em><span>代理返点</span></li>
										</ul>
									</div>
								</div>
							</div>
					   </div>
				   </div>
				   </c:if>
				   <!-- 安全中心 -->
                   <div class="userSafeArea bigCommonBox" id="safe" style="display: none;">
                       <div class="safeCenterBox">
                           <div class="safeCenter">
                               <!-- 个人信息 -->
                               <div class="personInfo" id="personInfo" style="display: block;">
                                   <form id="setInformation">
                                       <div> 
	                                   	   <span class="labelTitle">昵称：</span> 
	                                   	   <input class="labelCond" value="${user.niceName}" name="niceName" />
	                                       <a href="javascript:void(0);" class="btn" onclick="setInformation()">修改</a>
                                       </div> 
                                       <div> 
                                       		<span class="labelTitle">预留信息：</span>
                                            <input class="labelCond" value="${user.message}" name="message" /> 
                                            <a href="javascript:void(0);" class="btn" onclick="setInformation()">修改</a>
                                       </div>
                                   </form>
                               </div>
                               <!-- 我的银行卡 -->
                               <div class="myCard" id="myCard">
                                   <!-- 首次绑定银行卡 -->
                                   <div class="firstBindCard" style="display: none;">
                                       <!-- <p>您还未绑定任何银行卡哟~</p>
                                       <a href="javascript:;" onclick="beginBind()" class="btn addBankCard"><span class="bindCardImg">+</span>添加银行卡</a> -->
                                  
                                        <p>
				                            <span class="fontColorGray">安全密码：</span>
				                            <input type="password" id="safePassword" onblur="checkSafePassword();" name="safePassword" class="labelCond" />
				                            <span id="errorSpan" class="fontColorRed"></span>
			                            </p>
			                            <label><input class="btn" type="button" value="提交" onClick="verifySafePassword()"/></label>
                                  
                                   </div>
                                   <!-- 绑定银行卡信息 -->
                                   <div class="bindCardInfo" style="display: none;">
                                        <h2 class="headerTitle">绑定银行卡</h2>
                                        <div class="userMsgBox">
                                        	<p class="userMsgTitle">使用提示：</p>
                                            <div class="bindMsgDetail">
                                                <p>
                                                    <span>1. 银行卡绑定成功后，平台任何有区域都</span>
                                                    <span class="fontColorRed">不会</span> <span>出现您的完整银行账号、开户姓名等信息。</span>
                                                </p>
                                                <p>
                                                    <span>2. 银行卡绑定成功后，相关信息将无法查看与修改，只能进行</span>
                                                    <span class="fontColorRed">解除绑定</span> <span>操作，请仔细填写下表。</span>
                                                </p>
                                                <p>
                                                    <span>3. 每个游戏账户最多绑定</span> <span class="fontColorRed">5</span>
                                                    <span>张银行卡，您已经成功绑定</span> <span class="fontColorRed" id="hasCardNum">0</span>
                                                    <span>张。</span>
                                                </p>
                                                <p>
                                                    <span>4. 一个账户只能绑定同一个开户人姓名的银行卡。</span>
                                                </p>
                                            </div>
                                        </div>
                                        <form class="newBindCard" id="binCardForm">
                                            <div>
                                                <span class="labelTitle">开户银行：</span>
                                                <select class="labelCond selectCond" name="bankNameId" id="selectBank"></select>
                                            </div>
                                            <div>
                                                <span class="labelTitle">开户银行区域：</span>
                                                <select class="labelCond selectCond" id="selectProvince" onchange="selectProvince"></select>
                                                <select class="labelCond selectCond" id="selectCity">
                                                     <option value="0">请选择城市</option>
                                                </select>
                                            </div>
                                            <div>
                                                <span class="labelTitle">支行名称：</span>
                                                <input type="text" class="labelCond" maxLength=50 name="address" id="address" />
                                                <span class="fontGrey">由1至20个字符或汉字组成，不能使用特殊字符</span>
                                            </div>
                                            <div>
                                                <span class="labelTitle">开户人姓名：</span>
                                                <input type="text" class="labelCond" maxLength=30 name="niceName" id="niceName" />
                                                <span class="fontGrey">由1至20个字符或汉字组成，不能使用特殊字符</span>
                                            </div>
                                            <div>
                                                <span class="labelTitle">银行账号：</span>
                                                <input type="text" class="labelCond" maxLength=19 id="card1" onkeyup="inputNumber(this)" />
                                                <span class="fontGrey">银行卡卡号由16位或19位数字组成</span>
                                            </div>
                                            <div>
                                                <span class="labelTitle">确认银行账号：</span>
                                                <input type="text" class="labelCond" name="card" id="card" maxLength=19 onkeyup="inputNumber(this)" onpaste="return false;" />
                                                <span class="fontGrey">银行账号只能手动输入，不能粘贴</span>
                                            </div>
                                            <div>
                                                <span class="labelTitle"></span>
                                                <a href="javascript:;" class="btn nextBtn" onclick="BindCardconfirm()">下一步</a>
                                            </div>
                                        </form>
                                   </div>
                                   <!-- 确认绑卡信息-->
                                   <div class="confirmCardInfo" style="display: none;">
                                       <div class="tableBox">
                                           <h2 class="headerTitle">绑定银行卡-信息确认</h2>
                                           <table class="confirmCardInfoTable" id="confirmCardInfoTable">
                                               <tr class="listDetail">
                                                   <td width="300">绑定银行：</td>
                                                   <td></td>
                                               </tr>
                                               <tr class="listDetail">
                                                   <td>开户银行省份：</td>
                                                   <td></td>
                                               </tr>
                                               <tr class="listDetail">
                                                   <td>开户银行城市：</td>
                                                   <td></td>
                                               </tr>
                                               <tr class="listDetail">
                                                   <td>支行名称：</td>
                                                   <td></td>
                                               </tr>
                                               <tr class="listDetail">
                                                   <td>开户人姓名：</td>
                                                   <td></td>
                                               </tr>
                                               <tr class="listDetail">
                                                   <td>银行账号：</td>
                                                   <td></td>
                                               </tr>
                                           </table>
                                           <div class="cfrBindCardBtn">
                                               <a href="javascript:;" class="btn btnSubmit" onclick="doBindCard()">提交</a>
                                               <a href="javascript:;" class="btn btnReturn" onclick="javascript:$('#myCard>div').hide();$('.bindCardInfo').show();" >返回</a>
                                           </div>
                                       </div>
                                   </div>
                                   <!-- 我的银行卡信息 -->
                                   <div class="tableBox myCardInfo" style="display: none;">
                                       <table class="myCardTable" id="myCardTable">
                                       	<thead>
                                           <tr class="listHeader">
                                               <th>开户名</th>
                                               <th>开户行</th>
                                               <th>卡号</th>
                                               <th>绑定时间</th>
                                               <th>状态</th>
                                           </tr></thead>
                                           <tbody id="myCardBody"></tbody>
                                       </table>
                                       <div class="myCardBtn">
                                            <a href="javascript:void(0)" id="addBind" onclick="addBindCard()" class="btn bindCard">增加绑定</a>
											<a href="javascript:;" class="btn lockCard" onclick="doLockCard(this)">锁定银行卡</a>
                                       </div>
                                       <div class="userMsg fontColorGray">
                                           <p>
                                               <span>1.一个账户最多只能绑定</span> <span class="fontColorRed">5</span>
                                               <span>张银行</span>
                                           </p>
                                           <p>
                                               <span>2.</span> <span class="fontColorRed">银行卡锁定</span> <span>可以增强账户安全，推荐锁定（如：账户被他人盗用后，由于锁定的限制，您账户的资金不会被他人提款）；</span>
                                           </p>
                                           <p>
                                               <span>3. 银行卡锁定后，</span> <span class="fontColorRed">不能增加新银行卡绑定，自身不能解绑和解锁银行卡，需联系您的上级申请解锁。</span>
                                           </p>
                                       </div>
                                   </div>
                                   <!-- 历史银行卡验证 -->
                                   <div class="historyCardVerify" style="display: none;">
                                       <h2 class="headerTitle">增加绑定-历史银行卡验证</h2>
                                       <form class="verifyCard" id="verifyCard">
                                           <div>
                                           		<span class="labelTitle">选择验证银行卡：</span>
                                                <select class="labelCond selectCond" name="id" id="selectHistoryCard"></select>
                                           </div>
                                           <div>
                                           		<span class="labelTitle">开户人姓名：</span>
                                                <input type="text" class="labelCond" name="oldNiceName" />
                                                <span class="fontColorGray">请输入旧的银行卡开户人姓名</span>
                                           </div>
                                           <div>
                                           		<span class="labelTitle">银行账号：</span>
                                                <input type="text" class="labelCond" name="oldCard" maxLength=19 onkeyup="inputNumber(this)" />
                                                <span class="fontColorGray">请输入旧的银行卡号</span>
                                           </div>
                                           <div>
                                           		<span class="labelTitle">安全密码：</span>
                                                <input type="password" class="labelCond" id="safeWord" name="safeWord" />
                                                <span class="fontColorGray">请输入您的安全密码</span>
                                           </div>
                                           <div>
                                           		<span class="labelTitle"></span>
                                           		<a href="javascript:;" class="btn nextBtn" onclick="checkOldCard()" readonly>下一步</a>
                                           		<a href="javascript:;" class="btn btnReturn" onclick="$('#myCard>div').hide();$('.myCardInfo').show();" readonly>返回</a>
                                           </div>
                                       </form>
                                   </div>
                               </div>
                               <!-- 修改登录密码 -->
                               <form class="modLoginPwd" id="modLoginPwd" style="display: none;">
                                   <div>
	                                   <span class="labelTitle">旧登录密码：</span>
	                                   <input type="password" class="labelCond" name="oldpass" id="oldLoginPass" placeholder="8-16位" />
	                                   <span class="errorWarn"></span>
                                   </div>
                                   <div>
                                   		<span class="labelTitle">新登录密码：</span>
                                        <input type="password" class="labelCond" id="newLoginPass" placeholder="8-16位" />
                                        <span class="errorWarn"></span>
                                   </div>
                                   <div>
                                   		<span class="labelTitle">确认新登录密码：</span>
                                        <input type="password" class="labelCond" name="newpass" id="confirmLoginPass" placeholder="8-16位" />
                                        <span class="errorWarn"></span>
                                   </div>
                                   <div>
										<span class="labelTitle"></span>
										<a href="javascript:;" class="btn" id="changePassWord">修改</a>
                                   </div>
                               </form>
                               <!-- 修改安全密码 -->
                               <form class="modAccountPwd" id="modAccountPwd" style="display: none;">
                                   <div>
                                   		<span class="labelTitle">旧安全密码：</span>
                                   		<input type="password" class="labelCond" name="oldpass" id="oldSafeWord" placeholder="旧的安全密码" />
                                        <span class="errorWarn"></span>
                                   </div>
                                   <div>
                                   		<span class="labelTitle">新安全密码：</span>
                                        <input type="password" class="labelCond" id="newSafePass" placeholder="6位数,全数字" />
                                        <span class="errorWarn"></span>
                                   </div>
                                   <div>
                                   		<span class="labelTitle">确认新安全密码：</span>
                                        <input type="password" class="labelCond" name="newpass" id="confirmSafePass" placeholder="6位数,全数字" />
                                       	<span class="errorWarn"></span>
                                   </div>
                                   <div>
	                                    <span class="labelTitle"></span>
	                                    <a href="javascript:;" class="btn" id="changeSafeWord">修改</a>
                                   </div>
                               </form>
                               <!-- 安全问题 -->
                               <div class="safeQA" id="safeQA" style="display: none;">
                                   <form class="safeQAForm" id="safeQAForm">
                                       <div>
	                                       <span class="labelTitle"><strong class="fontColorRed">*</strong>问题一：</span>
	                                       <select class="labelCond selectCond" name="qsType1">
	                                               <option value="1">您的出生地是哪里?</option>
	                                               <option value="2">您高中的学校是?</option>
	                                               <option value="3">您父亲的名字是?</option>
	                                               <option value="4">您母亲的名字是?</option>
	                                               <option value="5">您的兴趣爱好是什么?</option>
	                                               <option value="6">您配偶的名字是?</option>
	                                               <option value="7">您大学学校的名字是?</option>
	                                               <option value="8">您的家乡是?</option>
	                                       </select>
                                       </div>
                                       <div>
	                                       <span class="labelTitle">&nbsp;&nbsp;答案：</span>
	                                       <input class="labelCond" name="answer1" />
	                                       <span class="errorWarn">（长度最多为25个字符，并只支持英文和数字）</span>
                                       </div>
                                       <div>
	                                       <span class="labelTitle"> <strong class="fontColorRed">*</strong>问题二：</span>
	                                       <select class="labelCond selectCond" name="qsType2">
	                                               <option value="2">您高中的学校是?</option>
	                                               <option value="1">您的出生地是哪里?</option>
	                                               <option value="3">您父亲的名字是?</option>
	                                               <option value="4">您母亲的名字是?</option>
	                                               <option value="5">您的兴趣爱好是什么?</option>
	                                               <option value="6">您配偶的名字是?</option>
	                                               <option value="7">您大学学校的名字是?</option>
	                                               <option value="8">您的家乡是?</option>
	                                       </select>
                                       </div>
                                       <div>
	                                       <span class="labelTitle">&nbsp;&nbsp;答案：</span>
	                                       <input class="labelCond" name="answer2" />
	                                       <span class="errorWarn">（长度最多为25个字符，并只支持英文和数字）</span>
                                       </div>
                                       <div>
	                                       <span class="labelTitle"></span>
	                                       <a href="javascript:;" class="btn safeQASubmit" id="safeQASubmit">保存</a>
                                       </div>
                                   </form>
                                   <div class="userSafeMsg">
                                       <p><span class="fontColorRed">温馨提示：</span>密保是由用户选定2个问题对应答案组成，如果您的账号安全密码忘记，您可以直接通过验证密保问题和答案，重新设置新密码。</p>
                                   </div>
                               </div>
							   <!-- 找回安全密码 -->
                               <div class="safeQA" id="findSafeByQa" style="display: none;">
                                   <form class="safeQAForm" id="findSafeQa">
                                       <div>
	                                       <span class="labelTitle"><strong class="fontColorRed">*</strong>问题一：</span>
	                                       <select class="labelCond selectCond" name="qsType1">
	                                               <option value="1">您的出生地是哪里?</option>
	                                               <option value="2">您高中的学校是?</option>
	                                               <option value="3">您父亲的名字是?</option>
	                                               <option value="4">您母亲的名字是?</option>
	                                               <option value="5">您的兴趣爱好是什么?</option>
	                                               <option value="6">您配偶的名字是?</option>
	                                               <option value="7">您大学学校的名字是?</option>
	                                               <option value="8">您的家乡是?</option>
	                                       </select>
                                       </div>
                                       <div>
                                       		<span class="labelTitle">&nbsp;&nbsp;答案：</span>
                                       		<input class="labelCond" name="answer1" />
                                            <span class="errorWarn">（长度最多为25个字符，并只支持英文和数字）</span>
                                       </div>
                                       <div>
											<span class="labelTitle"><strong class="fontColorRed">*</strong>问题二：</span>
											<select class="labelCond selectCond" name="qsType2">
                                               <option value="2">您高中的学校是?</option>
                                               <option value="1">您的出生地是哪里?</option>
                                               <option value="3">您父亲的名字是?</option>
                                               <option value="4">您母亲的名字是?</option>
                                               <option value="5">您的兴趣爱好是什么?</option>
                                               <option value="6">您配偶的名字是?</option>
                                               <option value="7">您大学学校的名字是?</option>
                                               <option value="8">您的家乡是?</option>
                                       		</select>
                                       </div>
                                       <div>
                                       		<span class="labelTitle">&nbsp;&nbsp;答案：</span>
                                       		<input class="labelCond" name="answer2" />
                                       		<span class="errorWarn">（长度最多为25个字符，并只支持英文和数字）</span>
                                       </div>
                                       <div>
                                       		<span class="labelTitle">&nbsp;&nbsp;新安全密码：</span>
                                            <input type="password" class="labelCond" name="safePassWord" placeholder="6位数字" />
                                            <span class="errorWarn"></span>
                                       </div>
                                       <div>
                                       		<span class="labelTitle"></span>
                                       		<a href="javascript:;" class="btn safeQASubmit" id="dofindSafeQA">保存</a>
                                       </div>
                                   </form>
                                   <div class="userSafeMsg">
                                       <p><span class="fontColorRed">温馨提示：</span>密保是由用户选定2个问题对应答案组成，如果您的账号安全密码忘记，您可以直接通过验证密保问题和答案，重新设置新密码。</p>
                                   </div>
                               </div>
						</div>
					</div>
				</div>
                <!-- 消息管理 -->
                <div class="msgManageArea bigCommonBox" id="msg" style="display: none;">
                    <p class="contactBOSSBtn">
                        <c:if test="${user.rootAccount != user.account }">
                            <a href="javascript:;" onclick="contact('up');" class="contactBOSS">联系上级</a>
                        </c:if>
                        <c:if test="${user.userType == 1 }">
                        <a href="javascript:;" onclick="contact('dw');" class="contactBOSS">联系下级</a>
                        </c:if>
                    </p>
                    <form class="searchBar userListSearch" id="messageForm" style="display: none;">
                        <input type="button" class="btn" id="messageSearch" onclick="javascript:ajaxLoad('msgTableBox','/message/messageTable', $('#messageForm').serialize(), function() {hs.pagination.refleshPages($('#pageCountMessage').val(), 'messageForm', 'messageSearch');});" value="搜索" />
                    </form>
                    <div class="tableBox" id="msgTableBox"></div>
                    <mx:pagerAjax formId="messageForm" btnId="messageSearch"></mx:pagerAjax>
                </div>
            </div>		
		</div>
	</div>
	<i class="shadow"></i>
	<i class="shadow2"></i>
	<i class="shadow3"></i>
</div>

	<!-- 快速返点设置弹框 -->
	<div id="setQueryRebateArea" style="display: none;">
		<table class="setRebateTable-t">
			<tr class="listDetail">
				<td width="100">用户信息</td>
				<td>
					<label><span>用户名：</span> <span id="rebatePerson"></span></label>
					<label><span>昵称：</span> <span id="rebateNickName"></span></label>
					<label><span>自身返点：</span> <span id="selfRebateRatio"></span></label>
				</td>
			</tr>
			<tr class="listDetail">
				<td height="60">调整返点(向上)</td>
				<td>
					<label for="#"> 
						<span>设置为：</span>
						<input type="text" class="selfRebateTotal" id="UserQuota" readonly="true" /> <span>%</span>
					</label>
				</td>
			</tr>
		</table>
		<div class="dialogBtn">
			<a href="javascript:void(0);" class="btn" onclick="doAdjustQuota()">完成</a>
		</div>
	</div>
	
	<!-- 配额分配弹框 -->
	<div id="quotaDistArea" style="display: none;">
		<table id="quotaDistTable">
			<tr class="listDetail">
				<td colspan="2" class="quotaIntro">
					<ul class="quotaNum">
						<li>
							<span>用户名：</span> <span class="fontColorRed" id="quotaUserName"></span>
							<span>返点：</span> <span class="fontColorRed" id="myRebateRatio"></span>
						</li>
					</ul>
				</td>
			</tr>
		</table>
		<p id="lastOne" class="errorWarn"></p>
		<div class="dialogBtn">
			<a href="javascript:void(0);" class="btn" onclick="doSetUserQuota(this)">提交</a>
		</div>
	</div>
	
	<!-- 添加备注弹框 -->
	<div id="remarkDiv" style="display: none">
        <div>
            <span class="labelTitle">用户:</span>
            <input type="text" class="labelCond" readonly="readonly" id="remarkAccount" />
        </div>
        <div class="msgSendContentDiv">
            <span class="labelTitle">备注:</span>
            <textarea class="msgSendContent" id="homeRemark" rows="3" maxlength="20"></textarea>
        </div>
        <div class="dialogBtn">
            <a href="javascript:;" onclick="updateRemark();" class="btn">提交</a>
        </div>
	</div>

	<!-- 下级充值弹框 -->
	<div id="accountRegLower" style="display: none;">
		<form action="" id="rechargeLowerForm" method="post" onsubmit="return checkForm();">
			<div id="changeBankArea" class="changeBankArea">
				<input type="hidden" id="rechargeType" name="rechargeType" value="2" />
				<div class="changeSumBox">
					<label>
						<span class="labelTitle">目标用户：</span>
						<input type="text" class="labelCond" id="targetUser" name="targetUser" readonly="true" />
					</label>
				</div>
				<div class="changeSumBox">
					<label>
						<span class="labelTitle">充值金额：</span>
						<input type="text" class="labelCond" id="chargeamount" name="chargeamount" onkeyup="checkMoney(this)" placeholder="请输入充值金额" />
					</label>
				</div>
				<div class="changeSumBox">
					<label>
						<span class="labelTitle">安全密码：</span>
						<input type="password" class="labelCond" id="sourceUserSafePassword" name="sourceUserSafePassword" maxLength=16 placeholder="请输入8-16位安全密码" />
					</label>
				</div>
				<div class="changeSumBox">
					<label>
						<span class="labelTitle">充值形式：</span>
						<select name="rechargeStyle" id="rechargeStyle" data-id='rechargeStyle0' class="labelCond selectCond">
	                        <option value="0" data-id='rechargeType0'>帐变形式（点击选择）</option>
	                        <option value="1" data-id='rechargeType1'>工资（影响盈亏）</option>
	                        <option value="2" data-id='rechargeType2'>团队活动（影响盈亏）</option>
	                        <option value="3" data-id='rechargeType3'>周期分红（无影响）</option>
	                        <option value="4" data-id='rechargeType4'>下级充值（无影响）</option>
                        </select>
                     </label>
                </div>
			</div>
			<div class="purposeArea">
				<div id="rechargeStyle0">
					<p class="titleBold"><span class="labelTitle">注意事项：</span>转点功能是您给下级进行转账的操作，请严格阅读注意事项！！</p>
					<p><span class="labelTitle titleBold">工资：</span>该帐变为等同于发放工资给下级，会影响盈亏报表金额；</p>
					<p><span class="labelTitle titleBold">团队活动：</span>发放活动金额给下级，会影响盈亏报表金额；</p>
					<p><span class="labelTitle titleBold">周期分红：</span>发送分红给下级，不影响盈亏报表金额；</p>
					<p><span class="labelTitle titleBold">下级充值：</span>帮下级充值，下级会增加相对应充值金额的所需出款消费量。</p>
				</div>
				<div id="rechargeStyle1">
					<p><span class="labelTitle titleBold">工资：</span>该帐变为等同于发放工资给下级，会影响盈亏报表金额。</p>
				</div>
				<div id="rechargeStyle2">
					<p><span class="labelTitle titleBold">团队活动：</span>发放活动金额给下级，会影响盈亏报表金额。</p>
				</div>
				<div id="rechargeStyle3">
					<p><span class="labelTitle titleBold">周期分红：</span>发送分红给下级，不影响盈亏报表金额。</p>
				</div>
				<div id="rechargeStyle4">
					<p><span class="labelTitle titleBold">下级充值：</span>帮下级充值，下级会增加相对应充值金额的所需出款消费量。</p>
					<label><span class="labelTitle titleBold">备注用途：</span></label>
					<ul class="purposeRadioList">
						<li><label><input type="radio" name="remark" value="上级代充"/>上级代充</label></li>
						<li><label><input type="radio" name="remark" value="借款"/>借款</label></li>
						<li><label><input type="radio" name="remark" value="红包"/>红包</label></li>
						<li class="last"><label><input type="radio" checked="checked" name="remark" value="0"/>其他原因</label><input type="text" class="labelCond" id="remarkOtherReasion"  maxLength=5 placeholder="不得超过5个字"  /></li>
					</ul>
				</div>
			</div>
		</form>
		<div class="dialogBtn">
			<a href="javascript:void(0);" class="btn" id="accountRegLowerBtn" onclick="doDownRecharge()">提交</a>
			<span class="errorWarn" id="accountRegLowerTip"></span>
		</div>
	</div>
	
	<!-- 发送站内信弹框 -->
    <div id="messageUserContactDiv" class="msgBox" style="display: none">
        <div>
           <span class="labelTitle">接收人:</span>
           <input type="text" class="labelCond" readonly="readonly" id="messageUserRever" />
        </div>
        <div>
            <span class="labelTitle">标题:</span>
            <input type="text" class="labelCond msgSendTitle" id="messageUserTitle" />
        </div>
        <div class="msgSendContentDiv">
            <span class="labelTitle">发送内容:</span>
            <textarea class="msgSendContent" id="messageUserSendContent" rows="5"></textarea>
		</div>
		<p class="errorWarn msgErrorWarn" id="msgUserErrorWarn"></p>
        <div class="dialogBtn">
            <a href="javascript:;" onclick="sendmessageUser();" class="btn">发送</a>
        </div>
   </div>
   
    <!-- 联系上下级弹框 -->
	<div id="messageContactDiv" class="msgBox" style="display: none">
		<div>
	     	<span class="labelTitle">接收人:</span>
	     	<input type="text" class="labelCond" readonly="readonly" id="messageRever" />
	     	<a id="messageReverCheck" href="javascript:;" onclick="messageReverCheck();" class="checkBtn">检测</a>
	    	<span class="errorWarn" id="messageReverCheckSpan"></span>
		</div>
	    <div>
	       <span class="labelTitle">标题:</span>
	       <input type="text" class="labelCond msgSendTitle" id="messageTitle" />
	    </div>
	    <div class="msgSendContentDiv">
	       <span class="labelTitle">发送内容:</span>
	       <textarea class="msgSendContent" id="messageSendContent" rows="5"></textarea>
	    </div>
		<p class="errorWarn msgErrorWarn" id="msgErrorWarn"></p>
		<div class="dialogBtn">
			<a href="javascript:;" onclick="sendMessage();" class="btn">发送</a>
		</div>
	</div>  
   
   	<!-- 站内信详情弹框 -->
	<div id="messageDetailDiv" style="display: none"></div>

	<!-- 设置安全密码弹框 -->
	<div id="setSafePassword" style="display: none;">
		<div class="improveHeader">
			<div class="improveLogoBox">
				<i class="improveLogo"></i>
			</div>
			<div class="improveHeaderInfo">
				<p class="improveHeaderTitle">
					<span>尊敬的</span> <span>${user.account }</span> <span>,请设置您的安全密码，为您账户的资金保驾护航</span>
				</p>
				<span class="improveHeaderTips">当涉及到“充值”、“提款”、“银行卡绑定”的操作时，需要安全密码验证。以确定是您本人进行操作。</span>
			</div>
			<a href="javascript:void(0);" class="closeDialog"></a>
		</div>
		<div class="improveContent">
			<form id="improveInfoForm" class="improveInfoForm" onsubmit="return false;">
				<div class="labelDiv">
					<span class="labelTitle"><strong class="fontColorRed">*</strong>安全密码</span>
					<input id="safePassWord" name="safePassWord" type="password" class="labelCond" placeholder="不能与登录密码重复,6位数字" />
					<span id="safePassWordMsg" class="labelMsg fontColorRed"></span>
				</div>
				<div class="labelDiv">
					<span class="labelTitle"><strong class="fontColorRed">*</strong>确认安全密码</span>
					<input id="safePassWordCheck" type="password" class="labelCond" placeholder="确认安全密码，需一致" />
					<span id="safePassWordCheckMsg" class="labelMsg fontColorRed"></span>
				</div>
				<div class="labelDiv">
					<span class="labelTitle"><strong class="fontColorRed">*</strong>设置安全信息</span>
					<select id="qsType1" name="qsType1" class="selectCond">
						<option value="1">您的出生地是哪里?</option>
						<option value="2">您高中的学校是?</option>
						<option value="3">您父亲的名字是?</option>
						<option value="4">您母亲的名字是?</option>
						<option value="5">您的兴趣爱好是什么?</option>
						<option value="6">您配偶的名字是?</option>
						<option value="7">您大学学校的名字是?</option>
						<option value="8">您的家乡是?</option>
					</select>
					<span id="qsType1Msg" class="labelMsg">找回安全密码的重要凭证！</span>
				</div>
				<div class="labelDiv">
					<span class="labelTitle"><strong class="fontColorRed">*</strong>答案</span>
					<input id="answer1" name="answer1" maxlength="50" type="text" class="labelCond" />
					<span id="answer1Msg" class="labelMsg fontColorRed"></span>
				</div>
				<div class="labelDiv">
					<span class="labelTitle"><strong class="fontColorRed">*</strong>设置安全信息</span>
					<select id="qsType2" name="qsType2" class="selectCond">
						<option value="2">您高中的学校是?</option>
						<option value="1">您的出生地是哪里?</option>
						<option value="3">您父亲的名字是?</option>
						<option value="4">您母亲的名字是?</option>
						<option value="5">您的兴趣爱好是什么?</option>
						<option value="6">您配偶的名字是?</option>
						<option value="7">您大学学校的名字是?</option>
						<option value="8">您的家乡是?</option>
					</select>
					<span id="qsType2Msg" class="labelMsg">找回安全密码的重要凭证！</span>
				</div>
				<div class="labelDiv">
					<span class="labelTitle"><strong class="fontColorRed">*</strong>答案</span>
					<input id="answer2" name="answer2" maxlength="50" type="text" class="labelCond" />
					<span id="answer2Msg" class="labelMsg fontColorRed"></span>
				</div>
				<div class="formBtns">
					<span class="labelTitle"></span>
					<input type="submit" class="btn" value="提交" onclick="setSafePassword();" />
				</div>
			</form>
		</div>
	</div>

</body>
</html>
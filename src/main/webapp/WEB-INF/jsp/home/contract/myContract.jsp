<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.sjc168.com/fn" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的契约</title>
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-accountCentre.css?ver=${VIEW_VERSION}'/>" />
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<script src="<c:url value='/res/home/js/contract/myContract.js?ver=${VIEW_VERSION}'/>"></script>
</head>

<body>
<div class="areaBigContainer mainWidth" id="myContractArea">
	<div class="containerFlex">
		<!-- 列表 -->
		<div class="leftListArea">
			<c:if test="${user.userType==1 }">
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail teamManageA" data-id="#teamManageArea"><i></i>团队管理</a>
				<div class="leftListItem" style="display:none;">
					<a class="nav active" href="<c:url value='/user/index?tabId=financeList'/>">会员管理</a>
					<a class="nav" href="<c:url value='/user/index?tabId=addUser'/>">新增会员</a>
					<a class="nav" href="<c:url value='/user/index?tabId=lowerDividend'/>">下级分红</a>
					<a class="nav" href="<c:url value='/user/index?tabId=teamInfo'/>">团队概况</a>
					<a class="nav" href="<c:url value='/user/index?tabId=extCode'/>">注册链接</a>
				</div>
			</div>
			</c:if>
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail userCenterA" data-id="#safe"><i></i>个人中心</a>
				<div class="leftListItem">
					<a class="nav" href="<c:url value='/user/index?tabId=personInfo'/>">个人信息</a>
					<a class="nav" href="<c:url value='/user/index?tabId=myCard'/>">我的银行卡</a>
					<a class="nav" href="<c:url value='/user/index?tabId=modLoginPwd'/>">修改登录密码</a>
					<a class="nav" href="<c:url value='/user/index?tabId=modAccountPwd'/>">修改安全密码</a>
					<a class="nav" href="<c:url value='/user/index?tabId=safeQA'/>">安全问答</a>
					<a class="nav" href="<c:url value='/user/index?tabId=findSafeByQa'/>">找回安全密码</a>
					<a class="nav active" href="javascript:;">我的契约</a>
				</div>
			</div>
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail msgA" href="<c:url value='/user/index?tabId=msg'/>"><i></i>消息管理</a>
			</div>
		</div>	
		<!-- 内容 -->
		<div class="rightArea">
             <div class="accountCentreContent">
                <dl class="contractTip">
                    <dt><i></i>温馨提示：</dt>
                    <dd>大順崇尚契约精神，与上级签订契约分红，将完美保障您的业绩分红！<br/>提示：签约前请详读大順<a href="javascript:;" id="showContractDisclaimer">契约免责声明</a></dd>
                </dl>
                <c:import url="contractDisclaimer.jsp"></c:import>
                <!--契约分红规则-->
                <c:if test="${contractRuleModel.contractRuleList !=null }">
                <div class="contractRule">
                    <h3>当前彩票契约分红<span class="time">契约签订时间为：<fmt:formatDate value="${contractRuleModel.contractTime }" type="both" pattern="yyyy-MM-dd"/></span></h3>
                    <ul>
	                    <c:forEach items="${contractRuleModel.contractRuleList }" var="a" varStatus="st">
						  <c:choose>
	                          <c:when test="${st.index==0 }">
							      <li>
		                            <span class="contractRuleTitle">契约规则一</span>
		                            <span class="contractRuleDetail">
		                            <c:choose>
		                               <c:when test="${contractConfig.bonusCycle==0 }">
		                                                                                     半月保底分紅 
		                               </c:when>
		                               <c:otherwise>
		                                                                                     一月保底分紅 
		                               </c:otherwise>
		                            </c:choose>
		                            <strong>${a.gtdBonuses }%</strong></span>
		                          </li>
						      </c:when>
						      <c:otherwise>
						           <li>
	                                 <span class="contractRuleTitle">
	                                <c:choose>
		                               <c:when test="${st.index==1 }">
		                                                                                     契约规则二
		                               </c:when>
		                               <c:when test="${st.index==2 }">
		                                                                                     契约规则三
		                               </c:when>
		                               <c:when test="${st.index==3 }">
		                                                                                     契约规则四
		                               </c:when>
		                               <c:when test="${st.index==4 }">
		                                                                                     契约规则五
		                               </c:when>
		                               <c:when test="${st.index==5 }">
		                                                                                     契约规则六
		                               </c:when>
		                               <c:when test="${st.index==6 }">
		                                                                                     契约规则七
		                               </c:when>
		                               <c:when test="${st.index==7 }">
		                                                                                     契约规则八
		                               </c:when>
		                               <c:when test="${st.index==8 }">
		                                                                                     契约规则九
		                               </c:when>
		                               <c:when test="${st.index==9 }">
		                                                                                     契约规则十
		                               </c:when>
		                            </c:choose> 
	                                 </span>
	                                 <span class="contractRuleDetail">
	                                 <c:choose>
		                               <c:when test="${contractConfig.bonusCycle==0 }">
		                                                                                    半月累积销量 
		                               </c:when>
		                               <c:otherwise>
		                                                                                     一月累积销量  
		                               </c:otherwise>
		                            </c:choose>
		                            <strong>${a.cumulativeSales }</strong> 元，投注人数${a.humenNum }，分紅 <strong>${a.dividend }%</strong></span>
	                               </li>
						      </c:otherwise>
						   </c:choose>
						</c:forEach>
					</ul>
                </div>
                </c:if>
                <c:if test="${newContractRuleModel.contractRuleList!=null }">
                <div class="contractRule">
                    <h3>新立彩票契约分红<span class="time">契约签订时间为：<fmt:formatDate value="${newContractRuleModel.contractTime }" type="both" pattern="yyyy-MM-dd"/></span></h3>
					<ul>
	                    <c:forEach items="${newContractRuleModel.contractRuleList }" var="a" varStatus="st">
						  <c:choose>
	                          <c:when test="${st.index==0 }">
							      <li>
		                            <span class="contractRuleTitle">契约规则一</span>
		                            <span class="contractRuleDetail">
		                            <c:choose>
		                               <c:when test="${contractConfig.bonusCycle==0 }">
		                                                                                     半月保底分紅 
		                               </c:when>
		                               <c:otherwise>
		                                                                                     一月保底分紅 
		                               </c:otherwise>
		                            </c:choose>
		                            <strong>${a.gtdBonuses }%</strong>
		                            </span>
		                          </li>
						      </c:when>
						      <c:otherwise>
						           <li>
	                                 <span class="contractRuleTitle">
	                                <c:choose>
		                               <c:when test="${st.index==1 }">
		                                                                                     契约规则二
		                               </c:when>
		                               <c:when test="${st.index==2 }">
		                                                                                     契约规则三
		                               </c:when>
		                               <c:when test="${st.index==3 }">
		                                                                                     契约规则四
		                               </c:when>
		                               <c:when test="${st.index==4 }">
		                                                                                     契约规则五
		                               </c:when>
		                               <c:when test="${st.index==5 }">
		                                                                                     契约规则六
		                               </c:when>
		                               <c:when test="${st.index==6 }">
		                                                                                     契约规则七
		                               </c:when>
		                               <c:when test="${st.index==7 }">
		                                                                                     契约规则八
		                               </c:when>
		                               <c:when test="${st.index==8 }">
		                                                                                     契约规则九
		                               </c:when>
		                               <c:when test="${st.index==9 }">
		                                                                                     契约规则十
		                               </c:when>
		                            </c:choose> 
	                                 </span>
	                                 <span class="contractRuleDetail">
	                                 <c:choose>
		                               <c:when test="${contractConfig.bonusCycle==0 }">
		                                                                                    半月累积销量 
		                               </c:when>
		                               <c:otherwise>
		                                                                                     一月累积销量  
		                               </c:otherwise>
		                            </c:choose>
		                            <strong>${a.cumulativeSales }</strong> 元，投注人数${a.humenNum }，分紅 <strong>${a.dividend }%</strong></span>
	                               </li>
						      </c:otherwise>
						   </c:choose>
						</c:forEach>
                    </ul>
                </div>
                </c:if>
                <!--分红周期-->
                <c:choose>
                    <c:when test="${newContractRuleModel.contractStatus==0}">
                        <!--签订契约前按钮-->
		                <div class="btnBox">
		                    <button class="btn signBtn" onclick="agree('${user.account}','${user.parentAccount }')">接受</button>
		                    <c:if test="${user.account!=user.parentAccount }">
		                        <button class="btn refuseBtn" onclick="refuse('${user.account}','${user.parentAccount }')">拒绝</button>
		                    </c:if>
		                </div>
                    </c:when>
                    <c:otherwise>
                          <div class="bonusCircleBox">
			                    <c:forEach items="${contractBonus}" var="b" varStatus="st">
			                        <c:choose>
			                            <c:when test="${st.index==0}">
			                                <div class="bonusCircleCommon currentBonusCircle">
				                                <h3>当前分红周期</h3>
						                        <ul>
						                            <li><span class="fl">当前周期累计销量</span><span class="fr">${b.cumulativeSales } 元</span></li>
						                            <li><span class="fl">当前周期累计盈亏</span><span class="fr">${b.cumulativeProfit }元</span></li>
						                            <li><span class="fl">当前周期分红比例</span><span class="fr">${b.dividend }%</span></li>
						                            <li><span class="fl">当前周期应分红</span><span class="fr"><strong>${b.dividendAmount }</strong>元</span></li>
						                            <li><span class="fl">当前周期实际分红</span><span class="fr"><strong>${b.dividendAmount }</strong>元</span></li>
						                        </ul>
						                        <p>
							                         <c:choose>
							                              <c:when test="${b.status==0}">
							                                                                               尚未发放
							                             </c:when>
							                             <c:when test="${b.status==1}">
							                                                                                已分红
							                             </c:when>
							                             <c:when test="${b.status==2}">
							                                                                                不需分红
							                             </c:when>
							                             <c:when test="${b.status==3}">
							                                                                                 逾期未发放
							                             </c:when>
							                             <c:when test="${b.status==4}">
							                                                                                强制已发放
							                             </c:when>
							                             <c:when test="${b.status==5}">
							                                                                                强制已发放
							                             </c:when>
							                             <c:when test="${b.status==6}">
							                                                                                强制已拒发
							                             </c:when>
							                         </c:choose>
						                        </p>
					                        </div>
			                            </c:when>
			                            <c:otherwise>
			                                 <div class="bonusCircleCommon lastBonusCircle">
						                        <h3>上个分红周期</h3>
						                        <ul>
						                            <li><span class="fl">上个周期累计销量</span><span class="fr">${b.cumulativeSales }元</span></li>
						                            <li><span class="fl">上个周期累计盈亏</span><span class="fr">${b.cumulativeProfit } 元</span></li>
						                            <li><span class="fl">上个周期分红比例</span><span class="fr">${b.dividend }%</span></li>
						                            <li><span class="fl">上个周期应分红</span><span class="fr"><strong>${b.dividendAmount }</strong>元</span></li>
						                            <li><span class="fl">上个周期实际分红</span><span class="fr"><strong>${b.dividendAmount }</strong>元</span></li>
						                        </ul>
						                        <p> 
						                             <c:choose>
							                             <c:when test="${b.status==0}">
							                                                                               尚未发放
							                             </c:when>
							                             <c:when test="${b.status==1}">
							                                                                                已分红
							                             </c:when>
							                             <c:when test="${b.status==2}">
							                                                                                不需分红
							                             </c:when>
							                             <c:when test="${b.status==3}">
							                                                                                 逾期未发放
							                             </c:when>
							                             <c:when test="${b.status==4}">
							                                                                                强制已发放
							                             </c:when>
							                             <c:when test="${b.status==5}">
							                                                                                强制已发放
							                             </c:when>
							                             <c:when test="${b.status==6}">
							                                                                                强制已拒发
							                             </c:when>
							                         </c:choose>
						                        </p>
			                                 </div>
			                            </c:otherwise>
			                        </c:choose>
			                      </c:forEach>
			                </div>
                    </c:otherwise>
                </c:choose>             
             </div>
        </div>
	</div>
</div>
</body>
</html>
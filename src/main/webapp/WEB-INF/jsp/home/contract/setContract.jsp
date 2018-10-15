<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>设置契约分红</title>
    <link rel="stylesheet" href="<c:url value='/res/home/css/wk-accountCentre.css?ver=${VIEW_VERSION}'/>" />
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
    <script type="text/javascript">
    var contractConfigType = ${contractConfig.bonusCycle==0?'true':'false'};	
    var ruleNum = ${contractConfig.ruleNum};
    </script>
    <script src="<c:url value='/res/home/js/contract/setContract.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body>

<div class="areaBigContainer mainWidth" id="setContractArea">
	<div class="containerFlex">
		<!-- 列表 -->
		<div class="leftListArea">
			<c:if test="${user.userType==1 }">
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail teamManageA active" data-id="#teamManageArea"><i></i>团队管理</a>
				<div class="leftListItem">
					<a class="nav" href="<c:url value='/user/index?tabId=financeList'/>">会员管理</a>
					<a class="nav" href="<c:url value='/user/index?tabId=addUser'/>">新增会员</a>
					<a class="nav" href="<c:url value='/user/index?tabId=lowerDividend'/>">下级分红</a>
					<a class="nav" href="<c:url value='/user/index?tabId=teamInfo'/>">团队概况</a>
					<a class="nav" href="<c:url value='/user/index?tabId=extCode'/>">注册链接</a>
					<a class="nav active" href="javascript:;">设置契约分红</a>
				</div>
			</div>
			</c:if>
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail userCenterA" data-id="#safe"><i></i>个人中心</a>
				<div class="leftListItem" style="display:none;">
					<a class="nav active" href="<c:url value='/user/index?tabId=personInfo'/>">个人信息</a>
					<a class="nav" href="<c:url value='/user/index?tabId=myCard'/>">我的银行卡</a>
					<a class="nav" href="<c:url value='/user/index?tabId=modLoginPwd'/>">修改登录密码</a>
					<a class="nav" href="<c:url value='/user/index?tabId=modAccountPwd'/>">修改安全密码</a>
					<a class="nav" href="<c:url value='/user/index?tabId=safeQA'/>">安全问答</a>
					<a class="nav" href="<c:url value='/user/index?tabId=findSafeByQa'/>">找回安全密码</a>
					<c:if test="${user.contractStatus == 0||user.contractStatus == 1||user.contractStatus == 9}">
						<a class="nav" href="<c:url value='/contract/findMyContract'/>">我的契约</a>
					</c:if>
				</div>
			</div>
			<div class="leftListDetail">
				<a class="leftListBigItem centreNavDetail msgA" href="<c:url value='/user/index?tabId=msg'/>"><i></i>消息管理</a>
			</div>
		</div>	
		<!-- 内容 -->
		<div class="rightArea">
             <div class="accountCentreContent">
		        <div class="setContractHeader">
		            <label>账号：<span>${contractUuser.account }</span></label>
		            <label>返点：<span><fmt:formatNumber type="number" value="${contractUuser.rebateRatio }" maxFractionDigits="1"/> </span>%</label>
		            <label>最近登入：<span><fmt:formatDate value="${contractUuser.loginTime }" type="both" pattern="yyyy-MM-dd"/></span></label>
		            <label>契约状态：<span>
		            <c:choose>
		               <c:when test="${contractUuser.contractStatus==0 }">
		                                             契约确认中
		               </c:when>
		                <c:when test="${contractUuser.contractStatus==1}">
		                                             已签约契约
		               </c:when>
		               <c:when test="${contractUuser.contractStatus==9}">
		                                             已签约（系统）
		               </c:when>
		               <c:when test="${contractUuser.contractStatus==2}">
		                                            契约被拒绝请重新发起或是撤销变回原本契约
		               </c:when>
		               <c:otherwise>
		                                            未签订契约
		               </c:otherwise>
		            </c:choose>
		            </span></label>
		            <a href="javascript:;" id="showContractRule">了解契约分红规则</a>
		            <c:import url="../contract/contractRuleLayer.jsp"></c:import>
		        </div>
		        <div class="setContractContent">
		            <h2>彩票</h2>
		            <form id ="contractForm">
		                 <input type="hidden" name="account" value="${contractUuser.account }"/>
		                 <input type="hidden" name="contractStatus" value="${contractUuser.contractStatus }" />
		                <ul class="setContractList" id="setContractList">
		                <c:if test="${contractRuleModel==null}">
		                    <li>
		                        <a href="javascript:;" class="active"></a>
		                        <label class="listTitle">契约规则<span class="ruleNum">一</span>：</label>
		                        <span class="inputBox"><label>保底分红：</label><input type="text"  class="labelCond gtdBonuses" name="contractRuleList[0].gtdBonuses" value="${a.gtdBonuses }" onkeyup="checkGtdBonuses(this)"/>%</span>
		                        <p class="error"></p>
		                    </li>
		                </c:if>
		                   
		                <c:forEach items="${contractRuleModel.contractRuleList }" var="a" varStatus="st">
		                <c:choose>
	                          <c:when test="${st.index==0 }">
	                              <li>
			                        <a href="javascript:;" class="active"></a>
			                        <label class="listTitle">契约规则<span class="ruleNum">一</span>：</label>
			                        <span class="inputBox">
			                        <c:choose>
			                               <c:when test="${contractConfig.bonusCycle==0 }">
			                                  <label> 半月保底分红：</label>                                                   
			                               </c:when>
			                               <c:otherwise>
			                                  <label> 一月保底分红：</label>
			                               </c:otherwise>
	                                 </c:choose>
			                        <input type="text" id="gtdBonuses" class="labelCond gtdBonuses" name="contractRuleList[${st.index }].gtdBonuses" value="${a.gtdBonuses }" disabled="disabled" onkeyup="checkGtdBonuses(this)" />%</span>
			                        <p class="error"></p>
		                           </li>
	                          </c:when>
	                          <c:otherwise>
	                              <li>
		                        <a href="javascript:;" class="removeBtn"  onClick="deleteContract(this)"></a>
		                       
		                             <c:choose>
		                               <c:when test="${st.index==1 }">
		                                  <label class="listTitle">契约规则<span class="ruleNum">二</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==2 }">
		                                 <label class="listTitle">契约规则<span class="ruleNum">三</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==3 }">
		                               <label class="listTitle">契约规则<span class="ruleNum">四</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==4 }">
		                                <label class="listTitle">契约规则<span class="ruleNum">五</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==5 }">
		                               <label class="listTitle">契约规则<span class="ruleNum">六</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==6 }">
		                               <label class="listTitle">契约规则<span class="ruleNum">七</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==7 }">
		                               <label class="listTitle">契约规则<span class="ruleNum">八</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==8 }">
		                               <label class="listTitle">契约规则<span class="ruleNum">九</span>：</label>
		                               </c:when>
		                               <c:when test="${st.index==9 }">
		                               <label class="listTitle">契约规则<span class="ruleNum">十</span>：</label>
		                               </c:when>
		                            </c:choose>
		                                                          
		                        <span class="inputBox">
		                        <c:choose>
	                               <c:when test="${contractConfig.bonusCycle==0 }">
	                                  <label> 半月累计销量：</label>                                                   
	                               </c:when>
	                               <c:otherwise>
	                                  <label> 一月累计销量：</label>
	                               </c:otherwise>
	                            </c:choose>
			                    <input type="text"  class="labelCond cumulativeSales" name="contractRuleList[${st.index }].cumulativeSales" value="${a.cumulativeSales }" disabled="disabled" onkeyup="checkCumulativeSales(this)"/></span>
		                        <span class="inputBox"><label>投注人数：</label><input type="text"  class="labelCond humenNum" name="contractRuleList[${st.index }].humenNum" value="${a.humenNum }" disabled="disabled" onkeyup="checkHumenNum(this)">人</span>
		                        <span class="inputBox"><label>分红：</label><input type="text"  class="labelCond dividend" name="contractRuleList[${st.index }].dividend" value="${a.dividend }" disabled="disabled" onkeyup="checkDividend(this)"/>%</span>
		                        <p class="ruleError"></p>
		                    </li>
	                          </c:otherwise>
	                       </c:choose>
		                </c:forEach>
		                </ul>
		                <c:choose>
		                   <c:when test="${contractRuleModel==null}">
		                        <!--签订契约前按钮-->
		                        <div class="addBox">
		                        	<a href="javascript:;" class="addBtn" onClick="addContract()">添加</a>
		                        </div>
				                <div class="btnBox">
				                    <input type="button" class="btn signBtn" value="签订契约" onClick="submitForm()">
				                </div>
		                   </c:when>
		                   <c:otherwise>
		                        <!--签订契约后按钮-->
		                        <div class="addBox">
		                        	<a href="javascript:;" class="addBtn" id="addContract" onClick="addContract()" style="display: none">添加</a>
		                        </div>
				                <div class="btnBox">
				                     <input type="button" class="btn" id="modifyBtn" value="修改契约" onClick="edit()"/>
				                     <input type="button" class="btn" id="reModifyBtn" value ="重新签订契约" disabled="disabled" onClick="submitForm()"/>
				                    <c:if test="${contractUuser.contractStatus==0||contractUuser.contractStatus==2}">
				                         <input type="button" class="btn" id="cancelBtn" value="撤销签约申请" onClick="undo('${contractUuser.account}','${contractUuser.parentAccount }','${contractUuser.contractStatus }')" />
				                    </c:if>
				                    <a href="<c:url value='/user/index?tabId=financeList'/>" class="btn">返回用户列表</a>
				                </div>
		                   </c:otherwise>
		                </c:choose>            
		            </form>
		        </div>             	
             </div>
        </div>
	</div>
</div>
</body>
</html>
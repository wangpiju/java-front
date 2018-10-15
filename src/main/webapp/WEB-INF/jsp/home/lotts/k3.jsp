<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.sjc168.com/fn" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fnx" %>
<link rel="stylesheet" href="<c:url value='/res/home/css/wk-lottery-k3.css?ver=${VIEW_VERSION}'/>"/>
<!--选号列表-->
<c:forEach items="${lottBase.players }" var="player">
	<c:if test="${player.id != 'k3_star3_big_odd' }">
		<!-- 号码盘 -->
		<div class="changeNumList k3 changeNumList-${lott.groupId}"
			 style="display: none;" id="${player.id }"
			 data-anySelect="${player.anySelect }">
			<div
					class="changeNumCell ${player.id=='k3_star1'? 'single' : ''} ${player.id=='k3_star3_and'? 'and' : ''}">
				<c:if test="${player.anyList!=null }">
					<c:forEach items="${player.anyList }" var="a" varStatus="stat">
						<c:choose>
							<c:when test="${player.anySelect>stat.index  }">
								<input class="anySelect" type="checkbox" checked="checked"
									   value="${a }"/>${a }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</c:when>
							<c:otherwise>
								<input class="anySelect" type="checkbox"
									   value="${a }"/>${a }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:if>
				<c:forEach items="${player.numView }" var="line">
					<!-- 行 -->
					<div
							class="changeNumDetail${line.towLine? ' changeNumDouble':'' } ${player.id == 'k3_star2_same' ? 'same' : ''}">
						<div class="changeNum">
							<!-- <span class="detailTitle">${line.title }</span> -->
							<ul class="numList" data-max="${line.maxSelect }">
								<c:if test="${player.id == 'k3_star3_and'}">
									<c:forEach items="${line.numBonus }" var="n" varStatus="st">
										<c:if test="${(n[0] != '03' && n[0] != '18') &&  lott.id != 'dfk3'}">
											<li>
												<a href="javascript:;" data-num="${n[0]}" class="smallNum">
													<b>${n[0]}</b><i>赔${n[1]}</i>
												</a>
											</li>
										</c:if>
										<c:if test="${ lott.id == 'dfk3'}">
											<li>
												<a href="javascript:;" data-num="${n[0]}" class="smallNum">
													<b>${n[0]}</b><i>赔${n[1]}</i>
												</a>
											</li>
										</c:if>
									</c:forEach>
								</c:if>


								<c:if test="${player.id != 'k3_star3_and'}">
									<c:forEach items="${line.nums}" var="n" varStatus="st">
										<li>
											<a href="javascript:;" data-num="${n}" class="smallNum">
												<c:forEach items="${fn:toListByLength(n,1)}" var="a" varStatus="stuts">
													<span class="k3-${a}"></span>
												</c:forEach>
											</a>
										</li>
										<c:if test="${player.id == 'k3_star2_same' && st.last}">
											<li class="last"><a href="javascript:;" data-num="${fnx:substring(n,0,2)}" class="smallNumBtn">
												<c:forEach items="${fn:toListByLength(n,1)}" var="a" varStatus="stuts">
													<c:if test="${stuts.last == false}">
														<span class="k3-${a}"></span>
													</c:if>
												</c:forEach>
											</a></li>
										</c:if>
									</c:forEach>
								</c:if>

							</ul>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</c:if>
</c:forEach>
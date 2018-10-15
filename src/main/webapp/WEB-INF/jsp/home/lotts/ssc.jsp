<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.sjc168.com/fn" prefix="fn" %>
<!--选号列表-->
<c:forEach items="${lottBase.players }" var="player">
	<!-- 号码盘 -->
	<div class="changeNumList changeNumList-${lott.id}" style="display: none;" id="${player.id }"
		 data-anySelect="${player.anySelect }">
		<c:if test="${player.anyList!=null }">
			<c:forEach items="${player.anyList }" var="a" varStatus="stat">
				<c:choose>
					<c:when test="${player.anySelect>stat.index  }">
						<input class="anySelect" type="checkbox" checked="checked"
							   value="${a }"/>${a }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
					<%-- 行 --%>
					<div class="changeNumDetail${line.towLine? ' changeNumDouble':'' }">
						<div class="changeNum">
							<span class="detailTitle">${line.title }</span>
							<ul class="numList" data-max="${line.maxSelect }">
								<c:forEach items="${line.nums }" var="n" varStatus="st">
									<li>
										<a href="javascript:;" data-num="${n}"
										   class="smallNum smallNum${fn:getTitle(n)}"></a>
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
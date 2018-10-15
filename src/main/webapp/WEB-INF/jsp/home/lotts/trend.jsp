<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>开奖走势图</title>
	<link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>" type="image/x-icon"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<link rel="stylesheet" href="/res/home/css/dh-trend.css"/>
	<link rel="stylesheet" href="<c:url value='/res/home/css/${theme}.css?ver=${VIEW_VERSION}'/>"/>
</head>
<body>
<!-- 开奖走势图 start -->
<div class="trendContainer">
	<!-- 开奖走势图头部 -->
	<div class="trendHeaderArea">
		<span class="lottsType-trend">彩种：${lott.title }</span>
		<ul class="lottsGroup-trend">
			<li class="lottsDetail-trend active">
				<a href="javascript:;">${trend.name }</a>
			</li>
		</ul>
		<div class="downloadStatementBox">
			<a href="javascript:;" class="hideSearchBar-trend" onclick="trendHideSearchBar(this)">
				<i class="hideSearchBarIcon"></i>
				<span>隐藏功能区</span>
			</a>
		</div>
	</div>
	<!-- 开奖走势图搜索条 -->
	<div class="trendSearchBarArea" id="trendSearchBarArea">
		<div class="areaContainer">
			<div class="trendSearchCheckBox">
				<label class="checkboxDetail">
					<input type="checkbox" class="checkbox" id="trendSplitLine" checked/>
					<span>辅助线</span>
				</label>
				<label class="checkboxDetail">
					<input type="checkbox" class="checkbox" id="trendOmission" checked/>
					<span>遗漏</span>
				</label>
				<label class="checkboxDetail">
					<input type="checkbox" class="checkbox" id="trendOmissionBar"/>
					<span>遗漏条</span>
				</label>
				<label class="checkboxDetail">
					<input type="checkbox" class="checkbox" id="trendLine" checked/>
					<span>走势</span>
				</label>
				<label class="checkboxDetail">
					<input type="checkbox" class="checkbox" id="trendNumWarmCold"/>
					<span>号温</span>
				</label>
			</div>
			<div class="latelyIssueBox-trend">
				<a href="trend?len=100" class="latelyIssue-trend ${len==100? 'active':'' }">近100期</a>
				<a href="trend?len=50" class="latelyIssue-trend ${len==50? 'active':'' }">近50期</a>
				<a href="trend?len=30" class="latelyIssue-trend ${len==30? 'active':'' }">近30期</a>
			</div>
		</div>
	</div>
	<!-- 开奖号码走势图 -->
	<div class="lotteryTrendArea" id="lotteryTrendArea">
		<table class="lottsTrendTable">
			<thead>
			<tr class="listHeader">
				<th rowspan="2" class="smallFont14">期号</th>
				<th rowspan="2" class="smallFont14">开奖号码</th>
				<c:forEach items="${trend.titles }" var="a">
					<th colspan="${trend.numLen }" width="17" class="smallFont14 trendLine">${a}</th>
				</c:forEach>
			</tr>
			<tr class="listHeader">
				<!--<td>-->
				<!--<label class="checkboxDetail">-->
				<!--<input type="checkbox" class="checkbox" />-->
				<!--<span>全部</span>-->
				<!--</label>-->
				<!--</td>-->
				<c:forEach items="${trend.titles }" var="t">
					<c:forEach items="${trend.nums }" var="n" varStatus="st">
						<c:choose><c:when test="${st.index==0}"><td class="trendBorder-l" width="17"></c:when>
							<c:otherwise><td width="17"></c:otherwise></c:choose>${n}</td></c:forEach>
				</c:forEach>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${trend.trends }" var="open">
				<tr>
					<td class="fontColorBlack" width="110">${open.seasonId }</td>
					<td class="fontColorBlack trendBorder-l" width="80">${open.nums }</td>
					<c:forEach items="${open.info }" var="info">
						<td class="${info.clazz }">${info.num}</td>
					</c:forEach>
				</tr>
			</c:forEach>
			</tbody>
			<tfoot>
			<c:forEach items="${trend.allTrends }" var="t">
				<tr class="trendNumTotal">
					<td class="smallFont14">${t.seasonId}</td>
					<td class="trendBorder-l"></td>
					<c:forEach items="${t.info }" var="info">
						<td class="${info.clazz }">${info.num }</td>
					</c:forEach>
				</tr>
			</c:forEach>
			<tr class="trendNumTotal">
				<td rowspan="2" class="smallFont14">期号</td>
				<td rowspan="2" class="trendBorder-l smallFont14">开奖号码</td>
				<c:forEach items="${trend.titles }" var="t">
					<c:forEach items="${trend.nums }" var="n" varStatus="st">
						<c:choose><c:when test="${st.index==0}"><td class="trendBorder-l"></c:when>
							<c:otherwise><td></c:otherwise></c:choose>${n}</td></c:forEach>
				</c:forEach>
			</tr>
			<tr class="trendNumTotal">
				<c:forEach items="${trend.titles }" var="a">
					<td colspan="${trend.numLen }" width="17" class="smallFont14 trendBorder-l">${a}</td>
				</c:forEach>
			</tr>
			</tfoot>
		</table>
		<div class="lottsTrendCanvasBox" id="lottsTrendCanvasBox"></div>
	</div>
	<!-- 号码示例 -->
	<!-- <div class="trendBallExampleBox">
		<label>
			<i class="trendBallRed">1</i>
			<span>正常显示</span>
		</label>
		<label>
			<i class="numBallHot">2</i>
			<span>热号</span>
		</label>
		<label>
			<i class="numBallCold">3</i>
			<span>冷号</span>
		</label>
		<label>
			<i class="numBallWarm">4</i>
			<span>温号</span>
		</label>
		<label>
			<i class="numBallOverlap">5</i>
			<span>重叠号</span>
		</label>
		<label>
			<i class="numBallOdd">6</i>
			<span>单号</span>
		</label>
	</div> -->
</div>
<!-- 开奖走势图 end -->
<script type="text/javascript" src="<c:url value='/res/home/js/jquery-1.11.0.min.js?ver=${VIEW_VERSION}'/>"></script>
<script src="/res/home/js/trend/dh-trend.js"></script>
<script src="/res/home/js/My97DatePicker/WdatePicker.js"></script>
</body>
</html>
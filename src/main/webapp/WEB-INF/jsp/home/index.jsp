<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>大順 - 首页</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
	<meta http-equiv="expires" content="0"/>
	<link rel="stylesheet" href="<c:url value='/res/home/css/reset.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="<c:url value='/res/home/css/wk-common.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="stylesheet" href="<c:url value='/res/home/css/wk-index.css?ver=${VIEW_VERSION}'/>"/>
	<link rel="icon" href="<c:url value='/res/home/images/favicon.ico'/>" type="image/x-icon"/>
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<script type="text/javascript"
			src="<c:url value='/res/home/js/jquery-1.11.0.min.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/jquery.timer.js?ver=${VIEW_VERSION}'/>"></script>
	<%--<script type="text/javascript" src="<c:url value='/res/home/js/layer/layer.js?ver=${VIEW_VERSION}'/>"></script>--%>
	<script type="text/javascript" src="<c:url value='/res/home/js/spinner.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript" src="<c:url value='/res/home/js/base.js?ver=${VIEW_VERSION}'/>"></script>
	<script type="text/javascript">
		var falg = '${flag}';
	</script>
	<script type="text/javascript" src="<c:url value='/res/home/js/index.js?ver=${VIEW_VERSION}'/>"></script>
</head>
<body>
<%@include file="/WEB-INF/jsp/home/header.jsp" %>
<%--<%@include file="/WEB-INF/jsp/home/right.jsp" %>--%>
<div class="main">
	<div class="middle-box">
		<div class="middle-box-left">
			<ul>
				<li>
					<a href="/lotts/dfk3/index">
						<img src="/res/home/images/cz_dfk3-theme-black.png" alt="">大順快3
						<span>1分1期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/pk10/index">
						<img src="/res/home/images/cz_pk10-theme-black.png" alt="">北京pk10
						<span>全天179期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/hbk3/index">
						<img src="/res/home/images/cz_hbk3-theme-black.png" alt="">湖北快3
						<span>全天78期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/ahk3/index">
						<img src="/res/home/images/cz_ahk3-theme-black.png" alt="">安徽快3
						<span>全天80期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/bjk3/index">
						<img src="/res/home/images/cz_bjk3-theme-black.png" alt="">北京快3
						<span>全天89期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/jsk3/index">
						<img src="/res/home/images/cz_jsk3-theme-black.png" alt="">江苏快3
						<span>全天82期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/sj1fc/index">
						<img src="/res/home/images/cz_sj1fc-theme-black.png" alt="">大順时时彩
						<span>1分1期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/cqssc/index">
						<img src="/res/home/images/cz_cqssc-theme-black.png" alt="">重庆时时彩
						<span>全天120期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/hebk3/index">
						<img src="/res/home/images/cz_hebk3-theme-black.png" alt="">河北快3
						<span>全天81期</span>
					</a>
				</li>
				<li>
					<a href="/lotts/shk3/index">
						<img src="/res/home/images/cz_shk3-theme-black.png" alt="">上海快3
						<span>全天82期</span>
					</a>
				</li>
			</ul>
		</div>
		<div class="middle-box-middle">
			<div class="banner">
				<div class="bannerBox">
					<div class="userAcctBgBox" id="userAcctBg">
						<c:forEach items="${imgs}" var="a">
							<a href="${a.link}" title="${a.title}" target="_blank">
								<img src="${a.img}"/>
							</a>
						</c:forEach>
					</div>
					<p class="userAcctBgList" id="userAcctBgList">
						<c:forEach items="${imgs}">
							<span></span>
						</c:forEach>
					</p>
				</div>
			</div>
			<div class="autoTab">
				<ul class="autoTabUl">
					<li class="active">江苏快3</li>
					<li>重庆时时彩</li>
					<li>大順快3</li>
				</ul>
				<div class="autoTabContent">
					<ul class="autoTabContentUl1 showautoTabContentUl">
						<li>
							<img class="img1" src="/res/home/images/dice/1.png" alt="">
							<span>+</span>
							<img class="img2" src="/res/home/images/dice/2.png" alt="">
							<span>+</span>
							<img class="img3" src="/res/home/images/dice/5.png" alt="">
							<span>=</span>
							<b class="sum"></b>
							<button>
								<a href="/lotts/jsk3/index">立即投注</a>
							</button>
						</li>
						<li>
							<span>当前期：第<em class="seasonId">20180522045</em>期</span>
							<span>开奖号码:<em class="sumString">1,2,5</em></span>
							<span>和值：<em class="sum">8</em></span>
						</li>
					</ul>
					<ul class="autoTabContentUl2">
						<li>
							<span class="n1">5</span>
							<span class="n2">6</span>
							<span class="n3">7</span>
							<span class="n4">8</span>
							<span class="n5">9</span>
							<button>
								<a href="/lotts/cqssc/index">立即投注</a>
							</button>
						</li>
						<li>
							<span>当前期：第<em class="seasonId">20180522045</em>期</span>
							<span>开奖号码:<em class="sumString">5,6,7,8,9</em></span>
						</li>
					</ul>
					<ul class="autoTabContentUl4">
						<li>
							<img class="img1" src="/res/home/images/dice/1.png" alt="">
							<span>+</span>
							<img class="img2" src="/res/home/images/dice/2.png" alt="">
							<span>+</span>
							<img class="img3" src="/res/home/images/dice/5.png" alt="">
							<span>=</span>
							<b class="sum"></b>
							<button>
								<a href="/lotts/dfk3/index">立即投注</a>
							</button>
						</li>
						<li>
							<span>当前期：第<em class="seasonId">20180522045</em>期</span>
							<span>开奖号码:<em class="sumString">1,2,5</em></span>
							<span>和值：<em class="sum">8</em></span>
						</li>
						<%--广东11选5--%>
						<%--<li>--%>
						<%--<span class="n1">5</span><span class="n2">6</span><span class="n3">7</span><span class="n4">8</span><span class="n5">9</span>--%>
						<%--<button><a href="/lotts/dfk3/index">立即投注</a></button>--%>
						<%--</li>--%>
						<%--<li>--%>
						<%--<span>当前期：第<em class="seasonId">20180522045</em>期</span><span>开奖号码:<em class="sumString">1,2,3,4,5</em></span>--%>
						<%--</li>--%>
					</ul>
				</div>
			</div>
		</div>
		<div class="middle-box-right">
			<c:if test="${null != user}">
				<div class="middle-box-right-top">
					<p>账号：${user.account}</p>
				</div>
			</c:if>
			<c:if test="${null == user}">
				<div class="middle-box-right-top">
					<button><a href="/login">登录</a></button>
					<button><a href="/registByCode">注册</a></button>
				</div>
			</c:if>
			<div class="middle-box-right-middle">
				<h3>昨日盈利榜</h3>
				<ul class="middle-box-right-middle-Ul">
					<li>
						<div class="champion">
							<img src="" alt="">
							<p>
								<span>账号昵称：<i class="nickname"></i></span>
								<span>昨日盈利：<i class="gain"></i>元</span>
							</p>
						</div>
						<p>1</p>
					</li>
					<li>
						<div class="runnerUp">
							<img src="" alt="">
							<p>
								<span>账号昵称：<i class="nickname"></i></span>
								<span>昨日盈利：<i class="gain"></i>元</span>
							</p>
						</div>
						<p>2</p>
					</li>
					<li>
						<div class="thirdPlace">
							<img src="" alt="">
							<p>
								<span>账号昵称：<i class="nickname"></i></span>
								<span>昨日盈利：<i class="gain"></i>元</span>
							</p>
						</div>
						<p>3</p>
					</li>
				</ul>
			</div>
			<div class="middle-box-right-bottom">
				<h3>中奖信息</h3>
				<div id="FontScroll">
					<ul class="middle-box-right-bottom-Ul">
						<li>
							<div>
								<img src="/res/home/images/heardimg/1.jpg" alt="">
								<p>
									<span>yq***6</span>
									<span>喜中：<i>￥1108640</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/2.jpg" alt="">
								<p>
									<span>w8***g</span>
									<span>喜中：<i>￥110864</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/7.jpg" alt="">
								<p>
									<span>z***w</span>
									<span>喜中：<i>￥40640.68</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/1.jpg" alt="">
								<p>
									<span>kk***j</span>
									<span>喜中：<i>￥1640.07</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/3.jpg" alt="">
								<p>
									<span>yh***2</span>
									<span>喜中：<i>￥408640</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/5.jpg" alt="">
								<p>
									<span>三***</span>
									<span>喜中：<i>￥305.42</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/0.jpg" alt="">
								<p>
									<span>hs***f</span>
									<span>喜中：<i>￥168.02</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/7.jpg" alt="">
								<p>
									<span>t***s</span>
									<span>喜中：<i>￥40640.68</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/1.jpg" alt="">
								<p>
									<span>kt***w</span>
									<span>喜中：<i>￥1640.07</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/3.jpg" alt="">
								<p>
									<span>钱***2</span>
									<span>喜中：<i>￥408640</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/5.jpg" alt="">
								<p>
									<span>嘿***</span>
									<span>喜中：<i>￥305.42</i></span>
								</p>
							</div>
						</li>
						<li>
							<div>
								<img src="/res/home/images/heardimg/0.jpg" alt="">
								<p>
									<span>ea***l</span>
									<span>喜中：<i>￥168.02</i></span>
								</p>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/jsp/home/footer.jsp" %>
<input type="hidden" id="userPasswordStatus" value="${user.passwordStatus}"/>
<div id="platNoticeContentBox" style="display:none;">
	<div class="layerBox platNoticeWarnDialog">
		<div class="noticeDetailHeader">
			<p class="noticeDetailTitle" id="noticeDetailTitle">${notice.title}</p>
			<p class="noticeDetailDate" id="noticeDetailDate">${notice.createTime}</p>
		</div>
		<div class="noticeDetailText" id="noticeDetailText">
			${notice.content}
		</div>
	</div>
	<div class="dialogBtn">
		<a href="javascript:;" class="btn closePlatNotice">确定</a>
	</div>
</div>
</body>
</html>
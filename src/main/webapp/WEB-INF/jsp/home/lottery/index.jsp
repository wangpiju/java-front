<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="<c:url value='/res/home/css/lotte.css?ver=${VIEW_VERSION}'/>">
	<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
	<script type="text/javascript">
		var falg = '${flag}';
	</script>
	<script type="text/javascript" src="<c:url value='/res/home/js/index.js?ver=${VIEW_VERSION}'/>"></script>
	<title>大順 - 彩票大厅</title>
</head>
<body>
<div class="main">
	<div class="lotteLobby">
		<div class="lotteBox">
			<div class="lotteBox-top">
				<div class="lotteBox-top-banner">
					<ul class="lotteBox-top-banner-Ul">
						<li><a href="javascript:;"><img src="/res/home/images/banner1.png" alt=""></a></li>
						<li><a href="javascript:;"><img src="/res/home/images/banner2.png" alt=""></a></li>
						<li><a href="javascript:;"><img src="/res/home/images/banner1.png" alt=""></a></li>
						<li><a href="javascript:;"><img src="/res/home/images/banner2.png" alt=""></a></li>
					</ul>
				</div>
				<div class="lotteBox-top-right">
					<h3>风云榜</h3>
					<div id="FontScroll">
						<ul class="lotteBox-top-right-Ul">
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
			<div class="lotteryLine fix">
				<i></i>
				<em></em>
			</div>
			<div class="lotteList">
				<div class="lotteList-top">
					<ul>
						<li class="active">热门</li>
						<li>全部</li>
						<li>快3</li>
						<li>时时彩</li>
						<li>快乐彩</li>
						<li>11选5</li>
					</ul>
				</div>
				<div class="lotteList-bott">
					<ul class="lotteList-hot lotteListUl active">
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/dfk3.png" alt="">
								<div>
									<p>大順快3</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/dfk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/zhssc.png" alt="">
								<div>
									<p>大順时时彩</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/sj1fc/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>湖北快3</p>
									<span>全天78期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/hbk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>安徽快3</p>
									<span>全天80期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/ahk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>北京快3</p>
									<span>全天89期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/bjk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>江苏快3</p>
									<span>全天82期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/jsk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/sscs.png" alt="">
								<div>
									<p>重庆时时彩</p>
									<span>全天120期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/cqssc/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>河北快3</p>
									<span>全天81期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/hebk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>上海快3</p>
									<span>全天82期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/shk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/klcs.png" alt="">
								<div>
									<p>北京PK10</p>
									<span>全天179期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/pk10/index">立即投注</a>
							</div>
						</li>
					</ul>
					<ul class="lotteList-all lotteListUl">
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/dfk3.png" alt="">
								<div>
									<p>大順快3</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/dfk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/zhssc.png" alt="">
								<div>
									<p>大順时时彩</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/sj1fc/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/x11x5.png" alt="">
								<div>
									<p>大順11选5</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/f1_11x5/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/klcs.png" alt="">
								<div>
									<p>大順pk10</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/ffpk10/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>湖北快3</p>
									<span>全天78期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/hbk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>安徽快3</p>
									<span>全天80期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/ahk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>北京快3</p>
									<span>全天89期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/bjk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/x11x5.png" alt="">
								<div>
									<p>上海11选5</p>
									<span>全天90期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/sh11x5/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>江苏快3</p>
									<span>全天82期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/jsk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/sscs.png" alt="">
								<div>
									<p>重庆时时彩</p>
									<span>全天120期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/cqssc/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/x11x5.png" alt="">
								<div>
									<p>安徽11选5</p>
									<span>全天81期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/ah11x5/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>河北快3</p>
									<span>全天81期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/hebk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>上海快3</p>
									<span>全天82期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/shk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/klcs.png" alt="">
								<div>
									<p>北京PK10</p>
									<span>全天179期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/pk10/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>甘肃快3</p>
									<span>全天72期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/gsuk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>吉林快3</p>
									<span>全天87期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/jlk3/index">立即投注</a>
							</div>
						</li>
					</ul>
					<ul class="lotteList-k3 lotteListUl">
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/dfk3.png" alt="">
								<div>
									<p>大順快3</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/dfk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>湖北快3</p>
									<span>全天78期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/hbk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>安徽快3</p>
									<span>全天80期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/ahk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>北京快3</p>
									<span>全天89期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/bjk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>江苏快3</p>
									<span>全天82期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/jsk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>上海快3</p>
									<span>全天82期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/shk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>河北快3</p>
									<span>全天81期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/txffc/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>甘肃快3</p>
									<span>全天72期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/gsuk3/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/3d.png" alt="">
								<div>
									<p>吉林快3</p>
									<span>全天87期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/jlk3/index">立即投注</a>
							</div>
						</li>
					</ul>
					<ul class="lotteList-ssc lotteListUl">
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/zhssc.png" alt="">
								<div>
									<p>大順时时彩</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/sj1fc/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/sscs.png" alt="">
								<div>
									<p>重庆时时彩</p>
									<span>全天120期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/cqssc/index">立即投注</a>
							</div>
						</li>
					</ul>
					<ul class="lotteList-klc lotteListUl">
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/klcs.png" alt="">
								<div>
									<p>北京PK10</p>
									<span>全天179期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/pk10/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/klcs.png" alt="">
								<div>
									<p>大順pk10</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/ffpk10/index">立即投注</a>
							</div>
						</li>
					</ul>
					<ul class="lotteList-x11x5 lotteListUl">
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/x11x5.png" alt="">
								<div>
									<p>大順11选5</p>
									<span>1分钟1期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/f1_11x5/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/x11x5.png" alt="">
								<div>
									<p>上海11选5</p>
									<span>全天90期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/sh11x5/index">立即投注</a>
							</div>
						</li>
						<li>
							<div class="Nopop">
								<img src="/res/home/images/lottery/x11x5.png" alt="">
								<div>
									<p>安徽11选5</p>
									<span>全天81期</span>
								</div>
							</div>
							<div class="pop">
								<a href="/lotts/ah11x5/index">立即投注</a>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
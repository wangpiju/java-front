<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0 user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
	<meta content="email=no" name="format-detection">
    <title>添加开户</title>
    <link rel="stylesheet" href="css/hs-mobile.css" />
</head>
<body class="bodyColorEEE">

<!-- 添加开户 -->
<div class="addPersonArea">
    <div class="mainNav">
        <a href="javascript:;" class="mainMenuBtn">
            <img src="images/mainMenuIcon.png" class="mainMenuIcon" />
        </a>
        <span class="headerTitle">添加开户</span>
    </div>
    <div class="container">
        <div class="addPersonBox">
            <div class="listForm">
                <div class="listDetail">
                    <p class="detailTitle fontColorTeal">
                        <i class="detailTitleLeft"></i>
                        <span>用户类型</span>
                    </p>
                    <select name="userType-add" class="labelCond">
                        <option value="">请选择</option>
                    </select>
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorTeal">
                        <i class="detailTitleLeft"></i>
                        <span>用户名</span>
                        <span class="detailTips fontColorGray">8~16位数字、字母</span>
                    </p>
                    <input type="text" class="labelCond" />
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorTeal">
                        <i class="detailTitleLeft"></i>
                        <span>默认密码</span>
                    </p>
                    <input type="text" class="labelCond" value="123456" readonly />
                </div>
                <div class="listDetail">
                    <p class="detailTitle fontColorTeal">
                        <i class="detailTitleLeft"></i>
                        <span>自身保留返点</span>
                    </p>
                    <select name="userType-add" class="labelCond">
                        <option value="">请选择</option>
                    </select>
                </div>
            </div>
            <div class="btnGroup">
                <a href="javascript:;" class="btn btnRed">提交</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>

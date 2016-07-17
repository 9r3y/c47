<!DOCTYPE html>
<!-- saved from url=(0038)http://v3.bootcss.com/examples/signin/ -->
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
<#--<link rel="icon" href="http://v3.bootcss.com/favicon.ico">-->

    <title>Easy</title>

    <!-- Bootstrap core CSS -->
    <link href="${rc.getContextPath()}/resources/css/bootstrap/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${rc.getContextPath()}/resources/css/login.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<div class="container">

<form class="form-signin" action="${rc.getContextPath()}/login" method="post">
    <h2 class="form-signin-heading">请登录</h2>
<#if RequestParameters.error??>
    <h4 style="color:#CC0000;">用户名或密码无效</h4>
</#if>
<#if RequestParameters.logout??>
    <h4 style="color:darkgreen;">你已经登出</h4>
</#if>
    <label for="iptUsername" class="sr-only">用户名</label>
    <input type="text" id="iptUsername" name="username" class="form-control" placeholder="用户名"
           required="" autofocus="">
    <label for="iptPassword" class="sr-only">密码</label>
    <input type="password" id="iptPassword" name="password" class="form-control" placeholder="密码"
           required="">
    <div class="checkbox">
        <label>
            <input id="iptRememberMe" type="checkbox" name="remember-me">记住我
        </label>
    </div>
    <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
</form>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<#--<script src="./Signin Template for Bootstrap_files/ie10-viewport-bug-workaround.js"></script>-->
<script src="${rc.getContextPath()}/resources/js/jquery/jquery-3.0.0.js"></script>
<script src="${rc.getContextPath()}/resources/js/bootstrap/bootstrap.js"></script>


</body></html>
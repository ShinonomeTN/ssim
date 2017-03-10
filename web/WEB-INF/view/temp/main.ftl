<#--
    Default body template
-->
<#macro body title>
<html>
<head>
    <title>${title}</title>
    <meta charset="UTF-8"/>
    <@_import_css></@_import_css>
</head>
<body>
    <#nested >

    <@_import_js></@_import_js>
</body>
</html>
</#macro>

<#--
    CSS import
-->

<#macro _import_css>
<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/animate.css/3.5.2/animate.css">

<link rel="stylesheet" href="/css/main.css"/>
</#macro>

<#--
    JS import
-->

<#macro _import_js>
<!-- Framework import -->
<script src="//cdn.bootcss.com/angular.js/1.3.0/angular.min.js"></script>
<!-- <script src="/framework/angularjs/1.3.0.14/angular-route.js"></script> -->
<script src="//cdn.bootcss.com/angular-ui-router/0.4.2/angular-ui-router.min.js"></script>
<script src="//cdn.bootcss.com/angular.js/1.3.0/angular-animate.min.js"></script>

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script src="//cdn.bootcss.com/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls.min.js"></script>

<!-- Application modules import -->
<script src="/js/app.js"></script>
<script src="/js/controllers.js"></script>
<script src="/js/filters.js"></script>
<script src="/js/services.js"></script>
<script src="/js/directives.js"></script>
</#macro>
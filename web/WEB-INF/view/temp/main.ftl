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
<div class="container">
    <div align="center" class="page-header">
        <h1>简单课表查询 <small style="display: inline-block">for 广东岭南职业技术学院</small></h1>
    </div>
    <div class="row">
        <#nested >
    </div>
    <div align="center">
        <hr>
        <small>本站开放源代码 : <a href="https://github.com/shinonometn/SimpleSubjectImporter/tree/master">Github</a></small><br>
        <small>使用请遵循 GPLv2 协议</small>
    </div>
</div>
<@_import_js></@_import_js>
</body>
</html>
</#macro>

<#--
    CSS import
-->

<#macro _import_css>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/animate.css/3.5.2/animate.css">
<link rel="stylesheet" type="text/css" href="/css/bootstrap-select.min.css"/>
<link rel="stylesheet" href="/css/main.css"/>
</#macro>

<#--
    JS import
-->

<#macro _import_js>
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script src="/js/bootstrap-select.min.js"></script>
</#macro>
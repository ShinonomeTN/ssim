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
<#nested >
</div>
<@_import_js></@_import_js>
</body>
</html>
</#macro>

<#--
    CSS import
-->

<#macro _import_css>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<link rel="stylesheet" href="/css/main.css"/>
</#macro>

<#--
    JS import
-->

<#macro _import_js>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</#macro>
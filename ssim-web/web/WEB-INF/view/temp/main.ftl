<#global webRoot=""/>
<#global weekdaysZh =["日","一","二","三","四","五","六"] />
<#--
    Default body template
-->
<#macro body title>
<html>
<head>
    <title>${title}</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <@_import_css></@_import_css>
</head>
<body>
<div class="container">
    <div align="center" class="page-header">
        <h1>课表查询 <small style="display: inline-block">for 广东岭南职业技术学院</small></h1>
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


<#macro siteBusyTitle status>
    <#if status == "capturing">
    <h2> <span class="glyphicon glyphicon-refresh"></span> 网站正在更新数据</h2>
    <p>建议稍后来访（一般10分钟左右）</p>
    <#elseif status == "importing">
    <h2> <span class="glyphicon glyphicon-hourglass"></span> 网站正在更新数据</h2>
    <p>此时网站压力较大，建议稍后来访（一般 5 分钟左右）</p>
    </#if>
</#macro>

<#--
    Rending schedule using table style
-->
<#macro scheduleTableRender lessonPosMap>
<table class="table table-striped table-condensed table-schedule hidden-xs hidden-sm">
    <thead align="center">
        <#list ["#"]+weekdaysZh as days>
        <td width="2.5%">${days}</td>
        </#list>
    </thead>
    <#list 1..12 as turns>
        <tr align="center">
            <td>${turns}</td>
            <#list 0..6 as day>
                <td>
                    <#assign pos="${day}-${turns}">
                    <#if lessonPosMap[pos]??>
                        <#list lessonPosMap[pos] as lesson>
                            <div class="item <#if lesson?is_last>item_latest</#if>">
                            ${lesson.name} <label class="label label-default">${lesson.category}</label>
                                <small> at ${lesson.address}</small>
                                <br>
                            </div>
                        </#list>
                    </#if>
                </td>
            </#list>
        </tr>
    </#list>
</table>
</#macro>

<#--
    Rending schedule using list style
-->
<#macro scheduleListRender lessonPosMap>
<ul class="list-group hidden-md hidden-lg fadeIn animated list-schedule">
    <#list 0..6 as day>
        <li class="list-group-item">
            <div class="media">
                <h3 class="media-left">周 ${weekdaysZh[day]} </h3>
                <div class="media-body">
                    <#assign lessonCount=0>
                    <#list 1..12 as turns>
                        <#assign pos="${day}-${turns}">
                        <#if lessonPosMap[pos]??>
                            <div class="media">
                                <h4 class="media-left">${turns}</h4>
                                <div class="media-body">
                                    <#list lessonPosMap[pos] as lesson>
                                        <#assign lessonCount++>
                                        <div class="item <#if lesson?is_last>item_latest</#if>">
                                            <div>
                                            ${lesson.name}
                                                <label class="label label-info">${lesson.category}</label>
                                            </div>
                                            <small>at ${lesson.address}</small>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </#if>
                    </#list>
                </div>
            </div>
        </li>
    </#list>
</ul>
</#macro>

<#--
    CSS import
-->

<#macro _import_css>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/animate.css/3.5.2/animate.css">
<link rel="stylesheet" type="text/css" href="/css/bootstrap-select.min.css"/>
<link rel="stylesheet" href="/css/cs-main.css"/>
</#macro>

<#--
    JS import
-->

<#macro _import_js>
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script src="${webRoot}/js/bootstrap-select.min.js"></script>
</#macro>
<#import "temp/main.ftl" as temp>
<@temp.body title="无课时间查询">
<div class="col-md-12">
    <#assign weekdaysZh =["日","一","二","三","四","五","六"] />
    <div class="panel panel-default">
        <div class="panel-heading">
            <div>
                <#if (classNames?size > 1) >
                    <h2 class="pull-left"> 无课时间 for </h2>
                    <div style="margin-left: 10pt; padding-bottom: 5pt" class="pull-left" align="center">
                        <#if (classNames?size < 5)>
                            <#list classNames as className>
                                <div>${className}</div>
                            </#list>
                        <#else >
                            <#list 0..3 as i>
                                <div>${classNames[i]}</div>
                            </#list>
                            <div>...</div>
                        </#if>
                    </div>
                <#else >
                    <h3 class="pull-left">${classNames[0]} 的无课时间</h3>
                </#if>
                <div class="clearfix"></div>
            </div>
            <div>
                <#if (ignored?? && ignored?size > 0) >
                    <label class="pull-left">忽略课程类型 :
                        <#list ignored as ii>
                            <label class="label label-default">${ii}</label>
                        </#list>
                    </label>
                </#if>
                <div class="pull-right">
                    <span>* <span class="glyphicon glyphicon-alert"></span> 图标代表有课</span>
                    <a class="btn btn-primary btn-sm" href="/cs/terms/${term}">返回</a>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
        <#list timePoints?keys as week>
            <ul class="list-group">
                <li class="list-group-item"><h4>第 ${week} 周</h4></li>
            </ul>
            <table class="table list-timePoint table-bordered table-striped">
                <thead align="center">
                    <#list ["#"]+weekdaysZh as days>
                    <td width="2.5%">${days}</td>
                    </#list>
                </thead>
                <#list 1..12 as t>
                    <tr align="center">
                        <td>${t}</td>
                        <#list 0..6 as w>
                            <#assign key= w + "-" + t >
                            <#if !timePoints[week][key]??>
                                <td>&nbsp;&nbsp;</td>
                            <#else>
                                <td class="disabled">
                                    <span class="glyphicon glyphicon-alert"></span>
                                </td>
                            </#if>
                        </#list>
                    </tr>
                </#list>
            </table>
        </#list>
    </div>
</div>
</@temp.body>

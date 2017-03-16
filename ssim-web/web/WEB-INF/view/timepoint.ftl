<#import "temp/main.ftl" as temp>
<@temp.body title="无课时间查询">
<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
    <#list timepoints?keys as week>
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h1>Week ${week}</h1>
            </div>
            <ul class="list-group">
                <#--<#assign key=""/>-->
                <#list 0..6 as w>
                <#list 1..12 as t>
                <#assign key=w + "-" + t>
                    <#if !timepoints[key]??>
                        <div>${w} - ${t} 无课</div>
                    </#if>
                </#list>
                </#list>
            </ul>
        </div>
    </#list>
</div>
</@temp.body>
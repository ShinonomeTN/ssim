<#import "temp/main.ftl" as temp>
<@temp.body "Index - LNC CS Query">
<div class="page-header">
    <h1>简单课表查询 <small>for 岭南职业技术学院</small></h1>
</div>
<div class="row">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h2 align="center">学期</h2>
            </div>
            <div class="list-group">
                <#if (terms?? && (terms?size > 0)) >
                    <#list terms as term>
                        <a href="#" class="list-group-item">${term}</a>
                    </#list>
                <#else >
                    <a class="list-group-item disabled">抱歉，暂时无可查询学期...</a>
                </#if>
            </div>
        </div>
    </div>
</div>
</@temp.body>
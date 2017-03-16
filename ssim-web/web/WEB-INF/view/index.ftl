<#import "temp/main.ftl" as temp>
<@temp.body "简单课表查询 - 岭南软件园协会">
<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
    <#if (terms?? && terms?size > 0) >
        <div class="panel panel-primary fadeInDown animated">
            <div align="center" class="panel-heading">
                <#if busy??>
                    <@temp.siteBusyTitle status=busy/>
                <#else >
                    <h2>请选择学期</h2>
                </#if>
            </div>
            <div class="list-group">
                <#list terms as term>
                    <a class="list-group-item" href="/cs/terms/${term}">${term}</a>
                </#list>
            </div>
        </div>
    <#else>
        <div class="panel panel-primary swing animated">
            <div class="panel-body">
                <div class="page-header">
                    <h2>抱歉，暂无可查询数据</h2>
                </div>
                <p>我们还没往数据库内添加数据，请稍后再来</p>
            </div>
        </div>
    </#if>
    <div align="center" class="container-fluid">

    </div>
</div>
</@temp.body>
<#import "temp/main.ftl" as temp>
<@temp.body title="${term!'未知'}">
<div class="col-md-6 col-md-offset-3">
    <div class="panel panel-primary swing animated">
        <div align="center" class="panel-body">
            <div class="page-header">
                <h2>抱歉，没有数据</h2>
            </div>
            <div class="col-sm-8 col-sm-offset-2">
                <#if term??>
                    <a href="/cs/terms/${term}" class="btn btn-primary btn-block">返回 ${term}</a>
                </#if>
                <a href="/cs" class="btn btn-block btn-default">主页</a>
            </div>
        </div>
    </div>
</div>
</@temp.body>
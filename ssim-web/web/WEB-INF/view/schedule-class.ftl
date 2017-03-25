<#import "temp/main.ftl" as temp>
<@temp.body title="${className} 在第 ${week} 周的课程 - ${term}">
<div class="col-md-12">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="fadeInDown animated">
                <small><b>${term}</b></small>
                <br>${className}在第${week}周的课程
            </h3>
            <div>
                <#if (ignored?? && ignored?size > 0) >
                    <label class="pull-left">忽略课程类型 :
                        <#list ignored as ii>
                            <label class="label label-default">${ii}</label>
                        </#list>
                    </label>
                </#if>
                <button id="btn-nav-back" class="btn btn-primary btn-sm pull-right">返回</button>
                <div class="clearfix"></div>
            </div>
        </div>
        <@temp.scheduleTableRender lessonPosMap=schedule/>
        <@temp.scheduleListRender lessonPosMap=schedule/>
    </div>
</div>
</@temp.body>
<script>
    $(document).ready(function () {
        $("#btn-nav-back").click(function () {
            window.history.back();
        });
    });
</script>
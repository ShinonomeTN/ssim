<#import "temp/main.ftl" as temp>
<@temp.body title="${className} 在第 ${week} 周的课程 - ${term}">
<div class="col-md-12">
    <#assign weekdaysZh =["日","一","二","三","四","五","六"] />
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="fadeInDown animated">
                <small><b>${term}</b></small>
                <br>${className}在第${week}周的课程
            </h3>
            <div>
                <a class="btn btn-primary btn-sm pull-right" href="/cs/terms/${term}">返回</a>
                <div class="clearfix"></div>
            </div>
        </div>
        <table class="table table-striped table-condensed table-schedule hidden-xs hidden-sm">
            <thead align="center">
                <#list ["#"]+weekdaysZh as days>
                <td width="2.5%">${days}</td>
                </#list>
            </thead>
            <#list 1..9 as turns>
                <tr align="center">
                    <td>${turns}</td>
                    <#list 0..6 as day>
                        <td>
                            <#assign pos="${day}-${turns}">
                            <#if schedule[pos]??>
                                <#list schedule[pos] as lesson>
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
        <ul class="list-group hidden-md hidden-lg fadeIn animated list-schedule">
            <#list 0..6 as day>
                <li class="list-group-item">
                    <div class="media">
                        <h3 class="media-left">周 ${weekdaysZh[day]} </h3>
                        <div class="media-body">
                            <#assign lessonCount=0>
                            <#list 1..9 as turns>
                                <#assign pos="${day}-${turns}">
                                <#if schedule[pos]??>
                                    <div class="media">
                                        <h4 class="media-left">${turns}</h4>
                                        <div class="media-body">
                                            <#list schedule[pos] as lesson>
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
    </div>
</div>
</@temp.body>
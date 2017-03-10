<#import "temp/main.ftl" as temp>
<@temp.body title="${class} 在第 ${week} 周的课程 - ${term}">
<div class="col-md-12">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3>
                <small><b>${term}</b></small>
                <br>${class}在第${week}周的课程
            </h3>
            <div>
                <a class="btn btn-primary btn-sm pull-right" href="/terms/${term}">返回</a>
                <div class="clearfix"></div>
            </div>
        </div>
        <table class="table table-striped table-condensed table-schedule hidden-xs hidden-sm">
            <thead align="center">
                <#list ["#","日","一","二","三","四","五","六"] as days>
                <td width="2.5%">${days}</td>
                </#list>
            </thead>
            <#list 1..8 as turns>
                <tr align="center">
                    <td>${turns}</td>
                    <#list 0..6 as day>
                        <td>
                            <#list schedule as lesson>
                                <#if lesson.weekday == day && lesson.turn == turns>
                                <div class="item">
                                ${lesson.name} <label class="label label-default">${lesson.category}</label><small> at ${lesson.address}</small><br>
                                </div>
                                </#if>
                            </#list>
                        </td>
                    </#list>
                </tr>
            </#list>
        </table>
        <#assign weekdaysZh =["日","一","二","三","四","五","六"] />
        <ul class="list-group hidden-md hidden-lg">
            <#list schedule as lessons>
                <li class="list-group-item">
                    <div>周${weekdaysZh[lessons.weekday]} 第 ${lessons.turn} 节 : ${lessons.name}</div>
                    <p> at ${lessons.address} <label class="label label-info">${lessons.category}</label></p>
                </li>
            </#list>
        </ul>
    </div>
</div>
</@temp.body>
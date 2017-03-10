<#import "temp/main.ftl" as temp>
<@temp.body title="${class} 在第 ${week} 周的课程 - ${term}">
    <#list schedule as lesson>
        ${lesson}<br>
    </#list>
</@temp.body>
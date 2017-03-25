<#import "temp/main.ftl" as temp />
<form action="${webRoot}/schedule/teachers.html" method="get" accept-charset="utf-8">
    <input type="hidden" name="term" value="${term}">
    <div class="form-group">
        <label>任课教师</label>
        <select name="teacher" class="selectpicker form-control" required multiple
                data-live-search="true" title="请选任课教师">
        <#list teachers as teacher>
            <option value="${teacher}">${teacher}</option>
        </#list>
        </select>
    </div>
    <div class="form-group">
        <label>周数</label>
        <select name="week" class="selectpicker form-control select-less-item" required
                title="指定周数">
        <#list 1..weeks as aWeek>
            <option value="${aWeek}">第 ${aWeek} 周</option>
        </#list>
        </select>
    </div>
    <div class="form-group">
        <label>忽略课程类型（可选）</label>
        <select name="ignoreType" class="selectpicker form-control select-less-item" multiple
                title="选择以忽略……">
        <#list categories as category >
            <option value="${category}">${category}</option>
        </#list>
        </select>
    </div>
    <div class="form-group">
        <div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
            <input type="submit" class="btn btn-success btn-block" value="查询">
            <a href="${webRoot}/" class="btn btn-default btn-block">返回首页</a>
        </div>
    </div>
</form>
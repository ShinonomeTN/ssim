<#import "temp/main.ftl" as temp />
<form action="/cs/terms/${term}/timepoints" method="get" accept-charset="utf-8">
    <input type="hidden" name="termName" value="${term}" required>
    <div class="form-group">
        <label>班级</label>
        <select name="class" class="selectpicker form-control" required multiple
                data-live-search="true" title="请选择班级">
        <#list classes as className>
            <option value="${className}">${className}</option>
        </#list>
        </select>
    </div>
    <div class="form-group">
        <label>周数</label>
        <select name="week" class="selectpicker form-control" required multiple
                title="指定周数（可多选）">
        <#list 1..weeks as aWeek>
            <option value="${aWeek}">第 ${aWeek} 周</option>
        </#list>
        </select>
    </div>
    <div class="form-group">
        <label>忽略课程类型</label>
        <select name="ignoreType" class="selectpicker form-control" multiple
                title="选择以忽略……">
        <#list categories as category >
            <option value="${category}">${category}</option>
        </#list>
        </select>
    </div>
    <div class="form-group">
        <div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
            <input type="submit" class="btn btn-success btn-block" value="查询">
            <a href="${webroot}" class="btn btn-default btn-block">返回首页</a>
        </div>
    </div>
</form>
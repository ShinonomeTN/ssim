<#import "temp/main.ftl" as temp>
<@temp.body title="${term}">
<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
    <div class="panel panel-primary fadeInDown animated">
        <div class="panel-heading">
            <h3>查询 ${term} 的课程</h3>
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#form-schedule" role="tab" data-toggle="tab">周课程表</a></li>
                <li role="presentation"><a href="#form-timepoint" role="tab" data-toggle="tab">无课时间</a></li>
            </ul>
            <div class="tab-content">
                <div id="form-schedule" role="tabpanel" class="tab-pane active">
                    <div class="container-fluid">
                        <form action="/cs/schedule" method="get" accept-charset="utf-8">
                            <input type="hidden" name="termName" value="${term}" required>
                            <div class="form-group">
                                <label>班级</label>
                                <select name="class" class="selectpicker form-control" required data-live-search="true" title="请选择班级">
                                    <#list classes as className>
                                        <option value="${className}">${className}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>周数</label>
                                <div class="input-group">
                                    <input name="week" class="form-control" type="number" required max="${weeks}" placeholder="本学期最大${weeks}周">
                                    <div class="input-group-addon">周</div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>忽略课程类型</label>
                                <select name="ignoreType" class="selectpicker form-control" multiple title="选择以忽略……">
                                    <#list categories as category >
                                        <option value="${category}">${category}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="form-group">
                                <div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                                    <input type="submit" class="btn btn-success btn-block" value="查询">
                                    <a href="/cs" class="btn btn-default btn-block">返回首页</a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="form-timepoint">
                    <div class="container-fluid">
                        <form action="/cs/term/${term}/timepoint" method="get" accept-charset="utf-8">
                            <input type="hidden" name="termName" value="${term}" required>
                            <div class="form-group">
                                <label>班级</label>
                                <select name="class" class="selectpicker form-control" required multiple data-live-search="true" title="请选择班级">
                                    <#list classes as className>
                                        <option value="${className}">${className}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>周数</label>
                                <select name="week" class="selectpicker form-control" required multiple title="指定周数（可多选）">
                                    <#list 1..weeks as aWeek>
                                        <option value="${aWeek}">第 ${aWeek} 周</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>忽略课程类型</label>
                                <select name="ignoreType" class="selectpicker form-control" multiple title="选择以忽略……">
                                    <#list categories as category >
                                        <option value="${category}">${category}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="form-group">
                                <div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                                    <input type="submit" class="btn btn-success btn-block" value="查询">
                                    <a href="/cs" class="btn btn-default btn-block">返回首页</a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@temp.body>
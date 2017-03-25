<#import "temp/main.ftl" as temp>
<@temp.body title="${term}">
<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
    <div class="panel panel-primary fadeInDown animated">
        <div class="panel-heading">
            <h3>查询 ${term} 的课程</h3>
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#form-schedule" role="tab" data-toggle="tab">周课程表</a>
                </li>
                <li role="presentation"><a href="#form-timepoint" role="tab" data-toggle="tab">无课时间</a></li>
            </ul>
            <div class="tab-content" style="margin-top: 5pt">
                <div id="form-schedule" role="tabpanel" class="tab-pane active">
                    <iframe frameborder="0" href="/query">

                    </iframe>
                </div>
                <div role="tabpanel" class="tab-pane" id="form-timepoint">

                </div>
            </div>
        </div>
    </div>
</div>
</@temp.body>
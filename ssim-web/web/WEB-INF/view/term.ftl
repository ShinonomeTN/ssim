<#import "temp/main.ftl" as temp>
<@temp.body title="${term}">
<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
    <div class="panel panel-primary fadeIn animated">
        <div class="panel-heading">
            <h3>查询 ${term} 的课程</h3>
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs" id="qf_tab_list">
                <li><a href="#query_form" data-target="schedule">周课表</a></li>
                <li><a href="#query_form" data-target="timepoint">无课表</a></li>
                <li><a href="#query_form" data-target="teacher">教师课表</a></li>
            </ul>
            <div id="query_form" style="margin-top: 5pt"></div>
        </div>
    </div>
</div>
</@temp.body>
<script>
    $(document).ready(function () {
        var qFormNode = $("#query_form");
        var tabList = $("#qf_tab_list").find("li");

        $(tabList).each(function (i,obj) {
            var t_content = $(obj).find("a").first();
            obj.index = i;
            $(t_content).click(function () {
                var target = $(t_content).data("target");
                var index = obj.index;

                $("#qf_tab_list").find("li").each(function (i, obj) {
                    if(i != index) $(obj).removeClass("active"); else $(obj).addClass("active");
                });

                $(qFormNode).html("<div class='loading-container'><span class='summary-thumbnail spinner'></span></div>");
                $(qFormNode).load("${webRoot}/terms/${term}/form.html?type="+target,function (content, status, xhr) {
                    switch (status){
                        case "success":
                            $('.selectpicker').selectpicker({style: 'btn-default', size: 10});

                            if( /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ) {
                                $('.select-less-item').selectpicker('mobile');
                            }

                            break;
                        case "error":
                            $(qFormNode).html("<div class='loading-container' align='center'><span class='glyphicon glyphicon-remove-circle'> </span> 网络错误</div>")
                            break;
                        default:
                            console.log(xhr.status);
                            break;
                    }
                });
            });
        });

        $($(tabList).find("a").first()).trigger("click");
    });
</script>
package cn.lncsa.ssim.web.controller;

import cn.lncsa.ssim.web.services.CoursesUpdateServices;
import cn.lncsa.ssim.web.services.RedisServices;
import cn.lncsa.ssim.web.services.util.CaptureThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by cattenlinger on 2017/3/9.
 */
@Controller
@RequestMapping("/api/manage")
public class ManageController {

    private CoursesUpdateServices coursesUpdateServices;
    private RedisServices redisServices;

    @Autowired
    public void setCoursesUpdateServices(CoursesUpdateServices coursesUpdateServices) {
        this.coursesUpdateServices = coursesUpdateServices;
    }

    @Autowired
    public void setRedisServices(RedisServices redisServices) {
        this.redisServices = redisServices;
    }

    @RequestMapping("/task")
    public Model getTaskStatus(Model model){
        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }

    @RequestMapping("/task/fire")
    public Model startTask(@RequestParam("code") String taskCode, Model model){

        switch (coursesUpdateServices.getStatus()){
            case CaptureThread.STATUS_ERROR:
            case CaptureThread.STATUS_READY:
            case CaptureThread.STATUS_STOPPED:
            case CaptureThread.STATUS_FINISHED:
                coursesUpdateServices.setTaskCode(taskCode);
                coursesUpdateServices.fireCaptureThread(false);
                break;

            default:
                break;
        }

        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }

    @RequestMapping("/task/import")
    public Model startImport(Model model){
        switch (coursesUpdateServices.getStatus()){
            case CaptureThread.STATUS_ERROR:
            case CaptureThread.STATUS_READY:
            case CaptureThread.STATUS_FINISHED:
            case CaptureThread.STATUS_STOPPED:
                coursesUpdateServices.fireCaptureThread(true);
                break;

            default:
                break;
        }

        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }

    @RequestMapping("/task/terminate")
    public Model stopTask(Model model){
        switch (coursesUpdateServices.getStatus()){
            case CaptureThread.STATUS_IMPORTING:
            case CaptureThread.STATUS_CAPTURING:
                coursesUpdateServices.terminateCaptureThread();
                break;

            default:
                break;
        }
        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }

    @RequestMapping("/cache/clear")
    public Model clearCache(Model model){
        coursesUpdateServices.clearCache();
        model.addAttribute("result",true);
        return model;
    }
}

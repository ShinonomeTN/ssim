package cn.lncsa.ssim.web.controller;

import cn.lncsa.ssim.web.services.CoursesUpdateServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cattenlinger on 2017/3/9.
 */
@Controller
@RequestMapping("/api/manage")
public class ManageController {

    private CoursesUpdateServices coursesUpdateServices;
    @Autowired
    public void setCoursesUpdateServices(CoursesUpdateServices coursesUpdateServices) {
        this.coursesUpdateServices = coursesUpdateServices;
    }

    @RequestMapping("/task")
    public Model getTaskStatus(Model model){
        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }

    @RequestMapping("/task/fire")
    public Model startTask(@RequestParam("code") String taskCode, Model model){
        coursesUpdateServices.setTaskCode(taskCode);
        coursesUpdateServices.fireCaptureThread(false);

        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }

    @RequestMapping("/task/import")
    public Model startImport(Model model){
        coursesUpdateServices.fireCaptureThread(true);
        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }

    @RequestMapping("/task/terminate")
    public Model stopTask(Model model){
        coursesUpdateServices.terminateCaptureThread();
        model.addAttribute("status",coursesUpdateServices.getStatus());
        model.addAttribute("task",coursesUpdateServices.getTaskStatus());
        return model;
    }
}

package net.catten.ssim.web.controller;

import net.catten.ssim.web.services.LessonServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * Created by catten on 3/7/17.
 */
@Controller
@RequestMapping("/api")
public class ApiController {

    private Logger logger = Logger.getLogger(this.getClass());

    private LessonServices lessonServices;

    @Autowired
    public void setLessonServices(LessonServices lessonServices) {
        this.lessonServices = lessonServices;
    }

    @RequestMapping("/terms")
    public Model terms(Model model) {
        model.addAttribute("data", lessonServices.querySchoolTerms());
        return model;
    }

    @RequestMapping("/weeks")
    public Model termWeekCount(@RequestParam("termName") String termName, Model model) {
        logger.info(termName);
        model.addAttribute("weeks",lessonServices.queryWeeks(termName));
        return model;
    }

    @RequestMapping("/classes")
    public Model termClasses(@RequestParam("termName") String termName, Model model) {
        model.addAttribute("data",lessonServices.listClassesInTerm(termName));
        return model;
    }

    @RequestMapping("/lessons")
    public Model weekClasses(
            @RequestParam("termName") String termName,
            @RequestParam(value = "week",required = false) Integer week,
            @RequestParam("className") String className,
            Model model){
        if(week != null){
            model.addAttribute("data",lessonServices.queryScheduleInTermInWeek(termName,className,week));
        }else {
            model.addAttribute("data",lessonServices.queryScheduleInTerm(termName,className));
        }
        return model;
    }
}

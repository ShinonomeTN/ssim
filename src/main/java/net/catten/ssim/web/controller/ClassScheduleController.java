package net.catten.ssim.web.controller;

import net.catten.ssim.web.model.Lesson;
import net.catten.ssim.web.services.LessonServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by catten on 3/7/17.
 */
@Controller
@RequestMapping("/api")
public class ClassScheduleController {

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
        model.addAttribute("data", lessonServices.queryWeeks(termName));
        return model;
    }

    @RequestMapping("/classes")
    public Model termClasses(@RequestParam("termName") String termName, Model model) {
        model.addAttribute("data", lessonServices.listClassesInTerm(termName));
        return model;
    }

    @RequestMapping("/lessons")
    public Model lessons(
            @RequestParam("termName") String termName,
            @RequestParam(value = "week", required = false) Integer week,
            @RequestParam("className") String className,
            @RequestParam(value = "ignoreType", required = false) List<String> ignoreTypes,
            Model model) {

        model.addAttribute("data", lessonServices.querySchedule(termName, className, week, ignoreTypes));

        return model;
    }

    @RequestMapping("/types")
    public Model lessonTypes(Model model) {
        model.addAttribute("data",lessonServices.listClassTypes());
        return model;
    }
}

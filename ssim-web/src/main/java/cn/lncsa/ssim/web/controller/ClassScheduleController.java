package cn.lncsa.ssim.web.controller;

import cn.lncsa.ssim.web.services.LessonServices;
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

    private LessonServices lessonSrv;

    @Autowired
    public void setLessonSrv(LessonServices lessonSrv) {
        this.lessonSrv = lessonSrv;
    }

    @RequestMapping("/terms")
    public Model terms(Model model) {
        model.addAttribute("data", lessonSrv.querySchoolTerms());
        return model;
    }

    @RequestMapping("/weeks")
    public Model termWeekCount(@RequestParam("termName") String termName, Model model) {
        model.addAttribute("data", lessonSrv.queryWeeks(termName));
        return model;
    }

    @RequestMapping("/classes")
    public Model termClasses(@RequestParam("termName") String termName, Model model) {
        model.addAttribute("data", lessonSrv.listClassesInTerm(termName));
        return model;
    }

    @RequestMapping("/lessons")
    public Model lessons(
            @RequestParam("termName") String termName,
            @RequestParam("week") Integer week,
            @RequestParam("className") String className,
            @RequestParam(value = "ignoreType", required = false) List<String> ignoreTypes,
            Model model) {

        model.addAttribute("data", lessonSrv.querySchedule(termName, className, week, ignoreTypes));

        return model;
    }

    @RequestMapping("/types")
    public Model lessonTypes(@RequestParam(value = "term",required = false) String term, Model model) {
        model.addAttribute("data", term == null ? lessonSrv.listClassTypes() : lessonSrv.listClassTypes(term));
        return model;
    }

    @RequestMapping("/lessons/timepoint")
    public Model timepoint(
            @RequestParam("termName") String termName,
            @RequestParam("class") List<String> className,
            @RequestParam("week") List<Integer> weeks,
            @RequestParam(value = "ignoreType",required = false) List<String> ignoreType,
            Model model){

        model.addAttribute("data",lessonSrv.getLessonTimePoint(
                termName,className,weeks,ignoreType
        ));
        return model;
    }
}

package net.catten.ssim.web.controller;

import net.catten.ssim.web.services.LessonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by catten on 3/7/17.
 */
@Controller
@RequestMapping("/api")
public class ApiController {

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

    @RequestMapping("/terms/{termName}/weeks")
    public Model termWeekCount(@PathVariable("termName") String termName, Model model) {
        model.addAttribute("weeks",lessonServices.queryWeeks(termName));
        return model;
    }

    @RequestMapping("/terms/{termName}/classes")
    public Model termClasses(@PathVariable("termName") String termName, Model model) {
        model.addAttribute("data",lessonServices.listClassesInTerm(termName));
        return model;
    }
}

package net.catten.ssim.web.controller;

import net.catten.ssim.web.services.LessonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by catten on 3/6/17.
 */
@Controller
@RequestMapping("")
public class IndexCtl {

    private LessonServices lessonServices;

    @Autowired
    public void setLessonServices(LessonServices lessonServices) {
        this.lessonServices = lessonServices;
    }

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("terms",lessonServices.querySchoolTerms());
        return "index";
    }
}

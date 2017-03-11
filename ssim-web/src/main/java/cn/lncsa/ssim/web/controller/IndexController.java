package cn.lncsa.ssim.web.controller;

import cn.lncsa.ssim.web.model.Lesson;
import cn.lncsa.ssim.web.services.LessonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cattenlinger on 2017/3/10.
 */
@Controller
@RequestMapping("")
public class IndexController {

    private LessonServices lessonServices;

    @Autowired
    public void setLessonServices(LessonServices lessonServices) {
        this.lessonServices = lessonServices;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("terms",lessonServices.querySchoolTerms());
        return "index";
    }

    @RequestMapping(value = "/terms/{termName}",method = RequestMethod.GET)
    public String termInfo(@PathVariable("termName") String termName, Model model){

        model.addAttribute("term",termName);
        Integer weekCount = lessonServices.queryWeeks(termName);
        if(weekCount == null) return "nodata";
        model.addAttribute("weeks",weekCount);

        model.addAttribute("classes",lessonServices.listClassesInTerm(termName));
        model.addAttribute("categories",lessonServices.listClassTypes());
        return "term";
    }

    @RequestMapping(value = "/schedule")
    public String querySchedule(
            @RequestParam("termName") String termName,
            @RequestParam("week") Integer week,
            @RequestParam("class") String className,
            @RequestParam(value = "ignoreType",required = false) List<String> ignoreTypes,
            Model model){

        model.addAttribute("term",termName);

        if(week == null) return "nodata";
        List<Lesson> lessons = lessonServices.querySchedule(termName, className, week, ignoreTypes);
        if(lessons == null || lessons.size() == 0) return "nodata";

        Queue<Lesson> sortedLessons = new LinkedList<>(lessons.parallelStream().sorted(
                Comparator.comparing(Lesson::getWeekday)
                        .thenComparing(Comparator.comparing(Lesson::getTurn)))
                .collect(Collectors.toList()));

//        List<Lesson>[][] lessonMap = new List[7][8];
//        for(Lesson lesson : lessons){
//            List<Lesson> turnLessons = lessonMap[lesson.getWeekday()][lesson.getTurn()];
//            if(turnLessons == null) {
//                lessonMap[lesson.getWeekday()][lesson.getTurn()] = new LinkedList<>();
//                turnLessons = lessonMap[lesson.getWeekday()][lesson.getTurn()];
//            }
//
//            turnLessons.add(lesson);
//        }

        model.addAttribute("schedule",sortedLessons);
        model.addAttribute("week",week);
        model.addAttribute("className",className);

        return "schedule";
    }
}

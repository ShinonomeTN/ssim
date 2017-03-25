package cn.lncsa.ssim.web.controller;

import cn.lncsa.ssim.web.model.Lesson;
import cn.lncsa.ssim.web.services.CoursesUpdateServices;
import cn.lncsa.ssim.web.services.LessonServices;
import cn.lncsa.ssim.web.services.util.CaptureThread;
import cn.lncsa.ssim.web.view.LessonTimePoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * Created by cattenlinger on 2017/3/10.
 */
@Controller
@RequestMapping("")
public class IndexController {

    private LessonServices lessonSrv;
    private CoursesUpdateServices coursesUpdateServices;

    @Autowired
    public void setLessonSrv(LessonServices lessonSrv) {
        this.lessonSrv = lessonSrv;
    }

    @Autowired
    public void setCoursesUpdateServices(CoursesUpdateServices coursesUpdateServices) {
        this.coursesUpdateServices = coursesUpdateServices;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("terms", lessonSrv.querySchoolTerms());
        switch (coursesUpdateServices.getStatus()) {
            case CaptureThread.STATUS_CAPTURING:
            case CaptureThread.STATUS_IMPORTING:
                model.addAttribute("busy", coursesUpdateServices.getStatus());
                break;

            default:
                break;
        }
        return "index";
    }

    @RequestMapping(value = "/terms/{termName}", method = RequestMethod.GET)
    public String termInfo(@PathVariable("termName") String termName, Model model) {

        model.addAttribute("term", termName);
        Integer weekCount = lessonSrv.queryWeeks(termName);
        if (weekCount == null) return "nodata";
        model.addAttribute("weeks", weekCount);

        model.addAttribute("classes", lessonSrv.listClassesInTerm(termName));
        model.addAttribute("categories", lessonSrv.listClassTypes(termName));
        return "term";
    }

    @RequestMapping("/terms/{termName}/form")
    public String termSchedule(
            @PathVariable("termName") String termName,
            @RequestParam("type") String formType,
            Model model){

        model.addAttribute("term", termName);
        Integer weekCount = lessonSrv.queryWeeks(termName);
        if (weekCount == null) return "nodata";
        model.addAttribute("weeks", weekCount);

        switch (formType){
            case "schedule":
            case "timepoint":
                model.addAttribute("classes", lessonSrv.listClassesInTerm(termName));
                model.addAttribute("categories", lessonSrv.listClassTypes(termName));
                switch (formType){
                    case "schedule": return "query-schedule";
                    case "timepoint": return "query-timepoint";
                    default: return "redirect:/";
                }
            case "teacher":
                model.addAttribute("teachers",lessonSrv.listTeacher(termName));
                return "query-teacher";
            default:
                return "redirect:/";

        }
    }

    @RequestMapping(value = "/schedule")
    public String querySchedule(
            @RequestParam("termName") String termName,
            @RequestParam("week") Integer week,
            @RequestParam("class") String className,
            @RequestParam(value = "ignoreType", required = false) List<String> ignoreTypes,
            Model model) {

        model.addAttribute("term", termName);

        if (week == null) return "nodata";
        List<Lesson> lessons = lessonSrv.querySchedule(termName, className, week, ignoreTypes);
        if (lessons == null || lessons.size() == 0) return "nodata";

        Map<String,List<Lesson>> lessonPositions = new HashMap<>();
        for (Lesson lesson : lessons){
            String pos = lesson.getWeekday().toString() + "-" + lesson.getTurn().toString();
            List<Lesson> posLesson = lessonPositions.computeIfAbsent(pos, k -> new ArrayList<>());
            posLesson.add(lesson);
        }

        model.addAttribute("schedule", lessonPositions);
        model.addAttribute("week", week);
        model.addAttribute("className", className);
        model.addAttribute("ignored",ignoreTypes);

        return "schedule";
    }

    @RequestMapping("/terms/{termName}/timepoints")
    public String timePoint(
            @PathVariable("termName") String termName,
            @RequestParam("class") List<String> className,
            @RequestParam("week") List<Integer> weeks,
            @RequestParam(value = "ignoreType",required = false) List<String> ignoreType,
            Model model){

        Map<String,Map<String,LessonTimePoint>> mappedPointsList = new HashMap<>();

        for (LessonTimePoint timePoint : lessonSrv
                .getLessonTimePoint(termName,className,weeks,ignoreType)){

            Map<String,LessonTimePoint> mappedPoints = mappedPointsList
                    .computeIfAbsent(String.valueOf(timePoint.getWeek()), k -> new HashMap<>());

            String key = timePoint.getDay() + "-" + timePoint.getTurn();
            mappedPoints.computeIfAbsent(key, k -> timePoint);
        }

        model.addAttribute("timePoints",mappedPointsList);
        model.addAttribute("classNames",className);
        model.addAttribute("weeks",weeks);
        model.addAttribute("ignored",ignoreType);
        model.addAttribute("term",termName);
        return "timepoint";
    }
}

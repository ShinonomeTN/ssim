package net.catten.ssim.schedule.factory;

import net.catten.ssim.web.model.Lesson;
import net.catten.ssim.schedule.model.KingoRawCourse;
import net.catten.ssim.schedule.model.KingoRawLesson;
import net.catten.ssim.schedule.model.Timepoint;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by catten on 2/24/17.
 */
public class LessonExpander {
    public List<Lesson> expand(KingoRawCourse rawCourse){
        List<Lesson> lessons = new LinkedList<>();

        for (KingoRawLesson rawLesson : rawCourse.getLessons()){
            for (Timepoint timepoint : rawLesson.getTimePoint().expand()){
                for (String c : rawLesson.getClassAttend()){
                    Lesson lesson = new Lesson();
                    lesson.setTerm(rawCourse.getTerm());
                    lesson.setUnit(rawCourse.getUnit());
                    lesson.setPeriod(rawCourse.getTimeSpend());
                    lesson.setCredit(rawCourse.getPoint());
                    lesson.setName(rawCourse.getName());
                    lesson.setTeacher(rawLesson.getTeacher());
                    lesson.setCategory(rawLesson.getClassType());
                    lesson.setAttendClass(c);
                    lesson.setWeek(timepoint.getWeek());
                    lesson.setWeekday(timepoint.getWeekday());
                    lesson.setTurn(timepoint.getTurn());
                    lesson.setAddress(rawLesson.getPosition());
                    lessons.add(lesson);
                }
            }
        }

        return lessons;
    }
}

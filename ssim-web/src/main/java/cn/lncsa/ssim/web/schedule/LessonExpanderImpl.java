package cn.lncsa.ssim.web.schedule;

import cn.lncsa.kingo.jw.cs.caterpillar.model.KingoRawCourse;
import cn.lncsa.kingo.jw.cs.caterpillar.model.KingoRawLesson;
import cn.lncsa.kingo.jw.cs.caterpillar.model.TimePoint;
import cn.lncsa.ssim.schedule.factory.LessonExpander;
import cn.lncsa.ssim.web.model.Lesson;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by catten on 2/24/17.
 */
public class LessonExpanderImpl implements LessonExpander<KingoRawCourse, Lesson> {
    @Override
    public List<Lesson> expand(KingoRawCourse rawCourse) {
        List<Lesson> lessons = new LinkedList<>();

        for (KingoRawLesson rawLesson : rawCourse.getLessons()) {
            for (TimePoint timepoint : rawLesson.getTimePoint().expand()) {
                for (String c : rawLesson.getClassAttend()) {
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

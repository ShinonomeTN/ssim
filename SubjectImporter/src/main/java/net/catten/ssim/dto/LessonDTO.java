package net.catten.ssim.dto;

import net.catten.ssim.schedule.model.Lesson;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by catten on 11/14/16.
 */
public class LessonDTO implements Cloneable,Serializable {
    private int id;
    private int subject;
    private String teacher;
    private String classNumber;
    private Integer attendAmount;
    private String classAttend;
    private String classType;
    private String assessmentType;
    private int weekday;
    private String timePoint;
    private String address;

    public static List<LessonDTO> splitLesson(Lesson lesson, Integer subjectId) {
        String classList[] = lesson.getClassAttend();
        List<Integer> weekdays = lesson.getWeeks();

        List<LessonDTO> lessons = new LinkedList<>();

        LessonDTO template = new LessonDTO();
        template.setSubject(subjectId);
        template.setTeacher(lesson.getTeacher());
        template.setAssessmentType(lesson.getAssessmentType());
        template.setClassNumber(lesson.getClassNumber());
        template.setAttendAmount(lesson.getAttendAmount());
        template.setClassType(lesson.getClassType());
        template.setTimePoint(lesson.getTimePoint().toString());
        template.setAddress(lesson.getPosition());

        for (String className : classList) {
            for (Integer weekday : weekdays) {
                LessonDTO clone = (LessonDTO) template.clone();
                clone.setClassAttend(className);
                clone.setWeekday(weekday);
                lessons.add(clone);
            }
        }

        return lessons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public Integer getAttendAmount() {
        return attendAmount;
    }

    public void setAttendAmount(Integer attendAmount) {
        this.attendAmount = attendAmount;
    }

    public String getClassAttend() {
        return classAttend;
    }

    public void setClassAttend(String classAttend) {
        this.classAttend = classAttend;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public int getWeek() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ignored) {}
        return null;
    }

    @Override
    public String toString() {
        return "LessonDTO{" +
                "id=" + id +
                ", subject=" + subject +
                ", teacher='" + teacher + '\'' +
                ", classNumber='" + classNumber + '\'' +
                ", attendAmount=" + attendAmount +
                ", classAttend='" + classAttend + '\'' +
                ", classType='" + classType + '\'' +
                ", assessmentType='" + assessmentType + '\'' +
                ", weekday=" + weekday +
                ", timePoint='" + timePoint + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

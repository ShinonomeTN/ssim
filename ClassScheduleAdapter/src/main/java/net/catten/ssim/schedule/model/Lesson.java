package net.catten.ssim.schedule.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by catten on 11/12/16.
 */
public class Lesson {
    private String teacher;//任课老师
    private String classNumber;//上课班号
    private Integer attendAmount;//上课人数
    private String[] classAttend;//上课班级构成
    private String classType;//课程类别
    private String assessmentType;//考核方式
    private List<Integer> weeks;//周次
    private String timePoint;//节次
    private String position;//地点

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

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeek(String parse){
        String[] list = parse.split(",");
        List<Integer> _weeks = new LinkedList<>();
        for (String s : list){
            String[] range = s.split("-");
            if(range.length == 1) {
                _weeks.add(Integer.parseInt(range[0]));
            }else {
                int _startWeek = Integer.parseInt(range[0]);
                int _endWeek = Integer.parseInt(range[range.length - 1]);
                for (int i = _startWeek; i <= _endWeek; i++){
                    _weeks.add(i);
                }
            }
        }
        this.weeks = _weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String[] getClassAttend() {
        return classAttend;
    }

    public void setClassAttend(String[] classAttend) {
        this.classAttend = classAttend;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "teacher='" + teacher + '\'' +
                ", classNumber='" + classNumber + '\'' +
                ", attendAmount=" + attendAmount +
                ", classAttend=" + Arrays.toString(classAttend) +
                ", classType='" + classType + '\'' +
                ", assessmentType='" + assessmentType + '\'' +
                ", weeks=" + weeks +
                ", timePoint=" + timePoint +
                ", position='" + position + '\'' +
                '}';
    }
}

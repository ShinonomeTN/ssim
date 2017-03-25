package cn.lncsa.ssim.web.model;

import javax.persistence.*;

/**
 * Created by catten on 2/24/17.
 */
@Entity
@Cacheable
@Table(name = "lesson",indexes = {
        @Index(name = "index_term",columnList = "term"),
        @Index(name = "index_attend_class", columnList = "attendClass"),
        @Index(name = "index_category", columnList = "category"),
        @Index(name = "index_teacher", columnList = "teacher")
})
public class Lesson {
    private Long id;
    private String term;
    private String unit;
    private String name;
    private Double period;
    private Double credit;
    private String teacher;
    private String category;
    private String attendClass;
    private Integer week;
    private Integer weekday;
    private Integer turn;
    private String address;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Double getPeriod() {
        return period;
    }

    public void setPeriod(Double period) {
        this.period = period;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAttendClass() {
        return attendClass;
    }

    public void setAttendClass(String attendClass) {
        this.attendClass = attendClass;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", term='" + term + '\'' +
                ", unit='" + unit + '\'' +
                ", name='" + name + '\'' +
                ", period=" + period +
                ", credit=" + credit +
                ", teacher='" + teacher + '\'' +
                ", category='" + category + '\'' +
                ", attendClass='" + attendClass + '\'' +
                ", week=" + week +
                ", weekday=" + weekday +
                ", turn=" + turn +
                ", address='" + address + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

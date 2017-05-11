package cn.lncsa.ssim.web.model;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Created by catten on 5/11/17.
 */
public class SchoolCalender {
    private Integer id;
    private String termName;
    private Date termStart;
    private Integer termEndWeek;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public Date getTermStart() {
        return termStart;
    }

    public void setTermStart(Date termStart) {
        this.termStart = termStart;
    }

    public Integer getTermEndWeek() {
        return termEndWeek;
    }

    public void setTermEndWeek(Integer termEndWeek) {
        this.termEndWeek = termEndWeek;
    }

}

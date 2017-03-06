package net.catten.ssim.web.services;

import net.catten.ssim.web.model.Lesson;
import net.catten.ssim.web.repositories.LessonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by catten on 2/24/17.
 */
@Service
public class LessonServices {

    private LessonDAO lessonDAO;

    @Autowired
    public void setLessonDAO(LessonDAO lessonDAO) {
        this.lessonDAO = lessonDAO;
    }

    public List<String> querySchoolTerms(){
        return lessonDAO.getTerms();
    }

    public List<Lesson> querySchedule(String termName, String attendClass){
        return lessonDAO.findAll((root, cq, cb) -> cb.and(
                cb.equal(root.get("term"),termName),
                cb.equal(root.get("attendClass"),attendClass))
        );
    }

    public List<Lesson> querySchedule(String termName,String attendClass, Integer week, List<String> ignoreType){
        return lessonDAO.findAll((root, cq, cb) -> {
            Predicate predicate = cb.and(
                    cb.equal(root.get("term"),termName),
                    cb.equal(root.get("attendClass"),attendClass)
            );

            if(week != null) predicate = cb.and(
                    predicate,
                    cb.equal(root.get("week"),week)
            );

            if(ignoreType != null && ignoreType.size() > 0) predicate = cb.and(
                    predicate,
                    cb.not(root.get("category").in(ignoreType))
            );

            return predicate;
        });
    }

    public List<Lesson> querySchedule(String termName, String attendClass, Integer week){
        return lessonDAO.findAll((root, criteriaQuery, cb) -> cb.and(
                cb.equal(root.get("term"),termName),
                cb.equal(root.get("attendClass"),attendClass),
                cb.equal(root.get("week"),week)
        ));
    }

    public Integer queryWeeks(String termName){
        return lessonDAO.getLatestWeek(termName);
    }

    public List<String> listClassesInTerm(String termName){
        return lessonDAO.getAttendClassesInTerm(termName);
    }
}

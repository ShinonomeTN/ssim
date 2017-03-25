package cn.lncsa.ssim.web.services;

import cn.lncsa.ssim.web.model.Lesson;
import cn.lncsa.ssim.web.repositories.LessonDAO;
import cn.lncsa.ssim.web.view.LessonTimePoint;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by catten on 2/24/17.
 */
@Service
public class LessonServices {

    Logger logger = Logger.getLogger(this.getClass());

    private LessonDAO lessonDAO;
    private RedisServices redisSrv;

    @Autowired
    public void setLessonDAO(LessonDAO lessonDAO) {
        this.lessonDAO = lessonDAO;
    }

    @Autowired
    public void setRedisSrv(RedisServices redisSrv) {
        this.redisSrv = redisSrv;
    }

    public List<String> querySchoolTerms() {
        String key = RedisServices.KEY_TERM_LIST;
        if (redisSrv.exist(key)) return redisSrv.getList(key);
        else {
            logger.info(String.format("Buffer has no %s , load from database.", key));
            List<String> termList = lessonDAO.getTerms();
            redisSrv.putToList(key,termList);
            return termList;
        }
    }

    public List<Lesson> querySchedule(String termName, String attendClass) {
        return lessonDAO.findAll((root, cq, cb) -> cb.and(
                cb.equal(root.get("term"), termName),
                cb.equal(root.get("attendClass"), attendClass))
        );
    }

    public List<Lesson> querySchedule(String termName, String attendClass, Integer week, List<String> ignoreType) {
        return lessonDAO.findAll((root, cq, cb) -> {
            Predicate predicate = cb.and(
                    cb.equal(root.get("term"), termName),
                    cb.equal(root.get("attendClass"), attendClass)
            );

            if (week != null) predicate = cb.and(
                    predicate,
                    cb.equal(root.get("week"), week)
            );

            if (ignoreType != null && ignoreType.size() > 0) predicate = cb.and(
                    predicate,
                    cb.not(root.get("category").in(ignoreType))
            );

            return predicate;
        });
    }

    public List<Lesson> querySchedule(String termName, String attendClass, Integer week) {
        return lessonDAO.findAll((root, criteriaQuery, cb) -> cb.and(
                cb.equal(root.get("term"), termName),
                cb.equal(root.get("attendClass"), attendClass),
                cb.equal(root.get("week"), week)
        ));
    }

    public Integer queryWeeks(String termName) {
        String keyHash = RedisServices.KEY_PREFIX_WEEKS + String.valueOf(termName.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getInt(keyHash);
        else {
            logger.info(String.format("Weeks at %s(%s) doesn't been cached , load from database.", termName, keyHash));
            Integer weeks = lessonDAO.getLatestWeek(termName);
            redisSrv.setInt(keyHash,weeks);
            return weeks;
        }
    }

    public List<String> listClassesInTerm(String termName) {
        String keyHash = RedisServices.KEY_PREFIX_CLASS + String.valueOf(termName.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getList(keyHash);
        else {
            logger.info(String.format(" %s(%s) doesn't been cached , load from database.", termName, keyHash));
            List<String> classNames = lessonDAO.getAttendClassesInTerm(termName);
            redisSrv.putToList(keyHash,classNames);
            return classNames;
        }
    }

    public List<String> listClassTypes() {
        String key = RedisServices.KEY_TYPES;
        if(redisSrv.exist(key)) return redisSrv.getList(key);
        else {
            logger.info(String.format("Global class types %s doesn't been cached, load from database.", key));
            List<String> typeList = lessonDAO.getTypes();
            redisSrv.putToList(key,typeList);
            return typeList;
        }
    }

    public List<String> listClassTypes(String termName){
        String keyHash = RedisServices.KEY_PREFIX_TYPES + String.valueOf(termName.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getList(keyHash);
        else {
            logger.info(String.format("Class types at %s(%s) doesn't been cached, load from database.", termName, keyHash));
            List<String> typeList = lessonDAO.getTermTypes(termName);
            redisSrv.putToList(keyHash,typeList);
            return typeList;
        }
    }

    public List<String> listTeacher(String termName){
        String keyHash = RedisServices.KEY_PREFIX_TEACHER + String.valueOf(termName.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getList(keyHash);
        else {
            logger.info(String.format("Teachers at %s(%s) doesn't been cached, load from database.",termName,keyHash));
            List<String> teachers = lessonDAO.teachersInTerm(termName);
            redisSrv.putToList(keyHash,teachers);
            return teachers;
        }
    }

    public List<LessonTimePoint> getLessonTimePoint(String termName, List<String> className, List<Integer> weeks, List<String> ignoreType){
        List<Object[]> list = (ignoreType == null || ignoreType.size() == 0) ?
                lessonDAO.classTimePointList(termName,className,weeks) : lessonDAO.classTimePointList(termName,className,weeks,ignoreType);
        List<LessonTimePoint> timePoints = new ArrayList<>();
        for (Object[] objects : list){
            timePoints.add(new LessonTimePoint((Integer)objects[0], (Integer)objects[1], (Integer)objects[2]));
        }
        return timePoints;
    }

    public List<Lesson> queryTeacherSchedule(String termName, Integer week, String teacherName, List<String> ignoreTypes){
        return lessonDAO.findAll((root, cq, cb) -> {
            Predicate predicate = cb.and(
                    cb.equal(root.get("term"),termName),
                    cb.equal(root.get("teacher"),teacherName),
                    cb.equal(root.get("week"),week));

            if(ignoreTypes != null && ignoreTypes.size() > 0) predicate = cb.and(predicate,root.get("category").in(ignoreTypes));

            return predicate;
        });
    }
}

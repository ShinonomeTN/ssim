package cn.lncsa.ssim.web.services;

import cn.lncsa.ssim.web.model.Lesson;
import cn.lncsa.ssim.web.repositories.LessonDAO;
import cn.lncsa.ssim.web.view.LessonTimePoint;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
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
        if (redisSrv.exist(RedisServices.KEY_TERM_LIST)) return redisSrv.getList(RedisServices.KEY_TERM_LIST);
        else {
            logger.info("Buffer has no " + RedisServices.KEY_TERM_LIST + " , load from database.");
            List<String> termList = lessonDAO.getTerms();
            redisSrv.putToList(RedisServices.KEY_TERM_LIST,termList);
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
        String key = RedisServices.KEY_PREFIX_WEEKS + termName;
        if(redisSrv.exist(key)) return redisSrv.getInt(key);
        else {
            logger.info("Buffer has no " + key + " , load from database.");
            Integer weeks = lessonDAO.getLatestWeek(termName);
            redisSrv.setInt(key,weeks);
            return weeks;
        }
    }

    public List<String> listClassesInTerm(String termName) {
        String key = RedisServices.KEY_PREFIX_CLASS + termName;
        if(redisSrv.exist(key)) return redisSrv.getList(key);
        else {
            logger.info("Buffer has no " + key + " , load from database.");
            List<String> classNames = lessonDAO.getAttendClassesInTerm(termName);
            redisSrv.putToList(key,classNames);
            return classNames;
        }
    }

    public List<String> listClassTypes() {
        if(redisSrv.exist(RedisServices.KEY_TYPES)) return redisSrv.getList(RedisServices.KEY_TYPES);
        else {
            logger.info("Buffer has no " + RedisServices.KEY_TYPES + " , load from database.");
            List<String> typeList = lessonDAO.getTypes();
            redisSrv.putToList(RedisServices.KEY_TYPES,typeList);
            return typeList;
        }
    }

    public List<String> listClassTypes(String termName){
        String key = RedisServices.KEY_PREFIX_TYPES + termName;
        if(redisSrv.exist(key)) return redisSrv.getList(key);
        else {
            logger.info("Buffer has no " + key + " , load from database.");
            List<String> typeList = lessonDAO.getTermTypes(termName);
            redisSrv.putToList(key,typeList);
            return typeList;
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
}

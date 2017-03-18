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
        String key = RedisServices.KEY_TERM_LIST;
        String keyHash = RedisServices.KEY_TERM_LIST + String.valueOf(key.hashCode());
        if (redisSrv.exist(keyHash)) return redisSrv.getList(keyHash);
        else {
            logger.info(String.format("Buffer has no %s(%s) , load from database.", key, keyHash));
            List<String> termList = lessonDAO.getTerms();
            redisSrv.putToList(keyHash,termList);
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
        String keyHash = RedisServices.KEY_PREFIX_WEEKS + String.valueOf(key.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getInt(keyHash);
        else {
            logger.info(String.format("Buffer has no %s(%s) , load from database.", key, keyHash));
            Integer weeks = lessonDAO.getLatestWeek(termName);
            redisSrv.setInt(keyHash,weeks);
            return weeks;
        }
    }

    public List<String> listClassesInTerm(String termName) {
        String key = RedisServices.KEY_PREFIX_CLASS + termName;
        String keyHash = RedisServices.KEY_PREFIX_CLASS + String.valueOf(key.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getList(keyHash);
        else {
            logger.info(String.format("Buffer has no %s(%s) , load from database.", key, keyHash));
            List<String> classNames = lessonDAO.getAttendClassesInTerm(termName);
            redisSrv.putToList(keyHash,classNames);
            return classNames;
        }
    }

    public List<String> listClassTypes() {
        String key = RedisServices.KEY_TYPES;
        String keyHash = RedisServices.KEY_TYPES + String.valueOf(key.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getList(keyHash);
        else {
            logger.info(String.format("Buffer has no %s(%s) , load from database.", key, keyHash));
            List<String> typeList = lessonDAO.getTypes();
            redisSrv.putToList(keyHash,typeList);
            return typeList;
        }
    }

    public List<String> listClassTypes(String termName){
        String key = RedisServices.KEY_PREFIX_TYPES + termName;
        String keyHash = RedisServices.KEY_PREFIX_TYPES + String.valueOf(key.hashCode());
        if(redisSrv.exist(keyHash)) return redisSrv.getList(keyHash);
        else {
            logger.info(String.format("Buffer has no %s(%s) , load from database.", key, keyHash));
            List<String> typeList = lessonDAO.getTermTypes(termName);
            redisSrv.putToList(keyHash,typeList);
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

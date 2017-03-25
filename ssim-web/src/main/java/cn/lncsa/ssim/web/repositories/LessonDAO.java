package cn.lncsa.ssim.web.repositories;

import cn.lncsa.ssim.web.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by catten on 2/24/17.
 */
@Repository
public interface LessonDAO extends JpaRepository<Lesson,Long>, JpaSpecificationExecutor<Lesson>{

    @Query(nativeQuery = true, value = "select distinct term from lesson order by term desc ")
    List<String> getTerms();

    @Query(nativeQuery = true, value = "select max(week) from lesson where term = ?1")
    Integer getLatestWeek(String term);

    @Query(nativeQuery = true, value = "select distinct attendClass from lesson " +
            "where term = ?1 order by attendClass desc")
    List<String> getAttendClassesInTerm(String term);

    @Query(nativeQuery = true, value = "select distinct category from lesson")
    List<String> getTypes();

    @Query(nativeQuery = true, value = "select distinct category from lesson where term = ?1")
    List<String> getTermTypes(String termName);

    @Query(nativeQuery = true, value = "SELECT DISTINCT week,weekday,turn from lesson " +
            "WHERE term = ?1 AND attendClass in ?2 AND week in ?3 AND category not in ?4")
    List<Object[]> classTimePointList(String termName, List<String> attendClass, List<Integer> week, List<String> ignoreType);

    @Query(nativeQuery = true, value = "SELECT DISTINCT week,weekday,turn from lesson " +
            "WHERE term = ?1 AND attendClass in ?2 AND week in ?3")
    List<Object[]> classTimePointList(String termName, List<String> attendClass, List<Integer> week);

    @Query(nativeQuery = true, value = "select distinct teacher from lesson where term = ?1")
    List<String> teachersInTerm(String termName);
}

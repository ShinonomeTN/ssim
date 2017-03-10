package net.catten.ssim.web.repositories;

import net.catten.ssim.web.model.Lesson;
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

    @Query("select distinct l.term from Lesson l order by l.term desc ")
    List<String> getTerms();

    @Query("select max(l.week) from Lesson l where l.term = ?1")
    Integer getLatestWeek(String term);

    @Query("select distinct l.attendClass from Lesson l where l.term = ?1 order by l.attendClass desc")
    List<String> getAttendClassesInTerm(String term);

    @Query("select distinct l.category from Lesson l")
    List<String> getTypes();
}

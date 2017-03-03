package net.catten.ssim.repositories;

import net.catten.ssim.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by catten on 2/24/17.
 */
@Repository
public interface LessonDAO extends JpaRepository<Lesson,Long>, JpaSpecificationExecutor<Lesson>{
}

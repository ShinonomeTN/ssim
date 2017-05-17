package cn.lncsa.ssim.web.repositories;

import cn.lncsa.ssim.web.model.SchoolCalender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by catten on 5/15/17.
 */
public interface SchoolCalenderRepository extends JpaRepository<SchoolCalender,Integer>, JpaSpecificationExecutor<SchoolCalender>{
}

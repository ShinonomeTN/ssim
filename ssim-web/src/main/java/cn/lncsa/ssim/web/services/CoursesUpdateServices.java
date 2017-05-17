package cn.lncsa.ssim.web.services;

import cn.lncsa.ssim.web.model.SchoolCalender;
import cn.lncsa.ssim.web.repositories.SchoolCalenderRepository;
import cn.lncsa.ssim.web.services.util.CaptureThread;
import cn.lncsa.ssim.web.services.util.TickModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by catten on 2/24/17.
 */
@Service
public class CoursesUpdateServices {

    private CaptureThread captureThread;
    private Thread captureThreadHolder;

    private TickModel tickModel;

    private SchoolCalenderRepository calenderRepository;
    private RedisServices redisServices;

    @Autowired
    public void setCaptureThread(CaptureThread captureThread) {
        this.captureThread = captureThread;
    }

    @Autowired
    public void setTickModel(TickModel tickModel) {
        this.tickModel = tickModel;
    }

    @Autowired
    public void setCalenderRepository(SchoolCalenderRepository calenderRepository) {
        this.calenderRepository = calenderRepository;
    }

    @Autowired
    public void setRedisServices(RedisServices redisServices) {
        this.redisServices = redisServices;
    }

    public TickModel getTaskStatus() {
        return tickModel;
    }

    public void setTaskCode(String taskCode) {
        captureThread.setTaskCode(taskCode);
    }

    public String getStatus() {
        return captureThread.getStatus();
    }

    public void fireCaptureThread(boolean skipCapture) {
        captureThreadHolder = new Thread(captureThread);
        captureThread.setSkipCapture(skipCapture);
        captureThreadHolder.start();
    }

    public void terminateCaptureThread() {
        captureThreadHolder.interrupt();
    }

    public void saveSchoolCalendar(SchoolCalender schoolCalender){
        SchoolCalender existed = calenderRepository
                .findOne((root, criteriaQuery, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get("termName"),schoolCalender.getTermName()));
        if(existed != null) existed.replace(schoolCalender);
        else existed = schoolCalender;
        calenderRepository.save(existed);
        redisServices.setString(
                RedisServices.KEY_PREFIX_TERM_CALENDER + schoolCalender.getTermName(),
                String.valueOf(schoolCalender.getTermStart()));
    }

    public void removeSchoolCalendar(String termName){
        calenderRepository.delete(calenderRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("termName"),termName)));
        redisServices.delete(RedisServices.KEY_PREFIX_TERM_CALENDER + termName);
    }
}

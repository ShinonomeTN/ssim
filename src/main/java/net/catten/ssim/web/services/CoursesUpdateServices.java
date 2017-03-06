package net.catten.ssim.web.services;

import net.catten.ssim.schedule.kingo.jw.caterpillar.CourseScheduleCaterpillar;
import org.springframework.stereotype.Service;

/**
 * Created by catten on 2/24/17.
 */
@Service
public class CoursesUpdateServices {

    private Thread captureThread;


    public void setCaptureThread(Thread captureThread) {
        this.captureThread = captureThread;
    }

    public void fireCaptureThread(){

    }
}

package net.catten.ssim.services;

import net.catten.ssim.common.events.TickEventReceiver;
import net.catten.ssim.schedule.kingo.jw.caterpillar.CourseScheduleCaterpillar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by catten on 2/24/17.
 */
@Service
public class CoursesUpdateServices {
    private CourseScheduleCaterpillar caterpillar;

    private Thread captureThread;

    public void fireCaptureThread(){

    }
}

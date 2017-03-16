package cn.lncsa.ssim.web.services;

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

    @Autowired
    public void setCaptureThread(CaptureThread captureThread) {
        this.captureThread = captureThread;
    }

    @Autowired
    public void setTickModel(TickModel tickModel) {
        this.tickModel = tickModel;
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
}

package cn.lncsa.ssim.web.services.util;

import cn.lncsa.kingo.jw.cs.caterpillar.model.KingoRawCourse;
import cn.lncsa.ssim.common.events.TickEventReceiver;
import cn.lncsa.ssim.schedule.CourseScheduleCaterpillar;
import cn.lncsa.ssim.schedule.factory.CourseFactory;
import cn.lncsa.ssim.schedule.factory.LessonExpander;
import cn.lncsa.ssim.web.model.Lesson;
import cn.lncsa.ssim.web.repositories.LessonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

/**
 * Created by catten on 3/4/17.
 */
public class CaptureThread implements Runnable {

    public final static String STATUS_ERROR = "error";
    public final static String STATUS_READY = "ready";
    public final static String STATUS_CAPTURING = "capturing";
    public final static String STATUS_IMPORTING = "importing";
    public final static String STATUS_STOPPED = "stopped";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TickEventReceiver receiver;
    private CourseScheduleCaterpillar caterpillar;
    private CourseFactory<KingoRawCourse> factory;
    private LessonExpander<KingoRawCourse,Lesson> expander;

    private String bufferPath;

    private String taskCode;
    private String status = STATUS_READY;
    private boolean skipCapture = false;

    private LessonDAO dao;

    @Override
    public void run() {
        //Provide a default ticker, output status to log.
        if (this.receiver == null) {
            logger.warn("No tick receiver, using default receiver!");
            this.receiver = message -> logger.info("Work status : " + message[1] + "/" + message[0] + " - " + message[2]);
        }

        File file = new File(bufferPath);
        if (file.exists() && file.isDirectory()) {
            logger.info("Directory : " + file.getAbsolutePath());

            try {
                if ((taskCode != null && !taskCode.equals("")) || skipCapture) {
                    status = STATUS_CAPTURING;
                    if (!skipCapture) {
                        logger.info("Start capturing from website.");
                        if(caterpillar.getTermSubjectToFiles(taskCode, file) < 0) {
                            logger.error("Capture Error.");
                            return;
                        } else logger.info("Capture finished");
                    }
                    status = STATUS_IMPORTING;
                    File[] files = file.listFiles();
                    if (files != null) {
                        logger.info("Start importing task.");
                        int count = 0;
                        for (File f : files) {
                            dao.save(expander.expand(factory.parse(f)));
                            count++;
                            receiver.tick(count, files.length, f.getName());
                        }
                        logger.info("Importing finished.");
                    }
                    status = STATUS_READY;
                } else {
                    logger.error("Task not available.");
                    status = STATUS_ERROR;
                }
            } catch (IOException e) {
                logger.error("An error occur, task interrupted.", e);
                status = STATUS_ERROR;
            } catch (InterruptedException e) {
                logger.error("Task interrupted.", e);
                status = STATUS_STOPPED;
            }
        } else {
            logger.error("Capture could not start, please check if buffer path exist and is a directory!");
            status = STATUS_ERROR;
        }
    }

    /*
    *
    *
    * Getter setter
    *
    *
    * */

    public CourseScheduleCaterpillar getCaterpillar() {
        return caterpillar;
    }

    public void setCaterpillar(CourseScheduleCaterpillar caterpillar) {
        this.caterpillar = caterpillar;
    }

    public TickEventReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(TickEventReceiver receiver) {
        this.receiver = receiver;
    }

    public LessonDAO getDao() {
        return dao;
    }

    @Autowired
    public void setDao(LessonDAO dao) {
        this.dao = dao;
    }

    public String getBufferPath() {
        return bufferPath;
    }

    public void setBufferPath(String bufferPath) {
        this.bufferPath = bufferPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public CourseFactory getFactory() {
        return factory;
    }

    public void setFactory(CourseFactory<KingoRawCourse> factory) {
        this.factory = factory;
    }

    public LessonExpander getExpander() {
        return expander;
    }

    public void setExpander(LessonExpander<KingoRawCourse,Lesson> expander) {
        this.expander = expander;
    }

    public boolean isSkipCapture() {
        return skipCapture;
    }

    public void setSkipCapture(boolean skipCapture) {
        this.skipCapture = skipCapture;
    }
}

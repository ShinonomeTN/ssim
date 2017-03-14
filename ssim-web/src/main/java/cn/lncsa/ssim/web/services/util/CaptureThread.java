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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

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
        File outDir = new File(bufferPath);
        if(!prepare(outDir)){
            logger.error("Capture could not start, please check if buffer path exist and is a directory!");
            status = STATUS_ERROR;
            return;
        }

        try {
            logger.info("Output files to directory " + outDir.getAbsolutePath());

            if ((taskCode != null && !taskCode.equals("")) || skipCapture) {
                if (!skipCapture) if (!capture(outDir)) status = STATUS_ERROR;
                if (!load(outDir)) status = STATUS_ERROR;
                else status = STATUS_READY;

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
    }

    private boolean prepare(File file){
        //Provide a default ticker, output status to log.
        if (this.receiver == null) {
            logger.warn("No tick receiver, using default receiver!");
            this.receiver = message -> logger.info("Work status : " + message[1] + "/" + message[0] + " - " + message[2]);
        }

        //If output directory doesn't exist, create.
        if(!file.exists()){
            //If could not create directory, report and stop importing.
            if(!file.mkdir()) {
                logger.error("Could not create directory : " + file.getAbsolutePath());
                return false;
            }
        } else if (!file.isDirectory()) {
            //Check if target is a directory.
            logger.error("Specified path isn't to a directory : " + file.getAbsolutePath());
            return false;
        }

        //Clear target directory
        File[] oldFiles = file.listFiles();
        if (oldFiles != null && oldFiles.length > 0) {
            logger.info("Cleaning old files...");
            for (File file1 : oldFiles) {
                try {
                    Files.delete(file1.toPath());
                } catch (IOException e) {
                    logger.error(file1.getAbsolutePath() + " could not be deleted.", e);
                    return false;
                }
            }
            logger.info("Cleaning finished.");
        }

        return true;
    }

    private boolean capture(File file) throws IOException, InterruptedException {
        status = STATUS_CAPTURING;
        logger.info("Start capturing from website.");
        if(caterpillar.getTermSubjectToFiles(taskCode, file) < 0) {
            logger.error("Error...");
            return false;
        } else logger.info("Capturing finished");

        return true;
    }

    private boolean load(File file) throws IOException {
        File[] files = file.listFiles();
        if (files != null) {
            status = STATUS_IMPORTING;
            logger.info("Start importing task.");
            int count = 0;
            for (File f : files) {
                dao.save(expander.expand(factory.parse(f)));
                count++;
                receiver.tick(count, files.length, f.getName());
            }
            logger.info("Importing finished.");
        }
        return true;
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

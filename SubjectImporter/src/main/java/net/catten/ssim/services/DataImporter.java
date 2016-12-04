package net.catten.ssim.services;

import net.catten.ssim.common.events.TickEventProvider;
import net.catten.ssim.common.events.TickEventReceiver;
import net.catten.ssim.dto.LessonDTO;
import net.catten.ssim.helper.Validator;
import net.catten.ssim.repository.LessonRepository;
import net.catten.ssim.repository.SubjectRepository;
import net.catten.ssim.schedule.factory.SubjectFactory;
import net.catten.ssim.schedule.model.Lesson;
import net.catten.ssim.schedule.model.Subject;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by catten on 11/14/16.
 */
public class DataImporter implements TickEventProvider {
    private File targetDir;
    private String fileSuffix;
    private String tablePrefix;

    private SubjectFactory subjectFactory;
    private Logger logger;

    private Map<Integer,String> bufferMap;

    private LessonRepository lessonRepository;
    private SubjectRepository subjectRepository;

    private TickEventReceiver tickEventReceiver = message -> {
        //do nothing...
    };

    public DataImporter(Connection connection,File targetDir, String charset, String fileSuffix, String tablePrefix) {

        //Validate if target file is a directory
        this.targetDir = targetDir;
        Validator.isDirectory(targetDir);

        //Set file suffix
        this.fileSuffix = fileSuffix;

        //Set table prefix
        this.tablePrefix = tablePrefix;

        initialize(connection,charset);
        logger.info("DataImporter initialized for import from file.");
    }

    public DataImporter(Connection connection, Map<Integer,String> bufferedData, String charset, String tablePrefix){
        this.bufferMap = bufferedData;
        initialize(connection,charset);
        logger.info("DataImporter initialized for import from buffer.");
        this.tablePrefix = tablePrefix;
    }

    private void initialize(Connection connection, String charset) {
        //Set connection

        //Create subject factory with charset
        this.subjectFactory = new SubjectFactory(charset);

        //Get logger for logging
        this.logger = Logger.getLogger(this.getClass().toString());

        lessonRepository = new LessonRepository(connection,tablePrefix);
        subjectRepository = new SubjectRepository(connection,tablePrefix);
    }

    public void startJobs() throws IOException, SQLException {
        bufferItems();
        int currentItem = 0;
        for (Integer item : bufferMap.keySet()) {
            currentItem++;

            Subject subject = subjectFactory.parse(bufferMap.get(item),item);
            subjectRepository.save(subject);
            for (Lesson lesson : subject.getLessons()){

                tickEventReceiver.tick(bufferMap.size(),currentItem, item);
                List<LessonDTO> lessonDTOList = LessonDTO.splitLesson(lesson,subject.getId());
                for (LessonDTO lessonDTO : lessonDTOList){
                    lessonRepository.save(lessonDTO);
                }
            }
        }
        System.out.println();
        logger.info("Work finished.");
    }

    private void bufferItems() throws IOException {
        if (targetDir != null) {
            logger.info("Fetching files : " + targetDir.getAbsolutePath());
            File[] files = targetDir.listFiles((file, s) -> s.endsWith(fileSuffix));
            bufferMap = new HashMap<>();
            if(files != null){
                logger.info(String.valueOf(files.length) + " Files match, added to file list.");

                int currentFile = 0;
                for (File file : files){
                    currentFile++;
                    int suffixPos = file.getName().indexOf(fileSuffix.charAt(0));
                    bufferMap.put(Integer.parseInt(file.getName().substring(0,suffixPos)), IOUtils.toString(new FileInputStream(file)));
                    tickEventReceiver.tick(files.length,currentFile,file.getName());
                }
                System.out.println();
                logger.info("Buffering finished.");
            }
        }
    }

    public String getCharset() {
        return subjectFactory.getCharset();
    }

    public void setCharset(String charset) {
        subjectFactory.setCharset(charset);
    }


    @Override
    public void setTickReceiver(TickEventReceiver receiver) {
        this.tickEventReceiver = receiver;
    }
}

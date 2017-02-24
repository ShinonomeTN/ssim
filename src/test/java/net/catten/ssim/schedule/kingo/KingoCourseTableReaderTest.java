package net.catten.ssim.schedule.kingo;

import net.catten.ssim.schedule.factory.CourseFactory;
import net.catten.ssim.schedule.model.KingoRawCourse;
import net.catten.ssim.schedule.model.KingoRawLesson;
import net.catten.ssim.schedule.model.Timepoint;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by catten on 2/23/17.
 */
public class KingoCourseTableReaderTest {

    private String testCaseFiles[] = new String[]{
            "000586",
            "012244"
    };

    @Test
    public void parse() throws Exception {
        for (String testCast : testCaseFiles){
            InputStream inputStream = this.getClass().getResourceAsStream("/kingo/" + testCast + ".html");
            CourseFactory reader = new CourseFactory();
            reader.setCharset("GBK");
            KingoRawCourse rawTable = reader.parse(inputStream,Integer.parseInt(testCast));
            for (KingoRawLesson lesson : rawTable.getLessons()){
                for (Timepoint timepoint : lesson.getTimePoint().expand()){
                    System.out.println(lesson + " - " + timepoint);
                }
            }
        }
    }

}
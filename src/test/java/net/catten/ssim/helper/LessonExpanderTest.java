package net.catten.ssim.helper;

import net.catten.ssim.schedule.factory.LessonExpander;
import net.catten.ssim.web.model.Lesson;
import net.catten.ssim.schedule.factory.CourseFactory;
import net.catten.ssim.schedule.model.KingoRawCourse;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by catten on 2/24/17.
 */
public class LessonExpanderTest {
    private String testCaseFiles[] = new String[]{
            "000586",
            "012244"
    };

    @Test
    public void expand() throws Exception {
        for (String testCast : testCaseFiles){
            InputStream inputStream = this.getClass().getResourceAsStream("/kingo/" + testCast + ".html");
            CourseFactory reader = new CourseFactory();
            reader.setCharset("GBK");
            KingoRawCourse rawTable = reader.parse(inputStream,Integer.parseInt(testCast));
            LessonExpander expander = new LessonExpander();
            List<Lesson> lessons = expander.expand(rawTable);
            for (Lesson lesson : lessons){
                System.out.println(lesson);
            }
        }
    }

}
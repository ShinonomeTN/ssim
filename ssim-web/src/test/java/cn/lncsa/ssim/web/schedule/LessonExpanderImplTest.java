package cn.lncsa.ssim.web.schedule;

import cn.lncsa.kingo.jw.cs.caterpillar.impl.KingoToTimePointCourseFactory;
import cn.lncsa.kingo.jw.cs.caterpillar.model.KingoRawCourse;
import cn.lncsa.ssim.schedule.factory.LessonExpander;
import cn.lncsa.ssim.web.model.Lesson;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by cattenlinger on 2017/3/12.
 */
public class LessonExpanderImplTest {

    private String testCaseFiles[] = new String[]{
            "000586",
            "012244"
    };

    @Test
    public void expand() throws Exception {
        for (String testCast : testCaseFiles) {
            InputStream inputStream = this.getClass().getResourceAsStream("/" + testCast + ".html");
            KingoToTimePointCourseFactory reader = new KingoToTimePointCourseFactory();
            reader.setCharset("GBK");
            KingoRawCourse rawTable = reader.parse(inputStream, Integer.parseInt(testCast));
            LessonExpander<KingoRawCourse,Lesson> expander = new LessonExpanderImpl();
            List<Lesson> lessons = expander.expand(rawTable);
            for (Lesson lesson : lessons) {
                System.out.println(lesson);
            }
        }
    }

    @Test
    public void expand2() throws Exception{
        InputStream inputStream = this.getClass().getResourceAsStream("/021460.html");
        KingoToTimePointCourseFactory reader = new KingoToTimePointCourseFactory();
        reader.setCharset("UTF8");
        KingoRawCourse rawTable = reader.parse(inputStream, Integer.parseInt("021460"));
        LessonExpander<KingoRawCourse,Lesson> expander = new LessonExpanderImpl();
        List<Lesson> lessons = expander.expand(rawTable);
        for (Lesson lesson : lessons) {
            System.out.println(lesson);
        }
    }
}
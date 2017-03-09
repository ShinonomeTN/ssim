package net.catten.ssim.web.services.util;

import net.catten.ssim.web.repositories.LessonDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * Created by catten on 3/4/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        //@ContextConfiguration(classes = TestConfiguration.class),
        @ContextConfiguration("classpath:cat-test.xml")
})

public class CaptureThreadTest {

    @Autowired
    private CaptureThread captureThread;

    @Autowired
    private LessonDAO lessonDAO;

    @Test
    public void run() throws Exception {
        String[] terms = new String[]{"20140","20141","20150","20151","20160","20161"};
        System.out.println("Current path : " + new File("").getAbsolutePath());
        captureThread.setDao(lessonDAO);

//        captureThread.setTaskCode("20161");
//        //captureThread.setSkipCapture(true);
//        captureThread.setDao(lessonDAO);
//        captureThread.run();

        for (String term : terms){
            captureThread.setTaskCode(term);
            captureThread.run();
        }
    }

}
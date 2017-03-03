package net.catten.ssim.services.util;

import configuration.TestConfiguration;
import net.catten.ssim.repositories.LessonDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by catten on 3/4/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(classes = TestConfiguration.class),
        @ContextConfiguration("classpath:cat-test.xml")
})

public class CaptureThreadTest {

    @Autowired
    private CaptureThread captureThread;

    @Autowired
    private LessonDAO lessonDAO;

    @Test
    public void run() throws Exception {
        System.out.println("Current path : " + new File("").getAbsolutePath());
        captureThread.setTaskCode("20141");
        captureThread.setSkipCapture(true);
        captureThread.setDao(lessonDAO);
        captureThread.run();
    }

}
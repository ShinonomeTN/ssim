package net.catten.ssim.schedule.factory;

import net.catten.ssim.schedule.model.Subject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by catten on 12/4/16.
 */
public class SubjectFactoryTest {

    @Test
    public void parse() throws Exception {
        SubjectFactory subjectFactory = new SubjectFactory("GB2312");
        Subject subject1 = subjectFactory.parse(this.getClass().getResourceAsStream("/000586.html"),586);
        System.out.println(subject1);
        Subject subject2 = subjectFactory.parse(this.getClass().getResourceAsStream("/012244.html"),12244);
        System.out.println(subject2);
    }

}
package net.catten.ssim.schedule.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by catten on 12/4/16.
 */
public class SubjectTest {

    @Test
    public void setNameWithCode(){
        Subject subject = new Subject();
        subject.setNameWithCode("[011795]编程基础");
        assertTrue(subject.getCode().equals("011795"));
        assertTrue(subject.getName().equals("编程基础"));
    }

}
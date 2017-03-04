package net.catten.ssim.schedule.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by catten on 3/5/17.
 */
public class KingoRawTimePointTest {
    @Test
    public void expand() throws Exception {
        KingoRawTimePoint timePoint = new KingoRawTimePoint("1-2","一[1-4]节");
        System.out.println(timePoint.expand());
    }

}
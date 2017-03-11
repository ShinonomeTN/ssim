package cn.lncsa.kingo.jw.cs.caterpillar.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cattenlinger on 2017/3/12.
 */
public class KingoRawTimePointTest {

    @Test
    public void expand() throws Exception {
        KingoRawTimePoint timePoint = new KingoRawTimePoint("1-2","一[1-4]节");
        System.out.println(timePoint.expand());
    }

}
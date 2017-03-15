package cn.lncsa.kingo.jw.cs.caterpillar.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cattenlinger on 2017/3/12.
 */
public class KingoRawTimePointTest {

    @Test
    public void expand() throws Exception {
        KingoRawTimePoint timePoint = new KingoRawTimePoint("2,8","一[1-4节]双");
        for (TimePoint timePoint1 : timePoint.expand()){
            System.out.println(timePoint1);
        }
    }

}
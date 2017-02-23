package net.catten.ssim.schedule.kingo;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by catten on 2/23/17.
 */
public class KingoCourseTableReaderTest {
    @Test
    public void parse() throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream("/000586.html");
        KingoCourseTableReader reader = new KingoCourseTableReader();
        reader.setCharset("GBK");
        System.out.println(reader.parse(inputStream));
    }

}
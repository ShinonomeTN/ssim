package net.catten.ssim.schedule.kingo;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by catten on 2/23/17.
 */
public class KingoCourseTableReaderTest {

    private String testCaseFiles[] = new String[]{
            "000586",
            "012244"
    };

    @Test
    public void parse() throws Exception {
        for (String testCast : testCaseFiles){
            InputStream inputStream = this.getClass().getResourceAsStream("/kingo/" + testCast + ".html");
            KingoCourseTableReader reader = new KingoCourseTableReader();
            reader.setCharset("GBK");
            KingoCourseRawTable rawTable = reader.parse(inputStream);
            System.out.println(testCast + " : " + rawTable);
            String a = IOUtils.toString(this.getClass().getResourceAsStream("/kingo/" + testCast + ".result"));
            assertTrue(rawTable.toString().equals(a));
        }
    }

}
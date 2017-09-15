package cn.lncsa.ssim.schedule.caterpillar;

import cn.lncsa.ssim.common.events.TickEventProvider;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by catten on 2/24/17.
 */
public interface CourseScheduleCaterpillar extends TickEventProvider {

    /**
     *
     * Get all terms from remote server
     *
     * @return term code as key, term name as value
     * @throws IOException
     */
    Map<String, String> dumpTermList() throws IOException;

    /**
     *
     * Get all courses that in the term
     *
     * @param termCode the code represented a term
     * @return course code as key, course name as value
     * @throws IOException
     */
    Map<String, String> dumpCourseList(String termCode) throws IOException;

    int getTermSubjectToFiles(String termCode, File outputFolder) throws IOException, InterruptedException;

    void captureSingleSubject(String termCode, String subjectCode, File outputFolder) throws IOException;

    boolean isLoginExpire() throws IOException;

    String getUserAgent();

    void setUserAgent(String userAgent);

    String getCharset();

    void setCharset(String charset);

    Properties getLoginProperties();

    void setLoginProperties(Properties loginProperties) throws IOException;
}

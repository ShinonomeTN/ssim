package net.catten.ssim.schedule.kingo.jw.caterpillar;

import net.catten.ssim.common.events.TickEventProvider;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by catten on 2/24/17.
 */
public interface CourseScheduleCaterpillar extends TickEventProvider {
    Map<String, String> getTermsFromRemote() throws IOException;

    Map<String, String> getCoursesFromRemote(String termCode) throws IOException;

    @Deprecated
    Map<String, String> getTermSubjectToMemory(String termCode) throws IOException, InterruptedException;

    int getTermSubjectToFiles(String termCode, File outputFolder) throws IOException, InterruptedException;

    boolean isLoginExpire() throws IOException;

    String getUserAgent();

    void setUserAgent(String userAgent);

    String getCharset();

    void setCharset(String charset);

    Properties getLoginProperties();

    void setLoginProperties(Properties loginProperties) throws IOException;
}

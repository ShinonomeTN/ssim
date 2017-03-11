package cn.lncsa.ssim.schedule.factory;

import java.io.File;
import java.io.IOException;

/**
 * Created by catten on 3/10/17.
 */
public interface CourseFactory<T> {
    T parse(File file) throws IOException;

    String getCharset();

    void setCharset(String charset);
}

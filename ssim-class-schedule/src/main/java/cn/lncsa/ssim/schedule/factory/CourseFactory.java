package cn.lncsa.ssim.schedule.factory;

import java.io.File;
import java.io.IOException;

/**
 * Created by catten on 3/10/17.
 */
public interface CourseFactory<T> {

    /**
     *
     * Parse an file to course data object
     *
     * @param file input file
     * @return If return null, it means this file contains an empty course
     * @throws IOException
     */
    T parse(File file) throws IOException;

    String getCharset();

    void setCharset(String charset);
}

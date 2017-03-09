package net.catten.ssim.schedule.factory;

import net.catten.ssim.schedule.model.KingoRawCourse;

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

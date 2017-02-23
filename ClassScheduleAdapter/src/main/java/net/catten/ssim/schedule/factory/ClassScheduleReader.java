package net.catten.ssim.schedule.factory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by catten on 2/23/17.
 */
public interface ClassScheduleReader<T> {

    void setCharset(String charset);
    T parse(InputStream stream) throws IOException;
}

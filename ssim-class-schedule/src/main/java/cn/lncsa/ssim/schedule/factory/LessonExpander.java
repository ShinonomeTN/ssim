package cn.lncsa.ssim.schedule.factory;

import java.util.List;

/**
 * Created by catten on 3/10/17.
 */
public interface LessonExpander<R, T> {
    List<T> expand(R rawCourse);
}

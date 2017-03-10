package net.catten.ssim.schedule.factory;

import net.catten.ssim.schedule.model.KingoRawCourse;
import net.catten.ssim.web.model.Lesson;

import java.util.List;

/**
 * Created by catten on 3/10/17.
 */
public interface LessonExpander<R,T> {
    List<T> expand(R rawCourse);
}

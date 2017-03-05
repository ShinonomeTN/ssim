package net.catten.ssim.web.services;

import net.catten.ssim.web.repositories.LessonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by catten on 2/24/17.
 */
@Service
public class LessonServices {

    private LessonDAO lessonDAO;

    @Autowired
    public void setLessonDAO(LessonDAO lessonDAO) {
        this.lessonDAO = lessonDAO;
    }


}

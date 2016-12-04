package net.catten.ssim.dto;

import net.catten.ssim.Main;
import net.catten.ssim.dto.LessonDTO;
import net.catten.ssim.schedule.factory.SubjectFactory;
import net.catten.ssim.schedule.model.Lesson;
import net.catten.ssim.schedule.model.Subject;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by catten on 11/14/16.
 */
public class LessonDTOTest {
    @Test
    public void splitLesson() throws Exception {
        SubjectFactory subjectFactory = new SubjectFactory("GB2312");
        Subject subject = subjectFactory.parse(Main.class.getResourceAsStream("/012213.html"), 12213);
        List<LessonDTO> lessonList = new LinkedList<>();
        for (Lesson lesson : subject.getLessons()) {
            lessonList.addAll(LessonDTO.splitLesson(lesson, subject.getId()));
        }
        System.out.println(lessonList);
    }

}
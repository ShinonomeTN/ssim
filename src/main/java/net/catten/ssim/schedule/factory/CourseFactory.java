package net.catten.ssim.schedule.factory;

import net.catten.ssim.schedule.model.KingoRawCourse;
import net.catten.ssim.schedule.model.KingoRawLesson;
import net.catten.ssim.schedule.model.KingoRawTimePoint;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

/**
 * Created by catten on 11/12/16.
 */
public class CourseFactory {

    private String baseUrl = "";
    private String charset = "UTF-8";

    /**
     * Default constructor
     */
    public CourseFactory(){
    }

    /**
     * Constructor with charset specified
     *
     * @param charset
     */
    public CourseFactory(String charset){
        this.charset = charset;
    }

    /**
     * Parse HTML strings
     *
     * @param s HTML
     * @param id id of this string
     * @return
     * @throws IOException
     */
    public KingoRawCourse parse(String s, Integer id) throws IOException {
        return parse(Jsoup.parse(s),id);
    }

    /**
     * Parse HTML file using file object
     *
     * @param file
     * @return
     * @throws IOException
     */
    public KingoRawCourse parse(File file) throws IOException {
        if(file.isFile() && file.canRead() && file.exists()){
            String name = file.getName().substring(0,file.getName().indexOf("."));
            int id = 0;
            if(name.matches("\\d+")) id = Integer.parseInt(name);
            return parse(new FileInputStream(file),id);
        }
        return null;
    }

    /**
     * Parse HTML file using stream
     *
     * @param inputStream stream
     * @param id Give a ID to this KingoRawCourse, can be null
     * @return
     * @throws IOException
     */
    public KingoRawCourse parse(InputStream inputStream, Integer id) throws IOException {
        Document document = Jsoup.parse(inputStream,charset, baseUrl);

        return parse(document,id);
    }

    public KingoRawCourse parse(Document document, Integer id){
        Element root = document.select("table").first();
        Elements tables = root.select("table");
        tables.remove(0);

        KingoRawCourse subject = new KingoRawCourse();
        subject.setId(id);

        subject = processSubjectTerm(subject,tables.get(0));
        subject = processSubjectTitle(subject,tables.get(1));
        subject.setLessons(processLessonList(tables.get(2)));
        return subject;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    /*
    *
    * Private procedure
    *
    *
    * */

    private KingoRawCourse processSubjectTerm(KingoRawCourse subject, Element tableElement){
        Elements elements = tableElement.select("tr");
        subject.setTerm(elements.get(1).children().text());
        return subject;
    }

    private KingoRawCourse processSubjectTitle(KingoRawCourse subject, Element tableElement){
        Elements fields = tableElement.select("td");
        String text = fields.first().text();
        String[] split = text.split(new String(new char[]{160}));
        for (String s : split){
            String[] fieldSplit = s.split("：");
            switch (fieldSplit[0]){
                case "承担单位":
                    subject.setUnit(fieldSplit[1]);
                    break;
                case "课程":
                    subject.setNameWithCode(fieldSplit[1]);
                    break;
                case "总学时":
                    subject.setTimeSpend(Double.parseDouble(fieldSplit[1]));
                    break;
                case "学分":
                    subject.setPoint(Double.parseDouble(fieldSplit[1]));
                    break;
                default:
                    break;
            }
        }
        return subject;
    }

    private List<KingoRawLesson> processLessonList(Element tableElement){
        LinkedList<KingoRawLesson> lessons = new LinkedList<>();
        KingoRawLesson lessonLatest = null;

        Elements tableItems = tableElement.select("tr");
        tableItems.removeIf(element -> !element.child(0).attr("width").equals(""));

        for (Element items : tableItems){
            KingoRawLesson lesson = new KingoRawLesson();
            KingoRawTimePoint timePoint = new KingoRawTimePoint();
            Elements elements = items.getElementsByTag("td");
            for (int i = 0; i < elements.size(); i++){
                Element contentChild = items.child(i);
                String c = contentChild.text().trim();
                boolean fillLatest = "".equals(c) && lessonLatest != null;
                switch (i){
                    case 0:
                        lesson.setTeacher(fillLatest ? lessonLatest.getTeacher() : c);
                        break;
                    case 1:
                        lesson.setClassNumber(fillLatest ? lessonLatest.getClassNumber() : c);
                        break;
                    case 2:
                        lesson.setAttendAmount(fillLatest ? lessonLatest.getAttendAmount() : Integer.parseInt(c));
                        break;
                    case 3:
                        lesson.setClassType(fillLatest ? lessonLatest.getClassType() : c);
                        break;
                    case 4:
                        lesson.setAssessmentType(fillLatest ? lessonLatest.getAssessmentType() : c);
                        break;
                    case 5:
                        lesson.setClassAttend(fillLatest ? lessonLatest.getClassAttend() : c.split(" "));
                        break;
                    case 6:
                        timePoint.setWeekRange(c);
                        break;
                    case 7:
                        timePoint.setTimePoints(c);
                        break;
                    case 8:
                        lesson.setPosition(c);
                        break;
                    default:
                        break;
                }
            }
            lesson.setTimePoint(timePoint);
            lessons.add(lesson);
            lessonLatest = lessons.getLast();
        }
        return lessons;
    }
}

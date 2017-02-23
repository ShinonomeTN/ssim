package net.catten.ssim.schedule.kingo;

import net.catten.ssim.schedule.factory.ClassScheduleReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by CattenLinger on 2017/2/23.
 */
public class KingoCourseTableReader implements ClassScheduleReader<KingoCourseRawTable> {

    private String charset = "UTF-8";

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public KingoCourseRawTable parse(InputStream htmlFileInputStream) throws IOException {

        Document document = Jsoup.parse(htmlFileInputStream,charset,"");
        Elements elements = document.select("table");
        elements.remove(0);

        KingoCourseRawTable rawTable = new KingoCourseRawTable();

        //Handle tables
        for (int i = 0; i < elements.size(); i++) {
            Element e = elements.get(i);

            switch (i){
                case 0:{
                    rawTable.setTerm(e.select("td").get(1).text());
                } break;

                case 1:{
                    String[] strings = e.select("td").first().text().split("\\u00A0|\\u0020");
                    for (String string : strings){
                        if(string.trim().equals("")) continue;

                        String[] field = string.split("：");
                        switch (field[0]){
                            case "承担单位":
                                rawTable.setUnit(field[1]);
                                break;
                            case "课程":
                                rawTable.setCourse(field[1]);
                                break;
                            case "总学时":
                                rawTable.setTotalPeriod(field[1]);
                                break;
                            case "学分":
                                rawTable.setCredit(field[1]);
                                break;
                            default:
                                break;
                        }
                    }
                } break;

                case 2:{
                    Elements elist = e.select("tr");
                    List<String> tableHead = elist
                            .get(0).select("td")
                            .stream().map(Element::text).collect(Collectors.toList());

                    elist.remove(0);

                    for (Element item : elist){
                        List<String> fields = item
                                .select("td")
                                .stream().map(Element::text).collect(Collectors.toList());

                        Iterator<String> iterator = fields.iterator();
                        for (String aTableHead : tableHead) {
                            String value = iterator.next().replaceAll("\\u0020"," ");

                            switch (aTableHead.replaceAll("\\u0020|\n","")) {
                                case "任课教师":
                                    rawTable.setTeacher(value);
                                    break;
                                case "上课班号":
                                    rawTable.setTeacher(value);
                                    break;
                                case "上课人数":
                                    rawTable.setMemberCount(value);
                                    break;
                                case "课程类别":
                                    rawTable.setCategory(value);
                                    break;
                                case "考核方式":
                                    rawTable.setExamineType(value);
                                    break;
                                case "上课班级构成":
                                    rawTable.setAttendClass(value);
                                    break;
                                case "周次":
                                    rawTable.setWeeks(value);
                                    break;
                                case "节次":
                                    rawTable.setTimePoint(value);
                                    break;
                                case "地点":
                                    rawTable.setAddress(value);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } break;

                default:
                    break;
            }
        }




        return rawTable;
    }

    /*
     * private methods
     *
     */
}

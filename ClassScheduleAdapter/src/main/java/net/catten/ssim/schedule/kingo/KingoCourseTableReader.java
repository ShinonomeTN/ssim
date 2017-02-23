package net.catten.ssim.schedule.kingo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by CattenLinger on 2017/2/23.
 */
public class KingoCourseTableReader {

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

        Node head = elements.get(0);
        Node content = elements.get(1);

        KingoCourseRawTable rawTable = new KingoCourseRawTable();

        return rawTable;
    }

    /*
     * private methods
     *
     */
}

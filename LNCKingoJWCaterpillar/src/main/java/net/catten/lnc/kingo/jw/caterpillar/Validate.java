package net.catten.lnc.kingo.jw.caterpillar;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

/**
 * Created by catten on 2/23/17.
 */
public class Validate {
    public static boolean isForm(Element element){
        return element.tag().getName().equals("form");
    }
}

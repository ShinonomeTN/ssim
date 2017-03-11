package cn.lncsa.kingo.jw.cs.caterpillar.impl;

import org.jsoup.nodes.Element;

/**
 * Created by catten on 2/23/17.
 */
public class Validate {
    public static boolean isForm(Element element){
        return element.tag().getName().equals("form");
    }
}

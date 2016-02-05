package com.github.wztbbs.parser;

import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;

/**
 * Created by wztbbs on 2016/1/11.
 */
public class NodeHelper {

    public static Element getElementById(Element root, String type, String id) {
        List list = root.elements(type);
        for(Iterator it = list.iterator(); it.hasNext();) {
            Element element = (Element)it.next();
            if(element.attribute("id") != null && element.attributeValue("id").equals(id)) {
                return element;
            }
        }

        return null;

    }

}

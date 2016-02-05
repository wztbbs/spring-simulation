package com.github.wztbbs;

import com.github.wztbbs.object.Shoutable;
import com.github.wztbbs.parser.BeanParser;

import java.io.InputStream;

/**
 * Created by wztbbs on 2016/1/11.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("beans.xml");
        Shoutable gun = (Shoutable)new BeanParser(inputStream).getBeanMap().get("gun");
        gun.shout();
    }
}

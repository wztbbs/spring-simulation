package com.github.wztbbs.parser;


import java.lang.reflect.InvocationTargetException;

/**
 * Created by wztbbs on 2016/1/12.
 */
public class TypeHelper {

    // 通过字符串反射类型，增加了对基本类型的包装
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object getInstanceForName(String name, String value) {
        Class clazz = nameToClass(name);

        Object object = null;
        try {
            object = clazz.getConstructor(String.class).newInstance(value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return object;
    }

    //通过类型名返回基本类型Class（Class.forName()貌似不行）
    @SuppressWarnings("rawtypes")
    public static Class nameToPrimitiveClass(String name) {
        Class clazz = null;
        if (name.equals("int")) {
            clazz = int.class;
        } else if (name.equals("char")) {
            clazz = char.class;
        } else if (name.equals("boolean")) {
            clazz = boolean.class;
        } else if (name.equals("short")) {
            clazz = short.class;
        } else if (name.equals("long")) {
            clazz = long.class;
        } else if (name.equals("float")) {
            clazz = float.class;
        } else if (name.equals("double")) {
            clazz = double.class;
        } else if (name.equals("byte")) {
            clazz = byte.class;
        } else {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return clazz;
    }

    //通过类型名获取包装器类
    @SuppressWarnings("rawtypes")
    public static Class nameToClass(String name) {
        Class clazz = null;
        if (name.equals("int")) {
            clazz = Integer.class;
        } else if (name.equals("char")) {
            clazz = Character.class;
        } else if (name.equals("boolean")) {
            clazz = Boolean.class;
        } else if (name.equals("short")) {
            clazz = Short.class;
        } else if (name.equals("long")) {
            clazz = Long.class;
        } else if (name.equals("float")) {
            clazz = Float.class;
        } else if (name.equals("double")) {
            clazz = Double.class;
        } else if (name.equals("byte")) {
            clazz = Byte.class;
        } else {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return clazz;
    }

}

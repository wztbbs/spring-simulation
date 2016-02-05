package com.github.wztbbs.parser;

import com.github.wztbbs.ProxyBeanHandler;
import com.github.wztbbs.advice.Advice;
import com.github.wztbbs.advice.AspectInfo;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by nhn on 2016/1/11.
 */
public class BeanParser {

    private Document document;

    private Element root;

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    public Object getBean(String id) {
        return beanMap.get(id);
    }

    public Map<String, Object> getBeanMap() {
        return this.beanMap;
    }

    public BeanParser(InputStream in) throws Exception{
        SAXReader reader = new SAXReader();
        document = reader.read(in);
        root = document.getRootElement();
        parseBeans();
        parseAop();
    }

    private void parseAop() throws Exception{
        List aopConfigs = root.elements("aop-config");
        for(Iterator it = aopConfigs.iterator(); it.hasNext();) {
            Element aopConfig = (Element)it.next();
            List aspects = aopConfig.elements("aspect");
            for(Iterator it1 = aspects.iterator(); it1.hasNext();) {
                parseAspect((Element)it1.next());
            }
        }
    }

    private void parseAspect(Element aspect) throws Exception {
        AspectInfo aspectInfo = new AspectInfo();
        String refId = aspect.attributeValue("ref");
        Object refObject = this.beanMap.get(refId);
        if(refObject == null) {
            parseBean(NodeHelper.getElementById(root, "bean", refId));
        }
        aspectInfo.setAdviceBean(refObject);

        Class clazz = refObject.getClass();

        String express = aspect.element("pointcut").attributeValue("expression");
        aspectInfo.setExpression(express);

        Method beforeMethod = null;
        Method aroundMethod = null;
        Method afterMethod = null;
        for(Method method : clazz.getDeclaredMethods()) {
            if(method.getName().equals(aspect.element("before").attributeValue("method"))) {
                beforeMethod = method;
            }else if(method.getName().equals(aspect.element("after").attributeValue("method"))) {
                afterMethod = method;
            }else if(method.getName().equals(aspect.element("around").attributeValue("method"))) {
                aroundMethod = method;
            }
        }

        if(beforeMethod != null) {
            aspectInfo.setBeforeMethod(beforeMethod);
        }

        if(aroundMethod != null) {
            aspectInfo.setAroundMethod(aroundMethod);
        }

        if(afterMethod != null) {
            aspectInfo.setAfterMethod(afterMethod);
        }
        ProxyBeanHandler.getAspectInfo().add(aspectInfo);
    }


    private void parseBeans() throws Exception{
        List nodes = root.elements("bean");
        for(Iterator it = nodes.iterator(); it.hasNext();) {
            Element node = (Element)it.next();
            parseBean(node);
        }
    }

    private Object parseBean(Element node) throws Exception{
        String id = node.attributeValue("id");
        if(id == null) {
            throw new InvalidParameterException("id can not be null, node:" + node);
        }
        List constructors = node.elements("constructor-arg");
        Object bean = null;
        Class type = Class.forName(node.attributeValue("class"));
        if(constructors == null || constructors.isEmpty()) {
            bean = type.newInstance();
        }else {
            List<Object> paramObjects = new ArrayList<Object>();
            List<Class> paramTypes = new ArrayList<Class>();
            for(Iterator it = constructors.iterator(); it.hasNext();) {
                Element element = (Element)it.next();
                String refName = element.attributeValue("ref");
                if(refName != null) {
                    Element refNode = NodeHelper.getElementById(root, "bean", refName);
                    Object object = beanMap.get(refName);
                    if(object == null) {
                        object = parseBean(refNode);
                    }
                    paramObjects.add(object);

                    paramTypes.add(Class.forName(refNode.attributeValue("class")));
                }else {
                    String paramType = element.attributeValue("type");
                    String value = element.attributeValue("value");
                    paramTypes.add(Class.forName(paramType));
                    paramObjects.add(TypeHelper.getInstanceForName(paramType, value));
                }
            }

            Class[] classes = new Class[paramTypes.size()];
            Object[] objects = new Object[paramObjects.size()];
            paramTypes.toArray(classes);
            paramObjects.toArray(objects);
            Constructor constructor = type.getConstructor(classes);
            bean = constructor.newInstance(objects);
        }

        parseProperty(bean, node);

        if(type.getInterfaces().length < 1 || type.isAssignableFrom(Advice.class)) {
            beanMap.put(id, bean);
        }else {
            beanMap.put(id, new ProxyBeanHandler(bean).getProxyInstance());
        }
        return bean;
    }

    private void parseProperty(Object object, Element node) throws Exception{
        Class type = object.getClass();
        List properties = node.elements("property");
        for(Iterator it = properties.iterator(); it.hasNext();) {
            Element element = (Element)it.next();
            String propertyName = element.attributeValue("name");
            String refObjectId = element.attributeValue("ref");
            if(refObjectId != null) {
                Element refElement = NodeHelper.getElementById(root, "bean", refObjectId);
                Object refObject = beanMap.get(refObjectId);
                if(refObject == null) {
                    refObject = parseBean(refElement);
                }
                Class refClass = Class.forName(refElement.attributeValue("class"));
                type.getMethod("set" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1), new Class[]{refClass}).invoke(object, refObject);
            }else {
                 Class refClass = type.getDeclaredField(propertyName).getType();
                 type.getMethod("set" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1), new Class[]{refClass}).invoke(object, element.attributeValue("value"));
            }
        }
    }

}

package com.github.wztbbs;

import com.github.wztbbs.advice.AspectInfo;
import com.github.wztbbs.advice.ProceedingJoinPoint;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * Created by nhn on 2016/1/12.
 */
public class ProxyBeanHandler implements InvocationHandler {

    private Object target;

    // 存储所有切面
    private static Set<AspectInfo> aspectInfos = new HashSet<AspectInfo>();


    public static void addAspectInfo(AspectInfo aspectInfo) {
        aspectInfos.add(aspectInfo);
    }

    public static Set<AspectInfo> getAspectInfo() {
        return aspectInfos;
    }

    public ProxyBeanHandler(Object target) {
        this.target = target;
    }

    // 获取代理实例
    public Object getProxyInstance() {
        if (target == null) {
            return null;
        }
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        List<AspectInfo> acceptAspects = new ArrayList<AspectInfo>();
        for(AspectInfo aspectInfo : aspectInfos) {
            String a = target.getClass().getName() + "." + method.getName();
            if(aspectInfo.getPattern().matcher(a).matches()) {
                acceptAspects.add(aspectInfo);
            }
        }

        for(AspectInfo aspectInfo : acceptAspects) {
            aspectInfo.getBeforeMethod().invoke(aspectInfo.getAdviceBean(), new Object[]{});
        }

        ProceedingJoinPoint joinPoint = new ProceedingJoinPoint(method, target, args);
        for(int i = acceptAspects.size() - 1 ; i >= 0; i--) {
            AspectInfo info = acceptAspects.get(i);
            joinPoint = new ProceedingJoinPoint(info.getAroundMethod(),info.getAdviceBean(), new Object[]{joinPoint});
        }
        Object result = joinPoint.getMethod().invoke(joinPoint.getTarget(), joinPoint.getArgs());

        for(int i = acceptAspects.size() - 1 ; i >= 0; i--) {
            AspectInfo info = acceptAspects.get(i);
            info.getAfterMethod().invoke(info.getAdviceBean(), new Object[]{});
        }

        return result;
    }
}

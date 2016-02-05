package com.github.wztbbs.advice;

import java.lang.reflect.Method;

/**
 * Created by wztbbs on 2016/1/12.
 */
public class ProceedingJoinPoint {

    private Method method;
    private Object[] args;
    private Object target;

    public ProceedingJoinPoint(Method method, Object target, Object[] args) {
        this.method = method;
        this.args = args;
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}

package com.github.wztbbs.advice;

/**
 * Created by wztbbs on 2016/1/12.
 */
public interface Advice {

    public void before();

    public Object around(ProceedingJoinPoint joinPoint) throws Exception;

    public void after();

}

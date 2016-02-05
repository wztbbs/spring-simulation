package com.github.wztbbs.advice;

/**
 * Created by wztbbs on 2016/1/12.
 */
public class LogAdvice implements Advice {
    @Override
    public void before() {
        System.out.println("log before");
    }

    @Override
    public Object around(ProceedingJoinPoint joinPoint) throws Exception {
        System.out.println("log before around");
        Object result = joinPoint.getMethod().invoke(joinPoint.getTarget(), joinPoint.getArgs());
        System.out.println("log after around");
        return result;
    }

    @Override
    public void after() {
        System.out.println("log after");
    }
}

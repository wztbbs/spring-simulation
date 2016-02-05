package com.github.wztbbs.advice;

public class DemoAdvice implements Advice {
	
	public void before(){
        System.out.println("Before...");
    }
 
    public Object around(ProceedingJoinPoint joinPoint) throws Exception{
        System.out.println("Before-around...");
        Object returnValue = joinPoint.getMethod().invoke(joinPoint.getTarget(), joinPoint.getArgs());
        System.out.println("After-around...");
        return returnValue;
    }
 
    public void after(){
        System.out.println("After...");
    }
}

package com.github.wztbbs.advice;

/**
 * Created by Administrator on 2014/4/8.
 */

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/*
 * 切面信息Bean
 * 每1个切面最多只有：
 * 1个切点expression
 * 1个增强bean
 * 1个前置增强、环绕增强、后置增强
 */
public class AspectInfo {
    @Override
	public String toString() {
		return "AspectInfo [expression=" + expression + ", adviceBean=" + adviceBean + ", beforeMethod=" + beforeMethod + ", aroundMethod=" + aroundMethod + ", afterMethod=" + afterMethod + "]";
	}

	private String expression = "";
    private Object adviceBean = null;
    private Method beforeMethod = null;
    private Method aroundMethod = null;
    private Method afterMethod = null;
    private Pattern pattern;

    public AspectInfo(){

    }

    public AspectInfo(String expression, Object adviceBean,
                      Method beforeMethod, Method aroundMethod, Method afterMethod) {
        setExpression(expression);
        setAdviceBean(adviceBean);
        setBeforeMethod(beforeMethod);
        setAroundMethod(aroundMethod);
        setAfterMethod(afterMethod);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.pattern = Pattern.compile(expression);
        this.expression = expression;
    }

    public Object getAdviceBean() {
        return adviceBean;
    }

    public void setAdviceBean(Object adviceBean) {
        this.adviceBean = adviceBean;
    }

    public Method getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(Method beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public Method getAroundMethod() {
        return aroundMethod;
    }

    public void setAroundMethod(Method aroundMethod) {
        this.aroundMethod = aroundMethod;
    }

    public Method getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(Method afterMethod) {
        this.afterMethod = afterMethod;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}

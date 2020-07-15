package com.ppdai.atlas.aop;

import org.aspectj.lang.annotation.Pointcut;

public class ResourcePointCuts {

    @Pointcut("execution(public * com.ppdai.atlas.controller..*.*(..))")
    public void webController() {
    }

}

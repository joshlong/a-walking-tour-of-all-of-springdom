package com.joshlong.spring.walkingtour.ioc.basicioc.componentscan;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {
        String pkgName = Config.class.getPackage().getName();
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(pkgName);
        ac.registerShutdownHook();
    }
}

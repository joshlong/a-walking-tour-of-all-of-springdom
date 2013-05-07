package com.joshlong.spring.walkingtour.ioc.strangebeans.spel;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {
        new AnnotationConfigApplicationContext(Config.class);
    }
}

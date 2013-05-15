package com.joshlong.spring.walkingtour.ioc.lifecycles.interfaces;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(Astronaut.class.getPackage().getName());

        // NB important!
        annotationConfigApplicationContext.registerShutdownHook();

    }
}

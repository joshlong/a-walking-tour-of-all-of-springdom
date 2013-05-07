package com.joshlong.spring.walkingtour.ioc.lifecycles.smartlifecycle;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.joshlong.spring.walkingtour.ioc.lifecycles.annotations.Astronaut;

public class Main {

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Astronaut.class.getPackage().getName());
        applicationContext.registerShutdownHook();
    }
}

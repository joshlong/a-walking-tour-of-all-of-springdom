package org.springsource.sawt.ioc.lifecycles.smartlifecycle;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.sawt.ioc.lifecycles.annotations.Astronaut;

public class Main {

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Astronaut.class.getPackage().getName());
        applicationContext.registerShutdownHook();
    }
}

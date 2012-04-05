package org.springsource.examples.sawt.lifecycles.container;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class.getPackage().getName());
        Client c = applicationContext.getBean(Client.class);
        c.begin();
    }
}

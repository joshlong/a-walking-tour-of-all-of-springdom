package org.springsource.examples.sawt.javaconfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * no XML: java config in the DS, annotations for everything else.
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Main.class.getPackage().getName());
    }
}

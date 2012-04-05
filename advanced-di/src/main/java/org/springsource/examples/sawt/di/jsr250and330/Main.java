package org.springsource.examples.sawt.di.jsr250and330;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {
        new AnnotationConfigApplicationContext(Main.class.getPackage().getName());
    }
}

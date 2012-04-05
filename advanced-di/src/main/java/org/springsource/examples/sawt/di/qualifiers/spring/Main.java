package org.springsource.examples.sawt.di.qualifiers.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {
        new AnnotationConfigApplicationContext(Main.class.getPackage().getName());
    }
}

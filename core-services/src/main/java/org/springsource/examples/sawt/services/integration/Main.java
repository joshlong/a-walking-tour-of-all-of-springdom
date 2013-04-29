package org.springsource.examples.sawt.services.integration;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(IntegrationConfiguration.class);
    }
}

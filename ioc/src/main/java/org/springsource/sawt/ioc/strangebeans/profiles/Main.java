package org.springsource.sawt.ioc.strangebeans.profiles;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.getEnvironment().setActiveProfiles("production");
        ac.register(Config.class);
        ac.refresh();
    }
}

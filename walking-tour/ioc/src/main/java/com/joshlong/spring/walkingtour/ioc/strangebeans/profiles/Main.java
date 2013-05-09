package com.joshlong.spring.walkingtour.ioc.strangebeans.profiles;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.getEnvironment().setActiveProfiles("production");
        ac.register(ProfileConfiguration.class);
        ac.refresh();
    }
}

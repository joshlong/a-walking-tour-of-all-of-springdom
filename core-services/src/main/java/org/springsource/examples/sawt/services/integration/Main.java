package org.springsource.examples.sawt.services.integration;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Launches our simple integration solution.
 *
 * @author Josh Long
 */
public class Main {
    public static void main(String[] args) throws Throwable {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().setActiveProfiles("default");
        applicationContext.scan(IntegrationConfiguration.class.getPackage().getName());
        applicationContext.refresh();

    }
}

package org.springsource.examples.sawt.services.integration;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Launches our simple integration solution.
 *
 * @author Josh Long
 */
public class Main {
    public static void main(String[] args) throws Throwable {
         new AnnotationConfigApplicationContext(IntegrationConfiguration.class);
    }
}

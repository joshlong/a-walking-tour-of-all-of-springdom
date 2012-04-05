package org.springsource.examples.sawt.services.integration;


import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launches our simple integration solution.
 *
 * @author Josh Long
 */
public class Main {
    public static void main(String[] args) throws Throwable {

        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("/org/springsource/examples/sawt/services/integration/context.xml");
    }

}

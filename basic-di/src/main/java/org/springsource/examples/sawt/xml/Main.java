package org.springsource.examples.sawt.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:/org/springsource/examples/sawt/xml/config.xml");

        CustomerService customerService = applicationContext.getBean(CustomerService.class);


    }
}

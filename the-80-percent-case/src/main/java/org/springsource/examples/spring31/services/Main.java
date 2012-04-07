package org.springsource.examples.spring31.services;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class Main {
    public static void main(String[] args) throws Throwable {

        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(ServicesConfiguration.class.getPackage().getName());
        CustomerService customerService = annotationConfigApplicationContext.getBean(CustomerService.class);
        Customer customer = customerService.createCustomer("Josh", "Long", new Date());
        customer = customerService.getCustomerById(customer.getId());
        customer = customerService.getCustomerById(customer.getId());
    }
}                           


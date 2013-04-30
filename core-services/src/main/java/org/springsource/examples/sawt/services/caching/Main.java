package org.springsource.examples.sawt.services.caching;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

public class Main {
    public static void main(String args[]) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(RedisCachingConfiguration.class);
        CustomerService customerService = annotationConfigApplicationContext.getBean(CustomerService.class);
        Customer customer = customerService.createCustomer("Cache", "Me");
        Customer result = null;
        int i = 0;
        while (i++ < 10) {
            result = customerService.getCustomerById(customer.getId());
            System.out.println(result.getId());
        }
        customerService.updateCustomer(customer.getId(), "Cache", "NoMore");
        result = customerService.getCustomerById(customer.getId());
        System.out.println(result.getId());
    }
}

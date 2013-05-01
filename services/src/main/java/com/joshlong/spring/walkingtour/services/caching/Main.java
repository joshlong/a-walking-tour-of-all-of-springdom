package com.joshlong.spring.walkingtour.services.caching;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;

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

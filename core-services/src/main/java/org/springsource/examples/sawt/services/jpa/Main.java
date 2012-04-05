package org.springsource.examples.sawt.services.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

public class Main {
    public static void main(String args[]) throws Throwable {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);


        Log log = LogFactory.getLog(Main.class);

        CustomerService customerService = applicationContext.getBean(CustomerService.class);

        Customer customer = customerService.createCustomer("Jpa", "Lover");

        Customer retrievedCustomer = customerService.getCustomerById(customer.getId());

        log.info(String.format("customer.id (%s) == retreivedCustomer.id (%s)?  %s",
                customer.getId(), retrievedCustomer.getId(), customer.getId().equals(retrievedCustomer.getId())));

        Customer updatedCustomer = customerService.updateCustomer(customer.getId(), "JPA", "Lover");
        log.info(String.format("updated customer's firstName: %s", updatedCustomer.getFirstName()));


    }
}

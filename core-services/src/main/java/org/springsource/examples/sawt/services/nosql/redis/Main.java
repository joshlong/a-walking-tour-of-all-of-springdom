package org.springsource.examples.sawt.services.nosql.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

/**
 * simple implementation of {@link org.springsource.examples.sawt.CustomerService} that works with a NoSQL store - in this case - Redis.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class.getPackage().getName());
        Log log = LogFactory.getLog(Main.class);
        CustomerService customerService = applicationContext.getBean(CustomerService.class);
        Customer customer = customerService.createCustomer("Josh", "Long");
        Customer retrievedCustomer = customerService.getCustomerById(customer.getId());
        log.info(String.format("customer.id (%s) == retrievedCustomer.id (%s)?  %s", customer.getId(), retrievedCustomer.getId(), customer.getId().equals(retrievedCustomer.getId())));
        Customer updatedCustomer = customerService.updateCustomer(customer.getId(), "NoSql", "Lover");
        log.info(String.format("updated customer's firstName: %s", updatedCustomer.getFirstName()));
    }
}

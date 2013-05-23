package com.joshlong.spring.walkingtour.services;

import com.joshlong.spring.walkingtour.services.data.redis.RedisConfiguration;
import com.joshlong.spring.walkingtour.services.jdbc.JdbcConfiguration;
import com.joshlong.spring.walkingtour.services.jpa.JpaConfiguration;
import com.joshlong.spring.walkingtour.services.model.Customer;
import org.apache.commons.logging.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * some place to host the various harnesses for the {@link CustomerService customer service} implementations.
 *
 * @author Josh Long
 */
public class CustomerServiceMain {

    private static Log log = LogFactory.getLog(CustomerServiceMain.class);

    public static void main(String args[]) throws Throwable {

        Class<?>[] configurationClasses = { JpaConfiguration.class,
                                            JdbcConfiguration.class,
                                            RedisConfiguration.class};

        for (Class<?> c : configurationClasses) {
            AnnotationConfigApplicationContext context = null;
            try {
                context = new AnnotationConfigApplicationContext();
                context.scan(c.getPackage().getName());
                context.refresh();
                System.out.println( "configurationClass = " + c.getName());
                CustomerService customerService = context.getBean(CustomerService.class);
                doWithCustomerService(customerService);
            } finally {
                if (context != null)
                    context.close();
            }
        }
    }

    public static void doWithCustomerService(CustomerService customerService) throws Throwable {

        Customer customer = customerService.createCustomer("Josh", "Long");

        Customer retrievedCustomer = customerService.getCustomerById(customer.getId());

        log.info(String.format("customer.id (%s) == retrievedCustomer.id (%s)?  %s",
                customer.getId(), retrievedCustomer.getId(), customer.getId().equals(retrievedCustomer.getId())));

        Customer updatedCustomer = customerService.updateCustomer(customer.getId(), "Jdbc", "Lover");
        log.info(String.format("updated customer's firstName: %s", updatedCustomer.getFirstName()));

        if (customerService instanceof SearchCapableCustomerService) {
            SearchCapableCustomerService searchCapableCustomerService = (SearchCapableCustomerService) customerService;
            for (Customer c : searchCapableCustomerService.search("josh")) {
                log.info("found customer " + c.toString());
            }
        }

    }


}

package org.springsource.examples.sawt.services.nosql.redis.caching;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.nosql.redis.model.Customer;


/**
 * Simple example that demonstrates persisting data using the {@link org.springframework.data.redis.cache.RedisCacheManager}
 * redis-backed cache manager implementation
 *
 * @author Josh Long
 */
public class Main {
    public static void main(String args[]) throws Throwable {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RedisCachingConfiguration.class.getPackage().getName());

        Log log = LogFactory.getLog(Main.class);

        CustomerService customerService = applicationContext.getBean(CustomerService.class);

        Customer customer = customerService.createCustomer("Redis", "Lover");

        for (int i = 0; i < 10; i++) {
            Customer retrievedCustomer = customerService.getCustomerById(customer.getId());
            log.info(String.format("customer.id (%s) == retreivedCustomer.id (%s)?  %s", customer.getId(), retrievedCustomer.getId(), customer.getId() == (retrievedCustomer.getId())));
        }

        Customer updatedCustomer = customerService.updateCustomer(customer.getId(), "JPA", "Lover");

        log.info(String.format("updated customer's firstName: %s", updatedCustomer.getFirstName()));

    }
}
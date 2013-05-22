package com.joshlong.spring.walkingtour.services.data.redis;

import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.Topic;

import java.util.Collection;

/**
 * @author Josh Long
 */
public class Main {
    public static void main(String args[]) throws Throwable {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RedisConfiguration.class);
        applicationContext.registerShutdownHook();

        CustomerService customerService = applicationContext.getBean(CustomerService.class);
        Topic topic = applicationContext.getBean(Topic.class);

        Log log = LogFactory.getLog(Main.class);


        Customer customer = customerService.createCustomer("Josh", "Long");

        Customer retrievedCustomer = customerService.getCustomerById(customer.getId());

        log.info(String.format("customer.id (%s) == retrievedCustomer.id (%s)?  %s",
                customer.getId(), retrievedCustomer.getId(), customer.getId().equals(retrievedCustomer.getId())));

        Customer updatedCustomer = customerService.updateCustomer(customer.getId(), "Redis", "Lover");
        log.info(String.format("updated customer's firstName: %s", updatedCustomer.getFirstName()));
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
        redisTemplate.convertAndSend(topic.getTopic(), " {'id':'" + updatedCustomer.getId() + "'}");


        Collection<Customer> allCustomers =  customerService.loadAllCustomers() ;
        for(Customer c  : allCustomers) {
            System.out.println(ToStringBuilder.reflectionToString(c));
        }
    }

}

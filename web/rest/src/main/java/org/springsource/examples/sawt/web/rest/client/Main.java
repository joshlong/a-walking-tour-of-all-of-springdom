package org.springsource.examples.sawt.web.rest.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;
import org.springsource.examples.sawt.services.model.Customer;

public class Main {
    public static void main(String[] args) throws Throwable {

        Log log = LogFactory.getLog(Main.class);

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class.getPackage().getName());

        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);


        String url = "http://127.0.0.1:8080/rest/"; // root


        Customer c = new Customer("New", "Customer" + System.currentTimeMillis());

        c = restTemplate.postForEntity(url + "customers", c, Customer.class).getBody();
        log.info("created customer " + c.toString());

        Customer customer1 = restTemplate.getForEntity(url + "customer/{customerId}", Customer.class, c.getId()).getBody();
        log.info("fetched customer " + customer1.toString());

        c.setFirstName(c.getFirstName() + "Updated");
        c = restTemplate.postForEntity(url + "customer/{customerId}", c, Customer.class, c.getId()).getBody();
        log.info("updated the customer:  " + c.toString());

    }
}

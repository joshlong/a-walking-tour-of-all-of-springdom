package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

public class Main {
    static public void main (String [] args) throws Throwable {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext( Main.class.getPackage().getName()) ;

        MongoCrossStoreCustomerService customerService = ac.getBean( MongoCrossStoreCustomerService.class);
        MongoCustomer mongoCustomer = customerService.createCustomer("Josh", "Long");





    }
}

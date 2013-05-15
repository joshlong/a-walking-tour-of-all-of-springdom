package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    static public void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MongoDbCrossStoreConfiguration.class.getPackage().getName());
        MongoDbCrossStoreCustomerService customerServiceDb = ac.getBean(MongoDbCrossStoreCustomerService.class);
        MongoCustomer mongoCustomer = customerServiceDb.createCustomer("Josh", "Long");
        customerServiceDb.addProduct(mongoCustomer.getId(), "Shamwow in Shanghai", 104.22);
    }
}

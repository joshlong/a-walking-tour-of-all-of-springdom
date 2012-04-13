package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    static public void main (String [] args) throws Throwable {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext( Main.class.getPackage().getName()) ;

        MongoDbCrossStoreCustomerService customerServiceDb = ac.getBean( MongoDbCrossStoreCustomerService.class);

        MongoCustomer mongoCustomer = customerServiceDb.createCustomer("Josh", "Long");
        mongoCustomer.getMongoProductInfo().addProduct( new Product( "Shamwow" , 104.22));




    }
}

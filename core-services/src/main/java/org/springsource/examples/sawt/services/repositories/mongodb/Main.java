package org.springsource.examples.sawt.services.repositories.mongodb;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.examples.sawt.services.model.Customer;

import java.util.ArrayList;

public class Main {

    static public void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
                MongoRepositoryConfiguration.class
        );
        CustomerRepository customerRepository = annotationConfigApplicationContext.getBean(CustomerRepository.class);

        ArrayList<Customer> customers = new ArrayList<Customer>();
        customers.add(new Customer("Josh", "Long"));
        customers.add(new Customer("Josh", "Williams"));
        customers.add(new Customer("Mark", "Fisher"));
        customers.add(new Customer("Mark", "Pollack"));
        customers.add(new Customer("Oliver", "Gierke"));
        customers.add(new Customer("Dave", "Turanski"));
        customers.add(new Customer("Chris", "Beams"));
        customers.add(new Customer("Chris", "Brown"));

        customerRepository.deleteAll();

        for (Customer customer : customers) {
            customerRepository.save(customer);
        }

        System.out.println("all customers: " + customerRepository.findAll());

    }
}

package com.joshlong.spring.walkingtour.services.repositories.jpa;

import org.apache.commons.logging.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.joshlong.spring.walkingtour.services.model.Customer;

import java.math.BigDecimal;
import java.util.*;

public class Main {

    private final static Random random = new Random();

    public static void main(String[] args) throws Throwable {

        Log log = LogFactory.getLog(Main.class);

        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(JpaRepositoryConfiguration.class);

        CustomerRepository customerRepository = annotationConfigApplicationContext.getBean(CustomerRepository.class);

        customerRepository.deleteAll();

        ArrayList<Customer> customers = new ArrayList<Customer>();
        customers.add(new Customer("Josh", "Long"));
        customers.add(new Customer("Josh", "Williams"));
        customers.add(new Customer("Mark", "Fisher"));
        customers.add(new Customer("Mark", "Pollack"));
        customers.add(new Customer("Oliver", "Gierke"));
        customers.add(new Customer("Dave", "Turanski"));
        customers.add(new Customer("Chris", "Beams"));
        customers.add(new Customer("Chris", "Brown"));

        ArrayList<Customer> savedCustomers =new ArrayList<Customer>() ;
        for (Customer customer : customers)
            savedCustomers.add(customerRepository.save(customer));

        Object[] returnValueForFrequencyGraph = customerRepository.findMostFrequentName();
        BigDecimal bd = (BigDecimal) returnValueForFrequencyGraph[0];
        String name = (String) returnValueForFrequencyGraph[1];
        System.out.println(String.format("the name %s occurs %s times", name, bd));

        System.out.println(customerRepository.toString());


        Customer customer = savedCustomers.get( random.nextInt(customers.size()));
        System.out.println("customer: " + customer.toString());

        Customer fromDataBase = customerRepository.findOne( customer.getId() ) ;

        System.out.println( customer.toString() + " = " +  fromDataBase.toString() +"? "+ ( fromDataBase.equals( customer)));

    }
}

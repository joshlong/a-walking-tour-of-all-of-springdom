package com.joshlong.spring.walkingtour.services.repositories.mongodb;


import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.*;
import com.joshlong.spring.walkingtour.services.model.Customer;

import java.io.InputStream;
import java.util.*;

public class Main {

    private final static Random random = new Random();

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = 
        		new AnnotationConfigApplicationContext( MongoRepositoryConfiguration.class );
        
        CustomerRepository customerRepository = annotationConfigApplicationContext.getBean(CustomerRepository.class);

        Customer[] customers = new Customer[]{
                (new Customer("Josh", "Long")),
                (new Customer("Josh", "Williams")),
                (new Customer("Mark", "Fisher")),
                (new Customer("Mark", "Pollack")),
                (new Customer("Oliver", "Gierke")),
                (new Customer("Dave", "Turanski")),
                (new Customer("Chris", "Beams"))
        };

        customerRepository.deleteAll();

        for (Customer customer : customers)
            customerRepository.save(customer);

        Iterable<Customer> customerList = customerRepository.findAll();

        List<Customer> allCustomersFromDatabase = new ArrayList<Customer>();
        for (Customer customer : customerList)
            allCustomersFromDatabase.add(customer);

        Customer randomCustomer = customers[random.nextInt(allCustomersFromDatabase.size())];

        Resource resource = new ClassPathResource("/sample/photo.jpg");
        InputStream inputStream = resource.getInputStream();
        customerRepository.storeProfilePhoto(randomCustomer.getId(), inputStream);

        InputStream readInputStream = customerRepository.readProfilePhoto(randomCustomer.getId());

        System.out.println(
                "Do the bytes stored in MongoDB match" +
                " the byte[]s for the image we stored in Mongo? " +
                    Arrays.equals(IOUtils.toByteArray(readInputStream), (IOUtils.toByteArray(resource.getInputStream()))));

    }

}

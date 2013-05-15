package com.joshlong.spring.walkingtour.services.data.mongodb;

import com.joshlong.spring.walkingtour.services.model.Customer;
import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.Assert;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Demonstration of Spring Data MongoDB
 *
 * @author Josh Long
 */
public class Main {
    static public void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(MongoConfiguration.class);

        MongoTemplate mongoTemplate = annotationConfigApplicationContext.getBean(MongoTemplate.class);

        Class<Customer> entityClass = Customer.class;

        Collection<Customer> customers = new ArrayList<Customer>();

        // delete
        mongoTemplate.dropCollection(entityClass);

        // insert
        for (long i = 0; i < 2; i++)
            customers.add(new Customer(BigInteger.valueOf(i), "First" + i, "Last" + i));
        mongoTemplate.insert(customers, entityClass);

        // find
        Query q = new Query(Criteria.where("id").is(BigInteger.valueOf(1L)));
        List<Customer> customersThatMatchTheQuery = mongoTemplate.find(q, entityClass);
        for (Customer customer : customersThatMatchTheQuery) {
            System.out.println("result: " + customer.toString());
        }

        // update
        for (Customer c : customersThatMatchTheQuery) {
            c.setFirstName((c.getFirstName() + "").toUpperCase());
            c.setLastName((c.getLastName() + "").toUpperCase());
            Query query = new Query(Criteria.where("id").is(c.getId()));

            Update update = new Update();
            update.set("firstName", c.getFirstName().toUpperCase());
            update.set("lastName", c.getLastName().toUpperCase());

            Customer updatedCustomer = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Customer.class);
            System.out.println("updated " + updatedCustomer.toString());
        }


        GridFsTemplate gridFsTemplate = annotationConfigApplicationContext.getBean(GridFsTemplate.class);

        // populate metadata
        String fileName = "photo.jpg";

        Resource file = new ClassPathResource("/sample/" + fileName);
        long originalSize = file.contentLength();
        gridFsTemplate.store(file.getInputStream(), fileName);
        Query queryForFile = new Query(Criteria.where("filename").is(fileName));
        List<GridFSDBFile> result = gridFsTemplate.find(queryForFile);
        GridFSDBFile gridFSDBFile = result.iterator().next();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File tmpOutputFile = File.createTempFile("profile", fileName);

        try {
            inputStream = gridFSDBFile.getInputStream();
            outputStream = new FileOutputStream(tmpOutputFile);
            IOUtils.copy(inputStream, outputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }

        Assert.isTrue(tmpOutputFile.length() == originalSize, "the file size should be the same as measured on the input side.");

    }

}

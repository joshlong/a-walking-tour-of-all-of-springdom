package com.joshlong.spring.walkingtour.services.repositories.mongodb;

import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import com.joshlong.spring.walkingtour.services.model.Customer;

import javax.inject.Inject;
import java.io.InputStream;
import java.math.BigInteger;

public class CustomerRepositoryImpl implements MongoCustomerRepository {


    private GridFsTemplate gridFsTemplate;
    private CustomerRepository customerRepository;

    @Inject
    public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Inject
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public InputStream readProfilePhoto(BigInteger customerId) {
        Customer customer = customerRepository.findOne((customerId));
        GridFSDBFile gridFSDBFile = this.gridFsTemplate.findOne(queryForManagedUpload(customer));
        return gridFSDBFile.getInputStream();
    }

    @Override
    public void storeProfilePhoto(BigInteger customerId, InputStream bytes) {
        Customer customer = customerRepository.findOne((customerId));
        try {
            this.gridFsTemplate.store(bytes, fileNameForManagedUpload(customer));
        } finally {
            IOUtils.closeQuietly(bytes);
        }
    }

    private String fileNameForManagedUpload(Customer c) {
        return c.getId().toString();
    }

    private Query queryForManagedUpload(Customer c) {
        String fileName = fileNameForManagedUpload(c);
        return (new Query().addCriteria(Criteria.where("filename").is(fileName)));
    }
}

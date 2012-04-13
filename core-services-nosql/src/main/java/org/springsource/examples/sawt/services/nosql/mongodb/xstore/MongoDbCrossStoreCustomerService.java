package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class MongoDbCrossStoreCustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public MongoCustomer getCustomerById(long id) {
        return this.entityManager.find(MongoCustomer.class, id);
    }

    public MongoCustomer createCustomer(String fn, String ln) {
        MongoCustomer newCustomer = new MongoCustomer();
        newCustomer.setFirstName(fn);
        newCustomer.setLastName(ln);
        this.entityManager.persist(newCustomer);
        return newCustomer;
    }

    public MongoCustomer updateCustomer(long id, String fn, String ln) {
        MongoCustomer customer = this.getCustomerById(id);
        customer.setFirstName(fn);
        customer.setLastName(ln);
        this.entityManager.merge(customer);
        return getCustomerById(id);
    }

}

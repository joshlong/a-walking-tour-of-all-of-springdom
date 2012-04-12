package org.springsource.examples.sawt.services.nosql.redis.caching;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
@Transactional
public class RedisCachingCustomerService implements CustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Cacheable( "customers")
    @Transactional(readOnly = true)
    public Customer getCustomerById(long id) {
        return this.entityManager.find(Customer.class, id);
    }

    public Customer createCustomer(String fn, String ln) {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(fn);
        newCustomer.setLastName(ln);
        this.entityManager.persist(newCustomer);
        return newCustomer;
    }

    @CacheEvict("customers")
    public Customer updateCustomer(long id, String fn, String ln) {
        Customer customer = this.getCustomerById(id);
        customer.setFirstName(fn);
        customer.setLastName(ln);
        this.entityManager.merge(customer);
        return getCustomerById(id);
    }
}

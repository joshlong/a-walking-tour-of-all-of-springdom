package com.joshlong.spring.walkingtour.services.jpa;

import org.springframework.cache.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;

import javax.persistence.*;
import java.math.BigInteger;
/**
 * 
 * @author Joshua Long
 *
 */
@Service
@Transactional
public class JpaCustomerService implements CustomerService {

    private final static String CUSTOMER_CACHE = "customers";

    @PersistenceContext
    private EntityManager entityManager;

    @Cacheable(CUSTOMER_CACHE)
    @Transactional(readOnly = true)
    public Customer getCustomerById(BigInteger id) {
        return this.entityManager.find(Customer.class, id);
    }

    public Customer createCustomer(String fn, String ln) {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(fn);
        newCustomer.setLastName(ln);
        this.entityManager.persist(newCustomer);
        return newCustomer;
    }

    @CacheEvict( value = CUSTOMER_CACHE,key = "#id")
    public Customer updateCustomer(BigInteger id, String fn, String ln) {
        Customer customer = this.getCustomerById(id);
        customer.setFirstName(fn);
        customer.setLastName(ln);
        this.entityManager.merge(customer);
        return this.getCustomerById(id);
    }

}

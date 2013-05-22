package com.joshlong.spring.walkingtour.services.jpa;

import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;
import org.springframework.cache.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.Collection;

/**
 * @author Joshua Long
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

    public Collection<Customer> loadAllCustomers() {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaBuilderQuery = criteriaBuilder.createQuery(Customer.class);
        CriteriaQuery<Customer> customerCriteriaQuery = criteriaBuilderQuery.select(
                criteriaBuilderQuery.from(Customer.class));
        return this.entityManager.createQuery(customerCriteriaQuery).getResultList();
    }

    @CacheEvict(value = CUSTOMER_CACHE, key = "#id")
    public Customer updateCustomer(BigInteger id, String fn, String ln) {
        Customer customer = this.getCustomerById(id);
        customer.setFirstName(fn);
        customer.setLastName(ln);
        this.entityManager.merge(customer);
        return this.getCustomerById(id);
    }

}

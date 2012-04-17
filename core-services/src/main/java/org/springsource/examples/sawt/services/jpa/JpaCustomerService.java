package org.springsource.examples.sawt.services.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

@Service
@Transactional
public class JpaCustomerService implements CustomerService {

    @PersistenceContext
    private EntityManager entityManager;

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

    public Customer updateCustomer(long id, String fn, String ln) {
        Customer customer = this.getCustomerById(id);
        customer.setFirstName(fn);
        customer.setLastName(ln);
        this.entityManager.merge(customer);
        return getCustomerById(id);
    }
}

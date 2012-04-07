package org.springsource.examples.spring31.services;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private Log log = LogFactory.getLog(getClass());

    static private final String CUSTOMERS_REGION = "customers";

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Customer createCustomer(String firstName, String lastName, Date signupDate) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setSignupDate(signupDate);

        sessionFactory.getCurrentSession().save(customer);
        return customer;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Customer> getAllCustomers() {
        return sessionFactory.getCurrentSession().createCriteria(Customer.class).list();
    }


    @Cacheable(CUSTOMERS_REGION)
    @Transactional(readOnly = true)
    public Customer getCustomerById(Integer id) {
        log.debug( String.format("fetching customer# %s" , id));
        return (Customer) sessionFactory.getCurrentSession().get(Customer.class, id);
    }

    @CacheEvict(CUSTOMERS_REGION)
    @Transactional
    public void deleteCustomer(Integer id) {
        log.debug( String.format("deleting customer# %s" , id));

        Customer customer = getCustomerById(id);
        sessionFactory.getCurrentSession().delete(customer);
    }

    @CacheEvict(CUSTOMERS_REGION)
    @Transactional
    public void updateCustomer(Integer id, String fn, String ln, Date birthday) {
        
        log.debug( String.format("updating customer# %s" , id));
        Customer customer = getCustomerById(id);
        customer.setLastName(ln);
        customer.setSignupDate(birthday);
        customer.setFirstName(fn);
        customer.setId(id);
        sessionFactory.getCurrentSession().update(customer);
    }


}

package com.joshlong.spring.walkingtour.services;

import com.joshlong.spring.walkingtour.services.model.Customer;

import java.util.Collection;

/**
 * @author Josh Long
 */
public  interface SearchCapableCustomerService extends CustomerService {
    Collection<Customer> search (String query) ;
}

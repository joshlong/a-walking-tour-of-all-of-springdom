package com.joshlong.spring.walkingtour.services;

import com.joshlong.spring.walkingtour.services.model.Customer;

import java.math.BigInteger;
import java.util.Collection;

public interface CustomerService {
    Customer updateCustomer( BigInteger id, String fn, String ln);

    Customer getCustomerById(BigInteger id);

    Customer createCustomer(String fn, String ln);

    Collection<Customer> loadAllCustomers();


}

package org.springsource.examples.sawt.web.android.service;

import org.springsource.examples.sawt.web.android.model.Customer;

// a client side representation of the server side interface 
public interface CustomerService {
    Customer updateCustomer(long id, String fn, String ln);

    Customer getCustomerById(long id);

    Customer createCustomer(String fn, String ln);
}

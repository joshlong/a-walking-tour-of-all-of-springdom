package org.springsource.examples.sawt.services.cache;


public interface CustomerService {
    Customer updateCustomer(long id, String fn, String ln);

    Customer getCustomerById(long id);

    Customer createCustomer(String fn, String ln);
}

package org.springsource.examples.sawt;

import org.springsource.examples.sawt.services.model.Customer;

import java.math.BigInteger;

public interface CustomerService {
    Customer updateCustomer( BigInteger id, String fn, String ln);

    Customer getCustomerById(BigInteger id);

    Customer createCustomer(String fn, String ln);
}

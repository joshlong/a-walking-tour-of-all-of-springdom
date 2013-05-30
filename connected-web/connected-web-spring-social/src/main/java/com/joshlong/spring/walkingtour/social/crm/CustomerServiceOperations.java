package com.joshlong.spring.walkingtour.social.crm;

import java.util.*;

/**
 * client-side perspective of the RESTful service
 */
public interface CustomerServiceOperations {

    User currentUser();

    User getUserById  (  Long id ) ;

    Customer createCustomer(   Long userId, String firstName, String lastName, Date signupDate);

    Collection<Customer> searchCustomers(String name);

    List<Customer> getAllUserCustomers(Long userid);

    void deleteCustomer(Long id);

    void updateCustomer(Long id, String fn, String ln, Date birthday);
}


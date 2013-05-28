package com.joshlong.spring.walkingtour.social.crm;

import java.util.*;

/**
 * client-side perspective of the RESTful service
 */
public interface CustomerServiceOperations {

    CrmUserProfile currentUser();

    Customer createCustomer(Long userId, String firstName, String lastName, Date signupDate);

    Collection<Customer> searchCustomers(String name);

    List<Customer> getAllUserCustomers(Long userid);

    void deleteCustomer(Long id);

    void updateCustomer(Long id, String fn, String ln, Date birthday);
}


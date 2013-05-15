package com.joshlong.spring.walkingtour.android.view;

import com.joshlong.spring.walkingtour.android.Customer;

public class CustomerEvent {
    private Customer customer;

    public CustomerEvent(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}

package com.joshlong.spring.walkingtour.social.crm;

import java.io.Serializable;

public class Customer implements Serializable {
    private String firstName, lastName;
    private long id;

    public Customer(long id, String f, String l) {
        this.firstName = f;
        this.lastName = l;
        this.id = id;
    }

    public Customer(String f, String l) {
        this.firstName = f;
        this.lastName = l;
    }

    public long getId() {
        return this.id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }
}
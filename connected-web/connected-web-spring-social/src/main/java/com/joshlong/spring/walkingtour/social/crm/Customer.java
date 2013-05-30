package com.joshlong.spring.walkingtour.social.crm;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {
    private String firstName, lastName;
    private long id;

    public Customer() {
    }

    public Customer(long id, String f, String l) {
        this.firstName = f;
        this.lastName = l;
        this.id = id;
    }

    public Customer(String f, String l) {
        this.firstName = f;
        this.lastName = l;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                '}';
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
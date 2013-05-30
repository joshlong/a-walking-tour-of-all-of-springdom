package com.joshlong.spring.walkingtour.social.crm;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.*;

@JsonIgnoreProperties( ignoreUnknown = true )
public class User implements Serializable {
    private String firstName, lastName, username;
    private long id;
     private Set<Customer> customers = new HashSet<Customer>();

    public User() {
    }

    public User(String f, String l, String user) {
        this.firstName = f;
        this.lastName = l;
        this.username = user;
    }

    public User(long id, String f, String l, String user) {
        this(f, l, user);
        this.id = id;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public long getId() {
        return this.id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }
}
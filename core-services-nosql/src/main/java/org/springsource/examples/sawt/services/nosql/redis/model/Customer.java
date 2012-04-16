package org.springsource.examples.sawt.services.nosql.redis.model;


import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Table(name = "customer")
public class Customer implements java.io.Serializable {

    private Long id;
    private String firstName;
    private String lastName;

    public Customer() {
    }

    public Customer(String fn, String ln) {
        this.firstName = fn;
        this.lastName = ln;
    }

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}



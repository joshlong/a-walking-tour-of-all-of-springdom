package com.joshlong.spring.walkingtour.social.crm;

import java.io.Serializable;

public class CrmUserProfile implements Serializable {
    private String firstName, lastName, username;
    private long id;

    public CrmUserProfile(String f, String l, String user) {
        this.firstName = f;
        this.lastName = l;
        this.username = user;
    }

    public CrmUserProfile(long id, String f, String l, String user) {
        this(f, l, user);
        this.id = id;
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
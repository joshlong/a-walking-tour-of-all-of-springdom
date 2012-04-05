package org.springsource.examples.sawt.web.gwt.client.entities;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CustomerDto implements IsSerializable {
    private long id;
    private String firstName, lastName;

    public boolean isSaved() {
        return this.id > 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

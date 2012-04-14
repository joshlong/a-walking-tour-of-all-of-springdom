package org.springsource.examples.sawt.web.android.model;

import org.simpleframework.xml.Attribute;

/**
 * simple object to represent the server-side Customer entity
 *
 * @author Josh Long
 */
public class Customer {

    @Attribute(required = false)
    private long id;

    @Attribute(required = false, name = "first-name")
    private String firstName;

    @Attribute(required = false, name = "last-name")
    private String lastName;

    public Customer() {
    }

    public Customer(long id, String firstName, String lastName) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String f, String l) {
        this.lastName = l;
        this.firstName = f;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

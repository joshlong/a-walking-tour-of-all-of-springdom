package com.joshlong.spring.walkingtour.services.model;


import org.apache.commons.lang.builder.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.math.BigInteger;


@Entity
@NamedQuery(name = "Customer.generateUsername", query = "select  lower(concat( c.firstName  , c.lastName )) from Customer c where c.id = ?1")
@XmlRootElement(name = "customer", namespace =  Constants.NAMESPACE )
@Table(name = "customer")
public class Customer implements java.io.Serializable {

    private BigInteger id;
    private String firstName;
    private String lastName;

    public Customer() {
    }

    public Customer(String fn, String ln) {
        this.firstName = fn;
        this.lastName = ln;
    }

    public Customer(BigInteger id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @XmlAttribute(name = "id", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public BigInteger getId() {
        return this.id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @XmlAttribute(name = "first-name", required = false)
    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlAttribute(name = "last-name", required = false)
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



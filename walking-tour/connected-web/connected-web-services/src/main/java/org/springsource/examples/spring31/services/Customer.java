package org.springsource.examples.spring31.services;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@Entity(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    private Date signupDate;

    private String firstName;

    private String lastName;

    public Long getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    @XmlAttribute
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @XmlAttribute
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getSignupDate() {
        return signupDate;
    }

    @XmlAttribute
    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    public User getUser() {
        return user;
    }

    @XmlTransient
    public void setUser(User user) {
        this.user = user;
    }

}

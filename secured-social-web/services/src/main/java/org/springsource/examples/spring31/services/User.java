package org.springsource.examples.spring31.services;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple object that administers all customer data. This user is the one on whose behalf modifications to
 * {@link Customer customer data} are made.
 *
 * @author Josh Long
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@Entity(name = "UserAccount")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Customer> customers = new HashSet<Customer>();
    private boolean importedFromServiceProvider = false;
    private String firstName, lastName, username;
    private String password;
    private boolean profilePhotoImported;
    private String profilePhotoExt;
    private boolean enabled;
    private Date signupDate;

    public String getFirstName() {
        return firstName;
    }

    @XmlAttribute
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isImportedFromServiceProvider() {
        return importedFromServiceProvider;
    }

    @XmlAttribute
    public void setImportedFromServiceProvider(boolean importedFromServiceProvider) {
        this.importedFromServiceProvider = importedFromServiceProvider;
    }

    public String getLastName() {
        return lastName;
    }
    @XmlAttribute
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePhotoExt() {
        return profilePhotoExt;
    }

    @XmlAttribute
    public void setProfilePhotoExt(String profilePhotoExt) {
        this.profilePhotoExt = profilePhotoExt;
    }

    public boolean isProfilePhotoImported() {
        return profilePhotoImported;
    }

    @XmlAttribute
    public void setProfilePhotoImported(boolean profilePhotoImported) {
        this.profilePhotoImported = profilePhotoImported;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @XmlAttribute
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getSignupDate() {
        return signupDate;
    }

    @XmlAttribute
    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    public String getUsername() {
        return username;
    }

    @XmlAttribute
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @XmlAttribute
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Long id) {
        this.id = id;
    }


    public Set<Customer> getCustomers() {
        return customers;
    }

    @XmlTransient
    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
}

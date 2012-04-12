package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.mongodb.crossstore.RelatedDocument;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer")
public class MongoCustomer  implements Serializable {

     // all were doing is adding a notion of which products the {@link Customer} has purchased

    private Long id;

    private String firstName;

    private String lastName;

    @RelatedDocument
    private MongoProductList mongoProductList ;

    public MongoCustomer() {
    }

    public MongoCustomer(String fn, String ln) {
        this.firstName = fn;
        this.lastName = ln;
    }

    public MongoCustomer(long id, String firstName, String lastName) {
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




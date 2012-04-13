package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.mongodb.crossstore.RelatedDocument;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class MongoCustomer   {

    // all were doing is adding a notion of which products the {@link Customer} has purchased
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @RelatedDocument @Transient  private MongoProductInfo mongoProductInfo = new MongoProductInfo();
    private String firstName;
    private String lastName;

    public MongoProductInfo getMongoProductInfo() {
        if( this.mongoProductInfo == null )
            this.mongoProductInfo = new MongoProductInfo();

        return this.mongoProductInfo;
    }

    public void setMongoProductInfo(MongoProductInfo mongoProductInfo) {
        this.mongoProductInfo = mongoProductInfo;
    }

    public Long getId() {
        return id;
    }

    public MongoCustomer(){}

    public MongoCustomer(String firstName, String lastName, Long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}




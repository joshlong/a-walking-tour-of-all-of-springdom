package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.mongodb.crossstore.RelatedDocument;

import javax.persistence.*;

@Entity
public class MongoCustomer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @RelatedDocument  
    @Transient
    MongoProductInfo mongoProductInfo = new MongoProductInfo();

    String firstName;

    String lastName;

    public MongoProductInfo getMongoProductInfo() {
        if (this.mongoProductInfo == null)
            this.mongoProductInfo = new MongoProductInfo();

        return this.mongoProductInfo;
    }

    public void setMongoProductInfo(MongoProductInfo mongoProductInfo) {
        this.mongoProductInfo = mongoProductInfo;
    }

    public Long getId() {
        return id;
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




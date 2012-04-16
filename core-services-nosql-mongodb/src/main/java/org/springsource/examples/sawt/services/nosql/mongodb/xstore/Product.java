package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

/**
 * Simple entity designed to hold the state for a customer's purchase
 */
//@Document(collection =  "products")
public class Product {
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    double price;
    String name;
}

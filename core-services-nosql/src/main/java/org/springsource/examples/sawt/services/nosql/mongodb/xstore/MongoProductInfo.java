package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class MongoProductInfo {
    private List<Product> products = new ArrayList<Product>();

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> prods) {
        this.products = prods;
    }

    public void addProduct(Product product) {
        getProducts().add(product);
    }
}

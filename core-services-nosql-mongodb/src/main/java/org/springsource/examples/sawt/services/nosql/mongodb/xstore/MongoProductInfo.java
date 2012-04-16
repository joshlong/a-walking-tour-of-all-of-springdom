package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import java.util.ArrayList;
import java.util.List;

//@Document(collection = "productsInfo")
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

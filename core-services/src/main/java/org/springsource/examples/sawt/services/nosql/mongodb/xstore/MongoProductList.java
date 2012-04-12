package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import java.util.ArrayList;
import java.util.List;

public class MongoProductList {
    private List<Product > productSet = new ArrayList<Product>  ();

    public List<Product> getProductSet() {
        return productSet;
    }

    public void setProductSet(List<Product> productSet) {
        this.productSet = productSet;
    }
}

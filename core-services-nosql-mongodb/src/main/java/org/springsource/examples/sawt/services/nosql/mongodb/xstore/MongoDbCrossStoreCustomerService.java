package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * To verify:
 * jlongmbp17:code jlong$ mongo  products
 * MongoDB shell version: 2.0.1
 * connecting to: products
 * > show collections
 * org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoCustomer
 * system.indexes
 * > db.org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoCustomer
 * products.org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoCustomer
 * > db.org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoCustomer.find({})
 * { "_id" : ObjectId("4f8ba5b003640ce436ff2a5f"), "_entity_id" : NumberLong(1), "_entity_class" : "org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoCustomer", "_entity_field_name" : "mongoProductInfo", "_class" : "org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoProductInfo", "products" : [ { "price" : 104.22, "name" : "Shamwow" } ], "_entity_field_class" : "org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoProductInfo" }
 * { "_id" : ObjectId("4f8ba5eb036432ac050e94f8"), "_entity_id" : NumberLong(2), "_entity_class" : "org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoCustomer", "_entity_field_name" : "mongoProductInfo", "_class" : "org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoProductInfo", "products" : [ { "price" : 104.22, "name" : "Shamwow" } ], "_entity_field_class" : "org.springsource.examples.sawt.services.nosql.mongodb.xstore.MongoProductInfo" }
 * ...
 * >
 */
@Transactional
@Service
public class MongoDbCrossStoreCustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public MongoCustomer getCustomerById(long id) {
        return this.entityManager.find(MongoCustomer.class, id);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    private StringBuffer debug() {
        final StringBuffer mongoData = new StringBuffer();
        mongoTemplate.execute(MongoProductInfo.class.getName(), new CollectionCallback<String>() {
            public String doInCollection(DBCollection collection) throws MongoException, DataAccessException {
                for (DBObject dbo : collection.find()) {
                    mongoData.append(mongoData.length() > 1 ? ", " : "");
                    mongoData.append(dbo.toString());
                }
                return null;
            }
        });
        mongoData.append("]");

        return mongoData;
    }

    @Transactional
    public MongoCustomer createCustomer(String fn, String ln) {
        MongoCustomer newCustomer = new MongoCustomer();
        newCustomer.setFirstName(fn);
        newCustomer.setLastName(ln);

        this.entityManager.persist(newCustomer);
        this.entityManager.flush();
        this.entityManager.refresh(newCustomer);

        //  System.out.println(debug());
        return newCustomer;
    }


    public MongoCustomer updateCustomer(long id, String fn, String ln) {
        MongoCustomer customer = this.getCustomerById(id);
        customer.setFirstName(fn);
        customer.setLastName(ln);
        this.entityManager.merge(customer);
        System.out.println(debug());
        return getCustomerById(id);
    }


    public MongoCustomer addProduct(long customerId, String product, double price) {
        MongoCustomer mongoCustomer = getCustomerById(customerId);
        mongoCustomer.getMongoProductInfo().addProduct(new Product(product, price));
        this.entityManager.merge(mongoCustomer);
        System.out.println(debug());
        return mongoCustomer;
    }
}

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


@Transactional
@Service
public class MongoDbCrossStoreCustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public MongoCustomer getCustomerById(long id) {
        return this.entityManager.find(MongoCustomer.class, id);
    }

    @Autowired private MongoTemplate mongoTemplate ;

    StringBuffer debug (){
        final StringBuffer mongoData = new StringBuffer();
        mongoTemplate.execute( MongoProductInfo.class.getName(), new CollectionCallback<String>() {
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




    public MongoCustomer createCustomer(String fn, String ln) {
        MongoCustomer newCustomer = new MongoCustomer();
        newCustomer.setFirstName(fn);
        newCustomer.setLastName(ln);

        this.entityManager.persist(newCustomer);
        this.entityManager.flush();
        this.entityManager.refresh(newCustomer);

        System.out.println(debug());
        return newCustomer;
    }


    public MongoCustomer updateCustomer(long id, String fn, String ln) {
        MongoCustomer customer = this.getCustomerById(id);
        customer.setFirstName(fn);
        customer.setLastName(ln);
        this.entityManager.merge(customer);
        return getCustomerById(id);
    }


    public MongoCustomer addProduct(long customerId, String product, double price) {
        MongoCustomer mongoCustomer = getCustomerById(customerId);
        mongoCustomer.getMongoProductInfo().addProduct(new Product(product, price));
        this.entityManager.merge(mongoCustomer);
        return mongoCustomer;
    }
}

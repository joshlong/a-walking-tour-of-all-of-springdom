package org.springsource.examples.sawt.services.nosql.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

/**
 * implementation of the {@link org.springsource.examples.sawt.CustomerService} interface that
 * delegates to Redis to handle persistence.
 */
@Component
public class NoSqlRedisCustomerService implements CustomerService {

    private String uniqueIdKey = "customerId";

    private long uniqueId() {

        return this.redisTemplate.opsForValue().increment(uniqueIdKey, 1);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String lastNameKey(long id) {
        return "customer:ln:" + id;
    }

    private String firstNameKey(long id) {
        return "customer:fn:" + id;
    }

    @Override
    public Customer getCustomerById(long id) {
        String ln = this.redisTemplate.opsForValue().get(lastNameKey(id));
        String fn = this.redisTemplate.opsForValue().get(firstNameKey(id));
        return new Customer(id, fn, ln);
    }

    private void setCustomerValues(long lid, String fn, String ln) {
        this.redisTemplate.opsForValue().set(lastNameKey(lid), ln);
        this.redisTemplate.opsForValue().set(firstNameKey(lid), fn);
    }

    @Override
    public Customer updateCustomer(long id, String fn, String ln) {
        setCustomerValues(id, fn, ln);
        return getCustomerById(id);
    }

    @Override
    public Customer createCustomer(String fn, String ln) {
        long lid = uniqueId();
        setCustomerValues(lid, fn, ln);
        return getCustomerById(lid);
    }
}

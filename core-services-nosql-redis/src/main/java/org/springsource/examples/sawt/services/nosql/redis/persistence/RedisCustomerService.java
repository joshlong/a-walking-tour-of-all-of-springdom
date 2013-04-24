package org.springsource.examples.sawt.services.nosql.redis.persistence;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.nosql.redis.model.Customer;

import javax.inject.Inject;

/**
 * Implementation of the {@link org.springsource.examples.sawt.CustomerService} interface that
 * delegates to Redis to handle persistence.
 *
 * @author Josh Long
 *
 */
@Component
public class RedisCustomerService implements CustomerService {

    private String uniqueIdKey = "customerId";

    @Inject
    private RedisTemplate<String, Object> redisTemplate;

    private String lastNameKey(long id) {
        return "customer:ln:" + id;
    }

    private String firstNameKey(long id) {
        return "customer:fn:" + id;
    }

    private long uniqueId() {
        return this.redisTemplate.opsForValue().increment(uniqueIdKey, 1);
    }

    private void setCustomerValues(long lid, String fn, String ln) {
        this.redisTemplate.opsForValue().set(lastNameKey(lid), ln);
        this.redisTemplate.opsForValue().set(firstNameKey(lid), fn);
    }

    @Override
    public Customer getCustomerById(long id) {
        String ln = (String) this.redisTemplate.opsForValue().get(lastNameKey(id));
        String fn = (String) this.redisTemplate.opsForValue().get(firstNameKey(id));
        return new Customer(id, fn, ln);
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

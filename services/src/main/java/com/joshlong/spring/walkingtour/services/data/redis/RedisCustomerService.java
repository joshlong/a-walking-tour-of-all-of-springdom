package com.joshlong.spring.walkingtour.services.data.redis;

import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class RedisCustomerService implements CustomerService {


    private String uniqueIdKey = "customerId";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private BigInteger uniqueId() {
        long uniqueId = this.redisTemplate.opsForValue().increment(uniqueIdKey, 1);
        return BigInteger.valueOf(uniqueId);
    }

    private String lastNameKey(BigInteger id) {
        return "customer:ln:" + id;
    }

    private String firstNameKey(BigInteger id) {
        return "customer:fn:" + id;
    }

    @Override
    public Customer getCustomerById(BigInteger id) {
        String ln = (String) this.redisTemplate.opsForValue().get(lastNameKey(id));
        String fn = (String) this.redisTemplate.opsForValue().get(firstNameKey(id));
        return new Customer(id, fn, ln);
    }

    private void setCustomerValues(BigInteger lid, String fn, String ln) {
        this.redisTemplate.opsForValue().set(lastNameKey(lid), ln);
        this.redisTemplate.opsForValue().set(firstNameKey(lid), fn);
    }

    @Override
    public Customer updateCustomer(BigInteger id, String fn, String ln) {
        setCustomerValues(id, fn, ln);
        return getCustomerById(id);
    }

    @Override
    public Customer createCustomer(String fn, String ln) {
        BigInteger lid = uniqueId();
        setCustomerValues(lid, fn, ln);
        return getCustomerById(lid);
    }
}
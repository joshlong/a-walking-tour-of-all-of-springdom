package com.joshlong.spring.walkingtour.services.repositories.mongodb;

import com.joshlong.spring.walkingtour.services.repositories.BaseCustomerRepository;

// picked up by Spring Data
public interface CustomerRepository extends BaseCustomerRepository, MongoCustomerRepository {
}

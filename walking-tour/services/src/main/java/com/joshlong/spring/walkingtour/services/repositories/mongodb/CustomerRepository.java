package com.joshlong.spring.walkingtour.services.repositories.mongodb;

import com.joshlong.spring.walkingtour.services.repositories.BaseCustomerRepository;

public interface CustomerRepository extends BaseCustomerRepository, MongoCustomerRepository {
}

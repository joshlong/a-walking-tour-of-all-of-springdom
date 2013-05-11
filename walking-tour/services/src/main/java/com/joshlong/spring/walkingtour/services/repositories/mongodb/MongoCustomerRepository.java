package com.joshlong.spring.walkingtour.services.repositories.mongodb;


import java.io.InputStream;
import java.math.BigInteger;

import com.joshlong.spring.walkingtour.services.repositories.BaseCustomerRepository;

public interface MongoCustomerRepository  {
    void storeProfilePhoto(BigInteger customerId, InputStream bytes);

    InputStream readProfilePhoto(BigInteger customerId);
}

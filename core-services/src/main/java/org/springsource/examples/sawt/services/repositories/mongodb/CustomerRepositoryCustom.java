package org.springsource.examples.sawt.services.repositories.mongodb;


import java.io.InputStream;
import java.math.BigInteger;

/**
 * base interface to house methods that are to be implemented in a repository-specific way
 *
 * @author Josh Long
 */
public interface CustomerRepositoryCustom {
    void storeProfilePhoto(BigInteger customerId, InputStream bytes);

    InputStream readProfilePhoto(BigInteger customerId);
}

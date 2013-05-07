package com.joshlong.spring.walkingtour.services.repositories;

import com.joshlong.spring.walkingtour.services.model.Customer;
import com.joshlong.spring.walkingtour.services.repositories.jpa.CustomerRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.repository.annotation.RestResource;

import java.math.BigInteger;
import java.util.Collection;

/**
 * functionality that we expect Spring Data to implement
 *
 * @author Josh Long
 */
@RestResource(path = "customers", rel = "customers")
public interface BaseCustomerRepository extends PagingAndSortingRepository<Customer, BigInteger>, CustomerRepositoryCustom {

    Collection<Customer> findByFirstName(String firstName);

    Collection<Customer> findByFirstName(String firstName, Pageable pageable);

    Collection<Customer> findByFirstNameAndLastName(String firstName, String lastName);

    Collection<Customer> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

}

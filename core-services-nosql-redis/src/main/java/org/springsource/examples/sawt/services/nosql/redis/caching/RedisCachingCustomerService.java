package org.springsource.examples.sawt.services.nosql.redis.caching;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.nosql.redis.model.Customer;

@Service
@Transactional
public class RedisCachingCustomerService implements CustomerService {

	@PersistenceContext
	private EntityManager entityManager;

	private static final String CUSTOMERS_REGION = "customers";

	@Transactional(readOnly = true)
	@Cacheable(CUSTOMERS_REGION)
	public Customer getCustomerById(long id) {
		return this.entityManager.find(Customer.class, id);
	}

	public Customer createCustomer(String fn, String ln) {
		Customer newCustomer = new Customer();
		newCustomer.setFirstName(fn);
		newCustomer.setLastName(ln);
		this.entityManager.persist(newCustomer);
		return newCustomer;
	}

	@CacheEvict(value = CUSTOMERS_REGION, key = "#id")
	public Customer updateCustomer(long id, String fn, String ln) {
		Customer customer = this.getCustomerById(id);
		customer.setFirstName(fn);
		customer.setLastName(ln);
		this.entityManager.merge(customer);
		return getCustomerById(id);
	}
}

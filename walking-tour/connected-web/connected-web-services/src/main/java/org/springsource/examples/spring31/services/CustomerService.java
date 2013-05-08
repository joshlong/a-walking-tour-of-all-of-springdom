package org.springsource.examples.spring31.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CustomerService {

    static private final String CUSTOMERS_CACHE_REGION = "customers";

    private EntityManager entityManager;

    private UserService userService;

    @Inject
    public void setUserService(UserService us) {
        this.userService = us;
    }

    @PersistenceContext
    public void setEntityManger(EntityManager em) {
        this.entityManager = em;
    }

    public Customer createCustomer(Long userId, String firstName, String lastName, Date signupDate) {
        User user = userService.getUserById(userId);

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setSignupDate(signupDate);
        customer.setUser(user);
        entityManager.persist(customer);

        return customer;
    }

    // todo do we need this ?
    @Cacheable(CUSTOMERS_CACHE_REGION)
    public Collection<Customer> search(String name) {
        assert StringUtils.hasText(name) && name.length() > 2 : "search by a name"; // must have at least two characters in query
        String sqlName = ("%" + name + "%").toLowerCase();
        String sql = "select c.* from customers c where (LOWER( c.firstName ) LIKE :fn OR LOWER( c.lastName ) LIKE :ln)";
        return entityManager.createNativeQuery(sql, Customer.class)
                .setParameter("fn", sqlName)
                .setParameter("ln", sqlName)
                .getResultList();
    }


    @Transactional(readOnly = true)
    public List<Customer> getAllUserCustomers(Long userid) {
        return entityManager.createQuery("SELECT c  FROM " + Customer.class.getName() + " c where c.user.id = :userId")
                .setParameter("userId", userid)
                .getResultList();
    }

    @Cacheable(CUSTOMERS_CACHE_REGION)
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @CacheEvict(CUSTOMERS_CACHE_REGION)
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customer.getUser().getCustomers().remove(customer);
        entityManager.merge(customer.getUser());
        entityManager.remove(customer);
    }

    @CacheEvict(value = CUSTOMERS_CACHE_REGION, key = "#id")
    public void updateCustomer(Long id, String fn, String ln, Date birthday) {
        Customer customer = getCustomerById(id);
        customer.setLastName(ln);
        customer.setSignupDate(birthday);
        customer.setFirstName(fn);
        entityManager.merge(customer);
    }
}

package org.springsource.examples.sawt.services.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springsource.examples.sawt.services.cache.Config.CUSTOMERS_REGION;

@Component
public class JdbcCachingCustomerService implements CustomerService {

    private Log log = LogFactory.getLog(getClass());

    @Value("${jdbc.sql.customers.queryById}")
    private String customerByIdQuery;

    @Value("${jdbc.sql.customers.update}")
    private String updateCustomerQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Customer createCustomer(String fn, String ln) {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("first_name", fn);
        args.put("last_name", ln);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.setTableName("customer");
        simpleJdbcInsert.setColumnNames(new ArrayList<String>(args.keySet()));
        simpleJdbcInsert.setGeneratedKeyName("id");

        Number id = simpleJdbcInsert.executeAndReturnKey(args); // the ID of the
        // inserted
        // record.
        Customer c = getCustomerById(id.longValue());
        log.info("createCustomer:" + c.toString());
        return c;
    }

    // DefaultKeyGenerator generates the map based on the hashCode of the
    // params, by default. Here, we're telling it to use parameter 0 (the ID)
    @Cacheable(value = CUSTOMERS_REGION, key = "#p0")
    @Transactional(readOnly = true)
    public Customer getCustomerById(long id) {
        Customer c = jdbcTemplate.queryForObject(customerByIdQuery,
                customerRowMapper, id);
        log.info("getCustomerById:" + c.toString());
        return c;
    }

    // DefaultKeyGenerator generates the map based on the hashCode of the
    // params, by default. Here, we're telling it to use parameter 0 (the ID)
    @CacheEvict(value = CUSTOMERS_REGION, key = "#p0")
    @Transactional
    public Customer updateCustomer(long id, String fn, String ln) {
        this.jdbcTemplate.update(updateCustomerQuery, fn, ln, id);
        Customer c = getCustomerById(id);
        log.info("updateCustomer:" + c.toString());
        return c;
    }

    private RowMapper<Customer> customerRowMapper = new RowMapper<Customer>() {

        public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            return new Customer(id, firstName, lastName);
        }
    };
}

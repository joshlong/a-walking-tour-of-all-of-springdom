package com.joshlong.spring.walkingtour.services.jdbc;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.joshlong.spring.walkingtour.services.CustomerService;
import com.joshlong.spring.walkingtour.services.model.Customer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

@Component
@Transactional
public class JdbcCustomerService implements CustomerService {

    private String customerByIdQuery;
    private String updateCustomerQuery;
    private String selectAllQuery;
    @Inject
    private Environment environment;
    @Inject
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Customer> customerRowMapper = new RowMapper<Customer>() {

        public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
            BigInteger id = BigInteger.valueOf(resultSet.getLong("id"));
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            return new Customer(id, firstName, lastName);
        }
    };

    @PostConstruct
    public void setup() {
        this.selectAllQuery = environment.getProperty("jdbc.sql.customers.allCustomers");
        this.customerByIdQuery = environment.getProperty("jdbc.sql.customers.queryById");
        this.updateCustomerQuery = environment.getProperty("jdbc.sql.customers.update");
    }

    public Customer createCustomer(String fn, String ln) {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("first_name", fn);
        args.put("last_name", ln);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.setTableName("customer");
        simpleJdbcInsert.setColumnNames(new ArrayList<String>(args.keySet()));
        simpleJdbcInsert.setGeneratedKeyName("id");

        Number id = simpleJdbcInsert.executeAndReturnKey(args);  // the ID of the inserted record.
        Long longId = (Long) id;
        BigInteger bigInteger = BigInteger.valueOf(longId);
        return getCustomerById(bigInteger);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(BigInteger id) {
        return jdbcTemplate.queryForObject(this.customerByIdQuery, this.customerRowMapper, id.longValue());
    }

    public Collection<Customer> loadAllCustomers() {
        return this.jdbcTemplate.query(this.selectAllQuery, this.customerRowMapper);
    }

    public Customer updateCustomer(BigInteger id, String fn, String ln) {
        this.jdbcTemplate.update(updateCustomerQuery, fn, ln, id);
        return getCustomerById(id);
    }
}

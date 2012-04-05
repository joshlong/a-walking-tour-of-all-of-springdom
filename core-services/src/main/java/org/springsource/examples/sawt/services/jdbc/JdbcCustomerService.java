package org.springsource.examples.sawt.services.jdbc;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springsource.examples.sawt.CustomerService;
import org.springsource.examples.sawt.services.model.Customer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@Component
public class JdbcCustomerService implements CustomerService {

    private String customerByIdQuery;
    private String updateCustomerQuery;

    @Inject
    private Environment environment;
    @Inject
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void setup() {
        this.customerByIdQuery = environment.getProperty("jdbc.sql.customers.queryById");
        this.updateCustomerQuery = environment.getProperty("jdbc.sql.customers.update");
    }


    @Transactional
    public Customer createCustomer(String fn, String ln) {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("first_name", fn);
        args.put("last_name", ln);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.setTableName("customer");
        simpleJdbcInsert.setColumnNames(new ArrayList<String>(args.keySet()));
        simpleJdbcInsert.setGeneratedKeyName("id");

        Number id = simpleJdbcInsert.executeAndReturnKey(args);  // the ID of the inserted record.
        return getCustomerById(id.longValue());
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(long id) {
        return jdbcTemplate.queryForObject(customerByIdQuery, customerRowMapper, id);
    }

    @Transactional
    public Customer updateCustomer(long id, String fn, String ln) {
        this.jdbcTemplate.update(updateCustomerQuery, fn, ln, id);
        return getCustomerById(id);
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

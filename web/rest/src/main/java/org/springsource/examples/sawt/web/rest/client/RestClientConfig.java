package org.springsource.examples.sawt.web.rest.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springsource.examples.sawt.services.model.Customer;

import javax.sql.DataSource;


@Configuration
public class RestClientConfig {


    void foo () throws Throwable {
        DataSource ds = null ;
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager( ds);
        final JdbcTemplate jdbcTemplate  = new JdbcTemplate(ds);
        TransactionTemplate transactionTemplate = new TransactionTemplate(  platformTransactionManager ) ;
        Customer result = transactionTemplate.execute(new TransactionCallback<Customer>() {
            @Override
            public Customer doInTransaction(TransactionStatus status) {
                RowMapper <Customer> rowMapper = null ;
                return  jdbcTemplate.queryForObject( "select * from CUSTOMERS" ,   rowMapper) ;
            }
        }) ;

    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

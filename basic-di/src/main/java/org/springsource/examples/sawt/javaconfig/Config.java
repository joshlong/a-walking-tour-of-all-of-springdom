package org.springsource.examples.sawt.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@Configuration
public class Config {
    @Bean
    public CustomerService customerService() {
        CustomerService customerService = new CustomerService();
        customerService.setDataSource(this.dataSource());
        return customerService;
    }

    @Bean
    public DataSource dataSource() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setUsername("sa");
        singleConnectionDataSource.setPassword("");
        singleConnectionDataSource.setUrl("jdbc:h2:~/test");
        singleConnectionDataSource.setDriverClassName(org.h2.Driver.class
                .getName());
        return singleConnectionDataSource;
    }
}

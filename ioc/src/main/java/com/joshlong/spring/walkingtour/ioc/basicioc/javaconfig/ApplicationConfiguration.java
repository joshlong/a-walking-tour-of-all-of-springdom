package com.joshlong.spring.walkingtour.ioc.basicioc.javaconfig;

import org.h2.Driver;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * simple Java configuration class
 */
@Configuration
@PropertySource("classpath:/config.properties")
public class ApplicationConfiguration {

    @Bean
    public DataSource dataSource(Environment environment) {
        Driver jdbcDriver = new Driver();
        return new SimpleDriverDataSource(
                jdbcDriver, environment.getProperty("ds.url"));

    }

    @Bean
    public CustomerService customerService(DataSource dataSource) {
        CustomerService customerService = new CustomerService();
        customerService.setDataSource(dataSource);
        return customerService;
    }
}

package org.springsource.sawt.ioc.basicioc.javaconfig;

import org.h2.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * simple Java configuration class
 */
@Configuration
@PropertySource("classpath:/config.properties")
public class Config {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        Driver d = new Driver();
        return new SimpleDriverDataSource(d, environment.getProperty("ds.url"));
    }

    @Bean
    public CustomerService customerService() {
        CustomerService cs = new CustomerService();
        cs.setDataSource(dataSource());
        return cs;
    }
}

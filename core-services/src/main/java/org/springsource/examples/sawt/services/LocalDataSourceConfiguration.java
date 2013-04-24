package org.springsource.examples.sawt.services;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * @author Josh Long
 */
@PropertySource("classpath:/services.properties")
@Configuration
@Profile("default")
public class LocalDataSourceConfiguration {
    @Bean
    public DataSource dataSource(Environment environment) throws Exception {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setPassword(environment.getProperty("dataSource.password"));
        dataSource.setUrl(environment.getProperty("dataSource.url"));
        dataSource.setUsername(environment.getProperty("dataSource.user"));
        dataSource.setDriverClassName(environment.getPropertyAsClass("dataSource.driverClass", Driver.class).getName());
        return dataSource;
    }
}

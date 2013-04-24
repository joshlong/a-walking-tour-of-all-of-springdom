package org.springsource.examples.sawt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class CoreCrmConfig {

    @Bean
    public DataSource dataSource(
            @Value("${dataSource.driverClassName}") String driverName,
            @Value("${dataSource.url}") String url,
            @Value("${dataSource.user}") String user,
            @Value("${dataSource.password}") String password) {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setPassword(password);
        simpleDriverDataSource.setUrl(url);
        simpleDriverDataSource.setUsername(user);
        simpleDriverDataSource.setDriverClass(org.h2.Driver.class);
        return simpleDriverDataSource;
    }


}

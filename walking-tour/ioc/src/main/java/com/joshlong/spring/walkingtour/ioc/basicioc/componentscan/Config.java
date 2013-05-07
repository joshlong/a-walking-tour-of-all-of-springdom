package com.joshlong.spring.walkingtour.ioc.basicioc.componentscan;

import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.inject.Inject;
import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:/config.properties")
public class Config {

    @Inject
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        Driver d = new Driver();
        return new SimpleDriverDataSource(d, environment.getProperty("ds.url"));
    }
}

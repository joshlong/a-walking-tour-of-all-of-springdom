package com.joshlong.spring.walkingtour.ioc.strangebeans.profiles;

import org.h2.Driver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("local")
public class LocalDataSource implements DataSourceProvider, InitializingBean {
    @Override
    public DataSource dataSource() {
    	System.out.println( "resolving " + getClass());
        Driver d = new Driver();
        return new SimpleDriverDataSource(d, "jdbc:h2:tcp://localhost/~/local_crm");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("starting " + getClass().getName());
    }
}
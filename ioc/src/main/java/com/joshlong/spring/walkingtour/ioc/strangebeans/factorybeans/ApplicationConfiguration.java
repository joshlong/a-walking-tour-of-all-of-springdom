package com.joshlong.spring.walkingtour.ioc.strangebeans.factorybeans;

import org.h2.Driver;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.joshlong.spring.walkingtour.ioc.strangebeans.factorybeans.entities.Customer;

import javax.sql.DataSource;

/**
 * Simple config demonstrating some {@link org.springframework.beans.factory.FactoryBean}s in action.
 */
@Configuration
@PropertySource("classpath:/config.properties")
@EnableTransactionManagement    /// nb: we're transparently enabling transaction managment
public class ApplicationConfiguration {

    private String entityPackage = Customer.class.getPackage().getName();

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource, SessionFactory sessionFactory) throws Throwable {
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setDataSource(dataSource);
        manager.setSessionFactory(sessionFactory);
        return manager;
    }

    @Bean
    public DataSource dataSource(Environment environment) {
        Driver driver = new Driver();
        return new SimpleDriverDataSource(driver, environment.getProperty("ds.url"));
    }

    @Bean
    public SessionFactory hibernate3SessionFactory(DataSource dataSource) throws Throwable {
        return new LocalSessionFactoryBuilder(dataSource)
                .scanPackages(this.entityPackage)
                .buildSessionFactory();
    }
}

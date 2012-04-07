package org.springsource.examples.spring31.services;

import static java.lang.Class.forName;

import java.sql.Driver;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("/config.properties")
@EnableCaching
@EnableTransactionManagement
public class ServicesConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager scm = new SimpleCacheManager();
        Cache cache = new ConcurrentMapCache("customers");
        scm.setCaches(Arrays.asList(cache));
        return scm;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new HibernateTransactionManager(this.sessionFactory());
    }

    @Bean
    public DataSource dataSource() throws Exception {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();

        Class<? extends Driver> d = (Class<? extends Driver>) forName(environment.getProperty("ds.driverClass"));

        String user = environment.getProperty("ds.user"),
                pw = environment.getProperty("ds.password"),
                url = environment.getProperty("ds.url");

        simpleDriverDataSource.setDriverClass(d);
        simpleDriverDataSource.setUsername(user);
        simpleDriverDataSource.setPassword(pw);
        simpleDriverDataSource.setUrl(url);
        return simpleDriverDataSource;
    }

    @Bean
    public SessionFactory sessionFactory() throws Exception {
        Properties props = new Properties();
        props.setProperty(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        props.setProperty(org.hibernate.cfg.Environment.DIALECT, H2Dialect.class.getName());
        props.setProperty(org.hibernate.cfg.Environment.SHOW_SQL,"true");

        return new LocalSessionFactoryBuilder(this.dataSource())
                .addAnnotatedClasses(Customer.class)
                .addProperties(props)
                .buildSessionFactory();
    }
}

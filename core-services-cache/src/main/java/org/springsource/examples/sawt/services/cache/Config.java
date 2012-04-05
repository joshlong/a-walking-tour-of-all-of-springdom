package org.springsource.examples.sawt.services.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class Config {

    public static final String CUSTOMERS_REGION = "customers";

    @Value("${dataSource.url}")
    private String url;

    @Value("${dataSource.user}")
    private String user;

    @Value("${dataSource.password}")
    private String password;

    @Value("${dataSource.driverClassName}")
    private Class<Driver> driverClassName;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(this.dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setPassword(this.password);
        simpleDriverDataSource.setUrl(this.url);
        simpleDriverDataSource.setUsername(this.user);
        simpleDriverDataSource.setDriverClass(driverClassName);
        return simpleDriverDataSource;
    }

    @Bean
    public ConcurrentMapCacheFactoryBean customersCacheFactoryBean() {
        ConcurrentMapCacheFactoryBean concurrentMapCacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        concurrentMapCacheFactoryBean.setStore(new ConcurrentHashMap());
        concurrentMapCacheFactoryBean.setName(Config.CUSTOMERS_REGION);
        return concurrentMapCacheFactoryBean;
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.<Cache>asList(customersCacheFactoryBean().getObject()));
        return simpleCacheManager;
    }

}

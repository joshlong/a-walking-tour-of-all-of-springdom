package com.joshlong.spring.walkingtour.services;

import com.mongodb.Mongo;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

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

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public MongoDbFactory mongoDbFactory1(Environment environment) throws Exception {
        String dbName = environment.getProperty("mongo.db");
        String host = environment.getProperty("mongo.host");
        Mongo mongo = new Mongo(host);
        return new SimpleMongoDbFactory(mongo, dbName);
    }
}

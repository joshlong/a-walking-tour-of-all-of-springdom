package com.joshlong.spring.walkingtour.services.data.mongodb;

import com.joshlong.spring.walkingtour.services.*;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * @author Josh Long
 */
@Configuration
@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
public class MongoConfiguration {
    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory, MongoTemplate mongoTemplate) throws Exception {
        return new GridFsTemplate(mongoDbFactory, mongoTemplate.getConverter());
    }
}

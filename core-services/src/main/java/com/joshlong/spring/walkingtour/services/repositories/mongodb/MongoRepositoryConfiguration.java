package com.joshlong.spring.walkingtour.services.repositories.mongodb;

import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.joshlong.spring.walkingtour.services.*;

@Configuration
@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
@EnableMongoRepositories
public class MongoRepositoryConfiguration {
    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory, MongoTemplate mongoTemplate) throws Exception {
        return new GridFsTemplate(mongoDbFactory, mongoTemplate.getConverter());
    }
}

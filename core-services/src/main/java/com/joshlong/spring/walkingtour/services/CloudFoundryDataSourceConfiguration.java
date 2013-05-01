package com.joshlong.spring.walkingtour.services;

import org.cloudfoundry.runtime.env.*;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;
import java.util.*;

/**
 * Simple version of the configuration that supports Cloud Foundry runtime
 *
 * @author Josh Long
 */
@Configuration
@Profile("cloud")
public class CloudFoundryDataSourceConfiguration {

    private CloudEnvironment cloudEnvironment = new CloudEnvironment();

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        List<MongoServiceInfo> serviceInfoList = cloudEnvironment.getServiceInfos(MongoServiceInfo.class);
        MongoServiceInfo mongoServiceInfo = serviceInfoList.iterator().next();
        return new MongoServiceCreator().createService(mongoServiceInfo);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        List<RedisServiceInfo> serviceInfoList = this.cloudEnvironment.getServiceInfos(RedisServiceInfo.class);
        RedisServiceInfo redisServiceInfo = serviceInfoList.iterator().next();
        return new RedisServiceCreator().createService(redisServiceInfo);
    }

    @Bean
    public DataSource dataSource() throws Exception {
        Collection<RdbmsServiceInfo> databases = cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class);
        RdbmsServiceInfo rdbmsServiceInfo = databases.iterator().next();
        RdbmsServiceCreator rdbmsServiceCreator = new RdbmsServiceCreator();
        return rdbmsServiceCreator.createService(rdbmsServiceInfo);
    }
}



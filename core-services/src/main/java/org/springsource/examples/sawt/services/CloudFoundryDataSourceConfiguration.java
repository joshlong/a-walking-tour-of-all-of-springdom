package org.springsource.examples.sawt.services;

import org.cloudfoundry.runtime.env.*;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;

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
    public DataSource dataSource() throws Exception {
        Collection<RdbmsServiceInfo> databases = cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class);
        RdbmsServiceInfo rdbmsServiceInfo = databases.iterator().next();
        RdbmsServiceCreator rdbmsServiceCreator = new RdbmsServiceCreator();
        return rdbmsServiceCreator.createService(rdbmsServiceInfo);
    }
}



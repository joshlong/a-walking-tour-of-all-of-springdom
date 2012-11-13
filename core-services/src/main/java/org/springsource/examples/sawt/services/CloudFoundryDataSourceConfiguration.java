package org.springsource.examples.sawt.services;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.RdbmsServiceInfo;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * Simple version of the configuration that supports Cloud Foundry runtime
 *
 * @author Josh Long
 */
@Configuration
@Profile("cloud")
public class CloudFoundryDataSourceConfiguration implements DataSourceConfiguration {

    @Bean
    public DataSource dataSource() throws Exception {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        Collection<RdbmsServiceInfo> databases = cloudEnvironment.getServiceInfos(RdbmsServiceInfo.class);
        RdbmsServiceInfo rdbmsServiceInfo = databases.iterator().next();
        assert rdbmsServiceInfo != null : "the database instance must be provisioned. Use 'vmc create-service' and 'vmc bind-service'";
        RdbmsServiceCreator rdbmsServiceCreator = new RdbmsServiceCreator();
        return rdbmsServiceCreator.createService(rdbmsServiceInfo);
    }
}



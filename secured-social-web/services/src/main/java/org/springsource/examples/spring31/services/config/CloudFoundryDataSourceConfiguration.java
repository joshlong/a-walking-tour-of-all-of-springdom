package org.springsource.examples.spring31.services.config;

import com.mongodb.WriteConcern;
import org.cloudfoundry.runtime.env.*;
import org.cloudfoundry.runtime.service.AbstractServiceCreator;
import org.cloudfoundry.runtime.service.document.CloudMongoConfiguration;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.cloudfoundry.runtime.service.relational.RdbmsServiceCreator;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.PostgreSQLDialect;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring {@link Configuration configuration } class that implements {@link DataSourceConfiguration the data source configuration contract for our application}
 * so that we can inject it, and - through the magic of Spring profiles - defer to the right implementation for the right environment at runtime.
 * <p/>
 * This implementation works in any  <a href  ="http://www.cloudfoundry.com">Cloud Foundry</a> environment, and requires
 * that there be an RDBMS bound (I've tested the code against PostgreSQL, though MySQL should work just fine), and a Redis instance bound.
 * The names of the services bound are unimportant, in this case, so long as there is only one instance bound.
 *
 * @author Josh Long
 */
@Configuration
@Profile("cloud")
public class CloudFoundryDataSourceConfiguration implements DataSourceConfiguration {

    public static final String DEFAULT_REDIS_SERVICE_NAME = "cloudfoundryCrmRedis";

    public static final String DEFAULT_POSTGRESQL_SERVICE_NAME = "cloudfoundryCrmPostgreSql";

    public static final String DEFAULT_MONGODB_SERVICE_NAME = "cloudfoundryCrmMongoDd";

    public static final String DEFAULT_RABBITMQ_SERVICE_NAME = "cloudfoundryCrmRabbitMq";

    // thread safe and long lived
    private CloudEnvironment cloudEnvironment = new CloudEnvironment();

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws Exception {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> rt) throws Exception {
        return new RedisCacheManager(rt);
    }

    @Bean
    public DataSource dataSource() throws Exception {
        return lookupCloudFoundryService(DEFAULT_POSTGRESQL_SERVICE_NAME, RdbmsServiceInfo.class, RdbmsServiceCreator.class);
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return lookupCloudFoundryService(DEFAULT_MONGODB_SERVICE_NAME, MongoServiceInfo.class, MongoServiceCreator.class, new ServiceCreatorConfigurationCallback<MongoServiceInfo, MongoServiceCreator, MongoDbFactory>() {
            @Override
            public void configureServiceCreator(MongoServiceCreator t) throws Throwable {
                CloudMongoConfiguration cloudMongoConfiguration = new CloudMongoConfiguration();
                cloudMongoConfiguration.setWriteConcern(WriteConcern.FSYNC_SAFE.getWString());
                t.setCloudMongoConfiguration(cloudMongoConfiguration);
            }
        });
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() throws Exception {
        return lookupCloudFoundryService(DEFAULT_REDIS_SERVICE_NAME, RedisServiceInfo.class, RedisServiceCreator.class);
    }


    public Map<String, String> contributeJpaEntityManagerProperties() {
        Map<String, String> propertiesToAddToJpaEntityManager = new HashMap<String, String>();
        propertiesToAddToJpaEntityManager.put(Environment.HBM2DDL_AUTO, "create");
        propertiesToAddToJpaEntityManager.put(Environment.HBM2DDL_IMPORT_FILES, "import_psql.sql");
        propertiesToAddToJpaEntityManager.put(Environment.DIALECT, PostgreSQLDialect.class.getName());
        propertiesToAddToJpaEntityManager.put(Environment.SHOW_SQL, "true");
        return propertiesToAddToJpaEntityManager;
    }


    private <SI extends AbstractServiceInfo, SC extends AbstractServiceCreator<DS, SI>, DS> DS
    lookupCloudFoundryService(
            String name,
            Class<SI> serviceInfoClass,
            Class<SC> dataSourceClass) {
        return this.lookupCloudFoundryService(name, serviceInfoClass, dataSourceClass, null);
    }

    /**
     * Looks up a service bound to the Cloud Foundry environment through the use of the <code>vmc bind-service</code> or
     * <code>vmc push</code> command line tools.
     * <p/>
     * It looks up a service by a {@link String } name and {@link SI service info type}. If no name is specified, it looks up
     * all services bound by {@link SI service info type}. It returns the
     * first instance retrieved, assuming there are 0 or more instances bound.
     * <p/>
     * If both of these fail, it will fail.
     * <p/>
     * Finally, it creates an instance of the {@link DS service type required} and returns it.
     *
     * @param name             the name to lookup the service by (optional)
     * @param serviceInfoClass the {@link AbstractServiceInfo service info} class
     * @param dataSourceClass  the service requested
     * @param <SI>             the service info class
     * @param <SC>             the service creator class
     * @param <DS>             the class of the final, returned service requested
     * @return the service instance
     */
    private <SI extends AbstractServiceInfo, SC extends AbstractServiceCreator<DS, SI>, DS> DS lookupCloudFoundryService(String name, Class<SI> serviceInfoClass, Class<SC> dataSourceClass, ServiceCreatorConfigurationCallback<SI, SC, DS> scCallback) {
        SI serviceInfo = null;
        if (StringUtils.hasText(name)) {
            SI abstractServiceInfo = cloudEnvironment.getServiceInfo(name, serviceInfoClass);
            if (null != abstractServiceInfo) {
                serviceInfo = abstractServiceInfo;
            } else {
                Collection<SI> tCollection = cloudEnvironment.getServiceInfos(serviceInfoClass);
                if (tCollection.size() > 0) {
                    serviceInfo = tCollection.iterator().next();
                }
            }
        }
        if (null == serviceInfo) {
            throw new RuntimeException("couldn't find a CloudFoundry service bound of type " +
                    serviceInfoClass.getName() + (StringUtils.hasText(name) ? " or of name '" + name + "'" : ""));
        }

        try {
            SC serviceCreator = dataSourceClass.newInstance();
            if (null != scCallback) {
                scCallback.configureServiceCreator(serviceCreator);
            }
            return serviceCreator.createService(serviceInfo);
        } catch (Throwable e) {
            throw new RuntimeException("couldn't instantiate the instance of " + dataSourceClass.getName());
        }
    }

    /**
     * Useful to handle the case where we want to tailor the ServiceCreator such as when setting the {@link WriteConcern} for the
     * MongoDB {@link MongoServiceCreator}
     *
     * @param <SI>
     * @param <SC>
     * @param <DS>
     */
    public static interface ServiceCreatorConfigurationCallback<SI extends AbstractServiceInfo, SC extends AbstractServiceCreator<DS, SI>, DS> {
        void configureServiceCreator(SC sc) throws Throwable;
    }
}


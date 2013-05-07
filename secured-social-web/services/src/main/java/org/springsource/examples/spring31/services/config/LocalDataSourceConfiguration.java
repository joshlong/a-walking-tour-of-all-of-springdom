package org.springsource.examples.spring31.services.config;


import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.dialect.PostgreSQLDialect;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Configuration class } that builds up local resources, consulting a properties assumed to be
 * in the {@link org.springframework.core.env.PropertySource property sources chain}:
 * <p/>
 * <p/>
 * <OL>
 * <LI>
 * <code>ds.driverClass</code> the driver class for the RDBMS database.
 * </LI>
 * <LI>
 * <code>ds.url</code> the JDBC connection URL for the RDBMS database
 * </LI>
 * <LI>
 * <code>ds.password</code> the password for the RDBMS database
 * </LI>
 * <LI>
 * <code>ds.user</code> the user name for the RDBMS database
 * </LI>
 * </OL>
 *
 * @author Josh Long
 */
@Configuration
@Profile("default")
public class LocalDataSourceConfiguration implements DataSourceConfiguration {
    private boolean resetDatabaseOnReset = false;
/*


    @Bean
    public ConnectionFactory rabbitMqConnectionFactory(Environment environment) throws Exception {
        String host = environment.getProperty("rabbit.host");
        int port = Integer.parseInt(environment.getProperty("rabbit.port"));
        CachingConnectionFactory cachingConnectionFactory;
        if (port == 0) {
            cachingConnectionFactory = new CachingConnectionFactory(host);
        } else {
            cachingConnectionFactory = new CachingConnectionFactory(host, port);
        }
        return cachingConnectionFactory;
    }
*/

    @Bean(destroyMethod = "close")
    public DataSource dataSource(Environment environment) throws Exception {
        String user = environment.getProperty("dataSource.user"),
                pw = environment.getProperty("dataSource.password"),
                host = environment.getProperty("dataSource.host");
        int port = Integer.parseInt(environment.getProperty("dataSource.port"));
        String db = environment.getProperty("dataSource.db");
        Class<Driver> driverClass = environment.getPropertyAsClass("dataSource.driverClassName", Driver.class);
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(driverClass.getName());
        basicDataSource.setPassword(pw);
        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(user);
        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxActive(10);

        return basicDataSource;
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        SimpleCacheManager scm = new SimpleCacheManager();
        Collection<Cache> caches = new ArrayList<Cache>();
        for (String cacheName : "customers,users".split(",")) {
            Cache cache = new ConcurrentMapCache(cacheName);
            caches.add(cache);
        }
        scm.setCaches(caches);
        return scm;
    }

    public Map<String, String> contributeJpaEntityManagerProperties() {
        Map<String, String> p = new HashMap<String, String>();
        p.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, resetDatabaseOnReset ? "create" : "create-update");
        p.put(org.hibernate.cfg.Environment.DIALECT, PostgreSQLDialect.class.getName());
        p.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");
        if (this.resetDatabaseOnReset) {
            p.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "import_psql.sql");
        }
        return p;
    }

    @Bean
    public MongoDbFactory mongoDbFactory(Environment environment) throws Exception {
        String dbName = environment.getProperty("mongo.fsbucket");
        String host = environment.getProperty("mongo.host");
        Mongo mongo = new Mongo(host);
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongo, dbName);
        simpleMongoDbFactory.setWriteConcern(WriteConcern.FSYNC_SAFE);
        return simpleMongoDbFactory;
    }


}

package org.springsource.examples.sawt.services.nosql.mongodb.xstore;

import com.mongodb.Mongo;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoExceptionTranslator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.crossstore.MongoChangeSetPersister;
import org.springframework.data.mongodb.crossstore.MongoDocumentBacking;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Driver;


@Configuration
@PropertySource("/services.properties")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class MongoDbCrossStoreConfiguration {

    @Inject
    private Environment environment;

    @Bean
    public MongoDbFactory mongoDbFactory() throws Throwable {
        Mongo mongo = new Mongo();
        return new SimpleMongoDbFactory(mongo, "products");
    }

    @Bean
    public MongoDocumentBacking mongoDocumentBacking() throws Throwable {
        MongoDocumentBacking mdb = MongoDocumentBacking.aspectOf();
        mdb.setChangeSetPersister(changeSetPersister());
        return mdb;
    }


    @Bean
    public MongoExceptionTranslator mongoExceptionTranslator() {
        return new MongoExceptionTranslator();
    }

    @Bean
    public MongoChangeSetPersister changeSetPersister() throws Throwable {
        MongoChangeSetPersister mongoChangeSetPersister = new MongoChangeSetPersister();
        mongoChangeSetPersister.setEntityManagerFactory(entityManagerFactory().getObject());
        mongoChangeSetPersister.setMongoTemplate(mongoTemplate());
        return mongoChangeSetPersister;
    }

    @Bean
    public  static PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
      public static PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        EntityManagerFactory entityManagerFactory = entityManagerFactory().getObject();
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public DataSource dataSource() throws Exception {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setPassword(environment.getProperty("dataSource.password"));
        dataSource.setUrl(environment.getProperty("dataSource.xstoreUrl"));
        dataSource.setUsername(environment.getProperty("dataSource.user"));
        dataSource.setDriverClass(environment.getPropertyAsClass("dataSource.driverClass", Driver.class));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan(MongoCustomer.class.getPackage().getName());
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());

        // look ma, no persistence.xml !
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Throwable {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.H2);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        return hibernateJpaVendorAdapter;
    }
}

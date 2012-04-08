package org.springsource.sawt.ioc.strangebeans.profiles;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springsource.sawt.ioc.strangebeans.factorybeans.entities.Customer;

@Configuration
@Import({CloudDataSource.class, LocalDataSource.class, ProductionDataSource.class})
public class Config {

    @Autowired
    private DataSourceProvider dataSourceProvider;

    private String entityPackage = Customer.class.getPackage().getName();

    @Bean
    public SessionFactory hibernate3SessionFactory() throws Throwable {
        return new AnnotationSessionFactoryBuilder()
                .setDataSource(dataSourceProvider.dataSource())
                .setAnnotatedPackages(entityPackage)
                .buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Throwable {
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setDataSource(dataSourceProvider.dataSource());
        manager.setSessionFactory(hibernate3SessionFactory());
        return manager;
    }
}

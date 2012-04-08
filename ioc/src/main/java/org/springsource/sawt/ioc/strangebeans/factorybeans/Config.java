package org.springsource.sawt.ioc.strangebeans.factorybeans;

import org.h2.Driver;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springsource.sawt.ioc.strangebeans.factorybeans.entities.Customer;

import javax.sql.DataSource;

/**
 * Simple config demonstrating some {@link org.springframework.beans.factory.FactoryBean}s in action.
 *
 */
@Configuration
@PropertySource("classpath:/config.properties")
@EnableTransactionManagement    /// nb: we're transparently enabling transaction managment
public class Config {

    @Autowired
    private Environment environment;
    private String entityPackage = Customer.class.getPackage().getName();

    @Bean
    public PlatformTransactionManager transactionManager() throws Throwable {
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setDataSource(dataSource());
        manager.setSessionFactory(hibernate3SessionFactory());
        return manager;
    }

    @Bean
    public DataSource dataSource() {
        Driver d = new Driver();
        return new SimpleDriverDataSource(d, environment.getProperty("ds.url"));
    }

    /// @Bean WE DONT WANT THIS ONE
    AnnotationSessionFactoryBean hibernate3SessionFactoryFactoryBean() {
        AnnotationSessionFactoryBean annotationSessionFactoryBean = new AnnotationSessionFactoryBean();
        annotationSessionFactoryBean.setAnnotatedPackages(new String[] {entityPackage});
        annotationSessionFactoryBean.setDataSource(dataSource());
        return annotationSessionFactoryBean;
    }

    // ahh.... much better!
    @Bean
    public SessionFactory hibernate3SessionFactory() throws Throwable {
        return new LocalSessionFactoryBuilder(this.dataSource())
                .scanPackages( this.entityPackage )
                .buildSessionFactory();
    }
}

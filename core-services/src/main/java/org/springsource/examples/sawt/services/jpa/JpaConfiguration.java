package org.springsource.examples.sawt.services.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springsource.examples.sawt.services.CloudFoundryDataSourceConfiguration;
import org.springsource.examples.sawt.services.DataSourceConfiguration;
import org.springsource.examples.sawt.services.LocalDataSourceConfiguration;
import org.springsource.examples.sawt.services.model.Customer;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;


@Configuration
@PropertySource("classpath:/services.properties")
@EnableTransactionManagement
@Import( { LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class })
public class JpaConfiguration {

    @Inject private DataSourceConfiguration dataSourceConfiguration;

    @Inject private Environment environment;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSourceConfiguration.dataSource() );
        localContainerEntityManagerFactoryBean.setPackagesToScan(Customer.class.getPackage().getName());

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);

        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        // look ma, no persistence.xml !
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        EntityManagerFactory entityManagerFactory = entityManagerFactory().getObject();
        return new JpaTransactionManager(entityManagerFactory);
    }

}

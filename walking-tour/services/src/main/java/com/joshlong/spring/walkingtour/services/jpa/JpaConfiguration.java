package com.joshlong.spring.walkingtour.services.jpa;

import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.joshlong.spring.walkingtour.services.CloudFoundryDataSourceConfiguration;
import com.joshlong.spring.walkingtour.services.LocalDataSourceConfiguration;
import com.joshlong.spring.walkingtour.services.model.Customer;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
@ComponentScan
public class JpaConfiguration {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws Exception {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(Customer.class.getPackage().getName());
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(javax.persistence.EntityManagerFactory entityManagerFactory) throws Exception {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

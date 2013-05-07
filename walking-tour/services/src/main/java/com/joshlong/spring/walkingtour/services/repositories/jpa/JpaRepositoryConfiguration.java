package com.joshlong.spring.walkingtour.services.repositories.jpa;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.joshlong.spring.walkingtour.services.*;
import com.joshlong.spring.walkingtour.services.model.Customer;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@EnableScheduling
@EnableTransactionManagement
@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
@EnableJpaRepositories
public class JpaRepositoryConfiguration {
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

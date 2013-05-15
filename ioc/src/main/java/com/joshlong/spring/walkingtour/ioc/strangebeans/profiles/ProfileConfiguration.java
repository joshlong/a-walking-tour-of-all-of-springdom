package com.joshlong.spring.walkingtour.ioc.strangebeans.profiles;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import com.joshlong.spring.walkingtour.ioc.strangebeans.factorybeans.entities.Customer;

@Configuration
@Import({ CloudDataSource.class, LocalDataSource.class, ProductionDataSource.class })
public class ProfileConfiguration {

	private String entityPackage = Customer.class.getPackage().getName();

	@Bean
	public SessionFactory hibernate3SessionFactory(DataSource dataSource)
			throws Throwable {
		return new LocalSessionFactoryBuilder(dataSource).scanPackages(
				this.entityPackage).buildSessionFactory();
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			SessionFactory sf,
			DataSource dataSource) throws Throwable {
		HibernateTransactionManager manager = new HibernateTransactionManager();
		manager.setDataSource(dataSource);
		manager.setSessionFactory(sf);
		return manager;
	}
}

package org.springsource.examples.sawt.services.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Driver;


@PropertySource("classpath:/services.properties")
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackageClasses = Config.class, excludeFilters = {@ComponentScan.Filter(Configuration.class)})
public class Config {

    @Inject
    private Environment environment;

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        return new JdbcTemplate(this.dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @SuppressWarnings("unchecked")
    public DataSource dataSource() throws Exception {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setPassword(environment.getProperty("dataSource.password"));
        dataSource.setUrl(environment.getProperty("dataSource.url"));
        dataSource.setUsername(environment.getProperty("dataSource.user"));
        dataSource.setDriverClass((Class<Driver>) Class.forName(environment.getProperty("dataSource.driverClass")));
        return dataSource;
    }

}

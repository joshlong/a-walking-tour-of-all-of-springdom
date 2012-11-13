package org.springsource.examples.sawt.services.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springsource.examples.sawt.services.CloudFoundryDataSourceConfiguration;
import org.springsource.examples.sawt.services.DataSourceConfiguration;
import org.springsource.examples.sawt.services.LocalDataSourceConfiguration;

import javax.inject.Inject;


@PropertySource("classpath:/services.properties")
@EnableTransactionManagement
@Configuration
@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})

public class JdbcConfiguration {

    @Inject
    private DataSourceConfiguration dataSourceConfiguration;

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        return new JdbcTemplate(this.dataSourceConfiguration.dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(this.dataSourceConfiguration.dataSource());
    }


}

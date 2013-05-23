package com.joshlong.spring.walkingtour.services.jdbc;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.joshlong.spring.walkingtour.services.CloudFoundryDataSourceConfiguration;
import com.joshlong.spring.walkingtour.services.LocalDataSourceConfiguration;

import javax.sql.DataSource;


@PropertySource("classpath:/services.properties")
@EnableTransactionManagement
@Configuration
@ComponentScan
@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
public class JdbcConfiguration {


    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws Exception {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }


}

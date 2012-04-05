package org.springsource.examples.sawt.services.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springsource.examples.sawt.services.model.Customer;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * configuration for our batch solution to read data and load it into the CRM.
 *
 * @author Josh Long
 */
@Configuration("batchConfiguration")
public class Config {

    private Log log = LogFactory.getLog(getClass());

    @Value("${jdbc.sql.customers.insert}")
    private String insertCustomersSql;

    @Value("${dataSource.url}")
    private String url;

    @Value("${dataSource.user}")
    private String user;

    @Value("${dataSource.password}")
    private String password;

    @Value("${dataSource.driverClass}")
    private Class<? extends Driver> driverClassName;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setPassword(password);
        simpleDriverDataSource.setUrl(url);
        simpleDriverDataSource.setUsername(user);
        simpleDriverDataSource.setDriverClass(driverClassName);
        return simpleDriverDataSource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean   // sets up infrastructure and scope
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(this.mapJobRegistry());
        return jobRegistryBeanPostProcessor;
    }

    @Bean(name = "jobRepository")
    public JobRepositoryFactoryBean jobRepository() throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(this.dataSource());
        jobRepositoryFactoryBean.setTransactionManager(this.platformTransactionManager());
        return jobRepositoryFactoryBean;
    }

    @Bean
    public MapJobRegistry mapJobRegistry() throws Exception {
        return new MapJobRegistry();
    }

    @Bean
    public SimpleJobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository((JobRepository) this.jobRepository().getObject());
        return simpleJobLauncher;
    }

    @Bean
    @Scope("step")
    public FlatFileItemReader reader(@Value("#{jobParameters['input.file']}") Resource resource) throws Exception {

        log.debug(String.format("building FlatFileItemReader to read in the file %s", resource.getFile().getAbsolutePath()));

        FlatFileItemReader<Customer> csvFileReader = new FlatFileItemReader<Customer>();
        csvFileReader.setResource(resource);

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_COMMA);
        delimitedLineTokenizer.setNames(new String[]{"lastName", "firstName"});

        BeanWrapperFieldSetMapper<Customer> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Customer>();
        beanWrapperFieldSetMapper.setTargetType(Customer.class);

        DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<Customer>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        csvFileReader.setLineMapper(defaultLineMapper);
        return csvFileReader;
    }


    @Bean
    public ItemProcessor<Customer, Customer> processor() {
        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                log.info(String.format("processing the customer %s", item.toString()));
                return item;
            }
        };
    }


    @Bean  // thread safe and stateless, no need to make it step-scoped.
    public JdbcBatchItemWriter writer() {
        JdbcBatchItemWriter<Customer> jdbcBatchItemWriter = new JdbcBatchItemWriter<Customer>();
        jdbcBatchItemWriter.setAssertUpdates(true);
        jdbcBatchItemWriter.setDataSource(this.dataSource());
        jdbcBatchItemWriter.setSql(this.insertCustomersSql);
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        return jdbcBatchItemWriter;
    }

}

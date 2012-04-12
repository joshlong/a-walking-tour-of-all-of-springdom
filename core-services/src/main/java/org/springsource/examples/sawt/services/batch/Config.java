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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springsource.examples.sawt.services.model.Customer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Driver;

/**
 * configuration for our batch solution to read data and load it into the CRM.
 *
 * @author Josh Long
 */
@Configuration
@PropertySource("/services.properties")
@ImportResource("/org/springsource/examples/sawt/services/batch/context.xml")
public class Config {

    @Autowired
    private Environment environment;

    private Log log = LogFactory.getLog(getClass());

    private String insertCustomersSql;
    private String url, user, password;
    private Class<? extends Driver> driverClassName;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @PostConstruct
    public void init() {
        this.insertCustomersSql = environment.getProperty("jdbc.sql.customers.insert");
        this.password = environment.getProperty("dataSource.password");
        this.user = environment.getProperty("dataSource.user");
        this.driverClassName = environment.getPropertyAsClass("dataSource.driverClass", Driver.class);
        this.url = environment.getProperty("dataSource.batchUrl");
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

    @Bean   // sets up infrastructure and scope
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(this.mapJobRegistry());
        return jobRegistryBeanPostProcessor;
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MapJobRegistry mapJobRegistry() throws Exception {
        return new MapJobRegistry();
    }

    @Bean(name = "jobRepository")
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(this.dataSource());
        jobRepositoryFactoryBean.setTransactionManager(this.transactionManager());
        jobRepositoryFactoryBean.afterPropertiesSet();
        return (JobRepository) jobRepositoryFactoryBean.getObject();
    }

    @Bean  // thread safe and stateless, no need to make it step-scoped.
    public JdbcBatchItemWriter<Customer> writer() {
        JdbcBatchItemWriter<Customer> jdbcBatchItemWriter = new JdbcBatchItemWriter<Customer>();
        jdbcBatchItemWriter.setAssertUpdates(true);
        jdbcBatchItemWriter.setDataSource(this.dataSource());
        jdbcBatchItemWriter.setSql(this.insertCustomersSql);
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        return jdbcBatchItemWriter;
    }

    @Bean
    public SimpleJobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(this.jobRepository());
        return simpleJobLauncher;
    }

    @Bean
    @Scope("step")
    public FlatFileItemReader<Customer> reader(@Value("#{jobParameters['input.file']}") Resource resource) throws Exception {

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


}

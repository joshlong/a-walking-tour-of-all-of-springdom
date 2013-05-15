package com.joshlong.spring.walkingtour.services.batch;

import org.apache.commons.logging.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import com.joshlong.spring.walkingtour.services.model.Customer;

import javax.sql.DataSource;
import java.sql.Driver;


@Configuration
@EnableBatchProcessing(modular = true)
@ComponentScan
@PropertySource("classpath:/services.properties")
public class BatchConfiguration {

    private static final String readCsvFileIntoTableStep = "readCsvFileIntoTableStep";
    private static final String readCsvFileIntoTableStepReader = readCsvFileIntoTableStep + "Reader";
    private static final String readCsvFileIntoTableStepWriter = readCsvFileIntoTableStep + "Writer";
    private static final String readCsvFileIntoTableStepProcessor = readCsvFileIntoTableStep + "Processor";
    private static final String customerLoaderJob = "customerLoaderJob";
    private Log log = LogFactory.getLog(getClass());

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public DataSource dataSource(Environment environment) {
        String pw = environment.getProperty("dataSource.password"),
                user = environment.getProperty("dataSource.user"),
                url = environment.getProperty("dataSource.url");

        Class<Driver> classOfDs = environment.getPropertyAsClass("dataSource.driverClass", Driver.class);

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setPassword(pw);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setDriverClass(classOfDs);
        return dataSource;
    }

    /**
     * maps CSV data into rows of fields, which are then
     * mapped to Customer.class instances based on conventions:
     * <p/>
     * <CODE>col 1 => firstName => customer.setFirstName(String)</CODE>
     */
    @Bean(name = readCsvFileIntoTableStepReader)
    @StepScope
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

    @Bean(name = readCsvFileIntoTableStepProcessor)
    public ItemProcessor<Customer, Customer> processor() {
        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                log.debug(String.format("processing the customer %s", item.toString()));
                return item;
            }
        };
    }

    @Bean(name = readCsvFileIntoTableStepWriter)
    public JdbcBatchItemWriter<Customer> writer(DataSource dataSource) throws Exception {
        JdbcBatchItemWriter<Customer> jdbcBatchItemWriter = new JdbcBatchItemWriter<Customer>();
        jdbcBatchItemWriter.setAssertUpdates(true);
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setSql(" INSERT INTO customer( first_name, last_name) VALUES ( :firstName , :lastName ) ");
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        return jdbcBatchItemWriter;
    }

    @Bean
    public Job customerLoaderJob(JobBuilderFactory jobs, @Qualifier(readCsvFileIntoTableStep) Step s1) {
        return jobs.get(customerLoaderJob)
                .flow(s1)
                .end()
                .build();
    }

    @Bean(name = readCsvFileIntoTableStep)
    public Step readCsvFileIntoTableStep(
            StepBuilderFactory stepBuilderFactory,
            PlatformTransactionManager platformTransactionManager,
            @Qualifier(readCsvFileIntoTableStepReader) ItemReader<Customer> ir,
            @Qualifier(readCsvFileIntoTableStepProcessor) ItemProcessor<Customer, Customer> itemProcessor,
            @Qualifier(readCsvFileIntoTableStepWriter) ItemWriter<Customer> iw) {

        StepBuilder builder = stepBuilderFactory.get(readCsvFileIntoTableStep);

        return builder.<Customer, Customer>chunk(3)
                .reader(ir)
                .processor(itemProcessor)
                .writer(iw)
                .transactionManager(platformTransactionManager)
                .build();
    }
}
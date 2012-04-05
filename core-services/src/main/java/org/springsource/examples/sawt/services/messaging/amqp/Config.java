package org.springsource.examples.sawt.services.messaging.amqp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SingleConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;

@Configuration
@PropertySource("classpath:/services.properties")
@EnableTransactionManagement
@ComponentScan(basePackageClasses = Config.class, excludeFilters = {@ComponentScan.Filter(Configuration.class)})

public class Config {

    @Inject
    private Environment environment;

    private String customersQueueAndExchangeName = "customers";

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(singleConnectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitTransactionManager amqpTransactionManager() {
        return new RabbitTransactionManager(this.singleConnectionFactory());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JsonMessageConverter();
    }


    @Bean
    public ConnectionFactory singleConnectionFactory() {
        SingleConnectionFactory connectionFactory = new SingleConnectionFactory(environment.getProperty("amqp.broker.url"));
        connectionFactory.setUsername(environment.getProperty("amqp.broker.username"));
        connectionFactory.setPassword(environment.getProperty("amqp.broker.password"));
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(this.rabbitTemplate());
    }


    @Bean
    public Queue customerQueue() {
        Queue q = new Queue(this.customersQueueAndExchangeName);
        amqpAdmin().declareQueue(q);
        return q;
    }

    @Bean
    public DirectExchange customerExchange() {
        DirectExchange directExchange = new DirectExchange(customersQueueAndExchangeName);
        this.amqpAdmin().declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding marketDataBinding() {
        return BindingBuilder.from(customerQueue()).to(customerExchange()).with(this.customersQueueAndExchangeName);
    }
}

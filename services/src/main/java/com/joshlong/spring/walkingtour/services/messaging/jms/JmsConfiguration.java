package com.joshlong.spring.walkingtour.services.messaging.jms;

import com.joshlong.spring.walkingtour.services.model.Customer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.task.*;
import org.springframework.jms.connection.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.*;
import java.util.concurrent.Executors;

/**
 * configuration for a raw JMS based solution
 */
@Configuration
@PropertySource("classpath:/services.properties")
@EnableTransactionManagement
public class JmsConfiguration {


    @Bean
    public ConnectionFactory connectionFactory(Environment environment) throws Exception {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(environment.getProperty("jms.broker.url"));
        return new CachingConnectionFactory(activeMQConnectionFactory);
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) throws Exception {
        return new JmsTransactionManager(connectionFactory);
    }

    @Bean //  optional
    public MessageConverter messageConverter() {
        return new MappingJacksonMessageConverter();
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) throws Exception {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public DefaultMessageListenerContainer defaultMessageListenerContainer(final MessageConverter messageConverter, TaskExecutor taskExecutor, ConnectionFactory connectionFactory) {

        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setMessageListener(new javax.jms.MessageListener() {
            @Override
            public void onMessage(javax.jms.Message message) {
                try {
                    Customer customer = (Customer) messageConverter.fromMessage(message);
                    System.out.println("Received new customer " + customer.toString());
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        defaultMessageListenerContainer.setConcurrentConsumers(10);
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
        defaultMessageListenerContainer.setDestinationName("customers");
        defaultMessageListenerContainer.setAutoStartup(false);
        defaultMessageListenerContainer.setTaskExecutor(taskExecutor);
        return defaultMessageListenerContainer;
    }

}

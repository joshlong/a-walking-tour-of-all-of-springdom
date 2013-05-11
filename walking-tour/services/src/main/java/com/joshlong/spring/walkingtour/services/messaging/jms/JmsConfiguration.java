package com.joshlong.spring.walkingtour.services.messaging.jms;

import java.util.concurrent.Executors;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJacksonMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
		activeMQConnectionFactory.setBrokerURL(environment
				.getProperty("jms.broker.url"));
		return new CachingConnectionFactory(activeMQConnectionFactory);
	}

	@Bean
	public JmsTransactionManager jmsTransactionManager(
			ConnectionFactory connectionFactory) throws Exception {
		return new JmsTransactionManager(connectionFactory);
	}

	@Bean
	public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) throws Exception {
		return new JmsTemplate(connectionFactory);
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
	public DefaultMessageListenerContainer defaultMessageListenerContainer(
			TaskExecutor taskExecutor, ConnectionFactory connectionFactory) {

		DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer
				.setMessageListener(new javax.jms.MessageListener() {
					@Override
					public void onMessage(javax.jms.Message message) {
						try {
							System.out.println("Received new customer " + message);
						} catch (Exception e) {
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

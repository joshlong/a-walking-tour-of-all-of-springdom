package org.springsource.examples.sawt.services.messaging.amqp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;

@Configuration
@PropertySource("/services.properties")
@EnableTransactionManagement
public class AmqpConfiguration {

    @Inject
    private Environment environment;

	private String customersQueueAndExchangeName = "customers";

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public RabbitTransactionManager amqpTransactionManager() {
		return new RabbitTransactionManager(this.connectionFactory());
	}

	@Bean
	// optional, this provides both Marshaller and Unmarshaller interfaces
	public CastorMarshaller oxmMarshaller() {
		return new CastorMarshaller();
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		MarshallingMessageConverter marshallingMessageConverter = new MarshallingMessageConverter();
		marshallingMessageConverter.setMarshaller(this.oxmMarshaller());
		marshallingMessageConverter.setUnmarshaller(this.oxmMarshaller());
		return marshallingMessageConverter;
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setUsername(environment.getProperty("amqp.broker.username"));
		cachingConnectionFactory.setPassword(environment.getProperty("amqp.broker.password"));
		cachingConnectionFactory.setHost(environment.getProperty("amqp.broker.host"));
		cachingConnectionFactory.setPort(environment.getProperty( "amqp.broker.port", Integer.class));
		// cachingConnectionFactory.setPort(60705);
		return cachingConnectionFactory;
	}

	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(this.connectionFactory());
	}

	@Bean
	public Queue customerQueue() {
		Queue q = new Queue(this.customersQueueAndExchangeName);
		amqpAdmin().declareQueue(q);
		return q;
	}

	@Bean
	public DirectExchange customerExchange() {
		DirectExchange directExchange = new DirectExchange(
				customersQueueAndExchangeName);
		this.amqpAdmin().declareExchange(directExchange);
		return directExchange;
	}

	@Bean
	public Binding marketDataBinding() {
		return BindingBuilder.bind(customerQueue()).to(customerExchange())
				.with(this.customersQueueAndExchangeName);
	}
}

package org.springsource.examples.sawt.services.messaging.amqp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("/services.properties")
@EnableTransactionManagement
public class AmqpConfiguration {


    private String customersQueueAndExchangeName = "customers";

    @Bean
    public RabbitTemplate rabbitTemplate(MessageConverter messageConverter, ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitTransactionManager amqpTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    public CastorMarshaller oxmMarshaller() {
        return new CastorMarshaller();
    }

    @Bean
    public MessageConverter jsonMessageConverter(CastorMarshaller castorMarshaller) {
        MarshallingMessageConverter marshallingMessageConverter = new MarshallingMessageConverter();
        marshallingMessageConverter.setMarshaller(castorMarshaller);
        marshallingMessageConverter.setUnmarshaller(castorMarshaller);
        return marshallingMessageConverter;
    }

    @Bean
    public ConnectionFactory connectionFactory(Environment environment) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setUsername(environment.getProperty("amqp.broker.username"));
        cachingConnectionFactory.setPassword(environment.getProperty("amqp.broker.password"));
        cachingConnectionFactory.setHost(environment.getProperty("amqp.broker.host"));
        cachingConnectionFactory.setPort(environment.getProperty("amqp.broker.port", Integer.class));
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue customerQueue(AmqpAdmin amqpAdmin) {
        Queue q = new Queue(this.customersQueueAndExchangeName);
        amqpAdmin.declareQueue(q);
        return q;
    }

    @Bean
    public DirectExchange customerExchange(AmqpAdmin amqpAdmin) {
        DirectExchange directExchange = new DirectExchange( customersQueueAndExchangeName);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding marketDataBinding(Queue customerQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(customerQueue)
                .to(directExchange)
                .with(this.customersQueueAndExchangeName);
    }
}

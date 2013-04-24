package org.springsource.examples.sawt.services.messaging.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.*;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.ConnectionFactory;

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

    @Bean // optional, this provides both Marshaller and Unmarshaller interfaces
    public CastorMarshaller oxmMarshaller() {
        return new CastorMarshaller();
    }

    @Bean //  optional
    public MessageConverter messageConverter(CastorMarshaller abstractMarshaller) {
        MarshallingMessageConverter marshallingMessageConverter = new MarshallingMessageConverter();
        marshallingMessageConverter.setMarshaller(abstractMarshaller);
        marshallingMessageConverter.setTargetType(MessageType.TEXT);
        marshallingMessageConverter.setUnmarshaller(abstractMarshaller);
        return marshallingMessageConverter;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) throws Exception {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }

}

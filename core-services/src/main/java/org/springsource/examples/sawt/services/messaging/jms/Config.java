package org.springsource.examples.sawt.services.messaging.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.oxm.support.AbstractMarshaller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;

/**
 * configuration for a raw JMS based solution
 */
@Configuration
@PropertySource("classpath:/services.properties")
@EnableTransactionManagement
@ComponentScan(basePackageClasses = Config.class, excludeFilters = {@ComponentScan.Filter(Configuration.class)})

public class Config {
    @Inject
    private Environment environment;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(environment.getProperty("jms.broker.url"));
        return new CachingConnectionFactory(activeMQConnectionFactory);
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager() throws Exception {
        return new JmsTransactionManager(this.connectionFactory());
    }

    @Bean    // optional, this provides both Marshaller and Unmarshaller interfaces
    public AbstractMarshaller oxmMarshaller() {
        return new CastorMarshaller();
    }

    @Bean   //  optional
    public MessageConverter messageConverter() {
        MarshallingMessageConverter marshallingMessageConverter = new MarshallingMessageConverter();
        marshallingMessageConverter.setMarshaller(this.oxmMarshaller());
        marshallingMessageConverter.setTargetType(MessageType.TEXT);
        marshallingMessageConverter.setUnmarshaller(this.oxmMarshaller());
        return marshallingMessageConverter;
    }

    @Bean
    public JmsTemplate jmsTemplate() throws Exception {
        JmsTemplate jmsTemplate = new JmsTemplate(this.connectionFactory());
        jmsTemplate.setMessageConverter(this.messageConverter());    // optional
        return jmsTemplate;
    }

}

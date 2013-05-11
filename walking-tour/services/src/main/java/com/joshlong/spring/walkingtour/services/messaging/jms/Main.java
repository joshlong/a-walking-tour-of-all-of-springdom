package com.joshlong.spring.walkingtour.services.messaging.jms;

import com.joshlong.spring.walkingtour.services.model.Customer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;

import javax.jms.*;

import java.math.BigInteger;
import java.util.Date;

public class Main {
	public static void main(String[] args) throws Exception {

		Log log = LogFactory.getLog(Main.class);

		final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext( JmsConfiguration.class);
		applicationContext.registerShutdownHook();
		final String destinationName = "customers";

		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);

		final Customer customer = new Customer("First", "Last");

		jmsTemplate.send( destinationName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(customer);
			}
		});
		jmsTemplate.convertAndSend(destinationName, customer);

		Message m = jmsTemplate.receive(destinationName);
		log.info("unconverted message: " + m);

		final DefaultMessageListenerContainer jmsMessageListenerContainer = applicationContext
				.getBean(DefaultMessageListenerContainer.class);
		final TaskScheduler taskScheduler = applicationContext
				.getBean(TaskScheduler.class);
		Runnable goodbyeCruelWorld = new Runnable() {
			@Override
			public void run() {
				System.out.println("shutting down!");
				System.exit(0);
			}
		};
		final Date dateToStopTheMessageListenerContainer = DateUtils
				.addSeconds(new Date(System.currentTimeMillis()), 10);
		taskScheduler.schedule(goodbyeCruelWorld,
				dateToStopTheMessageListenerContainer);

		for (long i = 0; i < 10; i++)
			jmsTemplate.convertAndSend(destinationName, new Customer("First"
					+ i, "Last" + i));

		jmsMessageListenerContainer.start();
	}
}
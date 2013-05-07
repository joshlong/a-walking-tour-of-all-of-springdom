package com.joshlong.spring.walkingtour.services.messaging.amqp;


import com.joshlong.spring.walkingtour.services.model.Customer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.TaskScheduler;

import java.math.BigInteger;
import java.util.Date;


// TODO make sure that you have RabbitMQ up and running
public class Main {
    public static void main(String[] args) throws Exception {

        Log log = LogFactory.getLog(Main.class);

        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AmqpConfiguration.class);
        applicationContext.registerShutdownHook();

        AmqpTemplate amqpTemplate = applicationContext.getBean(AmqpTemplate.class);

        Queue queue = applicationContext.getBean(Queue.class);
        String queueName = queue.getName();

        Customer customer = new Customer("First", "Last");

        amqpTemplate.convertAndSend(queueName, customer);
        amqpTemplate.convertAndSend(queueName, customer); // weve sent the same message, twice

        Customer ogCustomer = (Customer) amqpTemplate.receiveAndConvert(queueName);
        log.info("converted message: " + ToStringBuilder.reflectionToString(ogCustomer));

        Message message = amqpTemplate.receive(queueName);
        String msgBody = new String(message.getBody());
        log.info("unconverted message: " + msgBody);

        final SimpleMessageListenerContainer amqpMessageListenerContainer = applicationContext.getBean(SimpleMessageListenerContainer.class);
        final TaskScheduler taskScheduler = applicationContext.getBean(TaskScheduler.class);
        final Date dateToStopTheMessageListenerContainer = DateUtils.addSeconds(new Date(System.currentTimeMillis()), 10);
        Runnable goodbyeCruelWorld = new Runnable() {
            @Override
            public void run() {
                System.out.println("shutting down!");
                System.exit(0);
            }
        };
        taskScheduler.schedule(goodbyeCruelWorld, dateToStopTheMessageListenerContainer);
        for (long i = 0; i < 10; i++)
            amqpTemplate.convertAndSend(queueName, new Customer(BigInteger.valueOf(i), "First" + i, "Last" + i));

        amqpMessageListenerContainer.start();

    }
}

package org.springsource.examples.sawt.services.messaging.amqp;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.examples.sawt.services.model.Customer;


public class Main {
    public static void main(String[] args) throws Exception {

        Log log = LogFactory.getLog(Main.class);

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

        AmqpTemplate amqpTemplate = applicationContext.getBean(AmqpTemplate.class);

        Customer customer = new Customer("Mario", "Gray");

        String queue = "customers";

        amqpTemplate.convertAndSend(queue, customer);
        amqpTemplate.convertAndSend(queue, customer); // weve sent the same message, twice

        Customer ogCustomer = (Customer) amqpTemplate.receiveAndConvert(queue);
        log.info("converted message: " + ToStringBuilder.reflectionToString(ogCustomer));

        Message m = amqpTemplate.receive(queue);
        String msgBody = new String(m.getBody());
        log.info("unconverted message: " + msgBody);
    }
}

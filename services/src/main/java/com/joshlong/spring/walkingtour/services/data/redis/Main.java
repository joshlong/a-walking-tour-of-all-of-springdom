package com.joshlong.spring.walkingtour.services.data.redis;

import org.apache.commons.logging.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.listener.Topic;

/**
 * @author Josh Long
 */
public class Main {
    public static void main(String args[]) throws Throwable {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RedisConfiguration.class);
        applicationContext.registerShutdownHook();

        Topic topic = applicationContext.getBean(Topic.class);

        Log log = LogFactory.getLog(Main.class);





    }

}

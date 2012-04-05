package org.springsource.examples.sawt.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {

        Log log = LogFactory.getLog(Main.class);

        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:/org/springsource/examples/sawt/aop/config.xml");

        Calculator calculator = classPathXmlApplicationContext.getBean(Calculator.class);
        log.info("result is: " + calculator.add(2, 2));
        log.info("result is: " + calculator.add(2, 2));
    }
}

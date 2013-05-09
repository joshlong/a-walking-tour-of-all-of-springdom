package com.joshlong.spring.walkingtour.ioc.lifecycles.annotations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * A simple object with setup and tear down logic
 */
@Component
public class Astronaut {

	private Log log = LogFactory.getLog(getClass());

	@PostConstruct
	public void liftOff() throws Throwable {
		for (int i = 5; i > 0; i--) {
			log.debug(i + "...");
			Thread.sleep(1000);
		}
		log.debug("we have liftoff!");
	}

	@PreDestroy
	public void land() throws Throwable {
		System.out.println("this is one small step for man... one giant leap for mankind");
	}
}

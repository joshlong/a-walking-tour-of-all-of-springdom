package com.joshlong.spring.walkingtour.ioc.lifecycles.interfaces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * A simple object with setup and tear down logic
 */
@Component
public class Astronaut implements InitializingBean, DisposableBean {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void destroy() throws Exception {
        log.debug("this is one small step for man... one giant leap for mankind");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 5; i > 0; i--) {
            log.debug(i + "...");
            Thread.sleep(1000);
        }
        log.debug("we have liftoff!");
    }

}



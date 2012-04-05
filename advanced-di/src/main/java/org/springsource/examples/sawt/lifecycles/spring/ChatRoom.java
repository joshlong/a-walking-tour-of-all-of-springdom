package org.springsource.examples.sawt.lifecycles.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class ChatRoom implements InitializingBean {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("starting ChatRoom");
    }
}

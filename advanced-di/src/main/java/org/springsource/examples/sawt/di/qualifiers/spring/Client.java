package org.springsource.examples.sawt.di.qualifiers.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springsource.examples.sawt.di.qualifiers.spring.production.Production;

import javax.annotation.PostConstruct;

@Component
public class Client {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    @Production
    private CustomerService customerService;

    @PostConstruct
    public void begin() throws Exception {
        log.info("type of injected " + CustomerService.class.getName() + " is " + this.customerService.getClass());
    }
}

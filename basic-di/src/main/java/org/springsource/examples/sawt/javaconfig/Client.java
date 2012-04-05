package org.springsource.examples.sawt.javaconfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Client {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private CustomerService customerService;

    @PostConstruct
    public void start() throws Exception {
        log.info("Starting Client. CustomerService implementation is "
                + this.customerService.getClass());
    }
}

package org.springsource.examples.sawt.di.jsr250and330;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named
@Scope("prototype")
public class CustomerService {
    private Log log = LogFactory.getLog(getClass());

    @PostConstruct
    public void start() {
        log.info("starting a new CustomerService ");
    }
}

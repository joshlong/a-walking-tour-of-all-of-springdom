package org.springsource.examples.sawt.di.qualifiers.inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springsource.examples.sawt.di.qualifiers.inject.production.Production;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Client {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    @Production
    private CustomerService customerService;

    @PostConstruct
    public void begin() throws Exception {
        log.info("type of injected " + CustomerService.class.getName() + " is " + this.customerService.getClass());
    }
}

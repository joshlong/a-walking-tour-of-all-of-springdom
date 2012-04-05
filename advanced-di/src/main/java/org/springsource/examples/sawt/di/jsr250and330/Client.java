package org.springsource.examples.sawt.di.jsr250and330;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@Named
public class Client {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    private CustomerService customerServiceFromInject;
    @Inject
    private Provider<CustomerService> customerServiceProvider;
    @Resource
    private CustomerService customerServiceFromResource;

    @PostConstruct
    public void begin() throws Exception {
        log.info("@javax.inject.Inject: " + CustomerService.class.getName() + " is " + this.customerServiceFromInject.getClass());
        log.info("@javax.annotation.Resource: " + CustomerService.class.getName() + " is " + this.customerServiceFromResource.getClass());
        log.info("Provider<CustomerService>: " + CustomerService.class.getName() + " is " + this.customerServiceProvider.get().getClass());
    }
}

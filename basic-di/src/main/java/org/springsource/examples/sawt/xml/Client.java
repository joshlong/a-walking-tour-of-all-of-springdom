package org.springsource.examples.sawt.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class Client implements InitializingBean {

    private Log log = LogFactory.getLog(getClass());
    private CustomerService customerService;

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Starting Client. CustomerService implementation is " + this.customerService.getClass());
    }
}

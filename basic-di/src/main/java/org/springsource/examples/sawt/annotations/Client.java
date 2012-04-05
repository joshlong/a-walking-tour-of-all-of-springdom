package org.springsource.examples.sawt.annotations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Client implements InitializingBean {

    private Log log = LogFactory.getLog(getClass());

    //@Autowired
    public Client(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    private CustomerService customerService;

    // @Autowired
    public void setCustomerService(CustomerService cs) {
        this.customerService = cs;
    }

    public Client() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Starting Client. CustomerService implementation is "
                + this.customerService.getClass());
    }
}

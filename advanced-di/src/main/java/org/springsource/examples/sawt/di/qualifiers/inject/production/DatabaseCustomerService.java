package org.springsource.examples.sawt.di.qualifiers.inject.production;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springsource.examples.sawt.di.qualifiers.inject.CustomerService;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named
@Production
public class DatabaseCustomerService implements CustomerService {
    private Log log = LogFactory.getLog(getClass());

    @PostConstruct
    public void begin() throws Exception {
        log.info("starting " + getClass().getName() + ".");
    }
}

package org.springsource.examples.sawt.di.qualifiers.spring.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springsource.examples.sawt.di.qualifiers.spring.CustomerService;

import javax.annotation.PostConstruct;

@Component
@Test
public class MockCustomerService implements CustomerService {

    private Log log = LogFactory.getLog(getClass());

    @PostConstruct
    public void begin() throws Exception {
        log.info("starting " + getClass().getName() + ".");
    }
}

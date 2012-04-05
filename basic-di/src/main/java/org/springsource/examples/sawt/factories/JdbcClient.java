package org.springsource.examples.sawt.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;

@Component
public class JdbcClient {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("ptm")
    private PlatformTransactionManager transactionManager;

    @PostConstruct
    public void start() throws Throwable {
        log.info("injected transaction manager? " + transactionManager.toString());
    }
}

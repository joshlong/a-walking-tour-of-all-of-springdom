package org.springsource.examples.sawt.javaconfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

public class CustomerService {

    private Log log = LogFactory.getLog(getClass());

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }

    private DataSource dataSource;

    @PostConstruct
    public void start() throws Exception {
        log.info("CustomerService#dataSource : " + dataSource.toString());
    }
}

package org.springsource.examples.sawt.annotations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class CustomerService {

    private Log log = LogFactory.getLog(getClass());
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void start() throws Exception {
        log.info("CustomerService#dataSource : " + dataSource.toString());
    }
}

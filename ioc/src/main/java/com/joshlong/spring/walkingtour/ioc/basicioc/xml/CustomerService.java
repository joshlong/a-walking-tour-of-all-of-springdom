package com.joshlong.spring.walkingtour.ioc.basicioc.xml;

import javax.sql.DataSource;

/**
 * Example class that requires a {@link javax.sql.DataSource}
 */
public class CustomerService {

    private DataSource dataSource;

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
     }


}

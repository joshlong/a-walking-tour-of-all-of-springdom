package com.joshlong.spring.walkingtour.ioc.basicioc.javaconfig;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.sql.DataSource;


public class CustomerService {

    private DataSource dataSource;

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
    }
}

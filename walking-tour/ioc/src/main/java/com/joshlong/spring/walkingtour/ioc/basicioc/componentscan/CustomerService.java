package com.joshlong.spring.walkingtour.ioc.basicioc.componentscan;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Simple class that relies on component scanning for injection
 */
@Service
public class CustomerService {

    @Autowired
    private DataSource dataSource1;

    @Inject
    private DataSource dataSource2;

    @Resource
    private DataSource dataSource3;

    @PostConstruct
    public void analyse() throws Throwable {
        System.out.println(ToStringBuilder.reflectionToString(dataSource1));
        System.out.println(ToStringBuilder.reflectionToString(dataSource2));
        System.out.println(ToStringBuilder.reflectionToString(dataSource3));
    }
}

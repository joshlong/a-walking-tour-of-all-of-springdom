package com.joshlong.spring.walkingtour.social.crm;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class CrmConnectionFactory extends OAuth2ConnectionFactory<CustomerServiceOperations> {
    public CrmConnectionFactory(CrmServiceProvider serviceProvider, CrmApiAdapter apiAdapter) {
        super("crm", serviceProvider, apiAdapter);
    }
}
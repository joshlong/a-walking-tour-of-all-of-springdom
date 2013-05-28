package com.joshlong.spring.walkingtour.social.crm;

import org.springframework.social.oauth2.*;

// compare to TwitterServiceProvider
public class CrmServiceProvider extends AbstractOAuth2ServiceProvider<CustomerServiceOperations> {

    private String baseUrl;

    public CrmServiceProvider(
            String baseUrl,
            String clientId,
            String consumerSecret,
            String authorizeUrl,
            String accessTokenUrl) {

        super(new OAuth2Template(clientId, consumerSecret, authorizeUrl, accessTokenUrl));

        this.baseUrl = baseUrl;
    }

    @Override
    public CustomerServiceOperations getApi(String accessToken) {
        return new CustomerServiceTemplate(accessToken, baseUrl);
    }
}

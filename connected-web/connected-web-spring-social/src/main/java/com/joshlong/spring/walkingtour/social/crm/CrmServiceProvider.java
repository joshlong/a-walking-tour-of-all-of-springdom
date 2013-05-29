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
        this.baseUrl = safeBaseUrl(baseUrl);
    }

    protected String safeBaseUrl(String baseUrl) {
        if (baseUrl.endsWith("/"))
            return "" + baseUrl.subSequence(0, baseUrl.lastIndexOf("/") - 1);
        return baseUrl;
    }

    @Override
    public CustomerServiceOperations getApi(String accessToken) {
        return new CustomerServiceTemplate(accessToken, baseUrl);
    }
}

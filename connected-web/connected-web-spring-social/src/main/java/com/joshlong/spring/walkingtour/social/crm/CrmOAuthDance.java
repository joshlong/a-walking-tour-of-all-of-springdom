package com.joshlong.spring.walkingtour.social.crm;

import org.springframework.social.connect.Connection;
import org.springframework.social.oauth2.*;
import org.springframework.util.StringUtils;

/**
 * @author Josh Long
 */
public class CrmOAuthDance {

    private static OAuth2Operations configureOAuth2Operations( OAuth2Operations oAuth2Operations){
        if( oAuth2Operations instanceof OAuth2Template)
            ((OAuth2Template)oAuth2Operations).setUseParametersForClientAuthentication( false );
        return  oAuth2Operations ;
    }

    public static  Connection<CustomerServiceOperations> oauthWithUsernameAndPassword(CrmConnectionFactory connectionFactory,
                                                           String returnToUrl, String scope, String state,
                                                           String user, String pw) {

        OAuth2Operations oAuth2Operations = configureOAuth2Operations( connectionFactory.getOAuthOperations() );

        OAuth2Parameters parameters = new OAuth2Parameters();
        if (StringUtils.hasText(returnToUrl)) parameters.setRedirectUri(returnToUrl);
        if (StringUtils.hasText(scope)) parameters.setScope(scope);
        if (StringUtils.hasText(state)) parameters.setState(state);
        AccessGrant accessGrant = oAuth2Operations.exchangeCredentialsForAccess(user, pw, parameters);
        return connectionFactory.createConnection(accessGrant);

    }

    public static String start(
            CrmConnectionFactory connectionFactory,
            String state,
            String scope,
            String returnToUrl) throws Throwable {

        OAuth2Operations oAuth2Operations = connectionFactory.getOAuthOperations();
         // send
      //  ((OAuth2Template)oAuth2Operations).setUseParametersForClientAuthentication(  false);

        OAuth2Parameters parameters = new OAuth2Parameters();
        if (StringUtils.hasText(returnToUrl)) parameters.setRedirectUri(returnToUrl);
        if (StringUtils.hasText(scope)) parameters.setScope(scope);
        if (StringUtils.hasText(state)) parameters.setState(state);
        return oAuth2Operations.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, parameters);
    }

    public static Connection<CustomerServiceOperations> thenObtainConnectionFromCode(CrmConnectionFactory connectionFactory, String code, String returnToUrl) {
        OAuth2Operations oAuth2Operations = connectionFactory.getOAuthOperations();
        ((OAuth2Template)oAuth2Operations).setUseParametersForClientAuthentication(true);
        AccessGrant accessGrant = oAuth2Operations.exchangeForAccess(code, returnToUrl, null);
        return connectionFactory.createConnection(accessGrant);
    }


}

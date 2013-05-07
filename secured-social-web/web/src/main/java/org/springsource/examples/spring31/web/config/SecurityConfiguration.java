package org.springsource.examples.spring31.web.config;

import org.springframework.context.annotation.*;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.vote.ScopeVoter;
import org.springsource.examples.spring31.services.UserService;
import org.springsource.examples.spring31.web.oauth.RoleAwareOAuthTokenServicesUserApprovalHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * class for all the security aware parts of the system like form-based login and OAuth
 *
 * @author Josh long
 */
@Configuration
@ImportResource({"classpath:/security.xml"})
public class SecurityConfiguration {

    private String applicationName = "crm";

    @Bean
    public OAuth2AuthenticationEntryPoint oauthAuthenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setRealmName(this.applicationName);
        return oAuth2AuthenticationEntryPoint;
    }

    @Bean
    public UnanimousBased accessDecisionManager() {
        List<AccessDecisionVoter> decisionVoters = new ArrayList<AccessDecisionVoter>();
        decisionVoters.add(new ScopeVoter());
        decisionVoters.add(new RoleVoter());
        decisionVoters.add(new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

    @Bean
    public InMemoryTokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public DefaultTokenServices tokenServices(InMemoryTokenStore tokenStore, ClientDetailsService jpaUserCredentialsService) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setClientDetailsService(jpaUserCredentialsService);
        return defaultTokenServices;
    }

    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }

    @Bean
    public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter(AuthenticationManager authenticationManager, OAuth2AuthenticationEntryPoint entryPoint) {
        ClientCredentialsTokenEndpointFilter endpointFilter = new ClientCredentialsTokenEndpointFilter();
        endpointFilter.setAuthenticationManager(authenticationManager);
        endpointFilter.setAuthenticationEntryPoint(entryPoint);
        return endpointFilter;

    }

    @Bean
    public RoleAwareOAuthTokenServicesUserApprovalHandler oauthTokenServicesUserApprovalHandler(AuthorizationServerTokenServices tokenServices) {
        RoleAwareOAuthTokenServicesUserApprovalHandler approvalHandler = new RoleAwareOAuthTokenServicesUserApprovalHandler();
        approvalHandler.setUseTokenServices(true);
        approvalHandler.setTokenServices(tokenServices);
        return approvalHandler;
    }



}




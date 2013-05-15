package com.joshlong.spring.walkingtour.connectedweb.web.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableJdbcConnectionRepository;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.*;
import org.springframework.social.facebook.config.annotation.EnableFacebook;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.web.context.request.NativeWebRequest;
import com.joshlong.spring.walkingtour.connectedweb.services.UserService;

@Configuration
@PropertySource("classpath:social.properties")
@EnableJdbcConnectionRepository
@EnableFacebook(appId = "${facebook.clientId}", appSecret = "${facebook.clientSecret}")
public class SocialConfiguration {

    public Authentication establishSpringSecurityLogin(UserService userService, String localUserId) {
        UserService.CrmUserDetails details = userService.loadUserByUsername(localUserId);
        String pw = org.apache.commons.lang.StringUtils.defaultIfBlank(details.getPassword(), "");
        Authentication toAuthenticate = new UsernamePasswordAuthenticationToken(details, pw, details.getAuthorities());
         SecurityContextHolder.getContext().setAuthentication(toAuthenticate);
        return toAuthenticate;
    }

    @Bean
    public UserIdSource userIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Bean
    public ProviderSignInController providerSignInController(
            final UserService userService,
            final UsersConnectionRepository usersConnectionRepository,
            final ConnectionFactoryLocator connectionFactoryLocator) {

        SignInAdapter signInAdapter = new SignInAdapter() {
            @Override
            public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
                establishSpringSecurityLogin(userService, userId);
                return null;
            }
        };
        ProviderSignInController psic = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
        psic.setSignInUrl("/crm/signin.html");
        psic.setPostSignInUrl("/crm/customers.html");
        psic.setSignUpUrl("/crm/signup.html");
        return psic;
    }
}
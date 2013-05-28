package com.joshlong.spring.walkingtour.connectedweb.web.config;

import com.joshlong.spring.walkingtour.connectedweb.services.UserService;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableJdbcConnectionRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.*;
import org.springframework.social.facebook.config.annotation.EnableFacebook;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.web.context.request.NativeWebRequest;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

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

@Configuration
@PropertySource("classpath*:datasource.properties")
public class JdbcConfiguration {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public javax.sql.DataSource jdbcTemplate(Environment environment) {
        java.sql.Driver driver = environment.getProperty("jdbc.driverClassName", java.sql.Driver.class);
        String url = environment.getProperty("jdbc.url");
        String user = environment.getProperty("jdbc.username");
        String pw = environment.getProperty("jdbc.password");
        return new SimpleDriverDataSource(driver, url, user, pw);
    }
}

public class City {
    private int countOfBusinesses;
    private String cityName;

    public City(String name, int count) {
        this.cityName = name;
        this.countOfBusinesses = count;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCountOfBusinesses() {
        return countOfBusinesses;
    }
}

public class Main {
    public static void main(String args[]) throws Throwable {

        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(JdbcConfiguration.class);
        JdbcTemplate jdbcTemplate = annotationConfigApplicationContext.getBean(JdbcTemplate.class);

        List<City> rowRecords = jdbcTemplate.query(
                "select city,count(*) countOfBusinesses from business group by city",
                new RowMapper<City>() {
                    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new City(rs.getString("city"), rs.getInt("countOfBusinesses"));
                    }
                });
        System.out.println("Returned: " + rowRecords);

    }
}
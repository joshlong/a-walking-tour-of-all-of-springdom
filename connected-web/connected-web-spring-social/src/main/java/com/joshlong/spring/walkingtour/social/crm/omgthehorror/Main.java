package com.joshlong.spring.walkingtour.social.crm.omgthehorror;

import com.joshlong.spring.walkingtour.social.crm.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.*;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.oauth2.AccessGrant;

import javax.sql.DataSource;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * @author Josh Long
 */
public class Main {

    private static Map<String, Object> configurationProperties() {
        final String propertyNameRoot = "sscrm";
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(propertyNameRoot + ".base-url", "http://localhost:8080");
        properties.put(propertyNameRoot + ".client-id", "android-crm");
        properties.put(propertyNameRoot + ".client-secret", "123456");
        properties.put(propertyNameRoot + ".authorize-url", "/oauth/authorize");
        properties.put(propertyNameRoot + ".access-token-url", "/oauth/token");
        return properties;
    }

    public static void main(String args[]) throws Throwable {


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        MapPropertySource mapPropertySource = new MapPropertySource("oauth", configurationProperties());

        applicationContext.getEnvironment().getPropertySources().addLast(mapPropertySource);
        applicationContext.register(ConnectionInfrastructureConfiguration.class);
        applicationContext.refresh();

        CrmConnectionFactory crmConnectionFactory = applicationContext.getBean(CrmConnectionFactory.class);

        String state = null; // any arbitrary string
        String scopes = "read,write";
        String redirectUri = mapPropertySource.getProperty("sscrm.base-url") + "/crm/profile.html";

        String authorizationUrl = CrmOAuthDance.start(crmConnectionFactory, state, scopes, redirectUri);

        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", "-a", "/Applications/Google Chrome.app", authorizationUrl});
        String accessToken =
             // "57f52ff8-2d95-48fe-882a-183c48d821ce" ;
                safe(JOptionPane.showInputDialog(null, "What's the 'access_token'?"));
        Connection<CustomerServiceOperations> connection = crmConnectionFactory.createConnection(new AccessGrant(accessToken));

        UserProfile userProfile = connection.fetchUserProfile();
        System.out.println("obtained connection: " +   userProfile.getUsername() + "."  );

        CustomerServiceOperations customerServiceOperations = connection.getApi();
        Collection<Customer> customerCollection = customerServiceOperations.searchCustomers("andy");
        for (Customer c : customerCollection) {
            System.out.println( c.toString() );
        }

        User self =customerServiceOperations.currentUser();


        Customer customer = customerServiceOperations.createCustomer( self.getId(), "josh","long" , new java.util.Date());

        System.out.println(customer.toString());


    }

    static String str(InputStream inputStream)
            throws Throwable {
        return IOUtils.toString(inputStream);
    }

    static String safe(String i)
            throws Throwable {
        return i != null && !i.trim().equals("") ? i.trim() : null;
    }

    static String readLine() {
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * to test all of this out i need Spring to bootup the infrastructure for consuming Spring Social bindings and
     * persisting the data for later. Of course, on Android all of this will be irrelevant and we'll simply use the
     * SQL Lite support.
     */
    @Configuration
    public static class ConnectionInfrastructureConfiguration {

        private Log log = LogFactory.getLog(getClass());

        @Bean
        public DataSource dataSource() {
            SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
            simpleDriverDataSource.setDriverClass(org.h2.Driver.class);
            simpleDriverDataSource.setUrl("jdbc:h2:tcp://localhost/~/spring-social-crm");
            simpleDriverDataSource.setUsername("sa");
            simpleDriverDataSource.setPassword("");
            return simpleDriverDataSource;
        }

        // this is the recipe to construct an instance of the CrmServiceProvider API that we can use outside of
        // Spring's Java configuration (and assuming no Environment abstraction) as we won't have that on Android.
        private CrmServiceProvider crmServiceProvider(String clientId, String clientSecret, String baseUrl, String authorizeUrl, String accessTokenUrl) {
            log.debug(String.format("baseUrl=%s, clientSecret=%s, consumerSecret=%s, authorizeUrl=%s, accessTokenUrl=%s",
                    baseUrl, clientId, clientSecret, authorizeUrl, accessTokenUrl));
            final String http = "http://";
            assert baseUrl != null && baseUrl.length() > 0 : "the baseUrl can't be null!";
            if (!authorizeUrl.toLowerCase().startsWith(http)) authorizeUrl = baseUrl + authorizeUrl;
            if (!accessTokenUrl.toLowerCase().startsWith(http)) accessTokenUrl = baseUrl + accessTokenUrl;
            return new CrmServiceProvider(baseUrl, clientId, clientSecret, authorizeUrl, accessTokenUrl);
        }

        @Bean
        public CrmServiceProvider crmServiceProvider(Environment environment) {

            final String propertyNameRoot = "sscrm";

            String clientId = environment.getProperty(propertyNameRoot + ".client-id"),
                    clientSecret = environment.getProperty(propertyNameRoot + ".client-secret");

            String baseUrl = environment.getProperty(propertyNameRoot + ".base-url"),
                    authorizeUrl = environment.getProperty(propertyNameRoot + ".authorize-url"),
                    accessTokenUrl = environment.getProperty(propertyNameRoot + ".access-token-url");

            return this.crmServiceProvider(clientId, clientSecret, baseUrl, authorizeUrl, accessTokenUrl);
        }

        // this will get replaced with a SQL Lite compatible implementation on Android
        @Bean
        public JdbcUsersConnectionRepository jdbcUsersConnectionRepository(DataSource dataSource, ConnectionFactoryLocator locator) {
            return new JdbcUsersConnectionRepository(dataSource, locator, Encryptors.noOpText());
        }


        @Bean
        public CrmApiAdapter crmApiAdapter() {
            return new CrmApiAdapter();
        }

        @Bean
        public CrmConnectionFactory crmConnectionFactory(CrmServiceProvider crmServiceProvider,   CrmApiAdapter crmApiAdapter) {
            return new CrmConnectionFactory(crmServiceProvider, crmApiAdapter);
        }

        @Bean
        @Scope(proxyMode = ScopedProxyMode.INTERFACES)
        public ConnectionFactoryLocator connectionFactoryLocator(CrmConnectionFactory crmConnectionFactory) {
            ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
            registry.addConnectionFactory(crmConnectionFactory);
            return registry;
        }

        @Bean
        @Scope(proxyMode = ScopedProxyMode.INTERFACES)
        public UsersConnectionRepository usersConnectionRepository(DataSource dataSource, ConnectionFactoryLocator connectionFactoryLocator) {
            return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        }

        @Bean
        @Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
        public ConnectionRepository connectionRepository(UsersConnectionRepository usersConnectionRepository) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null)
                throw new IllegalStateException("Unable to get a " + ConnectionRepository.class.getName() +
                        ": no user signed in via Spring Security. Please fix this!");
            return usersConnectionRepository.createConnectionRepository(authentication.getName());
        }

        @Bean
        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        public CustomerServiceOperations customerServiceOperations(ConnectionRepository connectionRepository) {
            Connection<CustomerServiceOperations> customerServiceOperations = connectionRepository.findPrimaryConnection(CustomerServiceOperations.class);
            return customerServiceOperations != null ? customerServiceOperations.getApi() : null;
        }

    }
}

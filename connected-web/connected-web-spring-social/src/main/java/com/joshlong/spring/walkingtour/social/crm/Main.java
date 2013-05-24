package com.joshlong.spring.walkingtour.social.crm;

import org.apache.commons.logging.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.*;
import org.springframework.social.connect.*;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.*;
import org.springframework.social.oauth2.*;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.web.util.UriComponentsBuilder;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Josh Long
 */
public class Main {

    static Logger log = Logger.getLogger(Main.class.getName());

    private static Map<String, Object> configurationProperties() {
        final String propertyNameRoot = "sscrm";
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(propertyNameRoot + ".base-url", "http://127.0.0.1:8080/");
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

        OAuth2Operations oAuth2Operations = crmConnectionFactory.getOAuthOperations();
        OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
        oAuth2Parameters.setScope("read,write");
        oAuth2Parameters.setRedirectUri(mapPropertySource.getProperty("sscrm.base-url") + "/crm/profile.html");

        String authorizeUrl = oAuth2Operations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);


        log.info("authorizeUrl: " + authorizeUrl);


    }

    /**
     * client-side perspective of the RESTful service
     */
    public static interface CustomerServiceOperations {

        CrmUserProfile currentUser();

        Customer createCustomer(Long userId, String firstName, String lastName, Date signupDate);

        Collection<Customer> searchCustomers(String name);

        List<Customer> getAllUserCustomers(Long userid);

        void deleteCustomer(Long id);

        void updateCustomer(Long id, String fn, String ln, Date birthday);
    }

    public static class CrmConnectionFactory extends OAuth2ConnectionFactory<CustomerServiceOperations> {
        public CrmConnectionFactory(CrmServiceProvider serviceProvider, CrmApiAdapter apiAdapter) {
            super("crm", serviceProvider, apiAdapter);
        }
    }

    /// compare to TwitterAdapter
    public static class CrmApiAdapter implements ApiAdapter<CustomerServiceOperations> {
        private Log log = LogFactory.getLog(getClass());

        @Override
        public boolean test(CustomerServiceOperations customerServiceOperations) {
            return (null != customerServiceOperations.currentUser());
        }

        @Override
        public void setConnectionValues(CustomerServiceOperations customerServiceOperations, ConnectionValues values) {
            CrmUserProfile profile = customerServiceOperations.currentUser();
            log.info("profile= " + profile.toString());                          // TODO most of these values are incorrect!
            values.setProviderUserId(Long.toString(profile.getId()));
            values.setDisplayName(profile.getUsername());
            values.setProfileUrl("http://facebook.com/profile.php?id=" + profile.getId()); // todo show the user's profile URL (if any)
            values.setImageUrl("http://graph.facebook.com/" + profile.getId() + "/picture"); // todo show the user's current profile iamge
        }

        @Override
        public UserProfile fetchUserProfile(CustomerServiceOperations customerServiceOperations) {
            CrmUserProfile crmUserProfile = customerServiceOperations.currentUser();
            String name = crmUserProfile.getFirstName() + ' ' + crmUserProfile.getLastName();
            return new UserProfileBuilder()
                    .setName(name)
                    .setUsername(crmUserProfile.getUsername())
                    .setFirstName(crmUserProfile.getFirstName())
                    .setLastName(crmUserProfile.getLastName())
                    .build();
        }

        @Override
        public void updateStatus(CustomerServiceOperations customerServiceOperations, String message) {
            log.info(String.format("calling updateStatus(CustomerServiceOperations customerServiceOperations, " +
                    "String message) with the status '%s', but this method is a no-op!", message));
        }
    }

    // compare to TwitterServiceProvider
    public static class CrmServiceProvider extends AbstractOAuth2ServiceProvider<CustomerServiceOperations> {

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

    // todo
    public static class CustomerServiceTemplate extends AbstractOAuth2ApiBinding implements CustomerServiceOperations {

        private static Log log = LogFactory.getLog(Main.class.getName());
        // todo either the request params become constants or the urls become variables, but they should be the same thing !
        private final String USER_COLLECTION_URL = "/api/users";
        private final String USER_COLLECTION_USERNAMES_URL = USER_COLLECTION_URL + "/usernames";
        private final String USER_COLLECTION_ENTRY_URL = USER_COLLECTION_URL + "/{userId}";
        private final String USER_COLLECTION_ENTRY_PHOTO_URL = USER_COLLECTION_ENTRY_URL + "/photo"; // todo use this when handling setConnectionValues above.
        private final String requestParamLastName = "lastName",
                requestParamFirstName = "firstName",
                requestParamUserId = "userId",
                requestParamCustomerId = "customerId";
        private final String CUSTOMER_COLLECTION_URL = "/api/crm/{userId}/customers";
        private final String CUSTOMER_COLLECTION_ENTRY_URL = CUSTOMER_COLLECTION_URL + "/{customerId}";
        private final String CUSTOMER_SEARCH = "/api/crm/search";
        private final String slash = "/";
        private String baseServiceUrl  ;  // todo

        public CustomerServiceTemplate(String baseServiceUrl, String accessToken) {
            super(accessToken);
            setBaseServiceUrl(baseServiceUrl);
            setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory()));
        }

        // todo does this one work? or do i need to break apart the fields of the customer record into individual form fields or something?
        // todo look into the RestTemplate#post methods and, more importantly, look at how to dot his elsewhere
        // todo look also at the update version of this method #updateCustomer as it faces the same problem.
        @Override
        public Customer createCustomer(Long userId, String firstName, String lastName, Date signupDate) {
            // http://stackoverflow.com/questions/8297215/spring-resttemplate-get-with-parameters

            String url = UriComponentsBuilder.fromHttpUrl(urlForPath(CUSTOMER_COLLECTION_URL))
                    .queryParam(requestParamFirstName, firstName)
                    .queryParam(requestParamLastName, lastName)
                    .queryParam(requestParamUserId, userId)
                    .build()
                    .toUriString();

            Map<String, Long> vars = Collections.singletonMap(requestParamUserId, userId);
            return null;
            // return extractResponse(customerResponseEntity);
        }

        /**
         * searches customer information for the given, authenticated user.
         *
         * @param searchQuery a string to be used in searching a user's customer data.
         */
        @Override
        public Collection<Customer> searchCustomers(String searchQuery) {
            String url = urlForPath(CUSTOMER_SEARCH);
            String uriWithVariables = UriComponentsBuilder.fromUriString(url)
                    .queryParam("q", searchQuery)
                    .build()
                    .toUriString();
            return getRestTemplate().getForObject(uriWithVariables, CustomerList.class);
        }

        @Override
        public List<Customer> getAllUserCustomers(Long userid) {
            String url = urlForPath(CUSTOMER_COLLECTION_URL);
            return getRestTemplate().getForObject(url, CustomerList.class, Collections.singletonMap(requestParamUserId, userid));
        }

        @Override
        public void deleteCustomer(Long customerId) {
            String url = urlForPath(CUSTOMER_COLLECTION_ENTRY_URL);
            Map<String, Long> customerIdMap = Collections.singletonMap(requestParamCustomerId, customerId);
            getRestTemplate().delete(url, customerIdMap);
        }

        @Override
        public void updateCustomer(Long id, String fn, String ln, Date birthday) {
            String url = urlForPath(CUSTOMER_COLLECTION_ENTRY_URL);
            getRestTemplate().put(url, Collections.singletonMap(requestParamCustomerId, id));
        }

        private String urlForPath(final String p) {
            String inputPath = p;
            if (inputPath.startsWith(slash))
                inputPath = inputPath.substring(1);
            String wholeUrl = this.baseServiceUrl + inputPath;
            log.debug("the whole URL is " + wholeUrl);
            return wholeUrl;
        }

        // a class actually retains generics information.
        //
        private void setBaseServiceUrl(String url) {
            if (!url.endsWith(slash))
                url = url + slash;
            this.baseServiceUrl = url;
        }

        @Override
        public CrmUserProfile currentUser() {

            String url = urlForPath(USER_COLLECTION_ENTRY_URL);
            return getRestTemplate().getForObject(url, CrmUserProfile.class);
        }

        // hack so that we can retain the generic type information for jackson at runtime.
        //
        private static class CustomerList extends ArrayList<Customer> {
        }
    }

    public static class CrmUserProfile implements Serializable {
        private String firstName, lastName, username;
        private long id;

        public CrmUserProfile(String f, String l, String user) {
            this.firstName = f;
            this.lastName = l;
            this.username = user;
        }

        public CrmUserProfile(long id, String f, String l, String user) {
            this(f, l, user);
            this.id = id;
        }

        public long getId() {
            return this.id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class Customer implements Serializable {
        private String firstName, lastName;
        private long id;

        public Customer(long id, String f, String l) {
            this.firstName = f;
            this.lastName = l;
            this.id = id;
        }

        public Customer(String f, String l) {
            this.firstName = f;
            this.lastName = l;
        }

        public long getId() {
            return this.id;
        }

        public String getLastName() {
            return this.lastName;
        }

        public String getFirstName() {
            return this.firstName;
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

        @Bean
        public CrmServiceProvider crmServiceProvider(Environment environment) {

            final String propertyNameRoot = "sscrm",
                    slash = "/",
                    http = "http://";

            String clientId = environment.getProperty(propertyNameRoot + ".client-id"),
                    clientSecret = environment.getProperty(propertyNameRoot + ".client-secret");

            String baseUrl = environment.getProperty(propertyNameRoot + ".base-url"),
                    authorizeUrl = environment.getProperty(propertyNameRoot + ".authorize-url"),
                    accessTokenUrl = environment.getProperty(propertyNameRoot + ".access-token-url");

            log.debug(String.format("baseUrl=%s, clientSecret=%s, consumerSecret=%s, authorizeUrl=%s, accessTokenUrl=%s",
                    baseUrl, clientId, clientSecret, authorizeUrl, accessTokenUrl));

            assert baseUrl != null && baseUrl.length() > 0 : "the baseUrl can't be null!";

            if (!baseUrl.endsWith(slash)) baseUrl = baseUrl + slash;

            if (!authorizeUrl.toLowerCase().startsWith(http)) authorizeUrl = baseUrl + authorizeUrl;

            if (!accessTokenUrl.toLowerCase().startsWith(http)) accessTokenUrl = baseUrl + accessTokenUrl;

            return new CrmServiceProvider(baseUrl, clientId, clientSecret, authorizeUrl, accessTokenUrl);
        }

        @Bean
        public JdbcUsersConnectionRepository jdbcUsersConnectionRepository(DataSource dataSource, ConnectionFactoryLocator locator) {
            return new JdbcUsersConnectionRepository(dataSource, locator, Encryptors.noOpText());
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public CrmApiAdapter crmApiAdapter() {
            return new CrmApiAdapter();
        }

        @Bean
        public CrmConnectionFactory crmConnectionFactory(CrmServiceProvider crmServiceProvider,
                                                         CrmApiAdapter crmApiAdapter) {
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
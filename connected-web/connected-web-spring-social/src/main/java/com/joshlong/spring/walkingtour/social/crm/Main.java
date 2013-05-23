package com.joshlong.spring.walkingtour.social.crm;

import org.springframework.social.connect.*;
import org.springframework.social.connect.support.*;
import org.springframework.social.oauth2.*;
import org.springframework.social.support.ClientHttpRequestFactorySelector;

import java.io.Serializable;
import java.util.*;

/**
 * @author Josh Long
 */
public class Main {

    public static void main(String args[]) throws Throwable {

        // todo i need these ever so important values !!
        String clientId = "crm-android";
        String consumerSecret = "secret";
        String authorizeUrl = null,
                authenticateUrl = null,
                accessTokenUrl = null;


        CrmServiceProvider crmServiceProvider = new CrmServiceProvider(clientId, consumerSecret, authorizeUrl, authenticateUrl, accessTokenUrl);

        CrmApiAdapter crmApiAdapter = new CrmApiAdapter();

        CrmConnectionFactory clientConnectionFactory = new CrmConnectionFactory(crmServiceProvider, crmApiAdapter);

        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        connectionFactoryRegistry.addConnectionFactory(clientConnectionFactory);


        // now we setup Spring Social generical infrastructure
        // set up the database and encryption


    }

    public static interface CustomerServiceOperations {

        Customer createCustomer(Long userId, String firstName, String lastName, Date signupDate);

        Collection<Customer> search(String name);

        List<Customer> getAllUserCustomers(Long userid);

        void deleteCustomer(Long id);

        void updateCustomer(Long id, String fn, String ln, Date birthday);
    }

    public static class CrmConnectionFactory extends OAuth2ConnectionFactory<CustomerServiceOperations> {

        public final static String PROVIDER_ID = "crm";

        public CrmConnectionFactory(CrmServiceProvider serviceProvider, CrmApiAdapter apiAdapter) {
            super(PROVIDER_ID, serviceProvider, apiAdapter);
        }
    }

    /// compare to TwitterAdapter
    public static class CrmApiAdapter implements ApiAdapter<CustomerServiceOperations> {
        @Override
        public UserProfile fetchUserProfile(CustomerServiceOperations api) {
            return null;
        }

        @Override
        public boolean test(CustomerServiceOperations api) {
            return false;
        }

        @Override
        public void setConnectionValues(CustomerServiceOperations api, ConnectionValues values) {
        }

        @Override
        public void updateStatus(CustomerServiceOperations api, String message) {
        }
    }

    // compare to TwitterServiceProvider
    public static class CrmServiceProvider extends AbstractOAuth2ServiceProvider<CustomerServiceOperations> {

        public CrmServiceProvider(String clientId, String consumerSecret, String authorizeUrl, String authenticateUrl, String accessTokenUrl) {
            super(new OAuth2Template(clientId, consumerSecret, authorizeUrl, authenticateUrl, accessTokenUrl));
        }

        @Override
        public CustomerServiceOperations getApi(String accessToken) {
            return new CustomerServiceTemplate(accessToken);
        }


    }

    public static class CustomerServiceTemplate
            extends AbstractOAuth2ApiBinding
            implements CustomerServiceOperations {

        private final String CUSTOMER_COLLECTION_URL = "/api/crm/{userId}/customers";
        private final String CUSTOMER_COLLECTION_ENTRY_URL = CUSTOMER_COLLECTION_URL + "/{customerId}";
        private final String CUSTOMER_SEARCH = "/api/crm/search";

        public CustomerServiceTemplate() {
            initialize();
        }

        public CustomerServiceTemplate(String accessToken) {
            super(accessToken);
            initialize();
        }

        private void initialize() {
            setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory()));
        }

        @Override
        public Customer createCustomer(Long userId, String firstName, String lastName, Date signupDate) {
            return null;
        }

        @Override
        public Collection<Customer> search(String name) {
            return null;
        }

        @Override
        public List<Customer> getAllUserCustomers(Long userid) {
            return null;
        }

        @Override
        public void deleteCustomer(Long id) {
        }

        @Override
        public void updateCustomer(Long id, String fn, String ln, Date birthday) {
        }
    }

    /**
     * client side representation of the data on the server
     * for the signed in user.
     */
    public static class CrmUserProfile implements Serializable {
        private String firstName, lastName, username;

        public CrmUserProfile(String fr, String l, String user) {
            this.firstName = fr;
            this.lastName = l;
            this.username = user;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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
}

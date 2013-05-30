package com.joshlong.spring.walkingtour.social.crm;


import com.joshlong.spring.walkingtour.social.crm.omgthehorror.Main;
import org.apache.commons.logging.*;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.util.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

// todo
public class CustomerServiceTemplate extends AbstractOAuth2ApiBinding implements CustomerServiceOperations {

    private static Log log = LogFactory.getLog(Main.class.getName());
    // todo either the request params become constants or the urls become variables, but they should be the same thing !
    private final String USER_COLLECTION_URL = "/api/users";
    private final String USER_COLLECTION_USERNAMES_URL = USER_COLLECTION_URL + "/usernames";
    private final String USER_CURRENT_USER_URL = USER_COLLECTION_URL + "/self";
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
    private String baseServiceUrl;  // todo

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
        String createCustomerUrl = urlForPath( "/api/crm/{userId}/customers");
        URI uri = UriComponentsBuilder.fromHttpUrl(createCustomerUrl)
                .queryParam(requestParamFirstName, firstName)
                .queryParam(requestParamLastName, lastName)
                .buildAndExpand(Collections.singletonMap("userId", userId))
                .toUri();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add(requestParamFirstName, firstName);
        headers.add(requestParamLastName, lastName);
        URI resultingCustomerUri = getRestTemplate().postForLocation(uri, headers);
        return this.getRestTemplate().getForEntity(resultingCustomerUri, Customer.class).getBody();
    }

    @Override
    public User getUserById(Long id) {
        String url = null  ;
      return getRestTemplate().getForObject(url, User.class, id) ;
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

    // need this in other clases
    String urlForPath(final String p) {
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
    public User currentUser() {
        String url = urlForPath(USER_CURRENT_USER_URL);
        return getRestTemplate().getForObject(url, User.class);
    }

    // hack so that we can retain the generic type information for jackson at runtime.
    //
    private static class CustomerList extends ArrayList<Customer> {
    }
}
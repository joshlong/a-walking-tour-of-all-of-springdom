package com.joshlong.spring.walkingtour.android.service;

import android.util.Log;
import com.joshlong.spring.walkingtour.android.model.Customer;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class CustomerServiceClient implements CustomerService {

    private String baseServiceUrl;
    private RestTemplate restTemplate;

    public CustomerServiceClient(String url, RestTemplate restTemplate) {
        setBaseServiceUrl(url);
        setRestTemplate(restTemplate);
    }

    public CustomerServiceClient(String url) {
        this(url, new RestTemplate());
    }

    public void setRestTemplate(RestTemplate rt) {
        assert rt != null : "you must provide a non-null value for a 'RestTemplate' instance!";
        this.restTemplate = rt;
        List<HttpMessageConverter<?>> messageConverterList = rt.getMessageConverters();
        if ((messageConverterList == null || messageConverterList.size() == 0) || !containsObjectOfType(rt.getMessageConverters(), MappingJacksonHttpMessageConverter.class))
            rt.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    }

    private boolean containsObjectOfType(Collection<?> tCollection, Class<?> tClass) {
        for (Object object : tCollection)
            if (object.getClass().isAssignableFrom(tClass))
                return true;
        return false;
    }

    protected void setBaseServiceUrl(String url) {
        if (!url.endsWith("/"))
            url = url + "/";
        this.baseServiceUrl = url;
    }

    private String urlForPath(final String p) {
        String inputPath = p;
        if (inputPath.startsWith("/"))
            inputPath = inputPath.substring(1);
        return this.baseServiceUrl + inputPath;
    }

    private <T> T extractResponse(final ResponseEntity<T> responseEntity) {
        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }
        return null;
    }

    @Override
    public List<Customer> loadAllCustomers() {
        String url = urlForPath("customers");
        Log.d(getClass().getName(), "url for the customers request: " + url);
        return this.restTemplate.getForObject(url, CustomerList.class);
    }

    @Override
    public Customer updateCustomer(long id, String fn, String ln) {
        String urlForPath = urlForPath("customer/{customerId}");
        Customer customer = new Customer(id, fn, ln);
        ResponseEntity<Customer> customerResponseEntity =
                restTemplate.postForEntity(urlForPath, customer, Customer.class, java.util.Collections.singletonMap("customerId", id));
        return extractResponse(customerResponseEntity);
    }

    @Override
    public Customer getCustomerById(long id) {
        return extractResponse(restTemplate.getForEntity(
                urlForPath("customer/{customerId}"), Customer.class, id));
    }

    @Override
    public Customer createCustomer(String fn, String ln) {
        return extractResponse(this.restTemplate.postForEntity(
                urlForPath("customers"), new Customer(fn, ln), Customer.class));
    }

    // allows us to bake in the generic type, `Customer`, so that
    // it's visible at runtime for the Jackson marshalling process.
    private static class CustomerList extends ArrayList<Customer> {
    }
}

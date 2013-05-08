package com.joshlong.spring.walkingtour.android.service;

import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.joshlong.spring.walkingtour.android.model.Customer;

public class CustomerServiceClient implements CustomerService {

    private String baseServiceUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public CustomerServiceClient(String url) {
        setBaseServiceUrl(url);
        restTemplate.getMessageConverters().add( new MappingJacksonHttpMessageConverter());
    }

    public void setBaseServiceUrl(String url) {
        String u = url;

        if (!u.endsWith("/"))
            u = u + "/";

        this.baseServiceUrl = u;
    }

    private String urlForPath(final String p) {
        String inputPath = p;
        if (inputPath.startsWith("/"))
            inputPath = inputPath.substring(1); // redundant since the base
        // already ends with a '/'
        return this.baseServiceUrl + inputPath;
    }

    private <T> T extractResponse(final ResponseEntity<T> responseEntity) {
        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK) ) {
            return responseEntity.getBody();
        }
        return null ;
    }

    @Override
    public Customer updateCustomer(long id, String fn, String ln) {
        String urlForPath = urlForPath("customer/{customerId}");
        this.restTemplate.postForEntity(urlForPath, new Customer(id, fn, ln), Customer.class, id);
        return this.getCustomerById(id);
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
}

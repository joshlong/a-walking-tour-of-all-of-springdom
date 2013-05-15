package com.joshlong.spring.walkingtour.android.config;


import com.joshlong.spring.walkingtour.android.*;
import com.joshlong.spring.walkingtour.android.service.*;
import com.joshlong.spring.walkingtour.android.view.WelcomeActivity;
import com.squareup.otto.Bus;
import dagger.*;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springsource.examples.sawt.web.android.R;

import javax.inject.Singleton;

@Module(
    injects = {
      WelcomeActivity.class
    }
)
public class CrmModule {
    private  Crm application;

    public CrmModule(Crm crm) {
        this.application = crm;
    }

    @Provides
    @Singleton
    @InjectCrm
    public Crm provideCrm() {
        return this.application;
    }

    //  @Provides AndroidStringUtils androidStringUtils() { return new AndroidStringUtils(); }

    @Provides
    public AndroidStringUtils provideAndroidStringUtils() {
        return new AndroidStringUtils();
    }

    @Provides
    public RestTemplate provideRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        return restTemplate;
    }

    @Provides
    public CustomerService provideCustomerService(@InjectCrm Crm context, RestTemplate restTemplate) {
        CustomerServiceClient customerServiceClient = new CustomerServiceClient(context.getString(R.string.base_uri));
         customerServiceClient.setRestTemplate(restTemplate);
        return customerServiceClient;

    }

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }
}

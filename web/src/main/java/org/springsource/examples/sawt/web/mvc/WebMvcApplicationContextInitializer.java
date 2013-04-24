package org.springsource.examples.sawt.web.mvc;


import org.springframework.context.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springsource.examples.sawt.services.jpa.JpaConfiguration;

public class WebMvcApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {
    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
        applicationContext.register(JpaConfiguration.class, WebMvcConfiguration.class);
        applicationContext.refresh();
    }
}

package com.joshlong.spring.walkingtour.services.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Main {
    public static void main(String[] args) throws Throwable {
        ApplicationContext context = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        Resource samplesResource = new ClassPathResource("/sample/a.csv");
        CustomerLoaderService customerLoaderService = context.getBean(CustomerLoaderService.class);
        customerLoaderService.loadCustomersFrom(samplesResource.getFile());
    }
}

package com.joshlong.spring.walkingtour.services.integration;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.context.annotation.*;
import com.joshlong.spring.walkingtour.services.batch.BatchConfiguration;

@Configuration
@Import(BatchConfiguration.class)
@ComponentScan
@ImportResource("/com/joshlong/spring/walkingtour/services/integration/context.xml")
public class IntegrationConfiguration {
    @Bean
    public JobLaunchingMessageHandler jobMessageHandler(JobLauncher jobLauncher) throws Exception {
        return new JobLaunchingMessageHandler(jobLauncher);
    }
}
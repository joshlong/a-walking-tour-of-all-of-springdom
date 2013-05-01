package com.joshlong.spring.walkingtour.services.integration;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.context.annotation.*;
import com.joshlong.spring.walkingtour.services.batch.BatchConfiguration;

/**
 * Simple integration configuration that also works with configuration previously
 * defined in the batch solution.
 *
 * @author Josh Long
 */
@Configuration
@Import(BatchConfiguration.class)
@ComponentScan
@ImportResource("/org/springsource/examples/sawt/services/integration/context.xml")
public class IntegrationConfiguration {
    @Bean
    public JobLaunchingMessageHandler jobMessageHandler(JobLauncher jobLauncher) throws Exception {
        return new JobLaunchingMessageHandler(jobLauncher);
    }
}
package org.springsource.examples.sawt.services.integration;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Simple integration configuration that also works with configuration previously
 * defined in the batch solution.
 *
 * @author Josh Long
 */
@Configuration("integrationConfiguration")
@ImportResource("/org/springsource/examples/sawt/services/batch/context.xml")
public class Config {

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public JobLaunchingMessageHandler jobMessageHandler() throws Exception {
        return new JobLaunchingMessageHandler(jobLauncher);
    }

}

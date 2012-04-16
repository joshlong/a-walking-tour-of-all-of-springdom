package org.springsource.examples.sawt.web.rest.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springsource.examples.sawt.services.jpa.JpaConfiguration;

@EnableWebMvc
@Import(JpaConfiguration.class)
@Configuration
public class WebConfig {
}
package org.springsource.examples.sawt.web.rest.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springsource.examples.sawt.services.jdbc.Config;

@EnableWebMvc
@Import(Config.class)
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

}
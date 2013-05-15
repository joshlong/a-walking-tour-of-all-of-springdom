package org.springsource.examples.sawt.web.flex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springsource.examples.sawt.services.jdbc.Config;

@EnableWebMvc
@Configuration
@Import(Config.class)
public class FlexConfig extends WebMvcConfigurerAdapter {

    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver i = new InternalResourceViewResolver();
        i.setViewClass(JstlView.class);
        i.setPrefix("/WEB-INF/jsp/");
        i.setSuffix(".jsp");
        return i;
    }
}

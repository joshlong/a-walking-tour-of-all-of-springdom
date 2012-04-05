package org.springsource.examples.sawt.web.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springsource.examples.sawt.services.jdbc.Config;

@Configuration
@EnableWebMvc
@Import(Config.class)
public class WebConfig  extends WebMvcConfigurerAdapter{

    @Bean
    public UrlBasedViewResolver resolver() {
        UrlBasedViewResolver url = new UrlBasedViewResolver();
        url.setPrefix("views/");
        url.setViewClass(JstlView.class);
        url.setSuffix(".jsp");
        return url;
    }

    @Override
    public void configureViewControllers(ViewControllerConfigurer configurer) {
//        <mvc:view-controller path="/" view-name="welcome"/>
        configurer.mapViewName("/", "welcome") ;

    }
}

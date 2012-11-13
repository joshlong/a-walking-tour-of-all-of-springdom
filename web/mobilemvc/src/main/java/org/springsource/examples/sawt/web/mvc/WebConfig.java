package org.springsource.examples.sawt.web.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;
import org.springsource.examples.sawt.services.jpa.JpaConfiguration;
import org.springsource.examples.sawt.web.util.CloudFoundryAwareFullyQualifiedApplicationUrlResolver;

// http://localhost:8080/mobilemvc/display?id=23
@Configuration
@EnableWebMvc
@Import(JpaConfiguration.class)
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(TilesView.class);
        return viewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer configurer = new TilesConfigurer();
        configurer.setDefinitions(new String[]{
                "/WEB-INF/layouts/tiles.xml",
                "/WEB-INF/views/**/tiles.xml"
        });
        configurer.setCheckRefresh(true);
        return configurer;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
     configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println( "configuring resource handlers");

        for (String r : "js,css".split(","))
            registry.addResourceHandler("/" + r + "/**").addResourceLocations("/" + r + "/");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CloudFoundryAwareFullyQualifiedApplicationUrlResolver());
        registry.addInterceptor(new DeviceResolverHandlerInterceptor());
    }

}

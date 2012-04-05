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
import org.springsource.examples.sawt.services.jdbc.Config;


@Configuration
@EnableWebMvc
@Import(Config.class)
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(TilesView.class);
        return viewResolver;
    }

    @Override
    public void configureResourceHandling(ResourceConfigurer configurer) {
        configurer.addPathMapping("/js/**")
                .addResourceLocation("/js/");
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
    public void configureInterceptors(InterceptorConfigurer configurer) {
        configurer.addInterceptor(new DeviceResolverHandlerInterceptor());
    }
}

package com.joshlong.spring.walkingtour.mobileweb;

import com.joshlong.spring.walkingtour.services.jdbc.JdbcConfiguration;
import org.apache.commons.logging.*;
import org.springframework.context.annotation.*;
import org.springframework.mobile.device.*;
import org.springframework.mobile.device.site.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.*;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan
@Import(JdbcConfiguration.class)
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    private Log log = LogFactory.getLog(getClass());

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
        log.debug("configuring resource handlers...");

        for (String r : "js,css".split(","))
            registry.addResourceHandler("/" + r + "/**").addResourceLocations("/" + r + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DeviceResolverHandlerInterceptor());
        registry.addInterceptor(new SitePreferenceHandlerInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
        argumentResolvers.add(new SitePreferenceHandlerMethodArgumentResolver());
    }
}

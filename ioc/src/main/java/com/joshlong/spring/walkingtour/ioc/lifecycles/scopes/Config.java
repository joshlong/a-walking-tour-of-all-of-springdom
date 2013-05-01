package com.joshlong.spring.walkingtour.ioc.lifecycles.scopes;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {

    @Bean
    public CustomScopeConfigurer configurer() {
        CustomScopeConfigurer scopeConfigurer = new CustomScopeConfigurer();

        Map<String, Object> scopeMap = new HashMap<String, Object>();
        scopeMap.put("thread", scope());

        scopeConfigurer.setScopes(scopeMap);
        return scopeConfigurer;
    }
    @Bean
    public SimpleThreadScope scope() {
        return new SimpleThreadScope();
    }

    @Bean
    @org.springframework.context.annotation.Scope("thread")
    public ThreadAnnouncer announcer() {
        ThreadAnnouncer announcer = new ThreadAnnouncer();
        return announcer;
    }



    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        return executor;
    }

}

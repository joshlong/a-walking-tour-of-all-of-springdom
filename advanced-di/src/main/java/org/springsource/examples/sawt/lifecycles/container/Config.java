package org.springsource.examples.sawt.lifecycles.container;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springsource.examples.sawt.lifecycles.container.impl.CorporationCodeStandardsAwareBeanFactoryPostProcessor;

@Configuration
public class Config {

    @Bean
    public CorporationCodeStandardsAwareBeanFactoryPostProcessor corporationCodeStandardsAwareBeanFactoryPostProcessor() {
        return new CorporationCodeStandardsAwareBeanFactoryPostProcessor();
    }
}

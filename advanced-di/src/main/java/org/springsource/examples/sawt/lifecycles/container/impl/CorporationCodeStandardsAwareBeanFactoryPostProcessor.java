package org.springsource.examples.sawt.lifecycles.container.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * registers a few useful {@link BeanFactoryPostProcessor}s if they don't already exist.
 */
public class CorporationCodeStandardsAwareBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private Log log = LogFactory.getLog(getClass());

    private boolean beanExists(ConfigurableListableBeanFactory beanFactory, String beanName, Class<?> clz) {
        return beanFactory.containsBean(beanName) || beanFactory.getBeansOfType(clz).size() > 0;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        String wellKnownStartupDateBPP = "startupDatePostProcessor";
        if (!beanExists(beanFactory, wellKnownStartupDateBPP, StartupAwareBeanPostProcessor.class)) {
            beanFactory.addBeanPostProcessor(new StartupAwareBeanPostProcessor());
            log.info("no default " + StartupAwareBeanPostProcessor.class.getName() + ", registering one.");
        }

        String wellKnownToStringBPP = "toStringBeanPostProcessor";
        if (!beanExists(beanFactory, wellKnownToStringBPP, AutoToStringBeanPostProcessor.class)) {
            beanFactory.addBeanPostProcessor(new AutoToStringBeanPostProcessor());
            log.info("no default " + AutoToStringBeanPostProcessor.class.getName() + ", registering one.");
        }
    }
}

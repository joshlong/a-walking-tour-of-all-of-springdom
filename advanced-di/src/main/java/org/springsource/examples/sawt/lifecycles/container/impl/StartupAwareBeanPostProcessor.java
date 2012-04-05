package org.springsource.examples.sawt.lifecycles.container.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * automatically sets the startup date for the beans in the container that have the marker interface {@link StartupDateAware}.
 * <p/>
 * the next logical step is to start <em>wrapping</em> the objects using proxies...
 */
public class StartupAwareBeanPostProcessor implements BeanPostProcessor {

    private Log log = LogFactory.getLog(getClass().getName());

    private Date date = new Date();

    private String[] startupDateProperties = {"startupDate"};

    public void setStartupDateProperties(String[] startupDateProperties) {
        this.startupDateProperties = startupDateProperties;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof StartupDateAware) {
            ((StartupDateAware) bean).setStartupDate(date);
        }
        if (startupDateProperties != null && startupDateProperties.length > 0)
            invokeStartupDateProperties(bean, beanName);

        return bean;
    }

    private void invokeStartupDateProperties(Object bean, String beanName) throws BeansException {
        String startDateProperty = null;
        try {

            Class clz = bean.getClass();
            for (String sd : startupDateProperties) {
                startDateProperty = sd;
                Method m = ClassUtils.getMethodIfAvailable(clz, sd, Date.class);
                if (m != null) {
                    m.invoke(bean, date);
                    log.info(String.format("invoked start date property '%s' on bean '%s'", startDateProperty, beanName));
                }
            }
        } catch (Exception e) {
            throw new BeanInitializationException(String.format("encountered trouble invoking " +
                    "the start date property '%s'", startDateProperty != null ? startDateProperty : ""), e);
        }
    }
}

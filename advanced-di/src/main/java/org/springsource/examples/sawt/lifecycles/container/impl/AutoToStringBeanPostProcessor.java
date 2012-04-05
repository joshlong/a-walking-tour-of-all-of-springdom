package org.springsource.examples.sawt.lifecycles.container.impl;

import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springsource.examples.sawt.factories.AutoToStringFactoryBean;

public class AutoToStringBeanPostProcessor extends ProxyConfig implements BeanPostProcessor {

    private ToStringStyle toStringStyle = ToStringStyle.MULTI_LINE_STYLE;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AutoToStringRequired && !(bean instanceof AopInfrastructureBean)) {
            try {
                return proxy(bean);
            } catch (Exception e) {
                log.debug("exception occurred when trying to proxy the bean", e);
            }
        }
        return bean;
    }

    @SuppressWarnings("unused")
    public void setToStringStyle(ToStringStyle toStringStyle) {
        this.toStringStyle = toStringStyle;
    }

    private Object proxy(Object bean) throws Exception {

        AutoToStringFactoryBean autoToStringFactoryBean =
                new AutoToStringFactoryBean();
        autoToStringFactoryBean.setToStringStyle(this.toStringStyle);
        autoToStringFactoryBean.setTarget(bean);
        return autoToStringFactoryBean.getObject();
    }
}

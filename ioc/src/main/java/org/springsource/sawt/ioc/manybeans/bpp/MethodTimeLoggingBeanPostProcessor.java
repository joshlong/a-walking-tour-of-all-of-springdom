package org.springsource.sawt.ioc.manybeans.bpp;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springsource.sawt.ioc.manybeans.Loggable;

/**
 * Simple BPP that automatically adds logging functionality to all objects that implement
 * {@link org.springsource.sawt.ioc.manybeans.Loggable}
 */
public class MethodTimeLoggingBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return loggableObject(bean);
    }

    @SuppressWarnings("unchecked")
    private <T> T loggableObject(T o) {
        ProxyFactory factory = new ProxyFactory();
        factory.addAdvice(new TimeLoggingMethodInterceptor());
        factory.setTarget(o);
        return (T) factory.getProxy();
    }


    /**
     * logs the method invocation times
     */
    private class TimeLoggingMethodInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {

            Object result = null;
            if (invocation.getMethod().getAnnotation(Loggable.class) != null) {
                long start = System.currentTimeMillis();
                result = invocation.proceed();
                long stop = System.currentTimeMillis();
                System.out.println(invocation.getMethod().getName() + ": " + (stop - start) + "ms");
            } else {
                result = invocation.proceed();
            }
            return result;
        }
    }
}

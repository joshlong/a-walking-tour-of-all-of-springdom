package org.springsource.examples.sawt.factories;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ClassUtils;

/**
 * this class will proxy whatever is given to it for the 'target' and add a
 * toString method built by Jakarta Commons'
 * {@link org.apache.commons.lang.builder.ToStringBuilder}
 */
public class AutoToStringFactoryBean extends ProxyConfig implements FactoryBean {

    private Object target;
    private ToStringStyle toStringStyle = ToStringStyle.MULTI_LINE_STYLE;

    private Object proxyObjectAndProvideToStringMethod(Object bean) {
        if (bean instanceof Advised) {
            ((Advised) bean).addAdvice(toStringMethodInterceptor);
        } else {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.copyFrom(this);
            proxyFactory.addAdvice(toStringMethodInterceptor);
            proxyFactory.setProxyTargetClass(true);
            return proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
        }
        return bean;
    }

    @SuppressWarnings("unused")
    public void setToStringStyle(ToStringStyle toStringStyle) {
        this.toStringStyle = toStringStyle;
    }

    private MethodInterceptor toStringMethodInterceptor = new MethodInterceptor() {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {

            String mName = invocation.getMethod().getName();
            if (!mName.equals("toString")) {
                Object thisPtr = invocation.getThis();
                Object[] args = invocation.getArguments();
                return invocation.getMethod().invoke(thisPtr, args);
            } else {
                return ToStringBuilder.reflectionToString(invocation.getThis(), toStringStyle);
            }
        }
    };

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object getObject() throws Exception {
        return proxyObjectAndProvideToStringMethod(this.target);
    }

    @Override
    public Class<?> getObjectType() {
        return target.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

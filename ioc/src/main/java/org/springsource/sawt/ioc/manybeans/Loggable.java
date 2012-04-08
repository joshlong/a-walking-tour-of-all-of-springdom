package org.springsource.sawt.ioc.manybeans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to tell the {@link org.springsource.sawt.ioc.manybeans.bpp.MethodTimeLoggingBeanPostProcessor}
 * that methods should be logged
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {
}

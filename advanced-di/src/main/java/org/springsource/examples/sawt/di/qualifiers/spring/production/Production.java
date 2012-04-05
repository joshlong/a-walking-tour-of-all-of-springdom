package org.springsource.examples.sawt.di.qualifiers.spring.production;

import javax.inject.Qualifier;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Production {
}

package org.springsource.examples.sawt.di.qualifiers.inject.test;

import javax.inject.Qualifier;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Test {
}

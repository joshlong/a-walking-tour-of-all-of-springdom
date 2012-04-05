package org.springsource.examples.sawt.di.qualifiers.spring.test;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Test {
}

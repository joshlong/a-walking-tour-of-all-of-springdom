package com.joshlong.spring.walkingtour.android.config;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
public @interface InjectAndroidApplicationContext {
}
package org.springsource.sawt.ioc.manybeans.aop;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springsource.sawt.ioc.manybeans.Cat;
import org.springsource.sawt.ioc.manybeans.Dog;

@Configuration
public class AnimalFarmConfig {
    @Bean
    public Dog dog() {
        return new Dog();
    }

    @Bean
    public Cat cat() {
        return new Cat();
    }

    // once per context no matter how many AOP aspects  you create
    // equivalent to aop:aspectj-autoproxy
    @Bean
    public AnnotationAwareAspectJAutoProxyCreator aop() {
        return new AnnotationAwareAspectJAutoProxyCreator();
    }

  @Bean
    public MethodTimeLoggingAspect aspect() {
        return new MethodTimeLoggingAspect();
    }
}

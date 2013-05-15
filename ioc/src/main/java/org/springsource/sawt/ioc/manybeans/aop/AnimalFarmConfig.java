package org.springsource.sawt.ioc.manybeans.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springsource.sawt.ioc.manybeans.Cat;
import org.springsource.sawt.ioc.manybeans.Dog;

@Configuration
@EnableAspectJAutoProxy
public class AnimalFarmConfig {
    @Bean
    public Dog dog() {
        return new Dog();
    }

    @Bean
    public Cat cat() {
        return new Cat();
    }

    @Bean
    public MethodTimeLoggingAspect aspect() {
        return new MethodTimeLoggingAspect();
    }
}

package org.springsource.sawt.ioc.manybeans.bfpp;

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

    // NB it's STATIC!
    @Bean
    static public SoxComplianceSuite complianceSuite() throws Throwable {
        return new SoxComplianceSuite();
    }


}

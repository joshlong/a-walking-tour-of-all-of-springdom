package org.springsource.sawt.ioc.manybeans.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springsource.sawt.ioc.manybeans.Cat;
import org.springsource.sawt.ioc.manybeans.Dog;

public class Main {
    public static void main(String[] args) throws Throwable {

        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext(AnimalFarmConfig.class);

        Dog dog = annotationConfigApplicationContext.getBean(Dog.class);
        dog.bark();

        Cat cat = annotationConfigApplicationContext.getBean(Cat.class);
        cat.meow();
    }
}

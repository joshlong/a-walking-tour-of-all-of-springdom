package com.joshlong.spring.walkingtour.ioc.manybeans.bpp;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.joshlong.spring.walkingtour.ioc.manybeans.Cat;
import com.joshlong.spring.walkingtour.ioc.manybeans.Dog;

public class Main {

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnimalFarmConfig.class);

        Cat cat = annotationConfigApplicationContext.getBean(Cat.class);
        cat.meow();

        Dog dog = annotationConfigApplicationContext.getBean(Dog.class);
        dog.bark();
    }

}

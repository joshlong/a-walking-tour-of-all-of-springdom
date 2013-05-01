package com.joshlong.spring.walkingtour.ioc.basicioc.qualifiers;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AmazonBookShop implements BookShop {

    @PostConstruct
    public void setUp() {
        System.out.println("starting the " + getClass().getName() + ".");
    }

    @Override
    public long sell(String isbn) {
        return 0;
    }

    @Override
    public long buy(String isbn) {
        return 0;
    }
}

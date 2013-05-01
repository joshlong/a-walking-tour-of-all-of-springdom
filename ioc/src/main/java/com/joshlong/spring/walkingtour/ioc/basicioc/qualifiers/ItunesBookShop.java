package com.joshlong.spring.walkingtour.ioc.basicioc.qualifiers;

import javax.annotation.PostConstruct;

public class ItunesBookShop implements BookShop {
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

package com.joshlong.spring.walkingtour.ioc.basicioc.qualifiers;

public interface BookShop {

    long sell(String isbn);

    long buy(String isbn);

}

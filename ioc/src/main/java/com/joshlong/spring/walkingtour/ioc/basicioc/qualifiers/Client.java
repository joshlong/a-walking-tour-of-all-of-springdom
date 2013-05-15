package com.joshlong.spring.walkingtour.ioc.basicioc.qualifiers;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.joshlong.spring.walkingtour.ioc.basicioc.qualifiers.annotation.AndroidStore;
import com.joshlong.spring.walkingtour.ioc.basicioc.qualifiers.annotation.IOsStore;

import javax.annotation.PostConstruct;

public class Client {



    @Autowired
    @AndroidStore
    private BookShop androidByQualifierAnnotation;

    @Autowired
    @IOsStore
    private BookShop iosByQualifierAnnotation;

    @Autowired
    @Qualifier("ios")
    private BookShop iosBookshopByName;

    @Autowired
    @Qualifier("android")
    private BookShop androidBookshopByName;

    @PostConstruct
    public void start() throws Throwable {
        System.out.println("android store by name: " + ToStringBuilder.reflectionToString(this.androidBookshopByName));
        System.out.println("ios store by name: " + ToStringBuilder.reflectionToString(this.iosBookshopByName));

        System.out.println("android store by qualifier annotation: " + ToStringBuilder.reflectionToString(this.androidByQualifierAnnotation));
        System.out.println("ios store by qualifier annotation: " + ToStringBuilder.reflectionToString(this.iosByQualifierAnnotation));
    }
}

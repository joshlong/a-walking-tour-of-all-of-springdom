package com.joshlong.spring.walkingtour.ioc.basicioc.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String args[]) throws Throwable {
        String path = packageToFolders(Main.class) + "/context.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(path);
        CustomerService customerService = applicationContext.getBean(CustomerService.class);
        assert null != customerService : "the customerService reference can't be null!";
    }

    private static String packageToFolders(Class<?> clazzWhosePackageWeWant) {
        Package aPackage = clazzWhosePackageWeWant.getPackage();
        String packageName = aPackage.getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('/');

        for (char c : packageName.toCharArray())
            stringBuilder.append(c == '.' ? '/' : c);

        return stringBuilder.toString().trim();
    }
}

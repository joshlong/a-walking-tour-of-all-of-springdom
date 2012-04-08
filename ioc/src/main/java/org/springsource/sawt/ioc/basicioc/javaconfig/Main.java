package org.springsource.sawt.ioc.basicioc.javaconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
 public static void main(String [] args) throws Throwable {
     AnnotationConfigApplicationContext annotationConfigApplicationContext
             = new AnnotationConfigApplicationContext(Config.class) ;

 }
}

package org.springsource.sawt.ioc.basicioc.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
 static public void main (String args []) throws Throwable {

      ApplicationContext applicationContext =
     new ClassPathXmlApplicationContext("/org/springsource/sawt/ioc/basicioc/xml/context.xml") ;
 }
}

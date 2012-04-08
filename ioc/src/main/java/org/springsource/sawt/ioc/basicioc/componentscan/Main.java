package org.springsource.sawt.ioc.basicioc.componentscan;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	static public void main(String[] args) throws Throwable {
		String pkgName = Config.class.getPackage().getName();
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(pkgName);
		ac.registerShutdownHook() ;
	}
}

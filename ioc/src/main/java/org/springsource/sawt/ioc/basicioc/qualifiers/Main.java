package org.springsource.sawt.ioc.basicioc.qualifiers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	static public void main(String args[]) throws Throwable {
		AnnotationConfigApplicationContext ac  = 
			new AnnotationConfigApplicationContext( Config.class);
		ac.registerShutdownHook() ;
	}
}

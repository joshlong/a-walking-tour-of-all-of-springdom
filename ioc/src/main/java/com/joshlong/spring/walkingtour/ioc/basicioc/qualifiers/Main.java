package com.joshlong.spring.walkingtour.ioc.basicioc.qualifiers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	public static void main(String args[]) throws Throwable {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class);
		ac.registerShutdownHook();
	}
}

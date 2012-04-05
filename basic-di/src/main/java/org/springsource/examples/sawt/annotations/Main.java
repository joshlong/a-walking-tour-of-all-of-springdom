package org.springsource.examples.sawt.annotations;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * All except the DS is inferred.
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(Main.class.getPackage().getName());

    }
}

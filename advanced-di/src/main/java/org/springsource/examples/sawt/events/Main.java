package org.springsource.examples.sawt.events;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {
    public static void main(String[] ar) throws Throwable {
        new GenericXmlApplicationContext("/org/springsource/examples/sawt/events/context.xml");
    }
}

package org.springsource.examples.sawt.lifecycles.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * this might be a controller or a JSF managed bean or some object that interacts with stateful, scoped objects. this might typically be in
 * the web tier or in some other scope. For now, we'll simulate the effect using prototype beans.
 */
@Component
public class Client {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    private Provider<ShoppingCart> cart;

    public void begin() {
        // use @javax.inject.Provider<T> to re-obtain new instances
        log.info("starting client. the user is " + cart.get().getUser().toString());
        log.info("starting client. the user is " + cart.get().getUser().toString());
        log.info("starting client. the user is " + cart.get().getUser().toString());
    }
}

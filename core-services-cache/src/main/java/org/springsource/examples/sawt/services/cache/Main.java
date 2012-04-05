package org.springsource.examples.sawt.services.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class Main {

    static Log log = LogFactory.getLog(Main.class);

    static void debugCache(Cache c) {
        @SuppressWarnings("unchecked")
        Map<Object, Object> cm = (Map<Object, Object>) c.getNativeCache();
        for (Object s : cm.keySet()) {
            log.info(String.format("%s=%s", s, cm.get(s)));
        }

    }

    public static void main(String args[]) throws Throwable {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "/org/springsource/examples/sawt/services/cache/config.xml");
        applicationContext.refresh();

        CacheManager cacheManager = applicationContext.getBean(CacheManager.class);

        Cache c = cacheManager.getCache(Config.CUSTOMERS_REGION);

        CustomerService customerService = applicationContext
                .getBean(CustomerService.class);
        log.info("--------------------------------------------------------------");
        Customer customer = customerService.createCustomer("Spring", "Lover");
        debugCache(c); // nothing

        log.info("--------------------------------------------------------------");
        customerService.getCustomerById(customer.getId());
        debugCache(c); // 1 entry

        log.info("--------------------------------------------------------------");
        customerService.updateCustomer(customer.getId(), "Cache", "Lover");
        debugCache(c);

        log.info("--------------------------------------------------------------");
        log.info("retreiving the customer of ID " + customer.getId());
        customerService.getCustomerById(customer.getId());

        log.info("retreiving the customer of ID " + customer.getId());
        customerService.getCustomerById(customer.getId());

        log.info("--------------------------------------------------------------");
        debugCache(c);

    }

}

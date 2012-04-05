package org.springsource.examples.sawt.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer1 implements ApplicationListener<StockPriceEvent> {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void onApplicationEvent(StockPriceEvent event) {
        log.info(String.format("received event, the stock %s now has the price %f ", event.getSymbol(), event.getPrice()));
    }
}

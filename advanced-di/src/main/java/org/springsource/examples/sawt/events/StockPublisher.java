package org.springsource.examples.sawt.events;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The publisher can take advantage of the core Spring's support
 * for {@link org.springframework.scheduling.TaskScheduler}s and publish notifications
 * that other components in the Spring context can exploit.
 */
@Component
public class StockPublisher implements ApplicationContextAware {

    private ApplicationContext context;
    private ConcurrentHashMap<String, Float> mapOfPrices = new ConcurrentHashMap<String, Float>();
    private String[] stocks = {"VMW"};

    public void setStocks(String[] stocks) {
        this.stocks = stocks;
    }

    @PostConstruct
    public void start() throws Exception {

        Assert.notNull(this.stocks, "the stock symbols array must be non-null!");

        for (String s : this.stocks)
            this.mapOfPrices.put(s, -1F); // initialize everything
    }

    private float findLatestPriceForStock(String stockSymbol) {
        return (float) ((Math.random() * 1000)) + 90;
    }

    @Scheduled(fixedRate = 1000 * 5) // 5 seconds
    public void findStockPrice() {

        for (String symbl : stocks) {
            float price = findLatestPriceForStock(symbl);
            if (price != this.mapOfPrices.get(symbl)) {
                mapOfPrices.put(symbl, price);
                context.publishEvent(new StockPriceEvent(this.context, symbl, price));
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}

package org.springsource.examples.sawt.events;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * simple event used to communicate that a stock price has changed
 */
public class StockPriceEvent extends ApplicationContextEvent {


    private String symbol;

    private double price;

    public StockPriceEvent(ApplicationContext source, String symbol, double price) {
        super(source);
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {

        return symbol;
    }

    public double getPrice() {
        return price;
    }
}

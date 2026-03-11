package models;

import java.util.concurrent.atomic.AtomicInteger;

public class Stock {

    private String symbol;
    private AtomicInteger availableQuantity;

    public Stock(String symbol, int availableQuantity) {
        this.symbol = symbol;
        this.availableQuantity = new AtomicInteger(availableQuantity);
    }

    public String getSymbol() {
        return symbol;
    }

    public AtomicInteger getAvailableQuantity(){
        return availableQuantity;
    }
}

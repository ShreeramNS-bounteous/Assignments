package models;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Stock {

    private final String symbol;
    private final AtomicInteger availableQuantity;

    // price stored as atomic because multiple threads read it
    private final AtomicReference<Double> price;

    public Stock(String symbol, int quantity, double price) {
        this.symbol = symbol;
        this.availableQuantity = new AtomicInteger(quantity);
        this.price = new AtomicReference<>(price);
    }

    public String getSymbol() {
        return symbol;
    }

    public AtomicInteger getAvailableQuantity() {
        return availableQuantity;
    }

    public AtomicReference<Double> getPrice() {
        return price;
    }
}
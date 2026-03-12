package service;

import models.Stock;
import models.TradeRequest;
import models.TradeResult;
import models.TradeType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TradingService {

    // Shared concurrent structures
    private final ConcurrentHashMap<String, Stock> stocks;
    private final ConcurrentLinkedQueue<TradeResult> completedTrades;

    public TradingService(ConcurrentHashMap<String, Stock> stocks,
                          ConcurrentLinkedQueue<TradeResult> completedTrades) {
        this.stocks = stocks;
        this.completedTrades = completedTrades;
    }

    // Entry point for processing a trade request
    public void processTrade(TradeRequest request){

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Stock stock = stocks.get(request.getStockSymbol());

        // Ignore invalid stock symbol
        if(stock == null){
            return;
        }

        TradeType type = request.getTradeType();

        if(type == TradeType.BUY){
            processBuy(stock, request);
        } else {
            processSell(stock, request);
        }
    }

    // Handles BUY operation using CAS retry loop
    public void processBuy(Stock stock, TradeRequest request){

        int quantity = request.getQuantity();
        String user = request.getUserId();

        while (true){

            int current = stock.getAvailableQuantity().get();

            // Fail if not enough stock
            if(current < quantity){

                System.out.println(
                        user + " BUY " +
                                stock.getSymbol() + " " +
                                quantity +
                                " FAILED insufficient stock"
                );

                return;
            }

            int newQuantity = current - quantity;

            // Atomic update
            if(stock.getAvailableQuantity().compareAndSet(current, newQuantity)){
                recordTrade(stock, request);
                return;
            }

            Thread.onSpinWait(); // hint for CPU during retry
        }
    }

    // Handles SELL operation using CAS retry loop
    public void processSell(Stock stock, TradeRequest request){

        int quantity = request.getQuantity();

        while (true){

            int current = stock.getAvailableQuantity().get();
            int newQuantity = current + quantity;

            if(stock.getAvailableQuantity().compareAndSet(current, newQuantity)){
                recordTrade(stock, request);
                return;
            }

            Thread.onSpinWait();
        }
    }

    // Records successful trade and stores result
    private void recordTrade(Stock stock, TradeRequest request) {

        double price = stock.getPrice().get();

        int remaining = stock.getAvailableQuantity().get();

        System.out.println(
                request.getUserId() + " " +
                        request.getTradeType() + " " +
                        stock.getSymbol() + " " +
                        request.getQuantity() +
                        " @ $" + String.format("%.2f", price)+
                        " SUCCESS remaining=" + remaining
        );

        completedTrades.add(
                new TradeResult(
                        request.getUserId(),
                        stock.getSymbol(),
                        request.getQuantity(),
                        request.getTradeType(),
                        price,
                        System.currentTimeMillis()
                )
        );
    }
}
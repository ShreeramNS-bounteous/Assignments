package service;

import models.Stock;
import models.TradeRequest;
import models.TradeResult;
import models.TradeType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TradingService {
    private final ConcurrentHashMap<String , Stock> stocks;
    private final ConcurrentLinkedQueue<TradeResult> completedTrades;

    public TradingService(ConcurrentHashMap<String, Stock> stocks,
                          ConcurrentLinkedQueue<TradeResult> completedTrades) {
        this.stocks = stocks;
        this.completedTrades = completedTrades;
    }

    public void processTrade(TradeRequest request){
        Stock stock = stocks.get(request.getStockSymbol());

        if(stock == null){
            return;
        }

        if(request.getTradeType() == TradeType.BUY){
            processBuy(stock,request);
        }else {
            processSell(stock,request);
        }
    }

    public void processBuy(Stock stock, TradeRequest request){
        while (true){
            int current = stock.getAvailableQuantity().get();

            if(current < request.getQuantity()){
                return;
            }

            int newQuantity =  current- request.getQuantity();

            boolean updated = stock.getAvailableQuantity()
                    .compareAndSet(current,newQuantity);

            if (updated){
                recordTrade(stock,request);
                return;
            }

            Thread.onSpinWait();

        }
    }

    public void processSell(Stock stock,TradeRequest request){
        int quantity =  request.getQuantity();

        while (true){
            int current = stock.getAvailableQuantity().get();

            int newQuantity =  current +  request.getQuantity();

            boolean updated =  stock.getAvailableQuantity()
                    .compareAndSet(current,newQuantity);

            if(updated){
                recordTrade(stock,request);
                return;
            }

            Thread.onSpinWait();

        }
    }


    private void recordTrade(Stock stock, TradeRequest request) {

        completedTrades.add(
                new TradeResult(
                        request.getUserId(),
                        stock.getSymbol(),
                        request.getQuantity(),
                        request.getTradeType(),
                        System.currentTimeMillis()
                )
        );
    }
}

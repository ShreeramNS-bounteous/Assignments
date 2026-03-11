package app;

import config.TradingConfig;
import models.*;
import report.TradeAnalytics;
import service.TradingService;
import utils.SimulationHelper;

import java.util.List;
import java.util.concurrent.*;

public class TradingSimulationApp {

    public static void main(String[] args){

        long startTime = System.nanoTime();

        ConcurrentHashMap<String, Stock> stocks = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<TradeResult> completedTrades = new ConcurrentLinkedQueue<>();

        SimulationHelper.initializeStocks(stocks);

        TradingService tradingService =
                new TradingService(stocks, completedTrades);

        List<TradeRequest> tradeRequests =
                SimulationHelper.generateTrades();

        ExecutorService executor =
                Executors.newFixedThreadPool(TradingConfig.THREAD_POOL_SIZE);

        submitTrades(executor, tradingService, tradeRequests);

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        TradeAnalytics.printReport(completedTrades);

        long endTime = System.nanoTime();

        System.out.println("Execution Time: "
                + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void submitTrades(
            ExecutorService executor,
            TradingService service,
            List<TradeRequest> trades){

        for(TradeRequest trade : trades){
            executor.submit(() -> service.processTrade(trade));
        }
    }
}
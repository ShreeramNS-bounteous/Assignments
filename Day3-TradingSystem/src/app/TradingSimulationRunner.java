package app;

import config.TradingConfig;
import models.*;
import report.TradeAnalytics;
import service.TradingService;
import utils.SimulationHelper;

import java.util.List;
import java.util.concurrent.*;

public class TradingSimulationRunner {

    public void run() {

        // Start timer to measure total simulation execution time
        long startTime = System.nanoTime();

        // Thread-safe structures shared across worker threads
        ConcurrentHashMap<String, Stock> stocks = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<TradeResult> completedTrades = new ConcurrentLinkedQueue<>();

        // Initialize predefined stocks (AAPL, GOOG, MSFT)
        SimulationHelper.initializeStocks(stocks);

// Start market simulator (background price updates)
        ScheduledExecutorService marketExecutor =
                startMarketSimulator(stocks);

        // Core service responsible for processing trades
        TradingService tradingService =
                new TradingService(stocks, completedTrades);

        // Generate random trade requests for the simulation
        List<TradeRequest> tradeRequests =
                SimulationHelper.generateTrades();

        // Create a fixed-size thread pool to process trades concurrently
        ExecutorService executor =
                Executors.newFixedThreadPool(TradingConfig.THREAD_POOL_SIZE);

        // Submit each trade request as an independent task
        submitTrades(executor, tradingService, tradeRequests);

        // Stop accepting new tasks
        executor.shutdown();

        // Wait for all submitted tasks to finish
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupt status
        }

        // Stop market price simulator after trading completes
        marketExecutor.shutdownNow();

        // Generate analytical report using Java Streams
        TradeAnalytics.printReport(completedTrades);

        // End timer and print total execution time
        long endTime = System.nanoTime();

        System.out.println("Execution Time: "
                + (endTime - startTime) / 1_000_000 + " ms");
    }

    /**
     * Submits all trade requests to the ExecutorService.
     * Each trade is processed independently by a worker thread.
     */
    private void submitTrades(
            ExecutorService executor,
            TradingService service,
            List<TradeRequest> trades){

        for(TradeRequest trade : trades){
            executor.submit(() -> service.processTrade(trade));
        }
    }

    /**
     * Starts background market simulation that periodically updates stock prices.
     * Uses ScheduledExecutorService with a lambda Runnable.
     */
    private ScheduledExecutorService startMarketSimulator(
            ConcurrentHashMap<String, Stock> stocks) {

        ScheduledExecutorService marketExecutor =
                Executors.newScheduledThreadPool(1);

        marketExecutor.scheduleAtFixedRate(() -> {

            System.out.println("\n===== MARKET UPDATE =====");

            stocks.values().forEach(stock -> {

                double currentPrice = stock.getPrice().get();

                // Simulate small market fluctuation
                double change = (Math.random() - 0.5) * 2;

                double newPrice = currentPrice + change;

                stock.getPrice().set(newPrice);

                System.out.println(
                        "Market Update → "
                                + stock.getSymbol()
                                + " price updated to $"
                                + String.format("%.2f", newPrice)
                );

            });
            System.out.println();

        }, 0, 500, TimeUnit.MILLISECONDS);
        return marketExecutor;
    }
}
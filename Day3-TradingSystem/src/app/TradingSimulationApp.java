package app;

import config.TradingConfig;
import models.*;
import service.TradingService;

import java.util.*;
import java.util.concurrent.*;

public class TradingSimulationApp {

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.nanoTime();

        ConcurrentHashMap<String, Stock> stocks = new ConcurrentHashMap<>();

        ConcurrentLinkedQueue<TradeResult> completedTrades =
                new ConcurrentLinkedQueue<>();

        initializeStocks(stocks);

        TradingService tradingService =
                new TradingService(stocks, completedTrades);

        List<TradeRequest> tradeRequests = generateTrades();

        ExecutorService executor = Executors.newFixedThreadPool(TradingConfig.THREAD_POOL_SIZE);

        for (TradeRequest request : tradeRequests) {

            executor.submit(() ->
                    tradingService.processTrade(request));

        }

        executor.shutdown();
        executor.awaitTermination(1,TimeUnit.MINUTES);

        long endTime = System.nanoTime();

        System.out.println("Execution Time: "
                + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void initializeStocks(
            ConcurrentHashMap<String, Stock> stocks){

        stocks.put("AAPL", new Stock("AAPL", 20));
        stocks.put("GOOG", new Stock("GOOG", 20));
        stocks.put("MSFT", new Stock("MSFT", 20));
    }

    private static List<TradeRequest> generateTrades(){

        List<TradeRequest> requests = new ArrayList<>();
        Random random = new Random();

        String[] users = {"User1","User2","User3","User4","User5"};
        String[] stockSymbols = {"AAPL","GOOG","MSFT"};

        for(int i = 0; i < TradingConfig.TOTAL_TRADES; i++){

            String user = users[random.nextInt(users.length)];
            String stock = stockSymbols[random.nextInt(stockSymbols.length)];

            int quantity = random.nextInt(5) + 1;

            TradeType type =
                    random.nextBoolean() ? TradeType.BUY : TradeType.SELL;

            requests.add(new TradeRequest(user, stock, quantity, type));
        }

        return requests;
    }

}
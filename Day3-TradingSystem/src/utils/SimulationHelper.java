package utils;

import config.TradingConfig;
import models.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationHelper {

    private static final Random RANDOM = new Random();

    public static void initializeStocks(
            ConcurrentHashMap<String, Stock> stocks){

        stocks.put("AAPL", new Stock("AAPL", 20));
        stocks.put("GOOG", new Stock("GOOG", 20));
        stocks.put("MSFT", new Stock("MSFT", 20));
    }

    public static List<TradeRequest> generateTrades(){

        List<TradeRequest> requests =
                new ArrayList<>(TradingConfig.TOTAL_TRADES);

        String[] users = {"User1","User2","User3","User4","User5"};
        String[] stockSymbols = {"AAPL","GOOG","MSFT"};

        for(int i = 0; i < TradingConfig.TOTAL_TRADES; i++){

            String user = users[RANDOM.nextInt(users.length)];
            String stock = stockSymbols[RANDOM.nextInt(stockSymbols.length)];

            int quantity = RANDOM.nextInt(5) + 1;

            TradeType type =
                    RANDOM.nextBoolean() ? TradeType.BUY : TradeType.SELL;

            requests.add(new TradeRequest(user, stock, quantity, type));
        }

        return requests;
    }
}
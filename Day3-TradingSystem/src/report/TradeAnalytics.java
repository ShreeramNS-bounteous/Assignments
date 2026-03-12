package report;

import models.TradeResult;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

public class TradeAnalytics {

    public static void printReport(Collection<TradeResult> trades) {

        System.out.println("\n===== TRADING REPORT =====");

        if (trades.isEmpty()) {
            System.out.println("No trades were executed.");
            return;
        }

        long totalTrades = trades.size();
        System.out.println("Total Successful Trades: " + totalTrades);

        Map<String, Integer> stockVolume =
                trades.stream()
                        .collect(
                                Collectors.groupingBy(
                                        TradeResult::stockSymbol,
                                        Collectors.summingInt(TradeResult::quantity)
                                )
                        );

        System.out.println("\nQuantity Traded Per Stock:");
        stockVolume.forEach((stock, qty) ->
                System.out.println(stock + " → " + qty)
        );

        Map<String, Long> tradesPerUser =
                trades.stream()
                        .collect(
                                Collectors.groupingBy(
                                        TradeResult::userId,
                                        Collectors.counting()
                                )
                        );

        System.out.println("\nTrades Per User:");
        tradesPerUser.forEach((user, count) ->
                System.out.println(user + " → " + count)
        );

        stockVolume.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e ->
                        System.out.println(
                                "\nMost traded stock: "
                                        + e.getKey() + " → " + e.getValue()
                        ));

        int totalVolume =
                trades.stream()
                        .mapToInt(TradeResult::quantity)
                        .sum();

        System.out.println("\nTotal Trading Volume: " + totalVolume);

        double totalMarketValue =
                trades.stream()
                        .mapToDouble(t -> t.price() * t.quantity())
                        .sum();

        System.out.println(
                "Total Market Value Traded: $"
                        + String.format("%.2f", totalMarketValue)
        );

        Map<String, Double> avgPricePerStock =
                trades.stream()
                        .collect(Collectors.groupingBy(
                                TradeResult::stockSymbol,
                                Collectors.averagingDouble(TradeResult::price)
                        ));

        System.out.println("\nAverage Price Per Stock:");

        avgPricePerStock.forEach((stock, price) ->
                System.out.println(stock + " → $" + String.format("%.2f", price)));

        trades.stream()
                .max(Comparator.comparingDouble(TradeResult::price))
                .ifPresent(t ->
                        System.out.println(
                                "\nHighest Priced Trade: "
                                        + t.stockSymbol()
                                        + " $" + t.price()
                        ));
    }
}
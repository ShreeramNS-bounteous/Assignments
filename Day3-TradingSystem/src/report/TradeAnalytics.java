package report;

import models.TradeResult;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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
    }
}
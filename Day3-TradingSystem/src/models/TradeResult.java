package models;

public record TradeResult(
        String userId,
        String stockSymbol,
        int quantity,
        TradeType tradeType,
        double price,
        long timestamp
) {}
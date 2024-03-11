package bg.sofia.uni.fmi.mjt.trading.price;

import bg.sofia.uni.fmi.mjt.trading.DoubleRounder;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;

public class PriceChart implements PriceChartAPI {
    double microsoftStockPrice;
    double googleStockPrice;
    double amazonStockPrice;

    public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice) {
        this.microsoftStockPrice = microsoftStockPrice;
        this.googleStockPrice = googleStockPrice;
        this.amazonStockPrice = amazonStockPrice;
    }

    @Override
    public double getCurrentPrice(String stockTicker) {
        double price = switch (stockTicker) {
            case MicrosoftStockPurchase.STOCK_TICKER -> microsoftStockPrice;
            case AmazonStockPurchase.STOCK_TICKER -> amazonStockPrice;
            case GoogleStockPurchase.STOCK_TICKER -> googleStockPrice;
            case null, default -> 0.0;
        };

        return DoubleRounder.round(price);

    }

    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange) {
        if (percentChange > 0) {
            return switch (stockTicker) {
                case MicrosoftStockPurchase.STOCK_TICKER -> {
                    microsoftStockPrice = calculatePrice(microsoftStockPrice, percentChange);
                    yield true;
                }
                case AmazonStockPurchase.STOCK_TICKER -> {
                    amazonStockPrice = calculatePrice(amazonStockPrice, percentChange);
                    yield true;
                }
                case GoogleStockPurchase.STOCK_TICKER -> {
                    googleStockPrice = calculatePrice(googleStockPrice, percentChange);
                    yield true;
                }
                case null, default -> false;
            };
        }
        return false;
    }

    private double calculatePrice(double price, int percentChange) {
        final int hundredPercent = 100;
        price = price * (hundredPercent + percentChange) / hundredPercent;

        return DoubleRounder.round(price);
    }
}

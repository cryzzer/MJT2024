package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Portfolio implements PortfolioAPI {

    private static final int FIXED_STOCK_PRICE_INCREASE_PERCENTAGE = 5;

    private String owner;
    private PriceChartAPI priceChart;
    private StockPurchase[] stockPurchases;
    private double budget;
    private int maxSize;

    private int currSize;


    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this(owner, priceChart, new StockPurchase[]{}, budget, maxSize);
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.stockPurchases = Arrays.copyOf(stockPurchases, maxSize);
        this.budget = budget;
        this.maxSize = maxSize;
        this.currSize = stockPurchases.length;
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (stockTicker == null || quantity <= 0 || currSize >= maxSize) {
            return null;
        }

        return switch (stockTicker) {
            case MicrosoftStockPurchase.STOCK_TICKER ->
                    attemptPurchase(new MicrosoftStockPurchase(quantity, LocalDateTime.now(),
                            priceChart.getCurrentPrice(stockTicker)));
            case AmazonStockPurchase.STOCK_TICKER ->
                    attemptPurchase(new AmazonStockPurchase(quantity, LocalDateTime.now(),
                            priceChart.getCurrentPrice(stockTicker)));
            case GoogleStockPurchase.STOCK_TICKER ->
                    attemptPurchase(new GoogleStockPurchase(quantity, LocalDateTime.now(),
                            priceChart.getCurrentPrice(stockTicker)));
            default -> null;
        };
    }

    private StockPurchase attemptPurchase(StockPurchase pendingPurchase) {
        double totalPendingPurchasePrice = pendingPurchase.getTotalPurchasePrice();
        if (budget < totalPendingPurchasePrice) {
            return null;
        }

        stockPurchases[currSize++] = pendingPurchase;
        budget -= totalPendingPurchasePrice;

        priceChart.changeStockPrice(pendingPurchase.getStockTicker(), FIXED_STOCK_PRICE_INCREASE_PERCENTAGE);

        return pendingPurchase;
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return Arrays.copyOf(stockPurchases, currSize);
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        int sizeOfNewArray = numberOfPurchasesInAGivenPeriod(startTimestamp, endTimestamp);

        StockPurchase[] stockPurchasesInPeriod = new StockPurchase[sizeOfNewArray];
        int cursor = 0;

        for (StockPurchase stock : stockPurchases) {
            if(stock == null){
                break;
            }
            if (isBetweenTimestamps(stock.getPurchaseTimestamp(), startTimestamp, endTimestamp)) {
                stockPurchasesInPeriod[cursor++] = stock;
            }
        }

        return stockPurchasesInPeriod;
    }

    private int numberOfPurchasesInAGivenPeriod(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        int counter = 0;
        for (StockPurchase stock : stockPurchases) {
            if(stock == null){
                break;
            }
            if (isBetweenTimestamps(stock.getPurchaseTimestamp(), startTimestamp, endTimestamp)) {
                counter++;
            }
        }
        return counter;
    }

    private boolean isBetweenTimestamps(LocalDateTime purchaseTimestamp, LocalDateTime startTimestamp,
                                        LocalDateTime endTimestamp) {
        return (purchaseTimestamp.isEqual(startTimestamp) || purchaseTimestamp.isAfter(startTimestamp))
                && (purchaseTimestamp.isEqual(endTimestamp) || purchaseTimestamp.isBefore(endTimestamp));
    }

    @Override
    public double getNetWorth() {
        double sum = 0.0;

        for (StockPurchase stock : stockPurchases) {
            if(stock == null){
                break;
            }
            sum += stock.getQuantity() * priceChart.getCurrentPrice(stock.getStockTicker());
        }

        return DoubleRounder.round(sum);
    }

    @Override
    public double getRemainingBudget() {
        return DoubleRounder.round(budget);
    }

    @Override
    public String getOwner() {
        return owner;
    }
}

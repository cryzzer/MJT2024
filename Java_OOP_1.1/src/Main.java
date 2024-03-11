import bg.sofia.uni.fmi.mjt.trading.Portfolio;
import bg.sofia.uni.fmi.mjt.trading.PortfolioAPI;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        PriceChartAPI priceChart = new PriceChart(
                413.64,
                139.78,
                176.76);

        PortfolioAPI portfolio1 = new Portfolio("Gosho", priceChart, 4500, 10);

        System.out.println("Remaining budget: " + portfolio1.getRemainingBudget());

        System.out.println("Trying to buy Microsoft stocks: " + portfolio1.buyStock(MicrosoftStockPurchase.STOCK_TICKER, 3).getTotalPurchasePrice());
        System.out.println("Remaining budget: " + portfolio1.getRemainingBudget());

        System.out.println("Networth: " + portfolio1.getNetWorth());

        System.out.println("stocks:" + Arrays.toString(portfolio1.getAllPurchases()));

    }
}
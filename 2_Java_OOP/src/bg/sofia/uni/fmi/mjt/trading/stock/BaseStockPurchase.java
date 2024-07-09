package bg.sofia.uni.fmi.mjt.trading.stock;

import bg.sofia.uni.fmi.mjt.trading.DoubleRounder;

import java.time.LocalDateTime;

abstract class BaseStockPurchase implements StockPurchase {
    private final String ticker;
    private final int quantity;
    private final LocalDateTime purchaseTimestamp;
    private final double purchasePricePerUnit;

    public BaseStockPurchase(String ticker, int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.purchaseTimestamp = purchaseTimestamp;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp() {
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit() {
        return DoubleRounder.round(purchasePricePerUnit);
    }

    @Override
    public double getTotalPurchasePrice() {
        double total = purchasePricePerUnit * quantity;

        return DoubleRounder.round(total);
    }

    @Override
    public String getStockTicker() {
        return ticker;
    }

    @Override
    public String toString() {
        return "BaseStockPurchase{" +
                "ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", purchaseTimestamp=" + purchaseTimestamp +
                ", purchasePricePerUnit=" + purchasePricePerUnit +
                '}';
    }
}

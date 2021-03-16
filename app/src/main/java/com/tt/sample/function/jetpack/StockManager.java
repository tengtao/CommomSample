package com.tt.sample.function.jetpack;

import java.math.BigDecimal;

public class StockManager {


    private final String name;

    public StockManager(String name) {
        this.name = name;
    }

    public void requestPriceUpdates(SimplePriceListener listener) {

    }

    public void removeUpdates(SimplePriceListener listener) {

    }


    //
    public void setSimplePriceListener(SimplePriceListener simplePriceListener) {
        this.simplePriceListener = simplePriceListener;
    }

    private SimplePriceListener simplePriceListener;


    public interface SimplePriceListener {
        void onPriceChanged(BigDecimal price);
    }
}

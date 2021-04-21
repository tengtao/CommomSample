package com.tt.sample.function.jetpack;

import android.os.Handler;

import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.Random;

public class StockManager {


    private final String name;
    Handler handler = new Handler();

    public StockManager(String name) {
        this.name = name;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BigDecimal bigDecimal = new BigDecimal(new Random().nextInt(1000));
                Logger.d("======数据改变" + bigDecimal.toString());
                if (simplePriceListener != null) {
                    simplePriceListener.onPriceChanged(bigDecimal);
                }
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    public void requestPriceUpdates(SimplePriceListener listener) {
        this.simplePriceListener = listener;
    }

    public void removeUpdates(SimplePriceListener listener) {
        simplePriceListener = null;
    }

    private SimplePriceListener simplePriceListener;

    public interface SimplePriceListener {
        void onPriceChanged(BigDecimal price);
    }
}

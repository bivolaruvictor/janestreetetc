package com.etc;

import java.util.ArrayList;

public class Valbz extends Security {
    private static Integer fairValue = 0;

    public Valbz(String price, String quantity) {
        super(price, quantity);
    }

    public static Pair<Integer> computeFairValue(ArrayList<Security> buy, ArrayList<Security> sell) {
        Integer buyFairValue = 0;
        Integer sellFairValue = 0;
        buyFairValue = buy.get(0).getPrice();
        sellFairValue = sell.get(0).getPrice();

        return new Pair<>(buyFairValue, sellFairValue);
    }
}

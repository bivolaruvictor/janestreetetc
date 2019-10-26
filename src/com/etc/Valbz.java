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
        Integer count = 0;
        for (Security security : buy) {
            buyFairValue += security.getPrice();
            count++;
        }
        if (count == 0) {
            buyFairValue /= count;
        } else {
            buyFairValue = 0;
        }

        count = 0;
        for (Security security : sell) {
            sellFairValue += security.getPrice();
            count++;
        }
        sellFairValue /= count;
        if (count == 0){
            sellFairValue /= count;
        } else {
            sellFairValue = 0;
        }
        return new Pair<>(buyFairValue, sellFairValue);
    }
}

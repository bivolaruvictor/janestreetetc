package com.etc;

import java.net.Inet4Address;
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
        count == 0 ? buyFairValue /= count : buyFairValue = 0;

        count = 0;
        for (Security security : sell) {
            sellFairValue += security.getPrice();
            count++;
        }
        sellFairValue /= count;
        count == 0 ? sellFairValue /= count : sellFairValue = 0;
        return new Pair<>(buyFairValue, sellFairValue);
    }
}

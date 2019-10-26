package com.etc;

import java.util.ArrayList;

public class Security {
    private Integer price;
    private Integer quantity;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Security(String price, String quantity) {
        this.price = Integer.parseInt(price);
        this.quantity = Integer.parseInt(quantity);
    }

    @Override
    public String toString() {
        return "Bond{" +
                "price=" + price +
                ", quantity=" + quantity +
                '}';
    }
    public static Pair<Integer> computeFairValue(ArrayList<Security> buy, ArrayList<Security> sell) {
        Integer buyFairValue = 0;
        Integer sellFairValue = 0;
        if (buy.size() > 0) {
            buyFairValue = buy.get(0).getPrice();

        }
        if (sell.size() > 0) {
            sellFairValue = sell.get(0).getPrice();
        }
        return new Pair<Integer>(buyFairValue, sellFairValue);
    }
}

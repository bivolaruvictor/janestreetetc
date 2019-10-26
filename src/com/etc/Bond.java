package com.etc;

public class Bond implements Security {
    private Integer price;
    private Integer quantity;

    public Bond(String price, String quantity) {
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
}

package com.etc;

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
}

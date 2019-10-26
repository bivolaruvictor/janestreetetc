package com.etc;

public class Order {
    private Integer id; // id ul orderului
    private String type; // add / cancel
    private String symbol; // BOND sau ceva
    private boolean dir; // true - buy false - sell
    private Integer size;
    private Integer price;

    public Order(Integer id, String type, String symbol, boolean dir, int size, Integer price) {
        this.id = id;
        this.type = type;
        this.symbol = symbol;
        this.dir = dir;
        this.size = size;
        this.price = price;
    }

    public String orderMessage() {
        String directive = "";
        if (dir == true) {
            directive = directive + "BUY";
        } else if (dir == false){
            directive = directive + "SELL";
        }
        return "ADD" + " " + id + " " + symbol + " " + directive + " " + price + " " + size;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", symbol='" + symbol + '\'' +
                ", dir=" + dir +
                ", size=" + size +
                ", price=" + price +
                '}';
    }

    public String getType() {
        return type;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isDir() {
        return dir;
    }

    public Integer getPrice() {
        return price;
    }
}

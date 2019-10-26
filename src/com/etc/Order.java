package com.etc;

import java.util.HashMap;
import java.util.LinkedList;

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

    public static void waitForReply(HashMap<String, Integer> portofolio, String reply, LinkedList<Order> stack) {
        String[] splitted = reply.split(" ");
        if (splitted[0].equals("ACK")) {
            Order order = stack.peekLast();
            if (Integer.parseInt(splitted[1]) == order.getId()) {
                if (order.isDir()) {
                    portofolio.put(order.getType(), portofolio.get(order.getType()) + order.getSize());
                } else if (!order.isDir()) {
                    portofolio.put(order.getType(), portofolio.get(order.getType()) - order.getSize());
                }
            }
        } else if (splitted[0].equals("REJECT")) {
            stack.pop();
        }
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

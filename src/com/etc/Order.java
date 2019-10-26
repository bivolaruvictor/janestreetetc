package com.etc;

public class Order {
    private Integer id; // id ul orderului
    private String type; // add / cancel
    private String symbol; // BOND sau ceva
    private boolean dir; // true - buy false - sell
    private Integer size;
    private Integer price;
}

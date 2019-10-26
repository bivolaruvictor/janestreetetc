package com.etc;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Bot
{
    public static void main(String[] args)
    {
        /* The boolean passed to the Configuration constructor dictates whether or not the
           bot is connecting to the prod or test exchange. Be careful with this switch! */
        Configuration config = new Configuration(true);
        try
        {
            Socket skt = new Socket(config.exchange_name(), config.port());
            BufferedReader from_exchange = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            PrintWriter to_exchange = new PrintWriter(skt.getOutputStream(), true);

            /*
              A common mistake people make is to to_exchange.println() > 1
              time for every from_exchange.readLine() response.
              Since many write messages generate marketdata, this will cause an
              exponential explosion in pending messages. Please, don't do that!
            */
            HashMap<String, Integer> portofolio = new HashMap<>();
            to_exchange.println(("HELLO " + config.team_name).toUpperCase());
            LinkedList<Order> orderStack = new LinkedList<>();

            while (true) {
                Integer lastOrderId = 1;
                String reply = from_exchange.readLine().trim();
//            System.err.printf("The exchange replied: %s\n", reply);

                String[] splitted = reply.split(" ");
                if (splitted[0].equals("HELLO")) {
                    for (String security : splitted) {
                        if (!security.equals("HELLO")) {
                            String[] position = security.split(":");
                            portofolio.put(position[0], Integer.parseInt(position[1]));
                        }
                    }
                } else if (splitted[0].equals("BOOK") && splitted[1].equals("BOND")) {
                    ArrayList<Bond> bondsBuying = new ArrayList<>();
                    ArrayList<Bond> bondsSelling = new ArrayList<>();
                    boolean putInSelling = false;
                    for (int i = 3; i < splitted.length; i++) {
                        if (splitted[i].equals("SELL")) {
                            putInSelling = true;
                            continue;
                        }
                        if (!putInSelling) {
                            String[] offerDetails = splitted[i].split(":");
                            bondsBuying.add(new Bond(offerDetails[0], offerDetails[1]));
                        } else {
                            String[] offerDetails = splitted[i].split(":");
                            bondsSelling.add(new Bond(offerDetails[0], offerDetails[1]));
                        }
                    }
                    System.out.println(bondsBuying);
                    System.out.println(bondsSelling);
                    int howManyToBuy = 0;
                    for (Bond bond : bondsSelling) {
                        if (bond.getPrice() < 1000) {
                            howManyToBuy += bond.getQuantity();
                        } else if (howManyToBuy != 0){
                            orderStack.addLast(new Order(lastOrderId++, "ADD", "BOND",
                                    true, howManyToBuy, bond.getPrice()));
                            to_exchange.println(orderStack.peekLast().orderMessage());
                            System.out.println(orderStack.peekLast().orderMessage());
                            break;
                        }
                    }
                    int howManyToSell = 0;
                    for (Bond bond : bondsBuying) {
                        if (bond.getPrice() >= 1000) {
                            howManyToSell += bond.getQuantity();
                        } else if (howManyToSell != 0){
                            orderStack.addLast(new Order(lastOrderId++, "ADD", "BOND",
                                    false, howManyToSell, bond.getPrice()));
                            to_exchange.println(orderStack.peekLast().orderMessage());
                            System.out.println(orderStack.peekLast().orderMessage());
                            break;
                        }
                    }
                    System.out.println(orderStack);
                    System.out.printf("%d %d", howManyToBuy, howManyToSell);
                } else if (splitted[0].equals("ACK")) {
                    Order order = orderStack.peekLast();
                    if (Integer.parseInt(splitted[1]) == order.getId()) {
                        if (order.isDir()) {
                            portofolio.put(order.getType(), portofolio.get(order.getType()) + order.getSize());
                        } else if (!order.isDir()) {
                            portofolio.put(order.getType(), portofolio.get(order.getType()) - order.getSize());
                        }
                    }
                } else if (splitted[0].equals("REJECT")) {
                    orderStack.removeLast();
                }
                System.out.println(reply);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}
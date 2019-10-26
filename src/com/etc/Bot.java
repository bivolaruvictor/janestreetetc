package com.etc;


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
        Configuration config = new Configuration(false);
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
                    ArrayList<Security> bondsBuying = new ArrayList<>();
                    ArrayList<Security> bondsSelling = new ArrayList<>();
                    boolean putInSelling = false;
                    for (int i = 3; i < splitted.length; i++) {
                        if (splitted[i].equals("SELL")) {
                            putInSelling = true;
                            continue;
                        }
                        if (!putInSelling) {
                            String[] offerDetails = splitted[i].split(":");
                            bondsBuying.add(new Security(offerDetails[0], offerDetails[1]));
                        } else {
                            String[] offerDetails = splitted[i].split(":");
                            bondsSelling.add(new Security(offerDetails[0], offerDetails[1]));
                        }
                    }
                    System.out.println(bondsBuying);
                    System.out.println(bondsSelling);
                    int howManyToBuy = 0;
                    Integer lastPrice = 0;
                    for (Security security : bondsSelling) {
                        if (security.getPrice() < 1000) {
                            howManyToBuy += security.getQuantity();
                            lastPrice = security.getPrice();
                            orderStack.addLast(new Order(lastOrderId++, "ADD", "BOND",
                                    true, howManyToBuy, lastPrice));
                            to_exchange.println(orderStack.peekLast().orderMessage());
                            System.out.println(orderStack.peekLast().orderMessage());
                            reply = from_exchange.readLine().trim();
                            System.out.println(reply);
                            Order.waitForReply(portofolio, reply, orderStack);
                        }
                    }
                    int howManyToSell = 0;
                    for (Security security : bondsBuying) {
                        if (security.getPrice() >= 1000) {
                            howManyToSell = security.getQuantity();
                            lastPrice = security.getPrice();
                            orderStack.addLast(new Order(lastOrderId++, "ADD", "BOND",
                                    false, howManyToSell, lastPrice));
                            to_exchange.println(orderStack.peekLast().orderMessage());
                            System.out.println(orderStack.peekLast().orderMessage());
                            System.out.println(reply);
                            Order.waitForReply(portofolio, reply, orderStack);
                        }
                    }
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
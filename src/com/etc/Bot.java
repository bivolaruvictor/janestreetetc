package com.etc;


import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

public class Bot
{
    public static void main(String[] args)
    {
        /* The boolean passed to the Configuration constructor dictates whether or not the
           bot is connecting to the prod or test exchange. Be careful with this switch! */
        boolean isTest = false;
        if (args[0].equals("true")) {
            isTest = true;
        }
        Configuration config = new Configuration(isTest);
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
                Pair<Integer> valbzFairValue = new Pair<>(0, 0);
                String reply = from_exchange.readLine().trim();
                String[] splitted = reply.split(" ");
                if (splitted[0].equals("HELLO")) {
                    for (String security : splitted) {
                        if (!security.equals("HELLO")) {
                            String[] position = security.split(":");
                            portofolio.put(position[0], Integer.parseInt(position[1]));
                        }
                    }
                }
//                else if (splitted[0].equals("BOOK") && splitted[1].equals("BOND")) {
//                    SecurityContainer container = new SecurityContainer(splitted);
//                    int howManyToBuy = 0;
//                    Integer lastPrice = 0;
//                    for (Security security : container.buying) {
//                        if (security.getPrice() < 1000) {
//                            howManyToBuy += security.getQuantity();
//                            lastPrice = security.getPrice();
//                            orderStack.addLast(new Order(lastOrderId++, "ADD", "BOND",
//                                    true, howManyToBuy, lastPrice));
//                            to_exchange.println(orderStack.peekLast().orderMessage());
//                            System.out.println(orderStack.peekLast().orderMessage());
//                            reply = from_exchange.readLine().trim();
//                            System.out.println(reply);
//                            Order.waitForReply(portofolio, reply, orderStack);
//                        }
//                    }
//                    int howManyToSell = 0;
//                    for (Security security : container.selling) {
//                        if (security.getPrice() >= 1000) {
//                            howManyToSell = security.getQuantity();
//                            lastPrice = security.getPrice();
//                            orderStack.addLast(new Order(lastOrderId++, "ADD", "BOND",
//                                    false, howManyToSell, lastPrice));
//                            to_exchange.println(orderStack.peekLast().orderMessage());
//                            System.out.println(orderStack.peekLast().orderMessage());
//                            System.out.println(reply);
//                            Order.waitForReply(portofolio, reply, orderStack);
//                        }
//                    }
//                }
                else if (splitted[0].equals("BOOK") && splitted[1].equals("VALBZ")) {
                    SecurityContainer container = new SecurityContainer(splitted);
                    valbzFairValue = Valbz.computeFairValue(container.buying, container.selling);
                    System.out.println("VALBZ FAIR VALUE = " + valbzFairValue.toString());
                }
                else if (splitted[0].equals("BOOK") && splitted[1].equals("VALE")) {
                    SecurityContainer container = new SecurityContainer(splitted);
                    valbzFairValue = Valbz.computeFairValue(container.buying, container.selling);
                    if (!valbzFairValue.first.equals(0) && !valbzFairValue.second.equals(0)) {
                        System.out.println("Am intrat pe primul!!!!!!");
                        if ((valbzFairValue.first - container.buying.get(0).getPrice()) > 10) {
                            System.out.println("BUYING VALE");
                            orderStack.addLast(new Order(lastOrderId++, "ADD", "VALE", true,
                                    10, container.selling.get(0).getPrice()));
                            reply = from_exchange.readLine().trim();
                            Order.waitForReply(portofolio, reply, orderStack);
                            orderStack.addLast(new Order(lastOrderId++, "CONVERT", "VALE", true,
                                    portofolio.get("VALE")));
                            to_exchange.println(orderStack.peekLast().orderMessage());
                            reply = from_exchange.readLine().trim();
                            Order.waitForReply(portofolio, reply, orderStack);
                            orderStack.addLast(new Order(lastOrderId++, "ADD", "VALBZ", false,
                                    5, valbzFairValue.first + 1));
                            to_exchange.println(orderStack.peekLast().orderMessage());
                            reply = from_exchange.readLine().trim();
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
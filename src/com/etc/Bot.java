package com.etc;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

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

            String reply = from_exchange.readLine().trim();
            System.err.printf("The exchange replied: %s\n", reply);

            String[] splitted = reply.split(" ");
            if (splitted[0].equals("HELLO")) {
                for (String security : splitted) {
                    if (!security.equals("HELLO")) {
                        String[] position = security.split(":");
                        portofolio.put(position[0], Integer.parseInt(position[1]));
                    }
                }
            }
            System.out.println(portofolio);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}
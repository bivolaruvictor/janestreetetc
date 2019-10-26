package com.etc;

import java.lang.*;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;

public class Configuration {
    String exchange_name;
    int    exchange_port;
    /* 0 = prod-like
       1 = slow
       2 = empty
    */
    final Integer test_exchange_kind = 0;
    /* replace REPLACEME with your team name! */
    final String  team_name          = "BEESKNEES";

    Configuration(Boolean test_mode) {
        if(!test_mode) {
            exchange_port = 20000;
            exchange_name = "production";
        } else {
            exchange_port = 20000 + test_exchange_kind;
            exchange_name = "test-exch-" + this.team_name;
        }
    }

    String  exchange_name() { return exchange_name; }
    Integer port()          { return exchange_port; }
}
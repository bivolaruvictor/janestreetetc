package com.etc;

import java.util.ArrayList;
import java.util.spi.AbstractResourceBundleProvider;

public class SecurityContainer {
    public ArrayList<Security> buying;
    public ArrayList<Security> selling;

    public SecurityContainer(String[] splitted) {
        buying = new ArrayList<>();
        selling = new ArrayList<>();

        boolean putInSelling = false;
        for (int i = 3; i < splitted.length; i++) {
            if (splitted[i].equals("SELL")) {
                putInSelling = true;
                continue;
            }
            if (!putInSelling) {
                String[] offerDetails = splitted[i].split(":");
                buying.add(new Security(offerDetails[0], offerDetails[1]));
            } else {
                String[] offerDetails = splitted[i].split(":");
                selling.add(new Security(offerDetails[0], offerDetails[1]));
            }
        }
    }
}

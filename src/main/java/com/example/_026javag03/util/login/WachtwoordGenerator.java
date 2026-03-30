package com.example._026javag03.util.login;

import java.security.SecureRandom;

public class WachtwoordGenerator {

    private static final String KARAKTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String genereerWachtwoord(int lengte) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lengte; i++) {
            int index = RANDOM.nextInt(KARAKTERS.length());
            sb.append(KARAKTERS.charAt(index));
        }
        return sb.toString();
    }
}

package com.example._026javag03.util.login;

import org.mindrot.jbcrypt.BCrypt;

public class WachtwoordHasher {

    private static final int WORKLOAD = 12;

    public static String hash(String wachtwoord) {
        return BCrypt.hashpw(wachtwoord, BCrypt.gensalt(WORKLOAD));
    }

    public static boolean controleer(String wachtwoord, String hash) {
        if (hash == null || hash.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(wachtwoord, hash);
    }
}
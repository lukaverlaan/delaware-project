package com.example._026javag03.util.login;

import java.util.regex.Pattern;

public class WachtwoordValidator {

    private static final Pattern PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!?.*_-]).{8,}$"
    );

    public static void valideer(String wachtwoord) {

        if (wachtwoord == null || wachtwoord.isBlank()) {
            throw new IllegalArgumentException("Wachtwoord mag niet leeg zijn.");
        }

        if (!PATTERN.matcher(wachtwoord).matches()) {
            throw new IllegalArgumentException(
                    "Wachtwoord moet minstens 8 karakters bevatten, " +
                            "met een hoofdletter, kleine letter, cijfer en speciaal teken."
            );
        }
    }
}
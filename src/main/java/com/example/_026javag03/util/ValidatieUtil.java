package com.example._026javag03.util;

public final class ValidatieUtil {

    private ValidatieUtil() {}

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final String GSM_REGEX =
            "^\\+?[0-9]{8,15}$";

    private static final String POSTCODE_REGEX =
            "^\\d{4}$";

    public static boolean isGeldigEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean isGeldigGsm(String gsm) {
        return gsm != null && gsm.matches(GSM_REGEX);
    }

    public static boolean isGeldigePostcode(String postcode) {
        return postcode != null && postcode.matches(POSTCODE_REGEX);
    }
}
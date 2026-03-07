package com.example._026javag03.exceptions;

import com.example._026javag03.util.gebruiker.AdresAttributes;

import java.util.Map;

public class AdresException extends Exception {

    private Map<AdresAttributes,String> requiredAttrs;

    public AdresException(Map<AdresAttributes,String> reqAttributes) {
        this.requiredAttrs = reqAttributes;
    }

    public Map<AdresAttributes,String> getRequired() {
        return requiredAttrs;
    }
}

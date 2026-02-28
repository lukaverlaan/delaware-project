package com.example._026javag03.exceptions;

import com.example._026javag03.util.AdresAtrributes;

import java.util.Set;

public class AdresException extends Exception {

    private Set<AdresAtrributes> requiredAttrs;

    public AdresException(Set<AdresAtrributes> reqAttributes) {
        this.requiredAttrs = reqAttributes;
    }

    public Set<AdresAtrributes> getRequired() {
        return requiredAttrs;
    }
}

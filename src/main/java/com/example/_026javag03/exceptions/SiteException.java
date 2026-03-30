package com.example._026javag03.exceptions;


import com.example._026javag03.util.site.SiteAttributes;

import java.util.Map;
import java.util.Set;

public class SiteException extends Exception {

    private Map<SiteAttributes,String> requiredAttrs;

    public SiteException(Map<SiteAttributes,String> reqAttributes) {
        this.requiredAttrs = reqAttributes;
    }

    public Map<SiteAttributes,String> getRequired() {
        return requiredAttrs;
    }}
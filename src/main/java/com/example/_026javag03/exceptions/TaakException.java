package com.example._026javag03.exceptions;

import com.example._026javag03.util.taak.TaakAttributes;

import java.util.Map;

public class TaakException extends Throwable {
    private Map<TaakAttributes,String> reqAttributes;

    public TaakException(Map<TaakAttributes, String> reqattributes) {
    }

    public Map<TaakAttributes,String> getRequired(){return reqAttributes;}
}

package com.example._026javag03.exceptions;

import java.util.Map;

public class ValidatieException extends RuntimeException {

    private final Map<String,String> fouten;

    public ValidatieException(Map<String,String> fouten) {
        super(String.join("\n", fouten.values()));
        this.fouten = fouten;
    }

    public Map<String,String> getFouten() {
        return fouten;
    }
}

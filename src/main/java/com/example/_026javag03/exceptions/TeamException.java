package com.example._026javag03.exceptions;

import com.example._026javag03.util.team.TeamAttributes;

import java.util.Map;

public class TeamException extends Throwable {
    private Map<TeamAttributes,String> requiredAttrs;

    public TeamException(Map<TeamAttributes,String> reqAttributes) {
        this.requiredAttrs = reqAttributes;
    }

    public Map<TeamAttributes,String> getRequired() {
        return requiredAttrs;
    }
}

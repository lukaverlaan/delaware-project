package com.example._026javag03.exceptions;

import com.example._026javag03.util.TeamAttributes;

import java.util.Set;

public class TeamException extends Throwable {
    private Set<TeamAttributes> requiredAttrs;

    public TeamException(Set<TeamAttributes> reqAttributes) {
        this.requiredAttrs = reqAttributes;
    }

    public Set<TeamAttributes> getRequired() {
        return requiredAttrs;
    }
}

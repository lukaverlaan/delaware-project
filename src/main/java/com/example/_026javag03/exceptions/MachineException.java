package com.example._026javag03.exceptions;

import com.example._026javag03.util.machine.MachineAttributes;

import java.util.Map;
import java.util.Set;

public class MachineException extends Exception {

    private final Map<MachineAttributes, String> requiredattrs;

    public MachineException(Map<MachineAttributes, String> reqAttributes) {
        super("Machine validatie mislukt");
        this.requiredattrs = reqAttributes;
    }

    public MachineException(String message) {
        super(message);
        this.requiredattrs = null;
    }

    public Map<MachineAttributes, String> getRequired() {
        return requiredattrs;
    }

    public Set<MachineAttributes> getMissingAttributes() {
        return requiredattrs != null ? requiredattrs.keySet() : Set.of();
    }
}
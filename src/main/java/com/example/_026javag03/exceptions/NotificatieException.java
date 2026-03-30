package com.example._026javag03.exceptions;

import com.example._026javag03.util.notificatie.NotificatieAttributes;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public class NotificatieException extends Exception {

    private final Map<NotificatieAttributes, String> requiredAttrs;

    public NotificatieException(Map<NotificatieAttributes, String> reqAttributes) {
        super("Validatiefouten in Notificatie: " + reqAttributes.toString());
        this.requiredAttrs = reqAttributes;
    }

    public Set<NotificatieAttributes> getMissingAttributes() {
        return requiredAttrs != null ? requiredAttrs.keySet() : Set.of();
    }
}
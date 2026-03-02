package com.example._026javag03.exceptions;


import com.example._026javag03.util.SiteAttributes;

import java.util.Set;

public class SiteException extends Exception {
    private final Set<SiteAttributes> missingAttributes;

    public SiteException(Set<SiteAttributes> missingAttributes) {
        super("Ontbrekende verplichte attributen: " + missingAttributes);
        this.missingAttributes = missingAttributes;
    }

    public Set<SiteAttributes> getMissingAttributes() {
        return missingAttributes;
    }
}
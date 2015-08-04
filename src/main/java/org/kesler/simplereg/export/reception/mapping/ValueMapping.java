package org.kesler.simplereg.export.reception.mapping;

import org.kesler.simplereg.logic.Reception;

public abstract class ValueMapping {
    protected Reception reception;
    public ValueMapping(Reception reception) {
        this.reception = reception;
    }
    public abstract String getName();
    public abstract String getValue();
}

package org.kesler.simplereg.export.mapping.support;

import org.kesler.simplereg.export.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

public class ServiceValueMapping extends ValueMapping {
    public ServiceValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@Service@";
    }

    @Override
    public String getValue() {
        return reception.getService().getName();
    }
}

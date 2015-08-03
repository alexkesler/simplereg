package org.kesler.simplereg.export.reception.mapping.support;

import org.kesler.simplereg.export.reception.mapping.ValueMapping;
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

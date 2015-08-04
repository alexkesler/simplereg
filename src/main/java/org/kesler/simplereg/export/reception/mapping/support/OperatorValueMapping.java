package org.kesler.simplereg.export.reception.mapping.support;

import org.kesler.simplereg.export.reception.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

public class OperatorValueMapping extends ValueMapping {
    public OperatorValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@Operator@";
    }

    @Override
    public String getValue() {
        return reception.getOperator().getFIO();
    }
}

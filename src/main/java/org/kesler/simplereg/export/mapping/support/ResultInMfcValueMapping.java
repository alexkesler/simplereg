package org.kesler.simplereg.export.mapping.support;

import org.kesler.simplereg.export.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

public class ResultInMfcValueMapping extends ValueMapping {
    public ResultInMfcValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@ResultInMfc@";
    }

    @Override
    public String getValue() {
        String resultInMFCString = "__";
        if (reception.isResultInMFC()) resultInMFCString = "V";
        return resultInMFCString;
    }
}

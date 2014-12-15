package org.kesler.simplereg.export.mapping.support;

import org.kesler.simplereg.export.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

/**
 * Created by alex on 16.12.14.
 */
public class RosreestrCodeValueMapping extends ValueMapping {

    public RosreestrCodeValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@RosreestrCode@";
    }

    @Override
    public String getValue() {
        return reception.getRosreestrCode();
    }
}

package org.kesler.simplereg.export.mapping.support;

import org.kesler.simplereg.export.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

/**
 * Created by alex on 16.12.14.
 */
public class ReceptionCodeValueMapping extends ValueMapping {
    public ReceptionCodeValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@ReceptionCode@";
    }

    @Override
    public String getValue() {
        return reception.getReceptionCode();
    }
}

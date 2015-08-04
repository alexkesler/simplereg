package org.kesler.simplereg.export.reception.mapping.support;

import org.kesler.simplereg.export.reception.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

import java.text.SimpleDateFormat;

public class OpenDateValueMapping extends ValueMapping {
    public OpenDateValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@OpenDate@";
    }

    @Override
    public String getValue() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy г.");
        return reception.getOpenDate()==null?"":dateFormat.format(reception.getOpenDate());
    }
}

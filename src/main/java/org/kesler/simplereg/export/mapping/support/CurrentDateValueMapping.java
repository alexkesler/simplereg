package org.kesler.simplereg.export.mapping.support;

import org.kesler.simplereg.export.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDateValueMapping extends ValueMapping {
    public CurrentDateValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@CurrentDate@";
    }

    @Override
    public String getValue() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy г.");
        return dateFormat.format(new Date());
    }
}

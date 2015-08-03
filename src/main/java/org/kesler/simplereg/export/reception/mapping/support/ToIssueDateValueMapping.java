package org.kesler.simplereg.export.reception.mapping.support;

import org.kesler.simplereg.export.reception.mapping.ValueMapping;
import org.kesler.simplereg.logic.Reception;

import java.text.SimpleDateFormat;

public class ToIssueDateValueMapping extends ValueMapping {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy г.");
    public ToIssueDateValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@ToIssueDate@";
    }

    @Override
    public String getValue() {
        return reception.getToIssueDate()==null?"":dateFormat.format(reception.getToIssueDate());
    }
}

package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

public class RealtyObjectReestrColumn extends ReestrColumn {

    public RealtyObjectReestrColumn() {
        name = "Объект недвижимости";
        alias = "realty";
        width = 100;
    }

    public String getValue(Reception reception) {
        return reception.getRealtyObject().toString();
    }
}
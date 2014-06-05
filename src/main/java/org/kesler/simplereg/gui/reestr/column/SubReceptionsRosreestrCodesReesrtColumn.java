package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

/**
 * Created by alex on 04.06.14.
 */
public class SubReceptionsRosreestrCodesReesrtColumn extends ReestrColumn{

    public SubReceptionsRosreestrCodesReesrtColumn() {
        name = "Код Росреестра доп дел";
        alias = "subReceptionsRosreestrCodes";
        width = 20;
    }

    public String getValue(Reception reception) {


        return reception.getSubReceptionsRosreestrCodes();

    }
}

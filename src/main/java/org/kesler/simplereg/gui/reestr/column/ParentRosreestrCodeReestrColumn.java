package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

/**
 * Created by alex on 04.06.14.
 */
public class ParentRosreestrCodeReestrColumn extends ReestrColumn {
    public ParentRosreestrCodeReestrColumn() {
        name = "Код Росреестра осн дела";
        alias = "parentRosreestrCode";
        width = 15;
    }

    public String getValue(Reception reception) {

        Reception parentReception = reception.getParentReception();

        if (parentReception == null) return "";
        if (parentReception.getRosreestrCode() == null) return "Не опр.";

        return parentReception.getRosreestrCode();

    }
}

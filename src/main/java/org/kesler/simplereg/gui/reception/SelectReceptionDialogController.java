package org.kesler.simplereg.gui.reception;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.ModelState;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;

import javax.swing.*;
import java.util.Date;

/**
 * Контроллер для управления диалогом выбора дела
 */
public class SelectReceptionDialogController implements ReceptionsModelStateListener{

    protected final Logger log;
    private static SelectReceptionDialogController instance = new SelectReceptionDialogController();

    ReceptionsModel receptionsModel;
    SelectReceptionDialog dialog;

    private SelectReceptionDialogController () {
        log = Logger.getLogger(this.getClass().getSimpleName());
        receptionsModel = new ReceptionsModel();
        receptionsModel.addReceptionsModelStateListener(this);
    }

    public static SelectReceptionDialogController getInstance() {return instance;}

    public Reception showDialog(JDialog parentDialog) {
        log.info("Opening dialog");
        Reception reception = null;
        dialog = new SelectReceptionDialog(parentDialog,this);
        readReceptions();
        dialog.setVisible(true);
        if (dialog.getResult() == AbstractDialog.OK) reception = dialog.getSelectedReception();
        dialog.dispose();
        return reception;
    }

    void readReceptions() {
        receptionsModel.readReceptionsAndApplyFiltersInSeparateThread();
    }

    void searchReceptions(String rosreestrCodeString) {
        if (!rosreestrCodeString.isEmpty()) {
            receptionsModel.getFiltersModel().setQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE, rosreestrCodeString);
        } else {
            receptionsModel.getFiltersModel().resetQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE);
        }
        receptionsModel.applyFiltersInSeparateThread();

    }

    void setDates(Date fromDate, Date toDate) {
        receptionsModel.getFiltersModel().setOpenDates(fromDate,toDate);
        receptionsModel.readReceptionsAndApplyFiltersInSeparateThread();
    }

    Date getFromDate() {
        return receptionsModel.getFiltersModel().getFromOpenDate();
    }

    Date getToDate() {
        return receptionsModel.getFiltersModel().getToOpenDate();
    }

    @Override
    public void receptionsModelStateChanged(ModelState state) {

        if (state == ModelState.FILTERED) {
            dialog.setReceptions(receptionsModel.getFilteredReceptions());
        }
    }

}

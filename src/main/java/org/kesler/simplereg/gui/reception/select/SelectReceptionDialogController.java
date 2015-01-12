package org.kesler.simplereg.gui.reception.select;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.logic.ServiceState;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Контроллер для управления диалогом выбора дела
 */
public class SelectReceptionDialogController implements ReceptionsModelStateListener {

    protected final Logger log;
    private static SelectReceptionDialogController instance = new SelectReceptionDialogController();

    ReceptionsModel receptionsModel;
    SelectReceptionDialog dialog;

    List<Reception> receptionsToStrike;

    ProcessDialog processDialog;

    private SelectReceptionDialogController() {
        log = Logger.getLogger(this.getClass().getSimpleName());
        receptionsModel = new ReceptionsModel();
        receptionsModel.addReceptionsModelStateListener(this);
    }

    public static SelectReceptionDialogController getInstance() {
        return instance;
    }

    public Reception showDialog(JDialog parentDialog) {
        log.info("Opening dialog");
        dialog = new SelectReceptionDialog(parentDialog, this);
        receptionsToStrike = new ArrayList<Reception>();
        return performDialog();
    }

    public Reception showDialog(JDialog parentDialog, List<Reception> receptionsToStrike) {
        log.info("Opening dialog");
        dialog = new SelectReceptionDialog(parentDialog, this);
        this.receptionsToStrike = receptionsToStrike;
        return performDialog();
    }

    public Reception showDialog(JFrame parentFrame) {
        log.info("Opening dialog");
        dialog = new SelectReceptionDialog(parentFrame, this);
        receptionsToStrike = new ArrayList<Reception>();
        return performDialog();
    }

    public Reception showDialog(JFrame parentFrame, List<Reception> receptionsToStrike) {
        log.info("Opening dialog");
        dialog = new SelectReceptionDialog(parentFrame, this);
        this.receptionsToStrike = receptionsToStrike;
        return performDialog();
    }

    private Reception performDialog() {
        Reception reception = null;
        resetSearchFilter();
        readReceptions();
        dialog.setVisible(true);
        if (dialog.getResult() == AbstractDialog.OK) reception = dialog.getSelectedReception();
        dialog.dispose();
        return reception;
    }

    void readReceptionsFromDB() {
        processDialog = new ProcessDialog(dialog);
        processDialog.showProcess("Загружаю список дел из БД");
        receptionsModel.readReceptionsAndApplyFiltersInSeparateThread();

    }

    void readReceptions() {
        if(receptionsModel.getAllReceptions().size()==0) {
            readReceptionsFromDB();
        } else {
            processDialog = new ProcessDialog(dialog);
            processDialog.showProcess("Загружаю список дел");
            receptionsModel.applyFiltersInSeparateThread();
        }
     }

    void searchReceptions(String rosreestrCodeString) {
        if (!rosreestrCodeString.isEmpty()) {
            receptionsModel.getFiltersModel().setQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE, rosreestrCodeString);
        } else {
            receptionsModel.getFiltersModel().resetQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE);
        }
        receptionsModel.applyFiltersInSeparateThread();

    }

    void resetSearchFilter() {
        receptionsModel.getFiltersModel().resetQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE);
    }

    void setDates(Date fromDate, Date toDate) {
        receptionsModel.getFiltersModel().setFromOpenDate(fromDate);
        receptionsModel.getFiltersModel().setToOpenDate(toDate);
        receptionsModel.readReceptionsAndApplyFiltersInSeparateThread();
    }

    Date getFromDate() {
        return receptionsModel.getFiltersModel().getFromOpenDate();
    }

    Date getToDate() {
        return receptionsModel.getFiltersModel().getToOpenDate();
    }

    @Override
    public void receptionsModelStateChanged(ServiceState state) {

        if (state == ServiceState.FILTERED) {
            List<Reception> receptions = receptionsModel.getFilteredReceptions();
            receptions.removeAll(receptionsToStrike);
            dialog.setReceptions(receptions);
            if(processDialog!=null) processDialog.hideProcess();
        }
    }

}

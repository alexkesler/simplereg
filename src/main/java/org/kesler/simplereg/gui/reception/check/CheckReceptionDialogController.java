package org.kesler.simplereg.gui.reception.check;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.gui.reception.ReceptionDialogController;
import org.kesler.simplereg.gui.reception.select.SelectReceptionDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.logic.ModelState;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Контроллер для управления диалогом проверки статуса дела
 */
public class CheckReceptionDialogController{

    protected final Logger log;
    private static CheckReceptionDialogController instance = new CheckReceptionDialogController();

    ReceptionsModel receptionsModel;
    CheckReceptionDialog dialog;

    ProcessDialog processDialog;

    private CheckReceptionDialogController() {
        log = Logger.getLogger(this.getClass().getSimpleName());
        receptionsModel = new ReceptionsModel();
//        receptionsModel.addReceptionsModelStateListener(this);
    }

    public static CheckReceptionDialogController getInstance() {
        return instance;
    }

    public void showDialog(JDialog parentDialog) {
        log.info("Opening dialog");
        dialog = new CheckReceptionDialog(parentDialog, this);

        performDialog();
    }


    public void showDialog(JFrame parentFrame) {
        log.info("Opening dialog");
        dialog = new CheckReceptionDialog(parentFrame, this);

        performDialog();
    }


    private void performDialog() {

        dialog.setVisible(true);
        dialog.dispose();

    }


    void findReceptionsByRosreestrCode(String rosreestrCodeString) {
        processDialog = new ProcessDialog(dialog);
        processDialog.showProcess("Загружаю список дел из БД");
        ReceptionFinderSwingWorker worker = new ReceptionFinderSwingWorker(rosreestrCodeString,processDialog);
        worker.execute();

    }

//    @Override
//    public void receptionsModelStateChanged(ModelState state) {
//
////        if (state == ModelState.FILTERED) {
////            List<Reception> receptions = receptionsModel.getFilteredReceptions();
////            dialog.setReceptions(receptions);
////            if(processDialog!=null) processDialog.hideProcess();
////        }
//    }

    void openReceptionDialog(Reception reception) {
        ReceptionDialogController.getInstance().showReadOnlyDialog(dialog,reception);
    }

    private class ReceptionFinderSwingWorker extends SwingWorker<List<Reception>,Reception> {
        private String rosreestrCodeString;
        private ProcessDialog processDialog;

        ReceptionFinderSwingWorker(String rosreestrCodeString, ProcessDialog processDialog) {
            this.rosreestrCodeString = rosreestrCodeString;
            this.processDialog = processDialog;
        }

        @Override
        protected List<Reception> doInBackground() throws Exception {
            return receptionsModel.getReceptionsByRosreesrtCode(rosreestrCodeString);
        }

        @Override
        protected void done() {
            try {
                dialog.setReceptions(get());
                if(processDialog!=null) processDialog.hideProcess();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}

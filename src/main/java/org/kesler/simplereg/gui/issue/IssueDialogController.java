package org.kesler.simplereg.gui.issue;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionsModel;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Контроллер для диалога выдачи результата
 */
public class IssueDialogController {
    protected final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    private IssueDialog dialog;
    private static IssueDialogController instance = new IssueDialogController();
    private ReceptionsModel receptionsModel;
    private Reception reception;

    private IssueDialogController() {
        receptionsModel = new ReceptionsModel();

    }

    public static IssueDialogController getInstance()  {
        return instance;
    }

    public void openDialog(JFrame parentFrame, Reception reception) {

        if (dialog ==null || !dialog.getParent().equals(parentFrame))
            dialog = new IssueDialog(parentFrame, this);

        this.reception = reception;
        dialog.setReception(reception);

        dialog.setVisible(true);

        dialog.dispose();

    }
    public void openDialog(JDialog parentDialog, Reception reception) {

        if (dialog ==null || !dialog.getParent().equals(parentDialog))
            dialog = new IssueDialog(parentDialog, this);

        this.reception = reception;
        dialog.setReception(reception);

        dialog.setVisible(true);

        dialog.dispose();

    }


    void issueReception(ReceptionStatus receptionStatus, Date issueDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        int result = JOptionPane.showConfirmDialog(dialog,"<html><p>Выдать результат с простановкой статуса:</p><p><b>" +
                        receptionStatus + "</b> от <b><i>" + dateFormat.format(issueDate)+"</i><b>?</p></html>",
                "Внимание",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (result==JOptionPane.YES_OPTION) {
            reception.setStatus(receptionStatus, issueDate);
            saveReception();
            dialog.setIssued(true);
            dialog.updateViewData();
        }
    }

    public void saveReception() {
        (new ReceptionSaver()).execute();
    }

    class ReceptionSaver extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            receptionsModel.updateReception(reception);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                log.error("Error saving Reception: " + e);
            }

        }
    }



}

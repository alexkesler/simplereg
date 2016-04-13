package org.kesler.simplereg.gui.reception;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.gui.realty.RealtyObjectDialog;
import org.kesler.simplereg.gui.reception.make.MakeReceptionViewController;
import org.kesler.simplereg.gui.reception.select.SelectReceptionDialogController;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.realty.RealtyObjectsService;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionsModel;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Контроллер управления диалогом просмотра/редактирования дела
 */
public class ReceptionDialogController {
    protected final Logger log = Logger.getLogger(this.getClass().getSimpleName());
    private ReceptionDialog dialog;
    private Reception reception;

    private static ReceptionDialogController instance = new ReceptionDialogController();
    private ReceptionsModel receptionsModel;

    public static ReceptionDialogController getInstance() {return instance;}

    private ReceptionDialogController() {
        receptionsModel = new ReceptionsModel();
    }

    /**
     *
     * @param parentFrame родительское окно
     * @param reception дело для просмотра/редактирования
     * @return возвращает истину, если прием изменился, ложь, если нет
     */
    public boolean showDialog(JFrame parentFrame, Reception reception) {
        boolean result;
        this.reception = reception;
        createDialog(parentFrame);
        dialog.setDialogData(reception, false, false);
        dialog.setVisible(true);
        if (dialog.getResult()== AbstractDialog.OK) {
            receptionsModel.updateReception(reception);
            result = true;
        } else {
            result = false;
        }

        dialog.dispose();
        return result;
    }

    public boolean showDialog(JDialog parentDialog, Reception reception) {
        boolean result;
        this.reception = reception;
        createDialog(parentDialog);
        dialog.setDialogData(reception, false, false);
        dialog.setVisible(true);
        if (dialog.getResult()== AbstractDialog.OK) {
            receptionsModel.updateReception(reception);
            result = true;
        } else {
            result = false;
        }

        dialog.dispose();
        return result;
    }

    public void showReadOnlyDialog(JDialog parentDialog, Reception reception) {
        this.reception = reception;
        createDialog(parentDialog);
        dialog.setDialogData(reception, true, false);
        dialog.setVisible(true);

        dialog.dispose();

    }

    public void showIssueDialog(JDialog parentDialog, Reception reception) {
        this.reception = reception;
        createDialog(parentDialog);
        dialog.setDialogData(reception, true, true);
        dialog.setVisible(true);

        dialog.dispose();
    }


    void editReception() {
        MakeReceptionViewController.getInstance().openView(dialog, reception);
        dialog.updateViewData();
    }


    void issueReception(ReceptionStatus receptionStatus, Date issueDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        int result = JOptionPane.showConfirmDialog(dialog,"<html><p>Выдать результат с простановкой статуса:</p><p><b>" +
                        receptionStatus + "</b> от <b><i>" + dateFormat.format(issueDate)+"</i><b>?</p></html>",
                "Внимание",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (result==JOptionPane.YES_OPTION) {
            reception.setStatus(receptionStatus, issueDate);
            saveReception();
            dialog.setIssue(false);
            dialog.updateViewData();
        }
    }





    void editRealtyObject() {
        RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog,reception.getRealtyObject());
        realtyObjectDialog.setVisible(true);
        if (realtyObjectDialog.getResult()==AbstractDialog.OK) {
            RealtyObjectsService.getInstance().updateRealtyObject(reception.getRealtyObject());
            dialog.updateViewData();
        }

        realtyObjectDialog.dispose();

    }

    void addSubReception() {
        List<Reception> receptionsToStrike = new ArrayList<Reception>();
        receptionsToStrike.add(reception);
        receptionsToStrike.addAll(reception.getSubReceptions());
        Reception subReception = SelectReceptionDialogController.getInstance().showDialog(dialog, receptionsToStrike);
        if (subReception!=null) {
            reception.addSubReception(subReception);
            saveReception();
        }
    }

    void removeSubReception(Reception subReception) {
        if (subReception==null) {
            JOptionPane.showMessageDialog(dialog,"ничего не выбрано","Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(dialog,"Убрать связь дополнительного дела " + subReception.getRosreestrCode() + "?",
                "Убрать связь?",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (result==JOptionPane.YES_OPTION) {
            reception.removeSubReception(subReception);
            saveReception();
        }
    }


    private void createDialog(JFrame parentFrame) {
        if (dialog == null || !dialog.getParent().equals(parentFrame)) {
            log.info("Creating new ReceptionDialog");
            dialog = new ReceptionDialog(parentFrame, this);
        }
    }

    private void createDialog(JDialog parentDialog) {
        if (dialog == null || !dialog.getParent().equals(parentDialog)) {
            log.info("Creating new ReceptionDialog");
            dialog = new ReceptionDialog(parentDialog, this);
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

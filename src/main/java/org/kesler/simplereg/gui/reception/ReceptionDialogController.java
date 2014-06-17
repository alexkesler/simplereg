package org.kesler.simplereg.gui.reception;

import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.gui.realty.RealtyObjectDialog;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер управления диалогом просмотра/редактирования дела
 */
public class ReceptionDialogController {
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
        dialog = new ReceptionDialog(parentFrame, reception, this);
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

    void editReception() {
        MakeReceptionViewController.getInstance().openView(dialog, reception);
        dialog.updateViewData();
    }

    void setReceptionCode(String receptionCode) {
        reception.setReceptionCode(receptionCode);
        dialog.receptionChanged();
    }

    void setRosreestrCode(String rosreestrCode) {
        reception.setRosreestrCode(rosreestrCode);
        dialog.receptionChanged();
    }

    void editRealtyObject() {
        RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog,reception.getRealtyObject());
        realtyObjectDialog.setVisible(true);
        if (realtyObjectDialog.getResult()==AbstractDialog.OK) {
            RealtyObjectsModel.getInstance().updateRealtyObject(reception.getRealtyObject());
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
            receptionsModel.updateReception(reception);
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
            receptionsModel.updateReception(subReception);
        }
    }


}

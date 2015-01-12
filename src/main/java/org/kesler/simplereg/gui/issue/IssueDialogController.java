package org.kesler.simplereg.gui.issue;

import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.logic.ServiceState;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;

import javax.swing.*;

/**
 * Контроллер для диалога выдачи результата
 */
public class IssueDialogController implements ReceptionsModelStateListener{

    IssueDialog issueDialog;
    private static IssueDialogController instance = new IssueDialogController();
    private ReceptionsModel receptionsModel;

    private IssueDialogController() {
        receptionsModel = new ReceptionsModel();
        receptionsModel.addReceptionsModelStateListener(this);
    }

    public static IssueDialogController getInstance()  {
        return instance;
    }

    public void openDialog(JFrame parentFrame) {

        issueDialog = new IssueDialog(parentFrame, this);
        receptionsModel.readReceptionsAndApplyFiltersInSeparateThread();
        issueDialog.setVisible(true);

        issueDialog.dispose();

    }

    void filterReceptions(String filterString) {

        receptionsModel.addRosreestrCodeFilter(filterString);
        receptionsModel.applyFiltersInSeparateThread();

    }

    void issueReception(Reception reception) {
        JPanel messagePanel = new JPanel(new MigLayout());
        messagePanel.add(new JLabel("Код Росреестра:"));
        messagePanel.add(new JLabel(reception.getRosreestrCode()));

        int confirmResult = JOptionPane.showConfirmDialog(issueDialog,messagePanel,"Выдать результат?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (confirmResult==JOptionPane.YES_OPTION) {

        }
    }

    @Override
    public void receptionsModelStateChanged(ServiceState state) {
        if(state== ServiceState.FILTERED) {
            issueDialog.updateReceptions(receptionsModel.getFilteredReceptions());
        }
    }
}

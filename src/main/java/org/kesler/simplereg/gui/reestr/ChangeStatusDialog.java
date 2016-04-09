package org.kesler.simplereg.gui.reestr;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 09.04.16.
 */
public class ChangeStatusDialog extends AbstractDialog {

    private ReceptionCodeListDataModel receptionsCodeListDataModel;
    private JLabel countLabel;
    private JLabel statusLabel;
    private WebDateField statusChangeDateWebDateField;


    public ChangeStatusDialog(JFrame parentFrame) {
        super(parentFrame, true);
        createGUI();
        setLocationRelativeTo(parentFrame);
    }

    public void show(List<Reception> receptions, ReceptionStatus status) {

        List<String> receptionCodes = receptionsCodeListDataModel.getReceptionCodes();
        receptionCodes.clear();

        for (Reception reception : receptions) {
            receptionCodes.add(reception.getRosreestrCode());
        }
        receptionsCodeListDataModel.updated();

        countLabel.setText(receptions.size()+"");

        statusLabel.setText(status.getName());

        statusChangeDateWebDateField.setDate(new Date());

        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);

    }

    public Date getStatusChangeDate() {
        return statusChangeDateWebDateField.getDate();
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        receptionsCodeListDataModel = new ReceptionCodeListDataModel();
        JList<String> receptionCodeList = new JList<>(receptionsCodeListDataModel);
        JScrollPane receptionCodeListScrollPane = new JScrollPane(receptionCodeList);

        countLabel = new JLabel();

        statusLabel = new JLabel();

        statusChangeDateWebDateField = new WebDateField();


        dataPanel.add(new JLabel("Установить для запросов: "),"span 2,wrap");
        dataPanel.add(receptionCodeListScrollPane, "span 2, wrap");
        dataPanel.add(new JLabel("всего: "));
        dataPanel.add(countLabel, "wrap");
        dataPanel.add(new JLabel("новый статус: "));
        dataPanel.add(statusLabel,"wrap");
        dataPanel.add(new JLabel("дата: "));
        dataPanel.add(statusChangeDateWebDateField);

        /// Панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ok");
        this.getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                result = OK;
                setVisible(false);
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = CANCEL;
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Собираем основную панель
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(mainPanel);
//        this.setSize(600, 300);

    }

    private class ReceptionCodeListDataModel extends AbstractListModel<String> {

        private List<String> receptionCodes;
        public ReceptionCodeListDataModel() {
            receptionCodes = new ArrayList<>();
        }

        public List<String> getReceptionCodes() {
            return receptionCodes;
        }

        public void updated() {
            fireContentsChanged(this,0,getSize());
        }


        @Override
        public int getSize() {
            return receptionCodes.size();
        }

        @Override
        public String getElementAt(int index) {
            return receptionCodes.get(index);
        }
    }
}

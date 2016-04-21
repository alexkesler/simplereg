package org.kesler.simplereg.gui.statuschangesreestr;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.util.ProgressOverlay;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 20.04.16.
 */
public class ReceptionStatusChangesReestrView extends JFrame {
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    private ReceptionStatusChangesReestrViewController controller;

    private ReceptionStatusChangesTableModel receptionStatusChangesTableModel;
    private ProgressOverlay statusChangesUpdatingProgressOverlay;

    ReceptionStatusChangesReestrView(JFrame parentFrame, ReceptionStatusChangesReestrViewController controller) {
        super("Реестр изменений состояний");
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentFrame);

    }

    void setReceptionStatusChanges(List<ReceptionStatusChange> receptionStatusChanges) {
        receptionStatusChangesTableModel.update(receptionStatusChanges);
    }

    void showProgress() {
        statusChangesUpdatingProgressOverlay.showProgress();
    }

    void hideProgress() {
        statusChangesUpdatingProgressOverlay.hideProgress();
    }


    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel dataPanel = new JPanel(new MigLayout("fill"));



        receptionStatusChangesTableModel = new ReceptionStatusChangesTableModel();
        JTable receptionStatusChangesTable = new JTable(receptionStatusChangesTableModel);
        JScrollPane receptionStatusChangesTableScrollPane = new JScrollPane(receptionStatusChangesTable);

        statusChangesUpdatingProgressOverlay = new ProgressOverlay(receptionStatusChangesTable);


        dataPanel.add(receptionStatusChangesTableScrollPane);


        JPanel buttonPanel = new JPanel();

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(closeButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
    }



    class ReceptionStatusChangesTableModel extends AbstractTableModel {

        private List<ReceptionStatusChange> receptionStatusChanges = new ArrayList<>();

        void update(List<ReceptionStatusChange> receptionStatusChanges) {
            this.receptionStatusChanges = receptionStatusChanges;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return receptionStatusChanges.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ReceptionStatusChange statusChange = receptionStatusChanges.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return statusChange.getReception().getRosreestrCode();
                case 1:
                    return statusChange.getOperator().getFIO();
                case 2:
                    return statusChange.getStatus();
                case 3:
                    return statusChange.getChangeTime();
            }
            return null;
        }
    }
}

package org.kesler.simplereg.gui.pvd;


import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.pvdimport.domain.Cause;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PVDImportDialog extends AbstractDialog{
    private PVDImportDialogController controller;
    private CausesTableModel causesTableModel;
    private Cause selectedCause;

    PVDImportDialog(JDialog parentDialog, PVDImportDialogController controller) {
        super(parentDialog, true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentDialog);
    }

    PVDImportDialog(JFrame parentFrame, PVDImportDialogController controller) {
        super(parentFrame, true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentFrame);
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout("fill"));
        causesTableModel = new CausesTableModel();
        JTable causesTable = new JTable(causesTableModel);
        causesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        causesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedCause = controller.getCauses().get(e.getFirstIndex());
            }
        });
        JScrollPane causesTableScrollPane = new JScrollPane(causesTable);

        dataPanel.add(causesTableScrollPane, "grow");

        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Выбрать");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    Cause getSelectedCause() {return selectedCause;}

    void update() {
        causesTableModel.update();
    }

    class CausesTableModel extends AbstractTableModel {
        private final java.util.List<Cause> causes;

        CausesTableModel() {
            causes = controller.getCauses();
        }

        void update() {
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return causes.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Номер";
                case 1:
                    return "Дата";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Cause cause = causes.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return cause.getRegnum();
                case 1:
                    return cause.getStartDate();
                default:
                    return null;
            }

        }
    }

}

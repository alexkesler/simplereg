package org.kesler.simplereg.gui.pvd.type;

import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.pvdimport.domain.PackageType;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Диалог для отображения типов пакетов для диалога редактирования услуги
 */
class PVDPackageTypeDialog extends AbstractDialog {

    private PVDPackageTypeDialogController controller;
    private PackageTypeTableModel packageTypeTableModel;


    PVDPackageTypeDialog(JDialog parentDialog, PVDPackageTypeDialogController controller) {
        super(parentDialog, "Типы пакетов в ПК ПВД", true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentDialog);
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        //  Панель данных
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        packageTypeTableModel = new PackageTypeTableModel();
        JTable packageTypeTable = new JTable(packageTypeTableModel);
        JScrollPane packageTypeTableScrollPane = new JScrollPane(packageTypeTable);

        dataPanel.add(packageTypeTableScrollPane, "grow");

        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();

    }

    void update() {
        packageTypeTableModel.fireTableDataChanged();
    }

    class PackageTypeTableModel extends AbstractTableModel {
        private final java.util.List<CheckablePackageType> types;

        PackageTypeTableModel() {
            types = controller.getCheckablePackageTypes();
        }
        @Override
        public int getRowCount() {
            return types.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "V";
                case 1:
                    return "ID";
                case 2:
                    return "Группа";
                case 3:
                    return "Тип";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            CheckablePackageType packageType = types.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return packageType.isChecked();
                case 1:
                    return packageType.getId();
                case 2:
                    return packageType.getGroupType();
                case 3:
                    return packageType.getType();
                default:
                    return null;
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex==0) return Boolean.class;
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex==0;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            super.setValueAt(aValue, rowIndex, columnIndex);
            if(columnIndex==0 && aValue instanceof Boolean) {
                types.get(rowIndex).setChecked((Boolean)aValue);
            }
        }
    }

}

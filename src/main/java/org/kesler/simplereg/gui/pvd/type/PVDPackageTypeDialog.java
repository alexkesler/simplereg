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

        dataPanel.add(packageTypeTableScrollPane);

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
        private final java.util.List<PackageType> types;

        PackageTypeTableModel() {
            types = controller.getTypes();
        }
        @Override
        public int getRowCount() {
            return types.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "ID";
                case 1:
                    return "Группа";
                case 2:
                    return "Тип";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            PackageType packageType = types.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return packageType.getId();
                case 1:
                    return packageType.getGroupType();
                case 2:
                    return packageType.getType();
                default:
                    return null;
            }
        }
    }

}

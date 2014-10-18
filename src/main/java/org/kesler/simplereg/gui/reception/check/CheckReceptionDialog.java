package org.kesler.simplereg.gui.reception.check;

import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Диалог проверки статуса дела
 */
public class CheckReceptionDialog extends AbstractDialog {


    private CheckReceptionDialogController controller;
    private ReceptionsTableModel receptionsTableModel;
    private JTextField searchTextField;
    private Reception selectedReception = null;

    CheckReceptionDialog(JDialog parentDialog, CheckReceptionDialogController controller) {
        super(parentDialog, "Проверить статус дела", true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentDialog);
    }

    CheckReceptionDialog(JFrame parentFrame, CheckReceptionDialogController controller) {
        super(parentFrame, "Проверить статус дела", true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentFrame);
    }


    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout());

        JButton findReceptionsButton = new JButton(ResourcesUtil.getIcon("database_refresh.png"));
        findReceptionsButton.setToolTipText("Загрузить из базы данных");
        findReceptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.findReceptionsByRosreestrCode(searchTextField.getText());
            }
        });

        searchTextField = new JTextField(15);
//        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                controller.findReceptionsByRosreestrCode(searchTextField.getText());
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                controller.findReceptionsByRosreestrCode(searchTextField.getText());
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                controller.findReceptionsByRosreestrCode(searchTextField.getText());
//            }
//
//        });

        receptionsTableModel = new ReceptionsTableModel();
        final JTable receptionsTable = new JTable(receptionsTableModel);
        JScrollPane receptionsTableScrollPane = new JScrollPane(receptionsTable);
        receptionsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receptionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = receptionsTable.getSelectedRow();
                if (selectedIndex >= 0) {
                    selectedReception = receptionsTableModel.getReceptionAt(selectedIndex);
                } else {
                    selectedReception = null;
                }

            }
        });
        receptionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
//                    result = OK;
//                    setVisible(false);
                    controller.openReceptionDialog(selectedReception);
                }
            }
        });


        // Собираем панель данных


        dataPanel.add(new JLabel("Код Росреестра: "));
        dataPanel.add(searchTextField);
        dataPanel.add(findReceptionsButton, "wrap");
        dataPanel.add(receptionsTableScrollPane, "span,grow");

        // панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedReception == null) {
                    JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано", "Внимание", JOptionPane.WARNING_MESSAGE);
                } else {
                    result = OK;
                    setVisible(false);
                }
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

        setContentPane(mainPanel);
        setSize(500, 300);

    }

    Reception getSelectedReception() {
        return selectedReception;
    }

    void setReceptions(List<Reception> receptions) {
        receptionsTableModel.setReceptions(receptions);
    }

    class ReceptionsTableModel extends AbstractTableModel {
        private List<Reception> receptions;

        ReceptionsTableModel() {
            receptions = new ArrayList<Reception>();
        }

        void setReceptions(List<Reception> receptions) {
            this.receptions = receptions;
            fireTableDataChanged();
        }

        Reception getReceptionAt(int index) {
            return receptions.get(index);
        }

        @Override
        public int getRowCount() {

            return receptions.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int index) {
            switch (index) {
                case 0:
                    return "Код Росреестра";
                case 1:
                    return "Дата приема";
                case 2:
                    return "Состояние";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Reception reception = receptions.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return reception.getRosreestrCode();
                case 1:
                    return dateFormat.format(reception.getOpenDate());
                case 2:
                    return reception.getStatusName() + " (" + (reception.getStatusChangeDate()==null?"Не опр":dateFormat.format(reception.getStatusChangeDate())) + ")";
                default:
                    return null;
            }

        }

    }


}

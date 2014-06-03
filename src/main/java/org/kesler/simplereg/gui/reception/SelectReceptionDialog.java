package org.kesler.simplereg.gui.reception;

import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.ModelState;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;



/**
 * Диалог выбора дела
 */
public class SelectReceptionDialog extends AbstractDialog implements ReceptionsModelStateListener{

    ReceptionsModel receptionsModel;
    private ReceptionsTableModel receptionsTableModel;
    private JTextField searchTextField;
    private Reception selectedReception = null;

    public SelectReceptionDialog(JDialog parentDialog) {
        super(parentDialog,"Выбрать основное дело",true);
        receptionsModel = new ReceptionsModel();
        receptionsModel.addReceptionsModelStateListener(this);
        createGUI();
        setLocationRelativeTo(parentDialog);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout());

        searchTextField = new JTextField(15);
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchReceptions(searchTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchReceptions(searchTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchReceptions(searchTextField.getText());
            }

        });

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


        // Собираем панель данных

        dataPanel.add(new JLabel("Код Росреестра: "));
        dataPanel.add(searchTextField, "wrap");
        dataPanel.add(receptionsTableScrollPane, "span,grow");

        // панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedReception==null) {
                    JOptionPane.showMessageDialog(currentDialog,"Ничего не выбрано","Внимание",JOptionPane.WARNING_MESSAGE);
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
        mainPanel.add(dataPanel,BorderLayout.CENTER);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setSize(500,300);

    }

    public void showDialog() {
        readReceptions();
        setVisible(true);
    }


    public Reception getSelectedReception() {
        return selectedReception;
    }

    @Override
    public void receptionsModelStateChanged(ModelState state) {

        if (state==ModelState.FILTERED) {
            receptionsTableModel.setReceptions(receptionsModel.getFilteredReceptions());
        }
    }

    private void readReceptions() {
        receptionsModel.readReceptionsAndApplyFiltersInSeparateThread();
    }

    private void searchReceptions(String rosreestrCodeString) {
        if (!rosreestrCodeString.isEmpty()) {
            receptionsModel.getFiltersModel().setQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE, rosreestrCodeString);
        } else {
            receptionsModel.getFiltersModel().resetQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE);
        }
        receptionsModel.applyFiltersInSeparateThread();

    }

    class ReceptionsTableModel extends AbstractTableModel {
        private java.util.List<Reception> receptions;

        ReceptionsTableModel () {
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
                    return "Код дела";
                case 2:
                    return "Дата приема";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
            Reception reception = receptions.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return reception.getRosreestrCode();
                case 1:
                    return reception.getReceptionCode();
                case 2:
                    return dateFormat.format(reception.getOpenDate());
                default:
                    return null;
            }

        }
    }


}

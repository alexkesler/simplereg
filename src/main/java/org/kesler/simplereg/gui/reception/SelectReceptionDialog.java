package org.kesler.simplereg.gui.reception;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.ModelState;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;
import org.kesler.simplereg.util.ResourcesUtil;

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
public class SelectReceptionDialog extends AbstractDialog {



    private SelectReceptionDialogController controller;
    private ReceptionsTableModel receptionsTableModel;
    private WebDateField fromDateField;
    private WebDateField toDateField;
    private JTextField searchTextField;
    private Reception selectedReception = null;

    SelectReceptionDialog(JDialog parentDialog, SelectReceptionDialogController controller) {
        super(parentDialog,"Выбрать основное дело",true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentDialog);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout());

        fromDateField = new WebDateField(controller.getFromDate());
        toDateField = new WebDateField(controller.getToDate());

        JButton setDatesButton = new JButton(ResourcesUtil.getIcon("accept.png"));
        setDatesButton.setToolTipText("Перечитать для новых дат");
        setDatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDates(fromDateField.getDate(),toDateField.getDate());
            }
        });

        searchTextField = new JTextField(15);
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { controller.searchReceptions(searchTextField.getText()); }

            @Override
            public void removeUpdate(DocumentEvent e) { controller.searchReceptions(searchTextField.getText()); }

            @Override
            public void changedUpdate(DocumentEvent e) {
                controller.searchReceptions(searchTextField.getText());
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

        dataPanel.add(new JLabel("Дела с "),"span, split 5");
        dataPanel.add(fromDateField);
        dataPanel.add(new JLabel(" по "));
        dataPanel.add(toDateField);
        dataPanel.add(setDatesButton);

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

    Reception getSelectedReception() {
        return selectedReception;
    }

    void setReceptions(List<Reception> receptions) {
        receptionsTableModel.setReceptions(receptions);
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
            return 2;
        }

        @Override
        public String getColumnName(int index) {
            switch (index) {
                case 0:
                    return "Код Росреестра";
                case 1:
                    return "Дата приема";
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
                default:
                    return null;
            }

        }

    }


}

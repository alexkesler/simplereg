package org.kesler.simplereg.gui.pvd;


import com.alee.extended.date.DateSelectionListener;
import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.pvdimport.domain.Cause;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class PVDImportDialog extends AbstractDialog{
    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
    private PVDImportDialogController controller;
    private CausesTableModel causesTableModel;
    private JTable causesTable;
    private JComboBox<Period> periodComboBox;
    private WebDateField selectedDateWebDateField;
    private JTextField searchTextField;
    private Cause selectedCause;
    private JButton okButton;

    PVDImportDialog(JDialog parentDialog, PVDImportDialogController controller) {
        super(parentDialog, true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentDialog);
        searchTextField.requestFocus();
    }

    PVDImportDialog(JFrame parentFrame, PVDImportDialogController controller) {
        super(parentFrame, true);
        this.controller = controller;
        createGUI();
        pack();
        setLocationRelativeTo(parentFrame);
        searchTextField.requestFocus();
    }

    void disableControls() {
        periodComboBox.setEnabled(false);
        searchTextField.setEnabled(false);
        okButton.setEnabled(false);
    }

    void enableControls() {
        periodComboBox.setEnabled(true);
        searchTextField.setEnabled(true);
        searchTextField.requestFocus();
        okButton.setEnabled(true);
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        final JPanel dataPanel = new JPanel(new MigLayout("fill"));

        periodComboBox = new JComboBox<Period>();
        periodComboBox.addItem(Period.CURRENT_DAY);
        periodComboBox.addItem(Period.LAST_3_DAYS);
        periodComboBox.addItem(Period.LAST_WEEK);
        periodComboBox.addItem(Period.SELECTED_DATE);
        periodComboBox.setSelectedIndex(0);
        periodComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (periodComboBox.getSelectedItem().equals(Period.SELECTED_DATE)) {
                    selectedDateWebDateField.setVisible(true);
                    selectedDateWebDateField.requestFocus();
                } else {
                    selectedDateWebDateField.setVisible(false);
                    applyPeriod();
                }
            }
        });
        periodComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                setText(((Period)value).getDesc());
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        periodComboBox.setFocusable(false);

        selectedDateWebDateField = new WebDateField(new Date());
        selectedDateWebDateField.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(Date date) {
                if (periodComboBox.getSelectedItem().equals(Period.SELECTED_DATE)) {
                    log.debug("SelectedDate: " + date);
                    applyPeriod();
                }
            }
        });
        selectedDateWebDateField.setVisible(false);

        searchTextField = new JTextField(15);
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });

        causesTableModel = new CausesTableModel();
        causesTable = new JTable(causesTableModel);
        causesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane causesTableScrollPane = new JScrollPane(causesTable);

        dataPanel.add(periodComboBox,"growx,wrap");
        dataPanel.add(selectedDateWebDateField, "wrap");
        dataPanel.add(searchTextField, "wrap");
        dataPanel.add(causesTableScrollPane, "grow");

        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        okButton = new JButton("Выбрать");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCause = null;
                int selectedIndex = causesTable.getSelectedRow();
                if (selectedIndex >= 0) selectedCause = controller.getCauses().get(selectedIndex);
                if (selectedCause==null) {
                    JOptionPane.showMessageDialog(currentDialog,
                            "Ничего не выбрано",
                            "Внимание!",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
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
        getRootPane().setDefaultButton(okButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    void applyPeriod() {
        controller.setSelectedPeriod((Period)periodComboBox.getSelectedItem());
        controller.setSelectedDate(selectedDateWebDateField.getDate());
        controller.readCausesByPeriod();
        controller.filterCauses();
        searchTextField.requestFocus();
    }

    void applyFilter() {
        String filterString = searchTextField.getText();
        controller.setFilterString(filterString);
        controller.filterCauses();

        if (controller.getCauses().size()>0)
            causesTable.getSelectionModel().setSelectionInterval(0, 0);

        searchTextField.requestFocus();
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

    public enum Period {
        CURRENT_DAY("За текущий день"),
        LAST_3_DAYS("За 3 дня"),
        LAST_WEEK("За неделю"),
        SELECTED_DATE("За выбранную дату");
        private String desc;

        Period(String desc){
            this.desc = desc;
        }

        public String getDesc() {return desc;}

        @Override
        public String toString() {
            return getDesc();
        }
    }
}

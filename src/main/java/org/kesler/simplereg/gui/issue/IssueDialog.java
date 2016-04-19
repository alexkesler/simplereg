package org.kesler.simplereg.gui.issue;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Диалог выдачи результата
 */
public class IssueDialog extends AbstractDialog{

    private IssueDialogController controller;
    private ApplicatorsTableModel applicatorsTableModel;
    private JComboBox<ReceptionStatus> issueStatusesComboBox;
    private WebDateField issueDateWebDateField;
    private JLabel allIssuedLabel;
    private Reception reception;

    JButton closeButton;
    JButton issueButton;
    JButton cancelButton;

    private boolean issued = false;

    IssueDialog(JFrame parentFrame, IssueDialogController controller) {
        super(parentFrame,"Выдача результата",true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentFrame);
    }

    IssueDialog(JDialog parentDialog, IssueDialogController controller) {
        super(parentDialog,"Выдача результата",true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentDialog);
    }

    public void setReception(Reception reception) {
        this.reception = reception;
        boolean existNotIssued = false;
        for (Applicator applicator : reception.getApplicators()) {
            if (applicator.isIssued()==null || !applicator.isIssued()) existNotIssued = true;
        }
        allIssuedLabel.setVisible(!existNotIssued);
        issued = !existNotIssued;
        updateViewData();
    }

    public void setIssued(boolean issued) { this.issued = issued; }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));


        applicatorsTableModel = new ApplicatorsTableModel();
        JTable applicatorsTable = new JTable(applicatorsTableModel);
        applicatorsTable.getColumnModel().getColumn(0).setMaxWidth(30);
        applicatorsTable.getColumnModel().getColumn(2).setMaxWidth(70);


        JScrollPane applicatorsTableScrollPane = new JScrollPane(applicatorsTable);

        issueStatusesComboBox = new JComboBox<>();

        issueDateWebDateField = new WebDateField();

        allIssuedLabel = new JLabel("<html><span style='color:red; font-weight:bold;'>Выдано всем заявителям</span></html>");

        dataPanel.add(new JLabel("Заявители"), "wrap");
        dataPanel.add(applicatorsTableScrollPane, "span, grow");
        dataPanel.add(new JLabel("Установить статус: "), "align right");
        dataPanel.add(issueStatusesComboBox, "wrap");
        dataPanel.add(new JLabel("от "), "align right");
        dataPanel.add(issueDateWebDateField, "wrap");
        dataPanel.add(allIssuedLabel, "span,align center");

        JPanel buttonPanel = new JPanel();

        issueButton = new JButton("Выдать");
        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReceptionStatus receptionStatus = (ReceptionStatus) issueStatusesComboBox.getSelectedItem();
                Date issueDate = issueDateWebDateField.getDate();
                controller.issueReception(receptionStatus, issueDate);
            }
        });

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        closeButton = new JButton("Закрыть");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(closeButton);
        buttonPanel.add(issueButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setSize(700, 300);
//        pack();
    }

    void updateViewData() {
        if (reception==null) return;
        applicatorsTableModel.update(reception.getApplicators());

        issueDateWebDateField.setDate(new Date());

        List<ReceptionStatus> closedReceptionStatuses = ReceptionStatusesModel.getInstance().getClosedReceptionStatus();
        issueStatusesComboBox.removeAllItems();
        for (ReceptionStatus receptionStatus : closedReceptionStatuses) {
            issueStatusesComboBox.addItem(receptionStatus);
        }

        issueStatusesComboBox.setEnabled(!issued);
        issueDateWebDateField.setEnabled(!issued);

        issueButton.setVisible(!issued);
        cancelButton.setVisible(!issued);
        closeButton.setVisible(issued);

    }


    class ApplicatorsTableModel extends AbstractTableModel {

        private List<Applicator> applicators = new ArrayList<>();
        private List<Boolean> initiallyIssued = new ArrayList<>();

        public void update(List<Applicator> applicators) {
            this.applicators = applicators;
            initiallyIssued.clear();
            for (Applicator applicator : applicators) {
                initiallyIssued.add(applicator.isIssued());
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return applicators.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "#";
                case 1:
                    return "Заявитель";
                case 2:
                    return "Выдано";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex==2) return Boolean.class;
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex!=2) return false;
            if (initiallyIssued.get(rowIndex)!=null && initiallyIssued.get(rowIndex)) return false;
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex!=2) return;
            Applicator applicator = applicators.get(rowIndex);
            applicator.setIssued((Boolean)aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return rowIndex+1;
                case 1:
                    return applicators.get(rowIndex).toString();
                case 2:
                    return applicators.get(rowIndex).isIssued();
            }
            return null;
        }
    }

}

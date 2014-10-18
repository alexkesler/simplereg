package org.kesler.simplereg.gui.issue;

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
import java.util.*;

/**
 * Диалог выдачи результата
 */
public class IssueDialog extends AbstractDialog{

    private IssueDialogController controller;
    private JTextField searchTextField;
    private IssueReceptionsTableModel issueReceptionsTableModel;
    private Reception selectedReception;

    IssueDialog(JFrame parentFrame, IssueDialogController controller) {
        super(parentFrame,"Выдача результата",true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentFrame);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        searchTextField = new JTextField(15);
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }
            private void textChanged() {
                controller.filterReceptions(searchTextField.getText());
            }
        });

        issueReceptionsTableModel = new IssueReceptionsTableModel();
        JTable issueReceptionsTable = new JTable(issueReceptionsTableModel);
        issueReceptionsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        issueReceptionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedReception = issueReceptionsTableModel.getReceptionById(e.getFirstIndex());
            }
        });
        issueReceptionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) issueReception();
            }
        });
        JScrollPane issueReceptionsTableScrollPane = new JScrollPane(issueReceptionsTable);

//        JButton selectReceptionButton = new JButton("Выбрать");
//        selectReceptionButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectedReception = SelectReceptionDialogController.getInstance().showDialog(currentDialog);
//            }
//        });

        JButton issueButton = new JButton("Выдать результат");
        issueButton.setIcon(ResourcesUtil.getIcon("issue.png"));
        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                issueReception();
            }
        });



        dataPanel.add(new JLabel("Поиск"));
        dataPanel.add(searchTextField, "wrap");
        dataPanel.add(issueReceptionsTableScrollPane, "span, grow");
//        dataPanel.add(selectReceptionButton, "span, center");
        dataPanel.add(issueButton,"span, center");

        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Закрыть");

        buttonPanel.add(okButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
    }

    private void issueReception() {
        if (selectedReception!=null) {
            controller.issueReception(selectedReception);
        }
    }

    void updateReceptions(Collection<Reception> receptions) {
        issueReceptionsTableModel.updateReceptions(receptions);
    }

    class IssueReceptionsTableModel extends AbstractTableModel {

        private java.util.List<Reception> receptions;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        IssueReceptionsTableModel() {
            receptions = new ArrayList<Reception>();
        }

        void updateReceptions(Collection<Reception> receptions) {
            this.receptions = new ArrayList<Reception>(receptions);
            fireTableDataChanged();
        }

        Reception getReceptionById(int index) {
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
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Код Росреестра";
                case 1:
                    return "Дата открытия дела";
                case 2:
                    return "Состояние";
            }
            return "";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Reception reception = receptions.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return reception.getRosreestrCode();
                case 1:
                    return simpleDateFormat.format(reception.getOpenDate());
                case 2:
                    return reception.getStatusName() + " (" + simpleDateFormat.format(reception.getStatusChangeDate()) + ")";
            }
            return "";
        }
    }


}

package org.kesler.simplereg.gui.reception.check;

import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.util.ResourcesUtil;

import javax.swing.*;
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
public class CheckReceptionStatusDialog extends AbstractDialog {


    private CheckReceptionStatusDialogController controller;
    private ReceptionsTableModel receptionsTableModel;
    private JTextField searchTextField;
    private Reception selectedReception = null;

    CheckReceptionStatusDialog(JDialog parentDialog, CheckReceptionStatusDialogController controller) {
        super(parentDialog, "Проверить статус дела", true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentDialog);
    }

    CheckReceptionStatusDialog(JFrame parentFrame, CheckReceptionStatusDialogController controller) {
        super(parentFrame, "Проверить статус дела", true);
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentFrame);
    }


    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        JButton findReceptionsButton = new JButton(ResourcesUtil.getIcon("zoom.png"));
        findReceptionsButton.setToolTipText("Найти");
        findReceptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.findReceptionsByRosreestrCode(searchTextField.getText());
            }
        });

        searchTextField = new JTextField(15);

        receptionsTableModel = new ReceptionsTableModel();
        final JTable receptionsTable = new JTable(receptionsTableModel);
        for (int i=0; i<receptionsTableModel.getColumnCount();i++) {
            receptionsTable.getColumnModel().getColumn(i).setPreferredWidth(receptionsTableModel.getColumnSize(i));
        }
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

        JPopupMenu issuePopupMenu = new JPopupMenu();

        JMenuItem issueMenuItem = new JMenuItem("Выдать");
        issueMenuItem.setIcon(ResourcesUtil.getIcon("issue.png"));
        issueMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openIssueDialog(selectedReception);
            }
        });

        issuePopupMenu.add(issueMenuItem);

        receptionsTable.setComponentPopupMenu(issuePopupMenu);

        // Собираем панель данных


        dataPanel.add(new JLabel("Код Росреестра: "), "split 3");
        dataPanel.add(searchTextField);
        dataPanel.add(findReceptionsButton, "wrap");
        dataPanel.add(receptionsTableScrollPane, "span,grow");

        this.getRootPane().setDefaultButton(findReceptionsButton);

        // панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Закрыть");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
             }
        });


        buttonPanel.add(okButton);

        // Собираем основную панель
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setSize(700, 500);

    }


    void setReceptions(List<Reception> receptions) {
        receptionsTableModel.setReceptions(receptions);
    }

    private class ReceptionsTableModel extends AbstractTableModel {
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

        public int getColumnSize(int index) {
            switch (index) {
                case 0:
                    return 100;
                case 1:
                    return 50;
                case 2:
                    return 150;
                default:
                    return 100;
            }
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

package org.kesler.simplereg.gui.reception;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class ReceptionStatusDialog extends JDialog {

    public static int NONE = -1;
    public static int OK = 0;
    public static int CANCEL = 1;

    private JDialog parentDialog;

    private int result;
    private ReceptionStatus receptionStatus;

    private JTextField codeField;
    private JTextField nameField;

    private JCheckBox initialCheckBox;
    private JCheckBox closedCheckBox;

    public ReceptionStatusDialog(JDialog parentDialog) {
        super(parentDialog, "Создать", true);
        this.parentDialog = parentDialog;
        result = NONE;
        receptionStatus = new ReceptionStatus();
        createGUI();
    }

    public ReceptionStatusDialog(JDialog parentDialog, ReceptionStatus receptionStatus) {
        super(parentDialog, "Изменить", true);
        this.parentDialog = parentDialog;
        result = NONE;
        this.receptionStatus = receptionStatus;
        createGUI();
    }

    public int getResult() {
        return result;
    }

    public ReceptionStatus getReceptionStatus() {
        return receptionStatus;
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        codeField = new JTextField(5);
        codeField.setToolTipText("Введите код состояния (число)");
        nameField = new JTextField(20);
        initialCheckBox = new JCheckBox();
        closedCheckBox = new JCheckBox();

        // Собираем панель данных
        dataPanel.add(new JLabel("Код: "));
        dataPanel.add(codeField, "wrap");

        dataPanel.add(new JLabel("Наименование: "));
        dataPanel.add(nameField, "wrap");
        dataPanel.add(new JLabel("Начальное состояние"));
        dataPanel.add(initialCheckBox, "wrap");
        dataPanel.add(new JLabel("Cостояние закрытого дела"));
        dataPanel.add(closedCheckBox);




        // панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ok");
        okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                result = OK;
                if (saveStatusFromGUI()) {
                    setVisible(false);
                }

            }
        });


        JButton cancelButton = new JButton("Отмена");
        cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                result = CANCEL;
                setVisible(false);
            }
        });


        // Собираем панель кнопок
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(parentDialog);

        loadStatusToGUI();

    }

    private void loadStatusToGUI() {
        if (receptionStatus.getCode() != null) {
            codeField.setText(receptionStatus.getCode().toString());
        } else {
            codeField.setText("");
        }

        if (receptionStatus.getName() != null) {
            nameField.setText(receptionStatus.getName());
        } else {
            nameField.setText("");
        }

        initialCheckBox.setSelected(receptionStatus.getInitial()==null?false:receptionStatus.getInitial());
        closedCheckBox.setSelected(receptionStatus.getClosed()==null?false:receptionStatus.getClosed());

    }

    private boolean saveStatusFromGUI() {
        if (codeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(parentDialog, "Поле 'Код' не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            codeField.requestFocus();
            return false;
        }

        if (nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(parentDialog, "Поле 'Наименование' не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;

        }

        try {
            receptionStatus.setCode(Integer.parseInt(codeField.getText()));
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(parentDialog, "Поле 'Код' должно быть числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        receptionStatus.setName(nameField.getText());

        receptionStatus.setInitial(initialCheckBox.isSelected());
        receptionStatus.setClosed(closedCheckBox.isSelected());

        return true;
    }


}
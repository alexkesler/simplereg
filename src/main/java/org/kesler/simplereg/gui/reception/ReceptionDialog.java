package org.kesler.simplereg.gui.reception;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.RealtyObject;

import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;
import org.kesler.simplereg.gui.reception.MakeReceptionViewController;


import org.kesler.simplereg.util.ResourcesUtil;


public class ReceptionDialog extends AbstractDialog {

    private final boolean DEBUG = false;

    private ReceptionDialogController controller;

    private final Reception reception; // чтобы нигде ненароком не переназначить - исходник в контроллере

    private JLabel receptionCodeLabel;
    private JLabel byRecordLabel;
    private JLabel rosreestrCodeLabel;
    private JLabel parentRosreestrCodeLabel;
    private JLabel serviceNameLabel;
    private JLabel realtyObjectLabel;
    private ApplicatorsListModel applicatorsListModel;
    private SubReceptionsListModel subReceptionsListModel;
    private Reception selectedSubReception;
    private JComboBox statusesComboBox;
    private ReceptionStatusChangesTableModel receptionStatusChangesTableModel;
    private JButton saveNewReceptionStatusButton;
    private JButton removeLastReceptionStatusChangeButton;
    private JButton okButton;
    private JButton cancelButton;

    private ReceptionStatus currentReceptionStatus = null;
    private ReceptionStatus newReceptionStatus = null;
    private boolean statusChanged = false;

    ReceptionDialog(JFrame parentFrame, Reception reception, ReceptionDialogController controller) {
        super(parentFrame, true);
        this.controller = controller;
        currentDialog = this;
        this.reception = reception;

        createGUI();
        loadGUIDataFromReception();
        this.setSize(600, 600);
        this.setLocationRelativeTo(parentFrame);

    }

    void updateViewData() {
        loadGUIDataFromReception();
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

        receptionCodeLabel = new JLabel();
        byRecordLabel = new JLabel();
        rosreestrCodeLabel = new JLabel();
        parentRosreestrCodeLabel = new JLabel();

        serviceNameLabel = new JLabel();
        serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());

        realtyObjectLabel = new JLabel();
        realtyObjectLabel.setBorder(BorderFactory.createEtchedBorder());

        JButton editRealtyObjectButton = new JButton(ResourcesUtil.getIcon("pencil.png"));
        editRealtyObjectButton.setToolTipText("Редактировать объект недвижимости");
        editRealtyObjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.editRealtyObject();
            }
        });

        // Панель сведений об услуге
        JPanel serviceInfoPanel = new JPanel();


        applicatorsListModel = new ApplicatorsListModel();
        JList<Applicator> applicatorsList = new JList<Applicator>(applicatorsListModel);
        JScrollPane applicatorsListScrollPane = new JScrollPane(applicatorsList);

        subReceptionsListModel = new SubReceptionsListModel();
        JList<String> subReceptionsList = new JList<String>(subReceptionsListModel);
        JScrollPane subReceptionsListScrollPane = new JScrollPane(subReceptionsList);
        subReceptionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subReceptionsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getFirstIndex()>=0)
                    selectedSubReception = subReceptionsListModel.getSubReception(e.getFirstIndex());
            }
        });

        JButton addSubReceptionButton = new JButton(ResourcesUtil.getIcon("add.png"));
        addSubReceptionButton.setToolTipText("Добавить дополнительное дело");
        addSubReceptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addSubReception();
            }
        });

        JButton removeSubReceptionButton = new JButton(ResourcesUtil.getIcon("delete.png"));
        removeSubReceptionButton.setToolTipText("Удалить выбранное дополнительное дело");
        removeSubReceptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSubReception(selectedSubReception);
            }
        });

        // получаем новый статус дела
        statusesComboBox = new JComboBox();
        statusesComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    newReceptionStatus = (ReceptionStatus) statusesComboBox.getSelectedItem();

                    if (DEBUG)
                        System.out.println("Selected status: " + newReceptionStatus + " current status: " + currentReceptionStatus);

                    if (!newReceptionStatus.equals(currentReceptionStatus)) {
                        if (DEBUG) System.out.println("enabled");
                        statusChanged = true;
                    } else {
                        if (DEBUG) System.out.println("disabled");
                        statusChanged = false;
                    }

                    saveNewReceptionStatusButton.setEnabled(statusChanged);
                }
            }
        });

        // кнопка сохранения установленного статуса
        saveNewReceptionStatusButton = new JButton();
        saveNewReceptionStatusButton.setIcon(ResourcesUtil.getIcon("disk.png"));
        saveNewReceptionStatusButton.setEnabled(false);
        saveNewReceptionStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                saveStatus();
            }
        });

        // кнопка удаления последнего установленного статуса
        removeLastReceptionStatusChangeButton = new JButton(ResourcesUtil.getIcon("undo.png"));
        removeLastReceptionStatusChangeButton.setToolTipText("Отменить последнее изменение состояния");
        removeLastReceptionStatusChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeLastStatusChange();
            }
        });

        // таблица с измемениями статуса
        receptionStatusChangesTableModel = new ReceptionStatusChangesTableModel();
        JTable statusChangesTable = new JTable(receptionStatusChangesTableModel);
        JScrollPane statusChangesTableScrollPane = new JScrollPane(statusChangesTable);


        JButton editButton = new JButton("Изменить");
        editButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.editReception();
            }
        });

        // Собираем панель данных
        dataPanel.add(new JLabel("Код запроса:"));
        dataPanel.add(receptionCodeLabel, "growx");
        dataPanel.add(editButton, "right, wrap");
        dataPanel.add(new JLabel("По записи: "));
        dataPanel.add(byRecordLabel, "wrap");
        dataPanel.add(new JLabel("Код РосРеестра:"));
        dataPanel.add(rosreestrCodeLabel, "wrap");
        dataPanel.add(new JLabel("Код росреестра основного дела:"));
        dataPanel.add(parentRosreestrCodeLabel, "wrap");
        dataPanel.add(new JLabel("Услуга:"), "wrap");
        dataPanel.add(serviceNameLabel, "growx, wrap");
        dataPanel.add(new JLabel("Объект недвижимости:"), "wrap");
        dataPanel.add(realtyObjectLabel, "growx");
        dataPanel.add(editRealtyObjectButton, "wrap");
        dataPanel.add(new JLabel("Заявители:"), "wrap");
        dataPanel.add(applicatorsListScrollPane, "growx, h 50:100:, wrap");
        dataPanel.add(new JLabel("Дополнительные дела:"), "span, split 3, pushx");
        dataPanel.add(addSubReceptionButton);
        dataPanel.add(removeSubReceptionButton, "wrap");
        dataPanel.add(subReceptionsListScrollPane, "growx, h 50:100:, wrap");
        dataPanel.add(new JLabel("Состояние дела"), "right");
        dataPanel.add(serviceInfoPanel, "growx, wrap");
        dataPanel.add(statusesComboBox, "w 100");
        dataPanel.add(saveNewReceptionStatusButton);
        dataPanel.add(removeLastReceptionStatusChangeButton, "wrap");
        dataPanel.add(statusChangesTableScrollPane, "growx, h 100!, wrap");


        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                setVisible(false);

            }
        });
        okButton.requestFocus();

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = CANCEL;
                setVisible(false);
            }
        });
        cancelButton.setVisible(false);

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Собираем основную панель
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        this.setContentPane(mainPanel);

    }

    private void saveStatus() {
        reception.setStatus(newReceptionStatus);
        receptionStatusChangesTableModel.update(); // обновляем табличку со статусами
        checkStatusRemoveAbility();

//		ReceptionsModel.getInstance().updateReception(reception);
        currentReceptionStatus = newReceptionStatus;
        statusChanged = false;
        saveNewReceptionStatusButton.setEnabled(false);
        receptionChanged();
    }

    private void removeLastStatusChange() {
        reception.removeLastStatusChange();
        receptionStatusChangesTableModel.update();
        checkStatusRemoveAbility();
        receptionChanged();
    }

    void receptionChanged() {
        loadGUIDataFromReception();
        okButton.setText("Сохранить");
        cancelButton.setVisible(true);
        statusChanged = true;
        result = OK;
    }


    private void checkStatusRemoveAbility() {
        if (reception.getStatusChanges().size() > 1) {
            removeLastReceptionStatusChangeButton.setEnabled(true);
        } else {
            removeLastReceptionStatusChangeButton.setEnabled(false);
        }
    }


    private void loadGUIDataFromReception() {

        String receptionCode = reception.getReceptionCode();
        if (receptionCode == null) receptionCode = "Не опр";
        receptionCodeLabel.setText("<html><p color='blue'>" + receptionCode + "</p></html>");

        String byRecord = "<html><p color='limegreen'>нет</p></html>";
        if (reception.isByRecord() != null && reception.isByRecord())
            byRecord = "<html><p color='limegreen'>да</p></html>";
        byRecordLabel.setText(byRecord);

        String rosreestrCode = reception.getRosreestrCode();
        if (rosreestrCode == null) rosreestrCode = "Не опр";
        rosreestrCodeLabel.setText("<html><p color='green'>" + rosreestrCode + "</p></html>");

        String parentRosreestrCode = reception.getParentReception() == null ? "" : reception.getParentReception().getRosreestrCode();
        parentRosreestrCodeLabel.setText("<html><strong color='green'>" + parentRosreestrCode + "</strong></html>");

        // определяем наименование услуги
        serviceNameLabel.setText("<html>" + reception.getServiceName() + "</html>");

        // Заполняем информацию по объекту недвижимости
        String realtyObjectString = "";
        RealtyObject realtyObject = reception.getRealtyObject();
        if (realtyObject != null) {
            realtyObjectString = realtyObject.getType() + " " + realtyObject.getAddress();
        } else {
            realtyObjectString = "Не определен";
        }

        realtyObjectLabel.setText(realtyObjectString);

        // обновляем перечень заявителей
        applicatorsListModel.setApplicators(reception.getApplicators());

        // обновляем список подзапросов
        subReceptionsListModel.setSubReceptions(reception.getSubReceptions());

        // Получаем список статусов
        List<ReceptionStatus> receptionStatuses = ReceptionStatusesModel.getInstance().getReceptionStatuses();


        // определяем текущий статус
        currentReceptionStatus = reception.getStatus();
        int index = receptionStatuses.indexOf(currentReceptionStatus);


        // заполняем список статусов
        statusesComboBox.removeAllItems();
        for (ReceptionStatus receptionStatus : receptionStatuses) {
            statusesComboBox.addItem(receptionStatus);
        }

        // выбираем текущий статус
        statusesComboBox.setSelectedIndex(index);


    }

    class SubReceptionsListModel extends AbstractListModel<String> {
        private List<Reception> subReceptions;

        SubReceptionsListModel() {
            subReceptions = new ArrayList<Reception>();
        }

        public void setSubReceptions(List<Reception> subReceptions) {
            this.subReceptions = subReceptions;
            fireContentsChanged(this, 0, subReceptions.size() - 1);
        }

        Reception getSubReception(int index) {
            return subReceptions.get(index);
        }

        @Override
        public int getSize() {
            return subReceptions.size();
        }

        @Override
        public String getElementAt(int index) {
            return subReceptions.get(index).getRosreestrCode();
        }
    }

    class ApplicatorsListModel extends AbstractListModel<Applicator> {

        private List<Applicator> applicators;

        ApplicatorsListModel() {
            applicators = new ArrayList<Applicator>();
        }

        public void setApplicators(List<Applicator> applicators) {
            this.applicators = applicators;
            fireContentsChanged(this, 0, applicators.size() - 1);
        }

        @Override
        public int getSize() {
            return applicators.size();
        }

        @Override
        public Applicator getElementAt(int index) {
            return applicators.get(index);
        }
    }

    class ReceptionStatusChangesTableModel extends AbstractTableModel {

        private List<ReceptionStatusChange> statusChanges;

        ReceptionStatusChangesTableModel() {
            statusChanges = reception.getStatusChanges();
        }

        public void update() {
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return statusChanges.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int column) {
            String columnName = "Не  опр";

            switch (column) {
                case 0:
                    columnName = "Состояние";
                    break;
                case 1:
                    columnName = "Время установки";
                    break;
                case 2:
                    columnName = "Кто установил";
                    break;
            }

            return columnName;
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object value = null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            ReceptionStatusChange statusChange = statusChanges.get(row);
            switch (column) {
                case 0:
                    value = statusChange.getStatus().getName();
                    break;
                case 1:
                    value = dateFormat.format(statusChange.getChangeTime());
                    break;
                case 2:
                    value = statusChange.getOperator().getShortFIO();
            }

            return value;
        }

    }

}
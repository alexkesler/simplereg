package org.kesler.simplereg.gui.reception;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;
import org.kesler.simplereg.logic.RealtyObject;

import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;


import org.kesler.simplereg.util.ResourcesUtil;


public class ReceptionDialog extends AbstractDialog {

    private final boolean DEBUG = false;

    private ReceptionDialogController controller;

    private Reception reception; // чтобы нигде ненароком не переназначить - исходник в контроллере

    private JButton editButton;
    private JTextField receptionCodeTextField;
    private JButton saveReceptionCodeButton;
    private JLabel byRecordLabel;
    private JTextField rosreestrCodeTextField;
    private JButton saveRosreestrCodeButton;
    private JLabel parentRosreestrCodeLabel;
    private JLabel serviceNameLabel;

    private JPanel issuePanel;
    private JButton issueButton;
    private JTextArea realtyObjectTextArea;
    private JButton editRealtyObjectButton;
    private ApplicatorsTableModel applicatorsTableModel;
    private SubReceptionsListModel subReceptionsListModel;
    private JButton addSubReceptionButton;
    private JButton removeSubReceptionButton;
    private Reception selectedSubReception;
    private JComboBox<ReceptionStatus> statusesComboBox;
    private ItemListener statusesComboBoxListener;
    private WebDateField statusChangeDateWebDateField;
    private ReceptionStatusChangesTableModel receptionStatusChangesTableModel;
    private JButton saveNewReceptionStatusButton;
    private JButton removeLastReceptionStatusChangeButton;
    private JButton closeButton;
    private JButton saveButton;
    private JButton cancelButton;

    private ReceptionStatus currentReceptionStatus = null;
    private ReceptionStatus newReceptionStatus = null;
    private boolean receptionChanged = false;

    private boolean readOnly = false;
    private boolean issue = false;

    ReceptionDialog(JFrame parentFrame, Reception reception, ReceptionDialogController controller) {
        super(parentFrame, true);
        this.controller = controller;
        this.reception = reception;

        createGUI();
        loadGUIDataFromReception();
        this.setSize(600, 700);
        this.setLocationRelativeTo(parentFrame);

    }

    ReceptionDialog(JFrame parentFrame, ReceptionDialogController controller) {
        super(parentFrame, true);
        this.controller = controller;

        createGUI();
        this.setSize(600, 700);
        this.setLocationRelativeTo(parentFrame);

    }

    ReceptionDialog(JDialog parentDialog, Reception reception, ReceptionDialogController controller) {
        super(parentDialog, true);
        this.controller = controller;
        this.reception = reception;

        createGUI();
        loadGUIDataFromReception();
        this.setSize(600, 700);
        this.setLocationRelativeTo(parentDialog);

    }

    ReceptionDialog(JDialog parentDialog, ReceptionDialogController controller) {
        super(parentDialog, true);
        this.controller = controller;

        createGUI();
        this.setSize(600, 700);
        this.setLocationRelativeTo(parentDialog);

    }

   ReceptionDialog(JDialog parentDialog, Reception reception, ReceptionDialogController controller, boolean readOnly) {
        super(parentDialog, true);
        this.controller = controller;
        this.reception = reception;
        this.readOnly = readOnly;

        createGUI();
        loadGUIDataFromReception();
        this.setSize(600, 610);
        this.setLocationRelativeTo(parentDialog);

   }


    void setDialogData(Reception reception, boolean readOnly, boolean issue) {
        this.reception = reception;
        this.readOnly = readOnly;
        this.issue = issue;
        loadGUIDataFromReception();
        setLocationRelativeTo(getParent());
    }

    public void setIssue(boolean issue) { this.issue = issue; }

    void updateViewData() {
        loadGUIDataFromReception();
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        receptionCodeTextField = new JTextField(25);
        receptionCodeTextField.setEnabled(false);
        if (!readOnly)
            receptionCodeTextField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    receptionCodeTextField.setEnabled(true);
                    saveReceptionCodeButton.setVisible(true);
                }
            });

        saveReceptionCodeButton = new JButton(ResourcesUtil.getIcon("accept.png"));
        saveReceptionCodeButton.setVisible(false);
        saveReceptionCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reception.setReceptionCode(receptionCodeTextField.getText());
                receptionChanged();
                receptionCodeTextField.setEnabled(false);
                saveReceptionCodeButton.setVisible(false);
            }
        });

        byRecordLabel = new JLabel();

        rosreestrCodeTextField = new JTextField(25);
        rosreestrCodeTextField.setEnabled(false);
        if (!readOnly)
            rosreestrCodeTextField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    rosreestrCodeTextField.setEnabled(true);
                    saveRosreestrCodeButton.setVisible(true);
                }
            });

        saveRosreestrCodeButton = new JButton(ResourcesUtil.getIcon("accept.png"));
        saveRosreestrCodeButton.setVisible(false);
        saveRosreestrCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reception.setRosreestrCode(rosreestrCodeTextField.getText());
                receptionChanged();
                rosreestrCodeTextField.setEnabled(false);
                saveRosreestrCodeButton.setVisible(false);
            }
        });

        parentRosreestrCodeLabel = new JLabel();


        issueButton = new JButton("<html><span style='color:green;font-weight:bold;'>Выдать</span></html");
        issueButton.setIcon(ResourcesUtil.getIcon("issue.png"));
        issueButton.setPreferredSize(new Dimension(150,60));
        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openIssueDialog(reception);
            }
        });


        serviceNameLabel = new JLabel();
        serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());

//        realtyObjectLabel = new JLabel();
//        realtyObjectLabel.setBorder(BorderFactory.createEtchedBorder());
        realtyObjectTextArea = new JTextArea();
        realtyObjectTextArea.setBorder(BorderFactory.createEtchedBorder());
        realtyObjectTextArea.setLineWrap(true);
        realtyObjectTextArea.setWrapStyleWord(true);
        realtyObjectTextArea.setEditable(false);

        editRealtyObjectButton = new JButton(ResourcesUtil.getIcon("pencil.png"));
        editRealtyObjectButton.setToolTipText("Редактировать объект недвижимости");
        editRealtyObjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.editRealtyObject();
            }
        });




        applicatorsTableModel = new ApplicatorsTableModel();
        JTable applicatorsTable = new JTable(applicatorsTableModel);

        applicatorsTable.getColumnModel().getColumn(0).setMaxWidth(30);
        applicatorsTable.getColumnModel().getColumn(2).setMaxWidth(70);

        JScrollPane applicatorsTableScrollPane = new JScrollPane(applicatorsTable);

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

        addSubReceptionButton = new JButton(ResourcesUtil.getIcon("add.png"));
        addSubReceptionButton.setToolTipText("Добавить дополнительное дело");
        addSubReceptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addSubReception();
            }
        });

        removeSubReceptionButton = new JButton(ResourcesUtil.getIcon("delete.png"));
        removeSubReceptionButton.setToolTipText("Удалить выбранное дополнительное дело");
        removeSubReceptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSubReception(selectedSubReception);
            }
        });

        // получаем новый статус дела
        statusesComboBox = new JComboBox<>();

        statusesComboBoxListener = new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    newReceptionStatus = (ReceptionStatus) statusesComboBox.getSelectedItem();

                    if (DEBUG)
                        System.out.println("Selected receptionstatus: " + newReceptionStatus + " current receptionstatus: " + currentReceptionStatus);

                    if (!newReceptionStatus.equals(currentReceptionStatus)) {
                        if (DEBUG) System.out.println("enabled");
                        saveNewReceptionStatusButton.setEnabled(true);
                        statusChangeDateWebDateField.setEnabled(true);
                        statusChangeDateWebDateField.setDate(new Date());
                    } else {
                        if (DEBUG) System.out.println("disabled");
                        saveNewReceptionStatusButton.setEnabled(false);
                        statusChangeDateWebDateField.setEnabled(false);
                        statusChangeDateWebDateField.setDate(reception.getStatusChangeDate());
                    }

                }
            }
        };


        statusChangeDateWebDateField = new WebDateField();

        // кнопка сохранения установленного статуса
        saveNewReceptionStatusButton = new JButton();
        saveNewReceptionStatusButton.setIcon(ResourcesUtil.getIcon("disk.png"));
        saveNewReceptionStatusButton.setEnabled(false);
        statusChangeDateWebDateField.setEnabled(false);
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


        editButton = new JButton("Изменить");
        editButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.editReception();
            }
        });

        // Собираем панель данных

        dataPanel.add(editButton, "span, right");
        JPanel infoPanel = new JPanel(new MigLayout("fill, insets 0"));
        infoPanel.add(new JLabel("Код запроса:"),"span, split 3");
        infoPanel.add(receptionCodeTextField);
        infoPanel.add(saveReceptionCodeButton, "wrap");
        infoPanel.add(new JLabel("По записи: "));
        infoPanel.add(byRecordLabel, "wrap");
        infoPanel.add(new JLabel("Код Росреестра:"), "span, split 3");
        infoPanel.add(rosreestrCodeTextField);
        infoPanel.add(saveRosreestrCodeButton, "wrap");
        infoPanel.add(new JLabel("Код Росреестра основного дела:"), "span, split 2");
        infoPanel.add(parentRosreestrCodeLabel,"wrap");
        
        issuePanel = new JPanel(new MigLayout("fill"));
        
        issuePanel.add(issueButton, "wrap");

        
        dataPanel.add(infoPanel, "growx");
        dataPanel.add(issuePanel, "wrap");
        
        dataPanel.add(new JLabel("Услуга:"), "span");
        dataPanel.add(serviceNameLabel, "span, growx");
        dataPanel.add(new JLabel("Объект недвижимости:"), "span");
        dataPanel.add(realtyObjectTextArea, "span, split 2, growx");
        dataPanel.add(editRealtyObjectButton, "wrap");
        dataPanel.add(new JLabel("Заявители:"), "span");
        dataPanel.add(applicatorsTableScrollPane, "span, growx, h 50::");
        dataPanel.add(new JLabel("Дополнительные дела:"), "span, split 3");
        dataPanel.add(addSubReceptionButton);
        dataPanel.add(removeSubReceptionButton, "wrap");

        dataPanel.add(subReceptionsListScrollPane, "span, growx, h 50::");
        dataPanel.add(new JLabel("Состояние дела"), "span");
        dataPanel.add(statusesComboBox, "span,split 4,w 100");
        dataPanel.add(statusChangeDateWebDateField);
        dataPanel.add(saveNewReceptionStatusButton);
        dataPanel.add(removeLastReceptionStatusChangeButton, "wrap");
        dataPanel.add(statusChangesTableScrollPane, "span, growx, h 100!");


        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        closeButton = new JButton("Закрыть");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                setVisible(false);
            }
        });

        saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveReception();
                receptionChanged = false;
                updateButtons();
            }
        });
        saveButton.setVisible(false);

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = CANCEL;
                setVisible(false);
            }
        });
        cancelButton.setVisible(false);

        buttonPanel.add(closeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Собираем основную панель
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        closeButton.requestFocus();

        this.setContentPane(mainPanel);

    }

    private void saveStatus() {
        Date statusChangeDate = statusChangeDateWebDateField.getDate();
        reception.setStatus(newReceptionStatus, statusChangeDate);
        receptionStatusChangesTableModel.update(); // обновляем табличку со статусами
        checkStatusRemoveAbility();

//		ReceptionsModel.getInstance().updateReception(reception);
        currentReceptionStatus = newReceptionStatus;
        receptionChanged = false;
        statusChangeDateWebDateField.setEnabled(false);
        saveNewReceptionStatusButton.setEnabled(false);
        receptionChanged = true;
        updateButtons();
    }

    private void removeLastStatusChange() {
        reception.removeLastStatusChange();
        currentReceptionStatus = reception.getStatus();
        receptionStatusChangesTableModel.update();
        statusesComboBox.setSelectedItem(reception.getStatus());
        statusChangeDateWebDateField.setDate(reception.getStatusChangeDate());
        checkStatusRemoveAbility();
        receptionChanged = true;
        updateButtons();
    }

    private void receptionChanged() {
        receptionChanged = true;
        updateButtons();
    }

    private void updateButtons() {
        closeButton.setVisible(!receptionChanged);
        saveButton.setVisible(receptionChanged);
        cancelButton.setVisible(receptionChanged);
    }


    private void checkStatusRemoveAbility() {
        if (reception.getStatusChanges().size() > 1) {
            removeLastReceptionStatusChangeButton.setEnabled(true);
        } else {
            removeLastReceptionStatusChangeButton.setEnabled(false);
        }
    }


    private void loadGUIDataFromReception() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");


        String receptionCode = reception.getReceptionCode();
        if (receptionCode == null) receptionCode = "Не опр";
        receptionCodeTextField.setText(receptionCode);

        String byRecord = "<html><p color='limegreen'>нет</p></html>";
        if (reception.isByRecord() != null && reception.isByRecord())
            byRecord = "<html><p color='limegreen'>да</p></html>";
        byRecordLabel.setText(byRecord);

        String rosreestrCode = reception.getRosreestrCode();
        if (rosreestrCode == null) rosreestrCode = "Не опр";
        rosreestrCodeTextField.setText(rosreestrCode);

        String parentRosreestrCode = "";
        Reception parentReception  = reception.getParentReception();
        if (parentReception!= null) {
            parentRosreestrCode = parentReception.getRosreestrCode() + " от " + simpleDateFormat.format(parentReception.getOpenDate());
        }
        parentRosreestrCodeLabel.setText("<html><strong color='green'>" + parentRosreestrCode + "</strong></html>");

        // определяем наименование услуги
        serviceNameLabel.setText("<html>" + reception.getServiceName() + "</html>");

        // Заполняем информацию по объекту недвижимости
        String realtyObjectString = "";
        RealtyObject realtyObject = reception.getRealtyObject();
        if (realtyObject != null) {
            realtyObjectString = (realtyObject.getType()==null?"":realtyObject.getType() + " ") + realtyObject.getAddress();
        } else {
            realtyObjectString = "Не определен";
        }

//        realtyObjectLabel.setText(realtyObjectString);
        realtyObjectTextArea.setText(realtyObjectString);

        // обновляем перечень заявителей
        applicatorsTableModel.setApplicators(reception.getApplicators());

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

        statusChangeDateWebDateField.setDate(reception.getStatusChangeDate());


        receptionStatusChangesTableModel.setStatusChanges(reception.getStatusChanges());


        if (!readOnly) {
            editButton.setVisible(true);
            statusesComboBox.setEnabled(true);
            statusesComboBox.addItemListener(statusesComboBoxListener);
//            statusChangeDateWebDateField.setVisible(true);
            saveNewReceptionStatusButton.setVisible(true);
            removeLastReceptionStatusChangeButton.setVisible(true);
            addSubReceptionButton.setVisible(true);
            removeSubReceptionButton.setVisible(true);
            editRealtyObjectButton.setVisible(true);
        } else {
            editButton.setVisible(false);
            statusesComboBox.setEnabled(false);
            statusesComboBox.removeItemListener(statusesComboBoxListener);
//            statusChangeDateWebDateField.setVisible(false);
            saveNewReceptionStatusButton.setVisible(false);
            removeLastReceptionStatusChangeButton.setVisible(false);
            addSubReceptionButton.setVisible(false);
            removeSubReceptionButton.setVisible(false);
            editRealtyObjectButton.setVisible(false);

        }


        if (issue) {
            issuePanel.setVisible(true);
        } else {
            issuePanel.setVisible(false);
        }


        receptionChanged = false;
        updateButtons();

    }

    class SubReceptionsListModel extends AbstractListModel<String> {
        private List<Reception> subReceptions;
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

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
            Reception subReception = subReceptions.get(index);
            return subReception.getRosreestrCode() + " от " + simpleDateFormat.format(subReception.getOpenDate());
        }
    }


    class ApplicatorsTableModel extends AbstractTableModel {

        private List<Applicator> applicators = new ArrayList<>();

        void setApplicators(List<Applicator> applicators) {
            this.applicators = applicators;
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
            if (readOnly) return false;
            return true;
        }


        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex!=2) return;
            Applicator applicator = applicators.get(rowIndex);
            applicator.setIssued((Boolean)aValue);
            receptionChanged();
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

    class ReceptionStatusChangesTableModel extends AbstractTableModel {

        private List<ReceptionStatusChange> statusChanges;

        ReceptionStatusChangesTableModel() {
            statusChanges = new ArrayList<>();
        }

        public void setStatusChanges(List<ReceptionStatusChange> statusChanges) {
            this.statusChanges = statusChanges;
            update();
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
                    columnName = "Дата";
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

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
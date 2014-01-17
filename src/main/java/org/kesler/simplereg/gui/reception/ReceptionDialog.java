package org.kesler.simplereg.gui.reception;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.RealtyObject;

import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;

import org.kesler.simplereg.util.ResourcesUtil;


public class ReceptionDialog extends AbstractDialog {

	private final boolean DEBUG = false;

	private JFrame parentFrame;
	private JDialog currentDialog;
	
	private Reception reception;

	private JLabel receptionCodeLabel;
	private JLabel byRecordLabel;
	private JLabel rosreestrCodeLabel;
	private JLabel serviceNameLabel;
	private JLabel realtyObjectLabel;
	private JPanel applicatorsPanel;
	private JComboBox statusesComboBox;
	private JButton saveNewReceptionStatusButton;
    private JButton okButton;
    private JButton cancelButton;

	private ReceptionStatus currentReceptionStatus = null;
	private ReceptionStatus newReceptionStatus = null;
	private boolean statusChanged = false;

	public ReceptionDialog(JFrame parentFrame, Reception reception) {
		super(parentFrame, true);
		currentDialog = this;
		this.parentFrame = parentFrame;
		this.reception = reception;

		createGUI();
		loadGUIDataFromReception();
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout("fillx, nogrid"));

		receptionCodeLabel = new JLabel();
		byRecordLabel = new JLabel();
		rosreestrCodeLabel = new JLabel();

		serviceNameLabel = new JLabel();
		serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());

		realtyObjectLabel = new JLabel();
		realtyObjectLabel.setBorder(BorderFactory.createEtchedBorder());


		applicatorsPanel = new JPanel(new MigLayout("fillx"));
		JScrollPane applicatorsPanelScrollPane = new JScrollPane(applicatorsPanel);

		// Панель сведений об услуге
		JPanel serviceInfoPanel = new JPanel();




		// получаем новый статус дела
		statusesComboBox = new JComboBox();
		statusesComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				if (ev.getStateChange() == ItemEvent.SELECTED) {
					newReceptionStatus = (ReceptionStatus) statusesComboBox.getSelectedItem();
	
					if (DEBUG) System.out.println("Selected status: " + newReceptionStatus + " current status: " + currentReceptionStatus);

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

		
		saveNewReceptionStatusButton = new JButton();
		saveNewReceptionStatusButton.setIcon(ResourcesUtil.getIcon("disk.png"));
		saveNewReceptionStatusButton.setEnabled(false);
		saveNewReceptionStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				saveStatus();
			}
		});





		JButton editButton = new JButton("Изменить");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				MakeReceptionViewController.getInstance().openView(currentDialog, reception);
				loadGUIDataFromReception();
			}
		});

		// Собираем панель данных
		dataPanel.add(new JLabel("Код запроса:"));
		dataPanel.add(receptionCodeLabel);
		dataPanel.add(byRecordLabel);
		dataPanel.add(new JLabel("Код РосРеестра:"), "right");
		dataPanel.add(rosreestrCodeLabel, "right, wrap");
		dataPanel.add(new JLabel("Услуга:"),"wrap");
		dataPanel.add(serviceNameLabel,"growx, wrap");
		dataPanel.add(new JLabel("Объект недвижимости:"), "wrap");
		dataPanel.add(realtyObjectLabel, "growx, wrap");
		dataPanel.add(new JLabel("Заявители:"), "wrap");
		dataPanel.add(applicatorsPanelScrollPane,"push, grow, wrap");
		dataPanel.add(new JLabel("Состояние дела"), "right");
		dataPanel.add(serviceInfoPanel,"growx, wrap");
		dataPanel.add(statusesComboBox, "w 50");
		dataPanel.add(saveNewReceptionStatusButton, "wrap");
		dataPanel.add(editButton, "wrap, right");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev) {
                if (statusChanged) {
					int confirmResult = JOptionPane.showConfirmDialog(currentDialog, "<html>Установить статус: " + 
															newReceptionStatus.getName() + " ?</html>", 
															"Сменить статус?", JOptionPane.YES_NO_CANCEL_OPTION);

					if (confirmResult == JOptionPane.YES_OPTION) {
						saveStatus();
						setVisible(false);
					} else if (confirmResult == JOptionPane.NO_OPTION) {
						setVisible(false);
					} else {
						/// При отмене не делаем ничего
					}
				} else {
					setVisible(false);
				}
			}
		});

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
		this.setSize(600,450);
		this.setLocationRelativeTo(parentFrame);

	}

	private void saveStatus() {
		reception.setStatus(newReceptionStatus);
//		ReceptionsModel.getInstance().updateReception(reception);
		currentReceptionStatus = newReceptionStatus;
		statusChanged = false;
		saveNewReceptionStatusButton.setEnabled(false);
        okButton.setText("Сохранить");
        cancelButton.setVisible(true);
        result = OK;
	}

	private void loadGUIDataFromReception() {
		
		String receptionCode = reception.getReceptionCode();
		if (receptionCode == null) receptionCode = "Не опр";
		receptionCodeLabel.setText("<html><p color='blue'>" + receptionCode + "</p></html>");

		String byRecord = "";
		if (reception.isByRecord()!= null && reception.isByRecord()) byRecord = "По записи";
		byRecordLabel.setText(byRecord);

		String rosreestrCode = reception.getRosreestrCode();
		if (rosreestrCode == null) rosreestrCode = "Не опр";
		rosreestrCodeLabel.setText("<html><p color='green'>" + rosreestrCode + "</p></html>");

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

		applicatorsPanel.removeAll();
		// определяем перечень заявителей
		List<Applicator> applicators = reception.getApplicators();
		for (Applicator applicator: applicators) {
			JLabel applicatorLabel = new JLabel("<html>" + applicator.toString() + "</html>");
			applicatorLabel.setBorder(BorderFactory.createEtchedBorder());
			applicatorsPanel.add(applicatorLabel, "growx, wrap");
			// applicatorsPanel.add(applicatorButton,"wrap");
		}

		// Получаем список статусов
		List<ReceptionStatus> receptionStatuses = ReceptionStatusesModel.getInstance().getReceptionStatuses();


		// определяем текущий статус
		currentReceptionStatus = reception.getStatus();
		int index = receptionStatuses.indexOf(currentReceptionStatus);


		
		// заполняем список статусов
		statusesComboBox.removeAllItems();
		for (ReceptionStatus receptionStatus: receptionStatuses) {
			statusesComboBox.addItem(receptionStatus);
		}

		// выбираем текущий статус
		statusesComboBox.setSelectedIndex(index);


	}

}
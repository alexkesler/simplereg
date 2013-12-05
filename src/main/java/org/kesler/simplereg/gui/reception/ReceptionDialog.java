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
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;

import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;
import org.kesler.simplereg.gui.reception.MakeReceptionViewController;



import org.kesler.simplereg.util.ResourcesUtil;


public class ReceptionDialog extends JDialog {

	private final boolean DEBUG = false;

	private JFrame parentFrame;
	private JDialog currentDialog;
	
	private Reception reception;

	private JLabel serviceNameLabel;
	private JPanel applicatorsPanel;
	private JComboBox statusesComboBox;
	private JButton saveNewReceptionStatusButton;

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

		serviceNameLabel = new JLabel();
		serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());

		applicatorsPanel = new JPanel(new MigLayout("fillx"));
		JScrollPane applicatorsPanelScrollPane = new JScrollPane(applicatorsPanel);

		// Панель сведений об услуге
		JPanel serviceDataPanel = new JPanel();




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
			}
		});

		// Собираем панель данных
		dataPanel.add(new JLabel("Услуга:"),"wrap");
		dataPanel.add(serviceNameLabel,"growx, wrap");
		dataPanel.add(new JLabel("Заявители:"), "wrap");
		dataPanel.add(applicatorsPanelScrollPane,"push, grow, wrap");
		dataPanel.add(new JLabel("Состояние дела"), "right");
		dataPanel.add(serviceDataPanel,"growx, wrap");
		dataPanel.add(statusesComboBox, "w 50");
		dataPanel.add(saveNewReceptionStatusButton, "wrap");
		dataPanel.add(editButton, "wrap, center");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
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

		buttonPanel.add(okButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.setSize(500,500);
		this.setLocationRelativeTo(parentFrame);

	}

	private void saveStatus() {
		reception.setStatus(newReceptionStatus);
		ReceptionsModel.getInstance().updateReception(reception);
		currentReceptionStatus = newReceptionStatus;
		saveNewReceptionStatusButton.setEnabled(false);		
	}

	private void loadGUIDataFromReception() {
		// определяем наименование услуги
		serviceNameLabel.setText("<html>" + reception.getServiceName() + "</html>");
		
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
		for (ReceptionStatus receptionStatus: receptionStatuses) {
			statusesComboBox.addItem(receptionStatus);
		}

		// выбираем текущий статус
		statusesComboBox.setSelectedIndex(index);


	}

}
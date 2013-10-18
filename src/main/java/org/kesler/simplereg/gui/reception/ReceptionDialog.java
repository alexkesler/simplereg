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

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;

import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;



import org.kesler.simplereg.util.ResourcesUtil;


public class ReceptionDialog extends JDialog {

	private JFrame parentFrame;
	private Reception reception;

	private JLabel serviceNameLabel;
	private JPanel applicatorsPanel;
	private JComboBox statusesComboBox;

	private ReceptionStatus newReceptionStatus = null;

	public ReceptionDialog(JFrame parentFrame, Reception reception) {
		super(parentFrame, true);
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
		final JButton saveNewReceptionStatusButton = new JButton();
		saveNewReceptionStatusButton.setIcon(ResourcesUtil.getIcon("disk.png"));
		saveNewReceptionStatusButton.setEnabled(false);

		// получаем новый статус дела
		statusesComboBox = new JComboBox();
		statusesComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				newReceptionStatus = (ReceptionStatus) statusesComboBox.getSelectedItem();
				saveNewReceptionStatusButton.setEnabled(true);
			}
		});

		// Сохраняем новый статус дела
		saveNewReceptionStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				reception.setStatus(newReceptionStatus);
				ReceptionsModel.getInstance().updateReception(reception);
				saveNewReceptionStatusButton.setEnabled(false);
			}
		});

		// Собираем панель данных
		dataPanel.add(new JLabel("Услуга:"),"wrap");
		dataPanel.add(serviceNameLabel,"growx, wrap");
		dataPanel.add(new JLabel("Заявители:"), "wrap");
		dataPanel.add(applicatorsPanelScrollPane,"push, grow, wrap");
		dataPanel.add(new JLabel("Состояние дела"), "right");
		dataPanel.add(statusesComboBox, "w 50");
		dataPanel.add(saveNewReceptionStatusButton, "wrap");

		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");

		buttonPanel.add(okButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.setSize(500,500);
		this.setLocationRelativeTo(parentFrame);

	}

	private void loadGUIDataFromReception() {
		// определяем наименование услуги
		serviceNameLabel.setText("<html>" + reception.getServiceName() + "</html>");
		
		// определяем перечень заявителей
		List<Applicator> applicators = reception.getApplicators();
		for (Applicator applicator: applicators) {
			JLabel applicatorLabel = new JLabel("<html>" + applicator.toString() + "</html>");
			applicatorLabel.setBorder(BorderFactory.createEtchedBorder());
			// JButton applicatorButton = new JButton();
			// applicatorButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
			// applicatorButton.addActionListener(new ActionListener() {
			// 	public void actionPerformed(ActionEvent ev) {
			// 		if (applicator instanceof ApplicatorFL) {
						
			// 		}
			// 	}
			// });
			applicatorsPanel.add(applicatorLabel, "growx, wrap");
			// applicatorsPanel.add(applicatorButton,"wrap");
		}

		// определяем статус дела
		// заполняем список статусов
		List<ReceptionStatus> receptionStatuses = ReceptionStatusesModel.getInstance().getReceptionStatuses();
		for (ReceptionStatus receptionStatus: receptionStatuses) {
			statusesComboBox.addItem(receptionStatus);
		}

		// выбираем текущий статус
		ReceptionStatus currentReceptionStatus = reception.getStatus();
		int index = receptionStatuses.indexOf(currentReceptionStatus);

		statusesComboBox.setSelectedIndex(index);


	}

}
package org.kesler.simplereg.gui.services;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;

public class ServiceDialog extends JDialog {

	private JDialog parentDialog;

	private JTextField nameTextField;
	private JCheckBox enabledCheckBox;

	public ServiceDialog(JDialog parentDialog) {
		super(parentDialog,"Услуга", true);
		this.parentDialog = parentDialog;

		createGUI();
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		nameTextField = new JTextField();
		enabledCheckBox = new JCheckBox("Действ.");


		dataPanel.add(new JLabel("Наименование: "));
		dataPanel.add(nameTextField, "push, grow, wrap");
		dataPanel.add(enabledCheckBox);

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Отмена");

		// собираем панель кнопок

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// собираем главную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(200, 300);
		this.setLocationRelativeTo(parentDialog);

	}

}
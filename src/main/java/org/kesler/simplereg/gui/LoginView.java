package org.kesler.simplereg.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


import org.kesler.simplereg.logic.Operator;

public class LoginView extends JFrame{

	private Operator operator;
	private MainViewController controller;

	public LoginView(MainViewController controller) {
		super("Логин");
		this.controller = controller;
		createGUI();
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new GridBagLayout());
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JLabel loginLabel = new JLabel("Оператор: ");

		String[] operators = {
			"Иванов И.И.",
			"Петров П.П.",
			"Сидоров С.С."
		};

		JComboBox loginComboBox = new JComboBox(operators);

		JLabel passwordLabel = new JLabel("Пароль: ");

		JTextField passwordTextField = new JTextField(20);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		dataPanel.add(loginLabel, c);

		c.gridx = 1;
		dataPanel.add(loginComboBox, c);

		c.gridx = 0;
		c.gridy = 1;
		dataPanel.add(passwordLabel, c);

		c.gridx = 1;
		dataPanel.add(passwordTextField, c);


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);


		mainPanel.add(BorderLayout.CENTER, dataPanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);

		this.setLocationRelativeTo(null);
		this.pack();
	}

}

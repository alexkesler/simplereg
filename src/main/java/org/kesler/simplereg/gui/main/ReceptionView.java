package org.kesler.simplereg.gui.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


import org.kesler.simplereg.logic.Reception; 


public class ReceptionView extends JFrame {
	private Reception reception = null;
	private MainViewController controller = null;

	public ReceptionView(MainViewController controller) {
		super("Новая регистрация");
		this.controller = controller;
		createGUI();
	}

	public ReceptionView(MainViewController controller, Reception reception) {
		super("Редактирование регистрации");
		this.controller = controller;
		this.reception = reception;
		createGUI();
	}

	private void createGUI() {
		this.setSize(300,300);
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel topPanel = new JPanel();
		
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);

		JPanel operatorPanel = new JPanel(new GridBagLayout());

		JLabel operatorFIOLabel = new JLabel("Операторов Оператор Операторович");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		operatorPanel.add(operatorFIOLabel, c);

		JLabel curentTimeLabel = new JLabel("01-01-1970 21:00");
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		operatorPanel.add(curentTimeLabel, c);

		dataPanel.add(operatorPanel);

		JPanel servicePanel = new JPanel(new GridBagLayout());
		servicePanel.setBorder(BorderFactory.createTitledBorder("Услуга"));

		JLabel serviceNameLabel = new JLabel("Тестовая услуга");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		servicePanel.add(serviceNameLabel, c);

		JButton serviceSelectButton = new JButton("Выбрать");
		serviceSelectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
//				controller.openServicesView();
			}
		});
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		servicePanel.add(serviceSelectButton, c);

		dataPanel.add(servicePanel);

		// Панель заявителя
		JPanel applicatorPanel = new JPanel(new GridBagLayout());
		applicatorPanel.setBorder(BorderFactory.createTitledBorder("Заявитель"));
		
		JLabel applicatorSurNameLabel = new JLabel("Фамилия:");
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		applicatorPanel.add(applicatorSurNameLabel, c);

		JTextField applicatorSurNameText = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		applicatorPanel.add(applicatorSurNameText, c);

		JLabel applicatorFirstNameLabel = new JLabel("Имя:");
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		applicatorPanel.add(applicatorFirstNameLabel, c);

		JTextField applicatorFirstNameText = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		applicatorPanel.add(applicatorFirstNameText, c);


		JLabel applicatorParentNameLabel = new JLabel("Отчество:");
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		applicatorPanel.add(applicatorParentNameLabel, c);

		JTextField applicatorParentNameText = new JTextField(20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		applicatorPanel.add(applicatorParentNameText, c);

		dataPanel.add(applicatorPanel);

		Box buttonBox = new Box(BoxLayout.X_AXIS);
		JButton saveButton = new JButton("Сохранить");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		buttonBox.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(saveButton);
		buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonBox.add(cancelButton);

		mainPanel.add(BorderLayout.CENTER, dataPanel);
		mainPanel.add(BorderLayout.SOUTH, buttonBox);

		this.add(mainPanel, BorderLayout.CENTER);
		this.pack();

		this.setLocationRelativeTo(null);
	}

}
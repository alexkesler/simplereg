package org.kesler.simplereg.gui;

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
		
		JPanel dataPanel = new JPanel(new GridBagLayout());
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		GridBagConstraints c = new GridBagConstraints();

		JLabel operatorFIOLabel = new JLabel("Операторов Оператор Операторович");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		// c.ipadx = 10;
		// c.ipady = 10;
		c.insets = new Insets(5,5,5,5);
		c.anchor = GridBagConstraints.LINE_END;
		dataPanel.add(operatorFIOLabel, c);

		JLabel curentTimeLabel = new JLabel("01-01-1970 21:00");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		// c.ipadx = 10;
		// c.ipady = 10;
		c.insets = new Insets(5,5,5,5);
		c.anchor = GridBagConstraints.LINE_END;
		dataPanel.add(curentTimeLabel, c);


		JLabel serviceLabel = new JLabel("Услуга:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		// c.ipadx = 10;
		// c.ipady = 10;
		c.insets = new Insets(5,5,5,5);
		c.anchor = GridBagConstraints.LINE_END;
		dataPanel.add(serviceLabel, c);

		JLabel serviceNameLabel = new JLabel("Тестовая услуга");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		// c.ipadx = 10;
		// c.ipady = 10;
		dataPanel.add(serviceNameLabel, c);

		JButton serviceSelectButton = new JButton("Выбрать");
		c.fill = GridBagConstraints.LINE_END;
		c.gridx = 2;
		c.gridy = 1;
		// c.ipadx = 10;
		// c.ipady = 10;
		dataPanel.add(serviceSelectButton, c);


		JLabel applicatorLabel = new JLabel("Заявитель:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		// c.ipadx = 10;
		// c.ipady = 10;
		dataPanel.add(applicatorLabel, c);

		JTextField applicatorFIOText = new JTextField(50);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		// c.ipadx = 10;
		// c.ipady = 10;
		dataPanel.add(applicatorFIOText, c);

		Box buttonBox = new Box(BoxLayout.X_AXIS);
		JButton saveButton = new JButton("Сохранить");

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
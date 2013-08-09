package org.kesler.simplereg.gui.main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;

import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.List;


import org.kesler.simplereg.logic.Operator;

class LoginPanel extends JPanel{

	private Operator operator;
	private MainViewController controller;

	private JComboBox loginComboBox;
	private JPasswordField passwordTextField;

	public LoginPanel(List<Operator> operators) {
		super(new BorderLayout());

		createGUI();

		loginComboBox.removeAllItems();
		for (Operator operator : operators) {
			loginComboBox.addItem(operator);
		}

	}

	private void createGUI() {

		JPanel dataPanel = new JPanel(new GridBagLayout());
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JLabel loginLabel = new JLabel("Оператор: ");

		loginComboBox = new JComboBox();
		loginComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				operator = (Operator) loginComboBox.getSelectedItem();
			}

		});

		JLabel passwordLabel = new JLabel("Пароль: ");

		passwordTextField = new JPasswordField(20);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;
		dataPanel.add(loginLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		dataPanel.add(loginComboBox, c);
		c.fill = GridBagConstraints.NONE;

		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 1;
		dataPanel.add(passwordLabel, c);

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		dataPanel.add(passwordTextField, c);



		this.add(BorderLayout.CENTER, dataPanel);

		this.setMinimumSize(new Dimension(390,170));
	}

	public void setOperators(List<Operator> operators) {
		loginComboBox.removeAllItems();
		for (Operator operator : operators) {
			loginComboBox.addItem(operator);
		}
	}

	public Operator getOperator() {
		return operator;
	}

	public char[] getPassword() {
		return passwordTextField.getPassword();
	}


}

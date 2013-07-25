package org.kesler.simplereg.gui.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.Action;
import javax.swing.AbstractAction;

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

public class LoginView extends JFrame{

	public Action okAction, cancelAction;

	private Operator operator;
	private MainViewController controller;

	private JComboBox loginComboBox;
	private JPasswordField passwordTextField;

	public LoginView(MainViewController controller) {
		super("Логин");
		this.controller = controller;

		okAction = new OkAction();
		cancelAction = new CancelAction();

		createGUI();
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new GridBagLayout());
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JLabel loginLabel = new JLabel("Оператор: ");

		loginComboBox = new JComboBox();
		loginComboBox.addActionListener(new SelectActionListener());

		JLabel passwordLabel = new JLabel("Пароль: ");

		passwordTextField = new JPasswordField(20);
//		passwordTextField.setActionCommand("login");
		passwordTextField.addActionListener(new OkAction());

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


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton(okAction);

		JButton cancelButton = new JButton(cancelAction);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);


		mainPanel.add(BorderLayout.CENTER, dataPanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);

		this.setLocationRelativeTo(null);
		this.setMinimumSize(new Dimension(390,170));
		//this.pack();
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


	// классы Action для кнопок и управяющих элементов

	class OkAction extends AbstractAction {
		public OkAction() {
			putValue(Action.NAME, "ОК");
			putValue(Action.ACTION_COMMAND_KEY, "login");
		}

		public void actionPerformed(ActionEvent ev) {
			controller.login();
		}
	}


	class CancelAction extends AbstractAction {
		public CancelAction() {
			putValue(Action.NAME, "Отмена");
			putValue(Action.ACTION_COMMAND_KEY, "cancel");
		}

		public void actionPerformed(ActionEvent ev) {
			setVisible(false);
		}
	}


	class SelectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			operator = (Operator) loginComboBox.getSelectedItem();
		}
	}

}

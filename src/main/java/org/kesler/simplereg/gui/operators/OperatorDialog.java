package org.kesler.simplereg.gui.operators;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.gui.AbstractDialog;

import org.kesler.simplereg.logic.operator.Operator;

import org.kesler.simplereg.util.ResourcesUtil;

class OperatorDialog extends AbstractDialog {

	private Operator operator;

	private JTextField codeTextField;

	private JTextField firstNameTextField;
	private JTextField parentNameTextField;
	private JTextField surNameTextField;

	private JTextField passwordTextField;
	private JCheckBox controlerCheckBox;
	private JCheckBox adminCheckBox;
	private JCheckBox enabledCheckBox;


	OperatorDialog(JDialog parentDialog) {
		super(parentDialog, "Создать", true);
		operator = new Operator();

		createGUI();
		loadGUIFromOperator();
		this.setLocationRelativeTo(parentDialog);

	}

	OperatorDialog(JDialog parentDialog, Operator operator) {
		super(parentDialog, "Изменить", true);
		this.operator = operator;

		createGUI();
		loadGUIFromOperator();
		setLocationRelativeTo(parentDialog);
	}

	public Operator getOperator() {
		return operator;
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		codeTextField = new JTextField(10);

		JLabel surNameLabel = new JLabel("Фамилия");
		surNameTextField = new JTextField(20);

		JLabel firstNameLabel = new JLabel("Имя");
		firstNameTextField = new JTextField(20);

		JLabel parentNameLabel = new JLabel("Отчество");
		parentNameTextField = new JTextField(20);


		JLabel passwordLabel = new JLabel("Пароль");
		passwordTextField = new JTextField(20);

		controlerCheckBox = new JCheckBox("Контролер",false);

		adminCheckBox = new JCheckBox("Админ",false);

		enabledCheckBox = new JCheckBox("Действ",true);


		// Собираем панель данных
		dataPanel.add(new JLabel("Код"));
		dataPanel.add(codeTextField, "wrap");
		dataPanel.add(surNameLabel);
		dataPanel.add(surNameTextField, "wrap");
		dataPanel.add(firstNameLabel);
		dataPanel.add(firstNameTextField, "wrap");
		dataPanel.add(parentNameLabel);
		dataPanel.add(parentNameTextField, "wrap");
		dataPanel.add(passwordLabel);
		dataPanel.add(passwordTextField, "wrap");
		dataPanel.add(controlerCheckBox, "wrap");
		dataPanel.add(adminCheckBox, "wrap");
		dataPanel.add(enabledCheckBox, "wrap");


		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(readOperatorFromGUI()) {
					result = OK;
					setVisible(false);
				}
			}
		});


		JButton cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		// Собираем панель кнопок
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Собираем панель данных
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();

	}

	private void loadGUIFromOperator() {

		String code = operator.getCode();

		String firstName = operator.getFirstName();
		String surName = operator.getSurName();
		String parentName = operator.getParentName();

		String password = operator.getPassword();

		Boolean controler = operator.isControler();
		Boolean admin = operator.isAdmin();
		Boolean enabled = operator.isEnabled(); 

		// текстовые поля
		if (code != null) {
			codeTextField.setText(code);
		} else {
			codeTextField.setText("");
		}


		// текстовые поля
		if (firstName != null) {
			firstNameTextField.setText(firstName);
		} else {
			firstNameTextField.setText("");
		}

		if (surName != null) {
			surNameTextField.setText(surName);
		} else {
			surNameTextField.setText("");
		}

		if (parentName != null) {
			parentNameTextField.setText(parentName);	
		} else {
			parentNameTextField.setText("");
		}

		if (password != null) {
			passwordTextField.setText(password);
		} else {
			passwordTextField.setText("");
		}

		// логические поля
		if (controler != null) {
			controlerCheckBox.setSelected(controler);
		} else {
			controlerCheckBox.setSelected(false);
		}

		if (admin != null) {
			adminCheckBox.setSelected(admin);
		} else {
			adminCheckBox.setSelected(false);
		}

		if (enabled != null) {
			enabledCheckBox.setSelected(enabled);
		} else {
			adminCheckBox.setSelected(true);
		}
	}

	private boolean readOperatorFromGUI() {

		String code = codeTextField.getText();

		String firstName = firstNameTextField.getText();
		String surName = surNameTextField.getText();
		String parentName = parentNameTextField.getText();

		String password = passwordTextField.getText();

		Boolean controler = controlerCheckBox.isSelected();
		Boolean admin = adminCheckBox.isSelected();
		Boolean enabled = enabledCheckBox.isEnabled(); 


		if (surName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Поле Фамилия не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;		
		}

		operator.setCode(code);

		operator.setFirstName(firstName);
		operator.setSurName(surName);
		operator.setParentName(parentName);

		operator.setPassword(password);

		operator.setControler(controler);
		operator.setAdmin(admin);
		operator.setEnabled(enabled);

		return true;
	}

}
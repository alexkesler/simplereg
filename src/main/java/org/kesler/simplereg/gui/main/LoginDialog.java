package org.kesler.simplereg.gui.main;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;


import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.gui.util.InfoDialog;


/**
* Диалог входа в систему
* 
*/
class LoginDialog extends AbstractDialog{

	private JDialog currentDialog;

	private List<Operator> operators;	
	private Operator operator;


	private JComboBox loginComboBox;
	private JPasswordField passwordTextField;


	/**
	* Создает модальный диалог входа в систему
	* @param frame ссылка на родительское окно
	* @param operators список операторов 
	*/
	public LoginDialog(JFrame frame, List<Operator> operators) {
		super(frame,"Вход в систему", true);
		currentDialog = this;

		this.operators = operators;

		createGUI();
		loadGUIFromOperators();

		this.pack();
		this.setLocationRelativeTo(frame);

	}

	public LoginDialog(JFrame frame) {
		super(frame,"Вход в систему", true);
		currentDialog = this;

		this.operators = new ArrayList<Operator>();

		createGUI();
		loadGUIFromOperators();

		this.pack();
		this.setLocationRelativeTo(frame);

	}


	public void setOperators(List<Operator> operators) {
		this.operators = operators;
		loadGUIFromOperators();
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout());
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JLabel loginLabel = new JLabel("Оператор: ");

		loginComboBox = new JComboBox();
		// при выборе оператора - запоминаем его свойтство
		loginComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int selectedIndex = loginComboBox.getSelectedIndex();
				operator = operators.get(selectedIndex);

				// передаем фокус строке ввода пароля
				passwordTextField.selectAll();
				passwordTextField.requestFocusInWindow();

			}

		});

		JLabel passwordLabel = new JLabel("Пароль: ");

		passwordTextField = new JPasswordField(20);
		// при нажатии Enter - нажимаем кнопку ОК
		// passwordTextField.addActionListener(new OkActionListener());

		dataPanel.add(loginLabel);

		dataPanel.add(loginComboBox, "growx, wrap");

		dataPanel.add(passwordLabel);

		dataPanel.add(passwordTextField, "growx, wrap");


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new OkActionListener());
		this.getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);	


	}

	private void loadGUIFromOperators() {
		loginComboBox.removeAllItems();
		for (Operator operator : operators) {
			loginComboBox.addItem(operator.getShortFIO());
		}
	}

	/**
	* Открывает модальный диалог (ожидает закрытия окна диалога)
	*/
	public void showDialog() {
		setVisible(true);
	}


	/**
	* Возвращает выбранного оператора
	*/
	public Operator getOperator() {
		return operator;
	}

	/**
	* Отрабатывает действие по нажатию кнпки Ок или по нажатию клавиши Enter в поле ввода password.
	*/
	class OkActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			char[] password = operator.getPassword().toCharArray();
			char[] input = passwordTextField.getPassword();
			if (input.length != password.length || !Arrays.equals(input, password)) {
				// JOptionPane.showMessageDialog(currentDialog,
    //     										"Неправильный пароль. Попробуйте еще раз.",
    //     										"Ошибка",
    //     										JOptionPane.ERROR_MESSAGE);
				new InfoDialog(currentDialog, "Неправильный пароль", 1000, InfoDialog.RED).showInfo();
				
				passwordTextField.selectAll();
				passwordTextField.requestFocusInWindow();

			} else {
				passwordTextField.setText(null);
				Arrays.fill(input,'0');
				Arrays.fill(password, '0');
				
				result = OK;
				setVisible(false);

			}
		}	
	} 

}

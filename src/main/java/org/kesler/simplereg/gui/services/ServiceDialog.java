package org.kesler.simplereg.gui.services;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.util.ResourcesUtil;

public class ServiceDialog extends JDialog {

	public static int NONE = -1;
	public static int OK = 0;
	public static int CANCEL = 1;

	private JDialog parentDialog;
	private int result;

	private Service service;

	private JTextField nameTextField;
	private JCheckBox enabledCheckBox;

	public ServiceDialog(JDialog parentDialog) {
		super(parentDialog,"Создать", true);
		this.parentDialog = parentDialog;
		result = NONE;

		service = new Service();
		createGUI();
	}

	public ServiceDialog(JDialog parentDialog, Service service) {
		super(parentDialog,"Изменить",true);
		this.parentDialog = parentDialog;
		result = NONE;

		this.service = service;
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
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (saveServiceFromGUI()) { // если удалось сохранить услугу
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

		// собираем панель кнопок

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// собираем главную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(200, 300);
		this.setLocationRelativeTo(parentDialog);

		loadGUIFromService();

	}

	private void loadGUIFromService() {
		nameTextField.setText(service.getName());
		enabledCheckBox.setSelected(service.getEnabled());
	}

	private boolean saveServiceFromGUI() {
		
		String name = nameTextField.getText();
		if (!name.isEmpty()) {
			service.setName(name);
		} else {
			JOptionPane.showMessage(this, "Наименование услуги не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		service.setEnabled(enabledCheckBox.isSelected());

		return true;
	}

}
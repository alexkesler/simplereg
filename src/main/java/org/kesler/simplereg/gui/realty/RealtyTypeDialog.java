package org.kesler.simplereg.gui.realty;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.realty.RealtyType;

public class RealtyTypeDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	private JDialog parentDialog;

	private int result = NONE;

	private RealtyType realtyType;

	private JTextField typeNameTextField;

	public RealtyTypeDialog(JDialog parentDialog) {
		super(parentDialog, "Создать", true);
		this.parentDialog = parentDialog;

		realtyType = new RealtyType();

		createGUI();
		loadGUIDataFromRealtyType();
	}

	public RealtyTypeDialog(JDialog parentDialog, RealtyType realtyType) {
		super(parentDialog, "Изменить", true);
		this.parentDialog = parentDialog;

		this.realtyType = realtyType;

		createGUI();
		loadGUIDataFromRealtyType();
	}

	public int getResult() {
		return result;
	}

	public RealtyType getRealtyType() {
		return realtyType;
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Пенель данных
		JPanel dataPanel = new JPanel(new MigLayout());

		typeNameTextField = new JTextField(20);

		dataPanel.add(new JLabel("Наименование"));
		dataPanel.add(typeNameTextField);

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (readRealtyTypeFromGUI()) {
					result = OK;
					setVisible(false);
				}
			}
		});
		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		// Собираем панель кнопок
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(parentDialog);

	}

	private void loadGUIDataFromRealtyType() {
		typeNameTextField.setText(realtyType.getName());
	}

	private boolean readRealtyTypeFromGUI() {
		String typeName = typeNameTextField.getText();
		if (typeName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Наименование не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		realtyType.setName(typeName);

		return true;
	}

}
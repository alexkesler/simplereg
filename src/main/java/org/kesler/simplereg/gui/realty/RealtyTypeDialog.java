package org.kesler.simplereg.gui.realty;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;


import net.miginfocom.swing.MigLayout;

public class RealtyTypeDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	private JDialog parentDialog;

	private int result = NONE;


	private JTextField typeNameTextField;

	public RealtyTypeDialog(JDialog parentDialog) {
		super(parentDialog, "Тип объекта недвижимости", true);
		this.parentDialog = parentDialog;

		createGUI();
	}

	public int getResult() {
		return result;
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Пенель данных
		JPanel dataPanel = new JPanel(new MigLayout());

		typeNameTextField = new JTextField(20);

		dataPanel(new JLabel("Наименование"));

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Отмена");

		// Собираем панель кнопок
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(parentDialog);

	}

}
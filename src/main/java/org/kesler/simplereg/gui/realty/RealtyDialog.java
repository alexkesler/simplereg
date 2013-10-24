package org.kesler.simplereg.gui.realty;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.util.ResourcesUtil;

public class RealtyDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	private RealtyObject realtyObject;

	private JDialog parentDialog;

	private int result = NONE;

	private JComboBox realtyTypeComboBox;
	private JTextField addressTextField;

	public RealtyDialog(JDialog parentDialog) {
		super(parentDialog, "Объект недвижимости", true);
		this.parentDialog = parentDialog;

		realtyObject = new RealtyObject();

		createGUI();
	}

	public RealtyDialog(JDialog parentDialog, RealtyObject realtyObject) {
		super(parentDialog, "Изменить объект недвижимости", true);
		this.parentDialog = parentDialog;

		this.realtyObject = realtyObject;

		createGUI();

	}

	public int getResult() {
		return result;
	}

	private void createGUI() {

		// основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout("fill"));


		realtyTypeComboBox = new JComboBox();

		addressTextField = new JTextField(45);

		//собираем панель данных
		dataPanel.add(new JLabel("Тип объекта"), "right");
		dataPanel.add(realtyTypeComboBox, "wrap");
		dataPanel.add(new JLabel("Адрес объекта"), "right");
		dataPanel.add(addressTextField, "wrap");


		// панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (readRealtyObjectFromGUIData()) {
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

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(parentDialog);
	}

	private void loadGUIDataFromRealtyObject() {

	}

	private boolean readRealtyObjectFromGUIData() {
		boolean result = true;

		return result;
	} 

}
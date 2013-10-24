package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;

import org.kesler.simplereg.util.ResourcesUtil;



public class RealtyListDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1; 

	private JFrame parentFrame;

	private int result = NONE;
	private int selectedRealtyObjectIndex = -1;

	RealtyObjectListModel realtyObjectListModel;

	RealtyListDialogController controller;

	public RealtyListDialog(RealtyListDialogController controller, JFrame parentFrame) {
		super(parentFrame, "Объекты недвижимости", true);
		this.parentFrame = parentFrame;
		this.controller = controller;
		createGUI();
	}

	public int getResult() {
		return result;
	}

	public RealtyObject getSelectedRealtyObject() {
		RealtyObject selectedRealtyObject = null;

		if (selectedRealtyObjectIndex != -1) {
			selectedRealtyObject = controller.getAllRealtyObjects().get(selectedRealtyObjectIndex);
		}
		
		return selectedRealtyObject;
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

		realtyObjectListModel = new RealtyObjectListModel();

		JList realtyObjectList = new JList(realtyObjectListModel);
		JScrollPane realtyObjectListScrollPane = new JScrollPane(realtyObjectList);

		JButton addButton = new JButton();
		addButton.setIcon(ResourcesUtil.getIcon("add.png"));


		JButton editButtton = new JButton();
		editButtton.setIcon(ResourcesUtil.getIcon("pencil.png"));


		JButton removeButton = new JButton();
		removeButton.setIcon(ResourcesUtil.getIcon("delete.png"));

		// Собираем панель данных
		dataPanel.add(realtyObjectListScrollPane, "grow, w 200, h 80, wrap");
		dataPanel.add(addButton, "span");
		dataPanel.add(editButtton);
		dataPanel.add(removeButton, "span");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = OK;
				setVisible(false);
			}
		});

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

		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(parentFrame);

	}

	class RealtyObjectListModel extends AbstractListModel {

		List<RealtyObject> realtyObjects;

		RealtyObjectListModel() {
			realtyObjects = controller.getAllRealtyObjects();
		}

		@Override
		public int getSize() {
			return realtyObjects.size();
		}

		@Override
		public String getElementAt(int index) {

			String value = realtyObjects.get(index).toString();
			return value;

		}

	}

}
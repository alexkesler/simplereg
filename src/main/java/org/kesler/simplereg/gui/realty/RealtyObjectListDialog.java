package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;

import org.kesler.simplereg.util.ResourcesUtil;


public class RealtyObjectListDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1; 

	private static final boolean DEBUG = false;

	private JFrame parentFrame;

	private int result = NONE;
	private int selectedRealtyObjectIndex = -1;

	RealtyObjectListDialogController controller;

	JList realtyObjectList;
	RealtyObjectListModel realtyObjectListModel;
	JTextField filterTextField;	



	public RealtyObjectListDialog(RealtyObjectListDialogController controller, JFrame parentFrame) {
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
			selectedRealtyObject = controller.getRealtyObjects().get(selectedRealtyObjectIndex);
		}
		
		return selectedRealtyObject;
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

		filterTextField = new JTextField(15);

		filterTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent event) {
				filterChanged();
			}

			public void removeUpdate(DocumentEvent event) {
				filterChanged();
			}

			public void changedUpdate(DocumentEvent event) {
				filterChanged();
			}

			private void filterChanged() {
				// фильтруем список
				String filterString = filterTextField.getText();
				controller.filterRealtyObjects(filterString);
				realtyObjectListModel.updateRealtyObjects();

				if (DEBUG) System.out.println("list size: " + controller.getRealtyObjects().size());
				// Выбираем первый элемент
				if(controller.getRealtyObjects().size() > 0) {
					selectedRealtyObjectIndex = 0;
					realtyObjectList.setSelectedIndex(selectedRealtyObjectIndex);
					if (DEBUG) System.out.println("selected item: " + selectedRealtyObjectIndex);
				}				
			}
		});


		realtyObjectListModel = new RealtyObjectListModel();

		realtyObjectList = new JList(realtyObjectListModel);
		realtyObjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		realtyObjectList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedRealtyObjectIndex = realtyObjectList.getSelectedIndex();
				}				
			}
		});



		JScrollPane realtyObjectListScrollPane = new JScrollPane(realtyObjectList);

		JButton addButton = new JButton();
		addButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addRealtyObject();
			}
		});


		JButton editButtton = new JButton();
		editButtton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editButtton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.editRealtyObject(selectedRealtyObjectIndex);
			}
		});


		JButton removeButton = new JButton();
		removeButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.removeRealtyObject(selectedRealtyObjectIndex);
			}
		});

		JButton updateButton = new JButton();
		updateButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.readRealtyObjects();
				controller.filterRealtyObjects(filterTextField.getText());
				realtyObjectListModel.updateRealtyObjects();
			}
		});

		// Собираем панель данных
		dataPanel.add(filterTextField, "wrap");
		dataPanel.add(realtyObjectListScrollPane, "push, grow, w 200, h 80, wrap");
		dataPanel.add(addButton, "span");
		dataPanel.add(editButtton);
		dataPanel.add(removeButton);
		dataPanel.add(updateButton, "wrap");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = OK;
				setVisible(false);
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
		this.setSize(400, 300);
		this.setLocationRelativeTo(parentFrame);

	}

	// обновляют список, вызываются из контроллера
	public void realtyObjectAdded(int index) {
		realtyObjectListModel.realtyObjectAdded(index);  
	}

	public void realtyObjectUpdated(int index) {
		realtyObjectListModel.realtyObjectUpdated(index);
	}

	public void realtyObjectRemoved(int index) {
		realtyObjectListModel.realtyObjectRemoved(index);
	}

	class RealtyObjectListModel extends AbstractListModel {

		List<RealtyObject> realtyObjects;

		RealtyObjectListModel() {
			realtyObjects = controller.getRealtyObjects();
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

		public void realtyObjectAdded(int index) {
			fireIntervalAdded(this, index, index);
		}

		public void realtyObjectUpdated(int index) {
			fireContentsChanged(this, index, index);
		}

		public void realtyObjectRemoved(int index) {
			fireIntervalRemoved(this, index, index);
		}

		public void updateRealtyObjects() {
			realtyObjects = controller.getRealtyObjects();
			fireContentsChanged(this, 0, realtyObjects.size()-1);
		}

	}

}
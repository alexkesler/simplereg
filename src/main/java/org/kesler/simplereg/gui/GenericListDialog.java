package org.kesler.simplereg.gui;


import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class GenericListDialog<T> extends JDialog {

	public static int NONE = -1;
	public static int OK = 0;
	public static int CANCEL = 1;

	private int result = NONE;

	private JFrame frame;
	private GenericListDialogController<T> controller;

	private JList itemsList;
	private ItemsListModel itemsListModel;
	private int selectedIndex;

	public GenericListDialog(JFrame frame, String name, GenericListDialogController controller) {
		super(frame, name, true);
		this.frame = frame;
		this.controller = controller;

		selectedIndex = -1;

		createGUI();
	}

	public int getResult() {
		return result;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill,nogrid"));

		itemsListModel = new ItemsListModel();
		itemsList = new JList(itemsListModel);
		// Можно выбрать только один элемент
		itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		itemsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedIndex = itemsList.getSelectedIndex();
				}				
			}
		});


		JScrollPane itemsListScrollPane = new JScrollPane(itemsList);

		JButton addItemButton = new JButton();
		addItemButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openAddItemDialog();
			}
		});

		JButton editItemButton = new JButton();
		editItemButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedIndex != -1) {
					controller.openEditItemDialog(selectedIndex);
				} else {
					JOptionPane.showMessageDialog(frame, "Ничего не выбрано.", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});

		JButton removeItemButton = new JButton();
		removeItemButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedIndex != -1) {
					controller.removeItem(selectedIndex);
				} else {
					JOptionPane.showMessageDialog(frame, "Ничего не выбрано.", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		JButton updateButton = new JButton();
		updateButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.readItems();
				itemsListModel.updateItems();
			}
		});


		// собираем панель данных
		dataPanel.add(itemsListScrollPane, "push, grow, wrap, w 200");
		dataPanel.add(addItemButton);
		dataPanel.add(editItemButton);
		dataPanel.add(removeItemButton);

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

		// Собираем панель кнопок
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(300,300);
		this.setLocationRelativeTo(frame);

	}

	public void addedItem(int index) {
		itemsListModel.addedItem(index);
	}

	public void updatedItem(int index) {
		itemsListModel.updatedItem(index);
	}

	public void removedItem(int index) {
		itemsListModel.removedItem(index);
	}

	class ItemsListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return controller.getAllItems().size();
		}

		@Override
		public String getElementAt(int index) {
			String value = controller.getAllItems().get(index).toString();
			return value;
		}

		void addedItem(int index) {
			fireIntervalAdded(this, index, index);
		}

		void updatedItem(int index) {
			fireContentsChanged(this, index, index);
		}

		void removedItem(int index) {
			fireIntervalRemoved(this, index, index);
		}

		void updateItems() {
			fireContentsChanged(this, 0, controller.getAllItems().size()-1);
		}
	}


}

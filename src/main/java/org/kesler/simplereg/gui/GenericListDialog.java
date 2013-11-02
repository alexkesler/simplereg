package org.kesler.simplereg.gui;


import java.util.List;
import java.util.ArrayList;
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


import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.util.ResourcesUtil;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class GenericListDialog<T> extends JDialog {

	public static int NONE = -1;
	public static int OK = 0;
	public static int CANCEL = 1;

	private int result = NONE;
	private boolean isSelect = false;

	private JFrame parentFrame;
	private JDialog currentDialog;
	private GenericListDialogController<T> controller;

	private JList itemsList;
	private ItemsListModel itemsListModel;
	private int selectedIndex;

	public GenericListDialog(JFrame parentFrame, String name, GenericListDialogController controller) {
		super(parentFrame, name, true);
		this.parentFrame = parentFrame;
		currentDialog = this;
		this.controller = controller;

		selectedIndex = -1;

		createGUI();
	}

	public GenericListDialog(JFrame parentFrame, String name, boolean isSelect, GenericListDialogController controller) {
		super(parentFrame, name, true);
		this.parentFrame = parentFrame;
		this.controller = controller;
		this.isSelect = isSelect;

		selectedIndex = -1;

		createGUI();
	}

	public int getResult() {
		return result;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setItems(List<T> items) {
		itemsListModel.setItems(items);
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
					new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
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
					new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				}

			}
		});

		JButton updateButton = new JButton();
		updateButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.readItems();
				itemsListModel.updateItems();
				new InfoDialog(currentDialog, "Обновлено", 500, InfoDialog.GREEN).showInfo();
			}
		});


		// собираем панель данных
		dataPanel.add(itemsListScrollPane, "push, grow, wrap, w 200");
		dataPanel.add(addItemButton);
		dataPanel.add(editItemButton);
		dataPanel.add(removeItemButton);

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		String okString = "Ok";
		if(isSelect) okString = "Выбрать";
        JButton okButton = new JButton(okString);         
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (isSelect && selectedIndex == -1) {
					new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				} else {
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

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(400,500);
		this.setLocationRelativeTo(parentFrame);

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

		private List<T> items;

		ItemsListModel() {
			items = new ArrayList<T>();
		}

		void setItems(List<T> items) {
			this.items = items;
			updateItems();
		}

		@Override
		public int getSize() {
			return items.size();
		}

		@Override
		public String getElementAt(int index) {
			String value = items.get(index).toString();
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
			fireContentsChanged(this, 0, items.size()-1);
		}
	}


}

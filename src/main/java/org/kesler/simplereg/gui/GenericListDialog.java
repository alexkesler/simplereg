package org.kesler.simplereg.gui;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.util.ResourcesUtil;

public class GenericListDialog<T> extends AbstractDialog {
	private final Logger log = Logger.getLogger(this.getClass());

	public static final int VIEW_MODE=0;
	public static final int VIEW_FILTER_MODE = 1;
	public static final int SELECT_MODE = 2;
	public static final int SELECT_FILTER_MODE = 3;

	private boolean isSelect = false;
	private boolean isFilter = false;

	private GenericListDialogController<T> controller;

	private JTextField filterTextField;
	private JList<T> itemsList;
	private ItemsListModel itemsListModel;
	private int selectedIndex;
	private JLabel processLabel;



	public GenericListDialog(JFrame parentFrame, String name, GenericListDialogController controller, Dimension size, int mode) {
		super(parentFrame, name, true);
		this.controller = controller;

		setMode(mode);

		selectedIndex = -1;

		createGUI();
		setSize(size);
		setLocationRelativeTo(parentFrame);
	}


	public GenericListDialog(JDialog parentDialog, String name, GenericListDialogController controller, Dimension size, int mode) {
		super(parentDialog, name, true);
		this.controller = controller;

		setMode(mode);

		selectedIndex = -1;

		createGUI();
		setSize(size);
		setLocationRelativeTo(parentDialog);
	}

	public GenericListDialog(JFrame parentFrame, String name, GenericListDialogController controller, int mode) {
		this(parentFrame, name, controller, new Dimension(400,500), mode);
	}

	public GenericListDialog(JDialog parentDialog, String name, GenericListDialogController controller, int mode) {
		this(parentDialog, name, controller, new Dimension(400,500), mode);
	}


	public GenericListDialog(JFrame parentFrame, String name, GenericListDialogController controller) {
		this(parentFrame, name, controller, VIEW_MODE);
	}

	public GenericListDialog(JDialog parentDialog, String name, GenericListDialogController controller) {
		this(parentDialog, name, controller, VIEW_MODE);		
	}

	public GenericListDialog(JFrame parentFrame, GenericListDialogController controller) {
		this(parentFrame, "", controller);
	}

	public GenericListDialog(JDialog parentDialog, GenericListDialogController controller) {
		this(parentDialog, "", controller);		
	}

	private void setMode(int mode) {
		switch (mode) {
			case VIEW_MODE:
				isSelect = false;
				isFilter = false;
				break;
			case VIEW_FILTER_MODE:
				isSelect = false;
				isFilter = true;
				break;
			case SELECT_MODE:
				isSelect = true;
				isFilter = false;
				break;
			case SELECT_FILTER_MODE:
				isSelect = true;
				isFilter = true;
				break;		
		}		
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public T getSelectedItem() {
		return itemsList.getSelectedValue();
	}


	/**
	* Устанавливает перечень элементов для отображения/ редактирования
	*/
	public void setItems(List<T> items) {
		itemsListModel.setItems(items);
		if (items.size() > 0) {
			selectedIndex = 0;
			itemsList.setSelectedIndex(selectedIndex);			
		}
	}

	public void cleanFilter() {
		filterTextField.setText("");
	}

	public void showProcess() {
		processLabel.setVisible(true);
	}

	public void hideProcess() {
		processLabel.setVisible(false);
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill,nogrid"));


		filterTextField = new JTextField(20);

		filterTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent event) {
				filterChanged();
			}

			public void removeUpdate(DocumentEvent event) {
				filterChanged();
			}

			public void changedUpdate(DocumentEvent event) {}

			private void filterChanged() {
				String filterString = filterTextField.getText();
				controller.filterItems(filterString);
			}

		});

		processLabel = new JLabel();
		processLabel.setIcon(ResourcesUtil.getIcon("loading.gif"));
		processLabel.setVisible(false);

		itemsListModel = new ItemsListModel();
		itemsList = new JList<T>(itemsListModel);
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


		/// добавление реакции на двойной клик - открытие приема на просмотр
		itemsList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					if (isSelect) {
						result = OK;
						setVisible(false);										
					} else {
						if (controller.openEditItemDialog(itemsList.getSelectedValue())) {
							updatedItem(itemsList.getSelectedIndex());
						}
					}
				}
			}
		});


		JScrollPane itemsListScrollPane = new JScrollPane(itemsList);



		JButton addItemButton = new JButton();
		addItemButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (controller.openAddItemDialog()) {
					addedItem(itemsList.getModel().getSize()-1);
				}
			}
		});

		JButton editItemButton = new JButton();
		editItemButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedIndex != -1) {
					if (controller.openEditItemDialog(itemsList.getSelectedValue())) {
						updatedItem(itemsList.getSelectedIndex());
					}
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
					controller.removeItem(itemsList.getSelectedValue());
				} else {
					new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				}

			}
		});

		JButton updateButton = new JButton();
		updateButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.updateItems();
			}
		});


		// собираем панель данных
		if (isFilter) dataPanel.add(filterTextField, "wrap");
		dataPanel.add(itemsListScrollPane, "push, grow, wrap, w 200");
		dataPanel.add(addItemButton);
		dataPanel.add(editItemButton);
		dataPanel.add(removeItemButton);
		dataPanel.add(updateButton);
		dataPanel.add(processLabel);

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		String okString = "Ok";
		if (isSelect) okString = "Выбрать";
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
		if (isSelect) buttonPanel.add(cancelButton);


		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);

	}

	public void addedItem(int index) {
		itemsListModel.addedItem(index);
		itemsList.setSelectedIndex(index);
	}

	public void updatedItem(int index) {
		itemsListModel.updatedItem(index);
		itemsList.setSelectedIndex(index);
	}

	public void updatedItems() {
		itemsListModel.updateItems();
	}

	public void removedItem(int index) {
		itemsListModel.removedItem(index);
	}

	class ItemsListModel extends AbstractListModel<T> {

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
		public T getElementAt(int index) {
			T value = items.get(index);
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

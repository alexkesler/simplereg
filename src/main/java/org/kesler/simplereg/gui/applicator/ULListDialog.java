package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
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

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.applicator.UL;

public class ULListDialog extends JDialog {

	public static int NONE = -1;
	public static int OK = 0;
	public static int CANCEL = 1;

	private int result;

	private JDialog dialog;
	private ULListDialogController controller;

	private int selectedULIndex;
	private UL selectedUL;
	private String filterString;

	private JTextField filterTextField;
	private JList ulList;
	private ULListModel ulListModel;


	public ULListDialog(JFrame frame, ULListDialogController controller) {
		super(frame, true);
		this.dialog = dialog;
		this.controller = controller;

		selectedULIndex = -1;
		selectedUL = null;
		filterString = "";

		createGUI();
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

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
				filterString = filterTextField.getText();
				controller.filterULList(filterString);
				ulListModel.fireContentsChanged(this,0,controller.getULList().size());
				System.out.println("list size: " + controller.getULList().size());
				if(controller.getULList().size() > 0) {
					selectedULIndex = 0;
					ulList.setSelectedIndex(selectedULIndex);
					selectedUL = controller.getULList().get(selectedULIndex);
					System.out.println("selected ul: " + selectedUL);
				}				
			}
		});


		ulListModel = new ULListModel();
		ulList = new JList(ulListModel);
		ulList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Добавляем обработчик выбора - запоминаем индекс выбранного физ лица и выбранное физическое лицо
		ulList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedULIndex = ulList.getSelectedIndex();
					System.out.println("selected item: " + selectedULIndex);
					if (selectedULIndex != -1) {
						selectedUL = controller.getULList().get(selectedULIndex);
						System.out.println("selected ul: " + selectedUL);
					} else {
						selectedUL = null;
					}

				}				
			}
		});




		JScrollPane ulListScrollPane = new JScrollPane(ulList);

		JButton addULButton = new JButton();
		addULButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addULButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (!filterString.isEmpty()) {	
					filterTextField.setText("");								
				} 
					
				controller.openAddULDialog();
				
			}
		});

		JButton editULButton = new JButton();
		editULButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editULButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openEditULDialog(selectedULIndex);
			}
		});

		JButton removeULButton = new JButton();
		removeULButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeULButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.deleteUL(selectedULIndex);
			}
		});


		// Собираем панель данных

		dataPanel.add(new JLabel("Поиск: "), "span");
		dataPanel.add(filterTextField, "wrap");

		dataPanel.add(ulListScrollPane, "push, grow, wrap, w 500");
		
		dataPanel.add(addULButton, "span");
		dataPanel.add(editULButton);
		dataPanel.add(removeULButton, "wrap");


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
		this.pack();
		this.setLocationRelativeTo(dialog);
	
	}

	public int getResult() {
		return result;
	}

	public UL getSelectedUL() {
		return selectedUL;
	}

	public String getFilterString() {
		return filterString;
	}

	public void addedUL(int index) {
		ulListModel.fireIntervalAdded(this,index,index);
		ulList.setSelectedIndex(index);
		ulList.ensureIndexIsVisible(index);	
	}

	public void updatedUL(int index) {
		ulListModel.fireContentsChanged(this,index,index);
	}

	public void removedUL(int index) {
		ulListModel.fireIntervalRemoved(this,index,index);
	}


	class ULListModel extends AbstractListModel {

		public int getSize() {
			return controller.getULList().size();
		}

		public UL getElementAt(int index) {
			UL ul = null;
			if (index < controller.getULList().size()) {
				ul = controller.getULList().get(index);
			}
			
			return ul;
		}

		public void fireIntervalAdded(Object source, int index0, int index1) {
			super.fireIntervalAdded(source, index0, index1);
		}

		public void fireContentsChanged(Object source, int index0, int index1) {
			super.fireContentsChanged(source, index0, index1);
		}

		public void fireIntervalRemoved(Object source, int index0, int index1) {
			super.fireIntervalRemoved(source, index0, index1);
		}


	}
}
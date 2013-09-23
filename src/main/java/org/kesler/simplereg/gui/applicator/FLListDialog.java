package org.kesler.simplereg.gui.applicator;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.AbstractListModel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.applicator.FL;

public class FLListDialog extends JDialog{
	
	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	private int result;

	private JFrame frame;

	private FL selectedFL;
	private int selectedFLIndex;

	private String filterString;

	private FLListModel flListModel;
	private JList flList;
	private FLListDialogController controller;
	private JTextField filterTextField;

	public FLListDialog(JFrame frame, FLListDialogController controller) {
		super(frame,"Список физических лиц", true);
		this.frame = frame;
		this.controller = controller;
		createGUI();

		selectedFLIndex = -1;
		selectedFL = null;
		filterString = "";
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));


		filterTextField = new JTextField(15);

		filterTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent event) {
				filterString = filterTextField.getText();
				controller.filterFLList(filterString);
				flListModel.fireContentsChanged(this,0,controller.getFLList().size());
				// выбираем первый из списка
				if(controller.getFLList().size() > 0) {
					selectedFLIndex = 0;
					flList.setSelectedIndex(selectedFLIndex);
					selectedFL = controller.getFLList().get(selectedFLIndex);
				}	
				System.out.println("list size: " + controller.getFLList().size());			
			}

			public void removeUpdate(DocumentEvent event) {
				filterString = filterTextField.getText();
				controller.filterFLList(filterString);
				flListModel.fireContentsChanged(this,0,controller.getFLList().size());
				// Выбираем первый из списка
				if(controller.getFLList().size() > 0) {
					selectedFLIndex = 0;
					flList.setSelectedIndex(selectedFLIndex);
					selectedFL = controller.getFLList().get(selectedFLIndex);
				}	

			}

			public void changedUpdate(DocumentEvent event) {}
		});

		flListModel = new FLListModel(); 
		flList = new JList(flListModel);
		// Можно выбрать только один элемент
		flList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Добавляем обработчик выбора - запоминаем индекс выбранного физ лица и выбранное физическое лицо
		flList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedFLIndex = flList.getSelectedIndex();
					System.out.println("selected item: " + selectedFLIndex);
					if (selectedFLIndex != -1) {
						selectedFL = controller.getFLList().get(selectedFLIndex);
					} else {
						selectedFL = null;
					}

				}				
			}
		});

		JScrollPane flListScrollPane = new JScrollPane(flList); 

		// Кнопка добавления физ лица
		JButton addButton = new JButton();
		addButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (!filterString.isEmpty()) {	
					String filter = filterString;
					//filterTextField.setText("");								
					controller.openAddFLDialog(filter);
				} else {
					controller.openAddFLDialog();
				}
								
			}
		});
		
		// Кнопка редактироавния физ лица
		JButton editButton = new JButton();
		editButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openEditFLDialog(selectedFLIndex);
			}
		});

		// Кнопка удаления физ лица
		JButton deleteButton = new JButton();
		deleteButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.deleteFL(selectedFLIndex);
			}
		});

		dataPanel.add(new JLabel("Поиск: "),"span");
		dataPanel.add(filterTextField,"wrap");

		dataPanel.add(flListScrollPane, "span, pushy, grow, wrap, wmin 300");

		dataPanel.add(addButton, "split");
		dataPanel.add(editButton);
		dataPanel.add(deleteButton,"wrap");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedFLIndex != -1) {
					result = OK;
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(frame,
    									"Ничего не выбрано.",
    									"Ошибка",
    									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectedFLIndex = -1;
				selectedFL = null;
				result = CANCEL;
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Добавляем все на основную панель

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);

		setSize(500,500);
		pack();
		setLocationRelativeTo(frame);

	}

	public FL getSelectedFL() {
		return selectedFL;
	}

	public String getFilterString() {
		return filterString;
	}

	public void addedFL(int index) {
		flListModel.fireIntervalAdded(this,index,index);
		flList.setSelectedIndex(index);
		flList.ensureIndexIsVisible(index);	
	}

	public void updatedFL(int index) {
		flListModel.fireContentsChanged(this,index,index);
	}

	public void removedFL(int index) {
		flListModel.fireIntervalRemoved(this,index,index);
	}

	class FLListModel extends AbstractListModel {
		//private List<FL> flList;

		FLListModel() {
			//flList = controller.getFLList();
		}

		// public void setFLList(List<FL> flList) {
		// 	this.flList = flList;
		// }

		public int getSize() {
			return controller.getFLList().size();
		}

		public FL getElementAt(int index) {
			FL fl = null;
			if (index < controller.getFLList().size()) {
				fl = controller.getFLList().get(index);
			}
			
			return fl;
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
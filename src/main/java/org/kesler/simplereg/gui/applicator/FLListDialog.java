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
import javax.swing.JScrollPane;
import javax.swing.JButton;
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

	private FLListModel flListModel;
	private JList flList;
	private FLListDialogController controller;

	public FLListDialog(JFrame frame, FLListDialogController controller) {
		super(frame,"Список физических лиц", true);
		this.frame = frame;
		this.controller = controller;
		createGUI();

		selectedFLIndex = -1;
		selectedFL = null;
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

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
				controller.openAddFLDialog();
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

		dataPanel.add(flListScrollPane, "span, pushy, grow, wrap, w 300:n:n");

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

	public void addedFL(int index) {
		flListModel.fireIntervalAdded(this,index,index);
		flList.setSelectedIndex(index);
	}

	public void updatedFL(int index) {
		flListModel.fireContentsChanged(this,index,index);
	}

	public void removedFL(int index) {
		flListModel.fireIntervalRemoved(this,index,index);
	}

	class FLListModel extends AbstractListModel {
		private List<FL> flList;

		FLListModel() {
			flList = controller.getFLList();
		}

		public int getSize() {
			return flList.size();
		}

		public FL getElementAt(int index) {
			FL fl = flList.get(index);
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
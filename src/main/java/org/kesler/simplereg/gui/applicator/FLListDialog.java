package org.kesler.simplereg.gui.applicator;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
	
	private JFrame frame;

	private FL fl;

	private FLListModel flListModel;
	private FLListDialogController controller;

	public FLListDialog(JFrame frame, FLListDialogController controller) {
		super(frame,"Список физических лиц", true);
		this.frame = frame;
		this.controller = controller;
		createGUI();
		fl = null;
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fillx"));

		flListModel = new FLListModel(); 
		JList flList = new JList(flListModel);
		// При изменении выбора
		flList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		flList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				int selectedIndex = lse.getFirstIndex();
				if (selectedIndex!=-1) {
					fl = controller.getFLList().get(selectedIndex);
				} else {
					fl = null;
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
				controller.openEditFLDialog(fl);
			}
		});

		// Кнопка удаления физ лица
		JButton deleteButton = new JButton();
		deleteButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.deleteFL(fl);
			}
		});

		dataPanel.add(flListScrollPane, "span, growx, wrap");

		dataPanel.add(addButton, "split");
		dataPanel.add(editButton);
		dataPanel.add(deleteButton,"wrap");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (fl != null) {
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
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				fl = null;
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

	public FL getFL() {
		return fl;
	}

	public void addedFL(int index) {
		flListModel.fireIntervalAdded(this,index,index);
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
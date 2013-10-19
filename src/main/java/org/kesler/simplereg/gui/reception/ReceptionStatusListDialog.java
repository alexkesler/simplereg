package org.kesler.simplereg.gui.reception;

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

public class ReceptionStatusListDialog extends JDialog {

	public static int NONE = -1;
	public static int OK = 0;
	public static int CANCEL = 1;

	private int result = NONE;

	private JFrame frame;
	private ReceptionStatusListDialogController controller;

	private JList statusesList;
	private StatusesListModel statusesListModel;
	private int selectedIndex;

	public ReceptionStatusListDialog(JFrame frame, ReceptionStatusListDialogController controller) {
		super(frame, "Статусы дел", true);
		this.frame = frame;
		this.controller = controller;

		selectedIndex = -1;

		createGUI();
	}

	public int getResult() {
		return result;
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill,nogrid"));

		statusesListModel = new StatusesListModel();
		statusesList = new JList(statusesListModel);
		// Можно выбрать только один элемент
		statusesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		statusesList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedIndex = statusesList.getSelectedIndex();
				}				
			}
		});


		JScrollPane statusesListScrollPane = new JScrollPane(statusesList);

		JButton addStatusButton = new JButton();
		addStatusButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openAddStatusDialog();
			}
		});

		JButton editStatusButton = new JButton();
		editStatusButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedIndex != -1) {
					controller.openEditStatusDialog(selectedIndex);
				} else {
					JOptionPane.showMessageDialog(frame, "Ничего не выбрано.", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});

		JButton removeStatusButton = new JButton();
		removeStatusButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedIndex != -1) {
					controller.removeReceptionStatus(selectedIndex);
				} else {
					JOptionPane.showMessageDialog(frame, "Ничего не выбрано.", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		// собираем панель данных
		dataPanel.add(statusesListScrollPane, "push, grow, wrap, w 200");
		dataPanel.add(addStatusButton);
		dataPanel.add(editStatusButton);
		dataPanel.add(removeStatusButton);

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

	void addedStatus(int index) {
		statusesListModel.addedStatus(index);
	}

	void updatedStatus(int index) {
		statusesListModel.updatedStatus(index);
	}

	void removedStatus(int index) {
		statusesListModel.removedStatus(index);
	}

	class StatusesListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return controller.getReceptionStatuses().size();
		}

		@Override
		public String getElementAt(int index) {
			String value = controller.getReceptionStatuses().get(index).toString();
			return value;
		}

		void addedStatus(int index) {
			fireIntervalAdded(this, index, index);
		}

		void updatedStatus(int index) {
			fireContentsChanged(this, index, index);
		}

		void removedStatus(int index) {
			fireIntervalRemoved(this, index, index);
		}
	}


}